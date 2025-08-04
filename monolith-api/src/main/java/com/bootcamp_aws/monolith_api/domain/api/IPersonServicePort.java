package com.bootcamp_aws.monolith_api.domain.api;

import com.bootcamp_aws.monolith_api.domain.model.Person;

public interface IPersonServicePort {
    void savePerson(Person person);
    Person getPersonById(Long id);
}
