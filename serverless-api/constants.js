'use strict';

const HTTP_STATUS = {
  OK: 200,
  CREATED: 201,
  BAD_REQUEST: 400,
  NOT_FOUND: 404,
  INTERNAL_SERVER_ERROR: 500,
};

const MESSAGES = {
  USER_CREATED_SUCCESS: "Usuario creado exitosamente en DynamoDB.",
  USER_FOUND: "Usuario encontrado.",
  USER_UPDATED_SUCCESS: "Usuario actualizado exitosamente.",
  USER_DELETED_SUCCESS: "Usuario eliminado exitosamente.",

  USER_NOT_FOUND: "Usuario no encontrado.",
  COULD_NOT_CREATE_USER: "No se pudo crear el usuario.",
  COULD_NOT_GET_USER: "No se pudo obtener el usuario.",
  COULD_NOT_UPDATE_USER: "No se pudo actualizar el usuario.",
  COULD_NOT_DELETE_USER: "No se pudo eliminar el usuario.",

  REQUIRED_FIELDS: "Los campos 'name' y 'email' son obligatorios.",

  COULD_NOT_ERROR_CONTROL: "Error no controlado: ",
  INTERNAL_SERVER_ERROR: "Error interno del servidor.",
};

module.exports = {
  HTTP_STATUS,
  MESSAGES,
};