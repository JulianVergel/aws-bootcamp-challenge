package com.bootcamp_aws.monolith_api.domain.utils.validators;

import com.bootcamp_aws.monolith_api.domain.exception.ValidationException;
import com.bootcamp_aws.monolith_api.domain.model.Person;
import com.bootcamp_aws.monolith_api.domain.utils.constants.DomainConstants;
import com.bootcamp_aws.monolith_api.domain.utils.validations.PersonValidator;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PersonValidatorTest {
    @Test
    void validatePersonForCreation_shouldPass_whenPersonIsValid() {
        Person validPerson = new Person(1L, "Carlos Santana", "carlos@santana.com");

        assertDoesNotThrow(() -> PersonValidator.validatePersonForCreation(validPerson));
    }

    @Test
    void validatePersonForCreation_shouldThrowException_whenNameIsNull() {
        Person personWithNullName = new Person(1L, null, "carlos@santana.com");

        ValidationException exception = assertThrows(ValidationException.class, () -> {
            PersonValidator.validatePersonForCreation(personWithNullName);
        });

        assertEquals(DomainConstants.FIELD_NAME_MUST_NOT_BE_NULL, exception.getMessage());
    }

    @Test
    void validatePersonForCreation_shouldThrowException_whenEmailIsNull() {
        Person personWithNullEmail = new Person(1L, "Carlos Santana", null);

        ValidationException exception = assertThrows(ValidationException.class, () -> {
            PersonValidator.validatePersonForCreation(personWithNullEmail);
        });

        assertEquals(DomainConstants.FIELD_EMAIL_MUST_NOT_BE_NULL, exception.getMessage());
    }

    @Test
    void validatePersonForCreation_shouldThrowException_whenEmailIsInvalid() {
        Person personWithInvalidEmail = new Person(1L, "Carlos Santana", "email-invalido");

        ValidationException exception = assertThrows(ValidationException.class, () -> {
            PersonValidator.validatePersonForCreation(personWithInvalidEmail);
        });

        assertEquals(DomainConstants.INVALID_EMAIL_FORMAT, exception.getMessage());
    }
}
