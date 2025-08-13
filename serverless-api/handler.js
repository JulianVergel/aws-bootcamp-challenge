'use strict';

const getBody = (event) => {
  if (typeof event.body === 'string') {
    return JSON.parse(event.body);
  }
  return event.body;
};

// --- Función para CREAR un usuario (POST /users) ---
module.exports.createUser = async (event) => {
  try {
    const userData = getBody(event);
    console.log('Recibida petición para crear usuario:', userData);

    const response = {
      message: "Usuario creado exitosamente (simulación)",
      userData: userData,
    };

    return {
      statusCode: 201,
      body: JSON.stringify(response, null, 2),
    };
  } catch (error) {
    console.error("Error al parsear el body:", error);
    return {
      statusCode: 400, // Bad Request
      body: JSON.stringify({ message: "El cuerpo de la petición no es un JSON válido." }),
    };
  }
};

// --- Función para CONSULTAR un usuario (GET /users/{id}) ---
module.exports.getUser = async (event) => {
  const userId = event.pathParameters.id;
  console.log('Recibida petición para consultar el usuario con ID:', userId);

  const user = {
    id: userId,
    nombre: "Juan Gabriel",
    email: "juangabriel@eldivo.com",
  };

  return {
    statusCode: 200,
    body: JSON.stringify(user, null, 2),
  };
};

// --- Función para ACTUALIZAR un usuario (PUT /users/{id}) ---
module.exports.updateUser = async (event) => {
  try {
    const userId = event.pathParameters.id;
    const updatedData = getBody(event);
    console.log('Recibida petición para actualizar el usuario con ID:', userId);

    const response = {
      message: `Usuario con ID ${userId} actualizado exitosamente (simulación)`,
      updatedData: updatedData,
    };

    return {
      statusCode: 200,
      body: JSON.stringify(response, null, 2),
    };
  } catch (error) {
    console.error("Error al parsear el body:", error);
    return {
      statusCode: 400,
      body: JSON.stringify({ message: "El cuerpo de la petición no es un JSON válido." }),
    };
  }
};

// --- Función para ELIMINAR un usuario (DELETE /users/{id}) ---
module.exports.deleteUser = async (event) => {
  const userId = event.pathParameters.id;
  console.log('Recibida petición para eliminar el usuario con ID:', userId);

  const response = {
    message: `Usuario con ID ${userId} eliminado exitosamente (simulación)`,
  };

  return {
    statusCode: 200,
    body: JSON.stringify(response, null, 2),
  };
};