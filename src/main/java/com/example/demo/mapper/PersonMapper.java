package com.example.demo.mapper;

import com.example.demo.dto.PersonDto;
import com.example.demo.entity.Person;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface PersonMapper {
    PersonDto entityToDto(Person person);

    List<PersonDto> entityToDto(List<Person> people);

    Person dtoToEntity(PersonDto personDto);
}
