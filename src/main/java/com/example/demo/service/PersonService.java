package com.example.demo.service;

import com.example.demo.dto.PersonDto;

import java.util.List;

public interface PersonService {
    List<PersonDto> getAllPeople();

    PersonDto createPerson(PersonDto personDto);

    void delete(PersonDto personDto);

    PersonDto updatePerson(PersonDto personDto);

    PersonDto findById(String id);
}
