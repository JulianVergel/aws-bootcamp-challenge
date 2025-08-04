package com.bootcamp_aws.monolith_api.application.mapper.response;

import com.bootcamp_aws.monolith_api.application.dto.response.PersonResponseDto;
import com.bootcamp_aws.monolith_api.domain.model.Person;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        unmappedSourcePolicy = ReportingPolicy.IGNORE)
public interface IPersonResponseMapper {
    PersonResponseDto toResponseDto(Person person);
}
