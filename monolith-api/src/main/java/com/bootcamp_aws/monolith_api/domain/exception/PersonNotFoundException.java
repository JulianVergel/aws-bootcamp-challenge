package com.bootcamp_aws.monolith_api.domain.exception;

import static com.bootcamp_aws.monolith_api.domain.utils.constants.DomainConstants.PERSON_NOT_FOUND;

public class PersonNotFoundException extends RuntimeException {
    public PersonNotFoundException() {
        super(
                PERSON_NOT_FOUND
        );
    }
}
