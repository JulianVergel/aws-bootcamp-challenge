package com.bootcamp_aws.monolith_api.domain.usecase;

import com.bootcamp_aws.monolith_api.domain.exception.PersonNotFoundException;
import com.bootcamp_aws.monolith_api.domain.model.Person;
import com.bootcamp_aws.monolith_api.domain.spi.IPersonPersistencePort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PersonUseCaseTest {
    @Mock
    private IPersonPersistencePort personPersistencePort;

    @InjectMocks
    private PersonUseCase personUseCase;

    private Person person;

    @BeforeEach
    void setUp() {
        person = new Person(1L, "Test User", "test@user.com");
    }

    @Test
    void savePerson_shouldCallPersistencePort_whenPersonIsValid() {
        personUseCase.savePerson(person);

        verify(personPersistencePort, times(1)).savePerson(person);
    }

    @Test
    void getPersonById_shouldReturnPerson_whenFound() {
        when(personPersistencePort.findPersonById(1L)).thenReturn(Optional.of(person));

        Person result = personUseCase.getPersonById(1L);

        assertNotNull(result);
        assertEquals(person.getName(), result.getName());
        verify(personPersistencePort, times(1)).findPersonById(1L);
    }

    @Test
    void getPersonById_shouldThrowException_whenNotFound() {
        when(personPersistencePort.findPersonById(1L)).thenReturn(Optional.empty());

        assertThrows(PersonNotFoundException.class, () -> {
            personUseCase.getPersonById(1L);
        });
        verify(personPersistencePort, times(1)).findPersonById(1L);
    }
}
