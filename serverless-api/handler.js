'use strict';

const { DynamoDBClient } = require("@aws-sdk/client-dynamodb");
const { DynamoDBDocumentClient, PutCommand, GetCommand, UpdateCommand, DeleteCommand } = require("@aws-sdk/lib-dynamodb");
const crypto = require("crypto");
const { HTTP_STATUS, MESSAGES } = require('./constants');

const docClient = DynamoDBDocumentClient.from(new DynamoDBClient({}));
const USERS_TABLE = process.env.USERS_TABLE;

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
    return createResponse(HTTP_STATUS.BAD_REQUEST, { message: MESSAGES.REQUIRED_FIELDS }); // <-- USANDO CONSTANTE
  }

  const userId = crypto.randomUUID();
  const params = {
    TableName: USERS_TABLE,
    Item: { id: userId, name: userData.name, email: userData.email },
  };

  await docClient.send(new PutCommand(params));
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