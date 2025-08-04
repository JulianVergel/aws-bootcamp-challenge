package com.bootcamp_aws.monolith_api.domain.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class PersonTest {
    @Test
    void testPersonModel() {
        Long id = 1L;
        String name = "Test Name";
        String email = "test@email.com";

        Person person = new Person(id, name, email);
        person.setId(2L);
        person.setName("New Name");
        person.setEmail("new@email.com");

        assertEquals(2L, person.getId());
        assertEquals("New Name", person.getName());
        assertEquals("new@email.com", person.getEmail());
    }
}
