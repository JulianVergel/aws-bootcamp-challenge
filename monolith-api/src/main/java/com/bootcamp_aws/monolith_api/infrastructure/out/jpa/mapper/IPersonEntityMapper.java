package com.bootcamp_aws.monolith_api.infrastructure.out.jpa.mapper;

import com.bootcamp_aws.monolith_api.domain.model.Person;
import com.bootcamp_aws.monolith_api.infrastructure.out.jpa.entity.PersonEntity;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        unmappedSourcePolicy = ReportingPolicy.IGNORE)
public interface IPersonEntityMapper {
    PersonEntity toEntity(Person person);
    Person toPerson(PersonEntity personEntity);
}
