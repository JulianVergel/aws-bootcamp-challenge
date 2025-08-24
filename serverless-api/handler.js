'use strict';

const { DynamoDBClient } = require("@aws-sdk/client-dynamodb");
const { DynamoDBDocumentClient, GetCommand, DeleteCommand } = require("@aws-sdk/lib-dynamodb");
const { SNSClient, PublishCommand } = require("@aws-sdk/client-sns");
const { HTTP_STATUS, MESSAGES } = require('./constants');

const docClient = DynamoDBDocumentClient.from(new DynamoDBClient({}));
const USERS_TABLE = process.env.USERS_TABLE;
const snsClient = new SNSClient({});
const NOTIFICATIONS_TOPIC_ARN = process.env.NOTIFICATIONS_TOPIC_ARN;

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

const deleteUserLogic = async (event) => {
  const userId = event.pathParameters.id;
  const params = { TableName: USERS_TABLE, Key: { id: userId } };
  await docClient.send(new DeleteCommand(params));
  return createResponse(HTTP_STATUS.OK, { message: MESSAGES.USER_DELETED_SUCCESS });
};

// --- LÓGICA PARA ENVIAR CORREOS (Notificaciones) ---
const sendEmailLogic = async (event) => {
  console.log("Función sendEmail disparada por SQS:", JSON.stringify(event, null, 2));

  for (const record of event.Records) {
    try {
      const newUser = JSON.parse(record.body);

      const messageBody = `¡Bienvenido a bordo, ${newUser.name}! Estamos felices de tenerte con nosotros. Tu ID de usuario es ${newUser.id} y tu correo registrado es ${newUser.email}.`;

      const params = {
        TopicArn: NOTIFICATIONS_TOPIC_ARN,
        Subject: "¡Bienvenido a Nuestra Plataforma!",
        Message: messageBody,
      };
      
      await snsClient.send(new PublishCommand(params));
      console.log(`Notificación enviada para el usuario: ${newUser.id}`);

    } catch (error) {
      console.error("Error procesando un mensaje de SQS o enviando a SNS:", error);
    }
  }

  return {
    statusCode: 200,
    body: JSON.stringify({ message: "Proceso de notificaciones completado." }),
  };
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

module.exports.getUser = errorHandlerWrapper(getUserLogic);
module.exports.deleteUser = errorHandlerWrapper(deleteUserLogic);
module.exports.sendEmail = errorHandlerWrapper(sendEmailLogic);