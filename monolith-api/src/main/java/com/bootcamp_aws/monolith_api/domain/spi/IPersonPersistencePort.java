package com.bootcamp_aws.monolith_api.domain.spi;

import com.bootcamp_aws.monolith_api.domain.model.Person;

import java.util.Optional;

public interface IPersonPersistencePort {
    Person savePerson(Person person);
    Optional<Person> findPersonById(Long id);
}
