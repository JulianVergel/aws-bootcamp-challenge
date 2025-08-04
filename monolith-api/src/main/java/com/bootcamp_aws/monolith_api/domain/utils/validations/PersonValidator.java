package com.bootcamp_aws.monolith_api.domain.utils.validations;

import com.bootcamp_aws.monolith_api.domain.exception.UtilityClassException;
import com.bootcamp_aws.monolith_api.domain.exception.ValidationException;
import com.bootcamp_aws.monolith_api.domain.model.Person;

import static com.bootcamp_aws.monolith_api.domain.utils.constants.DomainConstants.*;

public class PersonValidator {
    private PersonValidator() {
        throw new UtilityClassException();
    }

    public static void validatePersonForCreation(Person person) {
        if (person.getName() == null || person.getName().trim().isEmpty()) {
            throw new ValidationException(FIELD_NAME_MUST_NOT_BE_NULL);
        }

        if (person.getEmail() == null || person.getEmail().trim().isEmpty()) {
            throw new ValidationException(FIELD_EMAIL_MUST_NOT_BE_NULL);
        }

        if (!EMAIL_PATTERN.matcher(person.getEmail()).matches()) {
            throw new ValidationException(INVALID_EMAIL_FORMAT);
        }
    }
}
