package com.bootcamp_aws.monolith_api.domain.usecase;

import com.bootcamp_aws.monolith_api.domain.api.IPersonServicePort;
import com.bootcamp_aws.monolith_api.domain.exception.PersonNotFoundException;
import com.bootcamp_aws.monolith_api.domain.model.Person;
import com.bootcamp_aws.monolith_api.domain.spi.IPersonPersistencePort;

import static com.bootcamp_aws.monolith_api.domain.utils.validations.PersonValidator.validatePersonForCreation;

public class PersonUseCase implements IPersonServicePort {

    private final IPersonPersistencePort personPersistencePort;

    public PersonUseCase(IPersonPersistencePort personPersistencePort) {
        this.personPersistencePort = personPersistencePort;
    }

    @Override
    public void savePerson(Person person) {
        validatePersonForCreation(person);
        personPersistencePort.savePerson(person);
    }

    @Override
    public Person getPersonById(Long id) {
        return personPersistencePort.findPersonById(id)
                .orElseThrow(PersonNotFoundException::new);
    }
}
