package com.bootcamp_aws.monolith_api.domain.exception;

import static com.bootcamp_aws.monolith_api.domain.utils.constants.DomainConstants.UTILITY_CLASS_MESSAGE;

public class UtilityClassException extends RuntimeException {
    public UtilityClassException() {
        super(
                UTILITY_CLASS_MESSAGE
        );
    }
}
