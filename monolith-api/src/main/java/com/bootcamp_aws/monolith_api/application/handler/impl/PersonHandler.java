package com.bootcamp_aws.monolith_api.application.handler.impl;

import com.bootcamp_aws.monolith_api.application.dto.request.PersonRequestDto;
import com.bootcamp_aws.monolith_api.application.dto.response.PersonResponseDto;
import com.bootcamp_aws.monolith_api.application.handler.IPersonHandler;
import com.bootcamp_aws.monolith_api.application.mapper.request.IPersonRequestMapper;
import com.bootcamp_aws.monolith_api.application.mapper.response.IPersonResponseMapper;
import com.bootcamp_aws.monolith_api.domain.api.IPersonServicePort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class PersonHandler implements IPersonHandler {

    private final IPersonServicePort personServicePort;
    private final IPersonRequestMapper personRequestMapper;
    private final IPersonResponseMapper personResponseMapper;

    @Override
    public void savePerson(PersonRequestDto personRequestDto) {
        personServicePort.savePerson(personRequestMapper.toPerson(personRequestDto));
    }

    @Override
    public PersonResponseDto getPersonById(Long id) {
        return personResponseMapper.toResponseDto(personServicePort.getPersonById(id));
    }
}
