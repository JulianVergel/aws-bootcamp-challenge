package com.bootcamp_aws.monolith_api.domain.exception;

public class ValidationException extends RuntimeException {
    public ValidationException(String message) {
        super(message);
    }
}
