'use strict';

const { DynamoDBClient } = require("@aws-sdk/client-dynamodb");
const { DynamoDBDocumentClient, PutCommand, GetCommand, UpdateCommand, DeleteCommand } = require("@aws-sdk/lib-dynamodb");
const { SNSClient, PublishCommand } = require("@aws-sdk/client-sns");
const { SQSClient, SendMessageCommand } = require("@aws-sdk/client-sqs");
const crypto = require("crypto");
const { HTTP_STATUS, MESSAGES } = require('./constants');

const docClient = DynamoDBDocumentClient.from(new DynamoDBClient({}));
const USERS_TABLE = process.env.USERS_TABLE;
const snsClient = new SNSClient({});
const NOTIFICATIONS_TOPIC_ARN = process.env.NOTIFICATIONS_TOPIC_ARN;
const sqsClient = new SQSClient({});
const USER_CREATED_QUEUE_URL = process.env.USER_CREATED_QUEUE_URL;

// --- FUNCIÓN AUXILIAR PARA CREAR RESPUESTAS ---
const createResponse = (statusCode, body) => {
  return {
    statusCode,
    headers: {
      "Content-Type": "application/json",
      "Access-Control-Allow-Origin": "*",
    },
    body: JSON.stringify(body),
  };
};

// --- LÓGICA DE NEGOCIO SEPARADA ---
const createUserLogic = async (event) => {
  const userData = JSON.parse(event.body);
  if (!userData.name || !userData.email) {
    return createResponse(HTTP_STATUS.BAD_REQUEST, { message: MESSAGES.REQUIRED_FIELDS });
  }

  const userId = crypto.randomUUID();
  const newUser = {
    id: userId,
    name: userData.name,
    email: userData.email
  };

  const dynamoParams = {
    TableName: USERS_TABLE,
    Item: newUser,
  };
  await docClient.send(new PutCommand(dynamoParams));

  const sqsParams = {
    QueueUrl: USER_CREATED_QUEUE_URL,
    MessageBody: JSON.stringify(newUser),
  };
  await sqsClient.send(new SendMessageCommand(sqsParams));

  console.log(MESSAGES.USER_CREATED_SUCCESS);

  return createResponse(HTTP_STATUS.CREATED, { message: MESSAGES.USER_CREATED_SUCCESS, userId });
};

const getUserLogic = async (event) => {
  const userId = event.pathParameters.id;
  const params = { TableName: USERS_TABLE, Key: { id: userId } };
  const { Item } = await docClient.send(new GetCommand(params));

  if (Item) {
    return createResponse(HTTP_STATUS.OK, Item);
  } else {
    return createResponse(HTTP_STATUS.NOT_FOUND, { message: MESSAGES.USER_NOT_FOUND });
  }
};

const updateUserLogic = async (event) => {
  const userId = event.pathParameters.id;
  const updatedData = JSON.parse(event.body);
  if (!updatedData.name || !updatedData.email) {
    return createResponse(HTTP_STATUS.BAD_REQUEST, { message: MESSAGES.REQUIRED_FIELDS }); // <-- USANDO CONSTANTE
  }

  const params = {
    TableName: USERS_TABLE,
    Key: { id: userId },
    UpdateExpression: "set #name = :n, email = :e",
    ExpressionAttributeNames: { "#name": "name" },
    ExpressionAttributeValues: { ":n": updatedData.name, ":e": updatedData.email },
    ReturnValues: "ALL_NEW",
  };

  const { Attributes } = await docClient.send(new UpdateCommand(params));
  return createResponse(HTTP_STATUS.OK, Attributes);
};

const deleteUserLogic = async (event) => {
  const userId = event.pathParameters.id;
  const params = { TableName: USERS_TABLE, Key: { id: userId } };
  await docClient.send(new DeleteCommand(params));
  return createResponse(HTTP_STATUS.OK, { message: MESSAGES.USER_DELETED_SUCCESS });
};

// --- ENVIAR CORREOS (Notificaciones) ---
const sendEmailLogic = async (event) => {
  console.log("Función sendEmail disparada por SQS:", JSON.stringify(event, null, 2));

  for (const record of event.Records) {
    const newUser = JSON.parse(record.body);

    const messageBody = MESSAGES.WELCOME_EMAIL_BODY
      .replace('{name}', newUser.name)
      .replace('{id}', newUser.id)
      .replace('{email}', newUser.email);

    const params = {
      TopicArn: NOTIFICATIONS_TOPIC_ARN,
      Subject: MESSAGES.WELCOME_EMAIL_SUBJECT,
      Message: messageBody,
    };

    try {
      await snsClient.send(new PublishCommand(params));
      console.log(`Notificación enviada para el usuario: ${newUser.id}`);
    } catch (snsError) {
      console.error(MESSAGES.ERROR_SENDING_NOTIFICATION, snsError);
    }
  }

  return createResponse(HTTP_STATUS.OK, { message: MESSAGES.NOTIFICATION_PROCESS_COMPLETE });
};

// --- MANEJADORES DE LAMBDA (WRAPPERS CON MANEJO DE ERRORES) ---
const errorHandlerWrapper = (handler) => async (event) => {
  try {
    return await handler(event);
  } catch (error) {
    console.error(MESSAGES.COULD_NOT_ERROR_CONTROL, error);
    const errorMessageKey = `COULD_NOT_${handler.name.replace('Logic', '').toUpperCase()}_USER`;
    const errorMessage = MESSAGES[errorMessageKey] || MESSAGES.INTERNAL_SERVER_ERROR;
    return createResponse(HTTP_STATUS.INTERNAL_SERVER_ERROR, { message: errorMessage, error: error.message });
  }
};

module.exports.createUser = errorHandlerWrapper(createUserLogic);
module.exports.getUser = errorHandlerWrapper(getUserLogic);
module.exports.updateUser = errorHandlerWrapper(updateUserLogic);
module.exports.deleteUser = errorHandlerWrapper(deleteUserLogic);
module.exports.sendEmail = errorHandlerWrapper(sendEmailLogic);