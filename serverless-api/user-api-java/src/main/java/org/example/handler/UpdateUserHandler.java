package org.example.handler;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Map;

import org.example.dto.UserDTO;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;
import software.amazon.awssdk.services.dynamodb.model.UpdateItemRequest;
import software.amazon.awssdk.services.dynamodb.model.ReturnValue;

public class UpdateUserHandler implements RequestHandler<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent> {

    private static final DynamoDbClient dynamoDbClient = DynamoDbClient.create();
    private static final ObjectMapper objectMapper = new ObjectMapper();
    private static final String USERS_TABLE = System.getenv("USERS_TABLE");

    @Override
    public APIGatewayProxyResponseEvent handleRequest(APIGatewayProxyRequestEvent input, Context context) {
        try {
            String userId = input.getPathParameters().get("id");

            String requestBody = input.getBody();
            UserDTO updatedData = objectMapper.readValue(requestBody, UserDTO.class);

            Map<String, AttributeValue> key = Map.of("id", AttributeValue.builder().s(userId).build());

            Map<String, String> expressionAttributeNames = Map.of("#name", "name");

            Map<String, AttributeValue> expressionAttributeValues = Map.of(
                    ":n", AttributeValue.builder().s(updatedData.getName()).build(),
                    ":e", AttributeValue.builder().s(updatedData.getEmail()).build()
            );

            UpdateItemRequest updateRequest = UpdateItemRequest.builder()
                    .tableName(USERS_TABLE)
                    .key(key)
                    .updateExpression("set #name = :n, email = :e")
                    .expressionAttributeNames(expressionAttributeNames)
                    .expressionAttributeValues(expressionAttributeValues)
                    .returnValues(ReturnValue.ALL_NEW)
                    .build();

            dynamoDbClient.updateItem(updateRequest);

            String jsonResponse = objectMapper.writeValueAsString(Map.of("message", "Usuario actualizado con éxito"));

            return new APIGatewayProxyResponseEvent()
                    .withStatusCode(200)
                    .withHeaders(Map.of("Content-Type", "application/json"))
                    .withBody(jsonResponse);

        } catch (Exception e) {
            context.getLogger().log("Error al actualizar usuario: " + e.getMessage());
            return new APIGatewayProxyResponseEvent()
                    .withStatusCode(500)
                    .withBody("{\"message\": \"Error interno del servidor\"}");
        }
    }
}