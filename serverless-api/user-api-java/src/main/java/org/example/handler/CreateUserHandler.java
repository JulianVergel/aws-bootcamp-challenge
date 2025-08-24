package org.example.handler;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Map;
import java.util.UUID;

import org.example.dto.UserDTO;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;
import software.amazon.awssdk.services.dynamodb.model.PutItemRequest;
import software.amazon.awssdk.services.sqs.SqsClient;
import software.amazon.awssdk.services.sqs.model.SendMessageRequest;

public class CreateUserHandler implements RequestHandler<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent> {

    private static final DynamoDbClient dynamoDbClient = DynamoDbClient.create();
    private static final SqsClient sqsClient = SqsClient.create();
    private static final ObjectMapper objectMapper = new ObjectMapper();

    private static final String USERS_TABLE = System.getenv("USERS_TABLE");
    private static final String USER_CREATED_QUEUE_URL = System.getenv("USER_CREATED_QUEUE_URL");

    @Override
    public APIGatewayProxyResponseEvent handleRequest(APIGatewayProxyRequestEvent input, Context context) {
        try {
            String requestBody = input.getBody();
            UserDTO userData = objectMapper.readValue(requestBody, UserDTO.class);

            if (userData.getName() == null || userData.getName().isEmpty() || userData.getEmail() == null || userData.getEmail().isEmpty()) {
                return new APIGatewayProxyResponseEvent()
                        .withStatusCode(400)
                        .withBody("{\"message\": \"Faltan los campos 'name' o 'email'\"}");
            }

            String userId = UUID.randomUUID().toString();

            Map<String, AttributeValue> userItem = Map.of(
                    "id", AttributeValue.builder().s(userId).build(),
                    "name", AttributeValue.builder().s(userData.getName()).build(),
                    "email", AttributeValue.builder().s(userData.getEmail()).build()
            );

            PutItemRequest putItemRequest = PutItemRequest.builder()
                    .tableName(USERS_TABLE)
                    .item(userItem)
                    .build();

            dynamoDbClient.putItem(putItemRequest);

            String sqsMessageBody = objectMapper.writeValueAsString(Map.of(
                    "id", userId,
                    "name", userData.getName(),
                    "email", userData.getEmail()
            ));

            SendMessageRequest sendMessageRequest = SendMessageRequest.builder()
                    .queueUrl(USER_CREATED_QUEUE_URL)
                    .messageBody(sqsMessageBody)
                    .build();

            sqsClient.sendMessage(sendMessageRequest);

            String jsonResponse = objectMapper.writeValueAsString(Map.of(
                    "message", "Usuario creado con éxito",
                    "userId", userId
            ));

            return new APIGatewayProxyResponseEvent()
                    .withStatusCode(201)
                    .withHeaders(Map.of("Content-Type", "application/json"))
                    .withBody(jsonResponse);

        } catch (Exception e) {
            context.getLogger().log("Error al crear usuario: " + e.getMessage());
            return new APIGatewayProxyResponseEvent()
                    .withStatusCode(500)
                    .withBody("{\"message\": \"Error interno del servidor\"}");
        }
    }
}