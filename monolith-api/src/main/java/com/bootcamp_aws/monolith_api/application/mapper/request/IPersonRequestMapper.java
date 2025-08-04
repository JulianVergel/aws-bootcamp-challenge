package com.bootcamp_aws.monolith_api.application.mapper.request;

import com.bootcamp_aws.monolith_api.application.dto.request.PersonRequestDto;
import com.bootcamp_aws.monolith_api.domain.model.Person;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        unmappedSourcePolicy = ReportingPolicy.IGNORE)
public interface IPersonRequestMapper {
    Person toPerson(PersonRequestDto personRequestDto);
}
