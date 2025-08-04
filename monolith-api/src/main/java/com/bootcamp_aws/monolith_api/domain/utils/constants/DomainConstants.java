package com.bootcamp_aws.monolith_api.domain.utils.constants;

import com.bootcamp_aws.monolith_api.domain.exception.UtilityClassException;

import java.util.regex.Pattern;

public class DomainConstants {
    private DomainConstants() {
        throw new UtilityClassException();
    }

    public static final String UTILITY_CLASS_MESSAGE = "Esta es una clase de utilidad y no puede ser instanciada.";

    // Validation Messages
    public static final String FIELD_NAME_MUST_NOT_BE_NULL = "El campo nombre no puede ser nulo.";
    public static final String FIELD_EMAIL_MUST_NOT_BE_NULL = "El campo email no puede ser nulo.";
    public static final String INVALID_EMAIL_FORMAT = "El formato del email es inválido.";

    // Regex simple para validar el formato de un email.
    public static final Pattern EMAIL_PATTERN = Pattern.compile(
            "^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE
    );

    // Success Messages
    public static final String PERSON_CREATED_SUCCESSFULLY = "Persona creada exitosamente.";

    // Error Messages
    public static final String PERSON_NOT_FOUND = "La persona con el ID proporcionado no fue encontrada.";
}
