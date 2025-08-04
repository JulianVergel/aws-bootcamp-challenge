package com.bootcamp_aws.monolith_api.application.handler;

import com.bootcamp_aws.monolith_api.application.dto.request.PersonRequestDto;
import com.bootcamp_aws.monolith_api.application.dto.response.PersonResponseDto;

public interface IPersonHandler {
    void savePerson(PersonRequestDto personRequestDto);
    PersonResponseDto getPersonById(Long id);
}
