package com.bootcamp_aws.monolith_api.infrastructure.out.jpa.adapter;

import com.bootcamp_aws.monolith_api.domain.model.Person;
import com.bootcamp_aws.monolith_api.domain.spi.IPersonPersistencePort;
import com.bootcamp_aws.monolith_api.infrastructure.out.jpa.entity.PersonEntity;
import com.bootcamp_aws.monolith_api.infrastructure.out.jpa.mapper.IPersonEntityMapper;
import com.bootcamp_aws.monolith_api.infrastructure.out.jpa.repository.IPersonRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class PersonPersistenceJpaAdapter implements IPersonPersistencePort {

    private final IPersonRepository personRepository;
    private final IPersonEntityMapper personEntityMapper;

    @Override
    public Person savePerson(Person person) {
        PersonEntity personEntity = personEntityMapper.toEntity(person);
        PersonEntity savedEntity = personRepository.save(personEntity);
        return personEntityMapper.toPerson(savedEntity);
    }

    @Override
    public Optional<Person> findPersonById(Long id) {
        return personRepository.findById(id)
                .map(personEntityMapper::toPerson);
    }
}
