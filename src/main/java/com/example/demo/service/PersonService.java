package com.example.demo.service;

import com.example.demo.dto.PersonDto;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

/**
 * Good practice in Spring is always have interfaces to define methods instead calling constructor class straight.
 */
public interface PersonService {
    List<PersonDto> getAllPeople();

    PersonDto createPerson(PersonDto personDto);

    void delete(PersonDto personDto);

    PersonDto updatePerson(PersonDto personDto);

    PersonDto findById(String id);

    List<PersonDto> getByNameEndingWithOrderByName(String name); // Another good practice is to have DTO instead calling entities straight.

    Integer totalByNameContaining(String name);

    void createPeople(InputStream is) throws IOException; // Better to use InputStream as we can get this by rest api

    void createPeople(File file) throws IOException; // It would work but not great because as the file must exist in OS
}
