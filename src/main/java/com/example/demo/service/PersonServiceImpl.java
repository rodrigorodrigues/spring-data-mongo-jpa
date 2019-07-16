package com.example.demo.service;

import com.example.demo.dto.PersonDto;
import com.example.demo.entity.Person;
import com.example.demo.mapper.PersonMapper;
import com.example.demo.repository.PersonRepository;
import com.example.demo.repository.mongo.PersonMongoRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.*;
import java.nio.file.Files;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Transactional
@Service
public class PersonServiceImpl implements PersonService {

    private final PersonRepository personRepository;

    private final PersonMapper personMapper;

    @Value("${csvSeparator:;}") // Get separator by parameter '-DcsvSeparator=|' or default ';'
    private String csvSeparator;

    public PersonServiceImpl(PersonRepository personRepository, PersonMapper personMapper) {
        this.personRepository = personRepository;
        this.personMapper = personMapper;
    }

    @Override
    public List<PersonDto> getAllPeople() {
        List<PersonDto> people = personMapper.entityToDto(personRepository.findAll());
        applyProviderType(people);
        return people;
    }

    @Override
    public PersonDto createPerson(PersonDto personDto) {
        if (personDto.getId() == null) {
            personDto.setId(UUID.randomUUID().toString());
        }
        return personMapper.entityToDto(personRepository.save(personMapper.dtoToEntity(personDto)));
    }

    @Override
    public void delete(PersonDto personDto) {
        personRepository.delete(personMapper.dtoToEntity(personDto));
    }

    @Override
    public PersonDto updatePerson(PersonDto personDto) {
        return createPerson(personDto);
    }

    @Override
    public PersonDto findById(String id) {
        Optional<Person> person = personRepository.findById(id);
        if (!person.isPresent()) {
            throw new ResourceNotFoundException(String.format("Person Not Found with id %s!", id));
        }
        return personMapper.entityToDto(person.get());
    }

    @Override
    public List<PersonDto> getByNameEndingWithOrderByName(String name) {
        List<PersonDto> people = personMapper.entityToDto(personRepository.findByNameEndingWithOrderByName(name));
        applyProviderType(people);
        return people;
    }

    private void applyProviderType(List<PersonDto> people) { //Simple example how to use lambda expression
        people.forEach(p -> p.setProvider(personRepository instanceof PersonMongoRepository ? "MongoDB" : "JPA"));
    }

    @Override
    public Integer totalByNameContaining(String name) {
        return personRepository.countByNameContaining(name);
    }

    /**
     * Example of creating a list of people by CSV.
     * In this case is used InputStream as we can get this file by Rest Api
     * @param is input stream file
     */
    @Override //TODO Juninho
    public void createPeople(InputStream is) throws IOException {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(is))) {
            reader.lines() // Read all lines
                    .map(l -> l.split(csvSeparator)) // Split by separator
                    .map(l -> Person.builder().id(l[0]).name(l[1]).build()) // Create person entity
                    .map(personRepository::save) // Save
                    .forEach(System.out::println); // This method it is important because the others just 'prepare' the stream, this one applies.
        }
    }

    @Override
    public void createPeople(File file) throws IOException {
        Files.lines(file.toPath())
                .map(l -> l.split(csvSeparator)) // Split by separator
                .map(l -> Person.builder().id(l[0]).name(l[1]).build()) // Create person entity
                .map(personRepository::save) // Save
                .forEach(System.out::println); // This method it is important because the others just 'prepare' the stream, this one applies.
    }
}
