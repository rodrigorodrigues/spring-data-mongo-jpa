package com.example.demo.service;

import com.example.demo.dto.PersonDto;
import com.example.demo.entity.Person;
import com.example.demo.mapper.PersonMapper;
import com.example.demo.repository.PersonRepository;
import com.example.demo.repository.mongo.PersonMongoRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@AllArgsConstructor
public class PersonServiceImpl implements PersonService {

    private final PersonRepository personRepository;

    private final PersonMapper personMapper;

    @Override
    public List<PersonDto> getAllPeople() {
        List<PersonDto> people = personMapper.entityToDto(personRepository.findAll());
        people.forEach(p -> p.setProvider(personRepository instanceof PersonMongoRepository ? "MongoDB" : "JPA"));
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
}
