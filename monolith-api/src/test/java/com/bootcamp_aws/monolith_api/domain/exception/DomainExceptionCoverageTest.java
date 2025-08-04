package com.bootcamp_aws.monolith_api.domain.exception;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;

class DomainExceptionCoverageTest {
    @Test
    void testExceptionConstructors() {
        String message = "Test message";
        assertNotNull(new PersonNotFoundException());
        assertNotNull(new ValidationException(message));
        assertNotNull(new UtilityClassException());
    }
}
