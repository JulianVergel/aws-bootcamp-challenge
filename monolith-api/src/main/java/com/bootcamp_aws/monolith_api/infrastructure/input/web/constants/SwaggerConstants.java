package com.bootcamp_aws.monolith_api.infrastructure.input.web.constants;

import com.bootcamp_aws.monolith_api.domain.exception.UtilityClassException;

public final class SwaggerConstants {

    private SwaggerConstants() {
        throw new UtilityClassException();
    }

    public static final String PERSON_SAVE_OPERATION_SUMMARY = "Guardar una nueva persona";
    public static final String PERSON_GET_BY_ID_OPERATION_SUMMARY = "Obtener una persona por su ID";

    public static final String RESPONSE_200_DESCRIPTION = "Recurso encontrado exitosamente.";
    public static final String RESPONSE_201_DESCRIPTION = "Recurso creado exitosamente.";
    public static final String RESPONSE_400_DESCRIPTION = "Petición inválida o mal formada.";
    public static final String RESPONSE_404_DESCRIPTION = "Recurso no encontrado.";

}
