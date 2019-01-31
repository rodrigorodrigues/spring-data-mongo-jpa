package com.example.demo.controller;

import com.example.demo.dto.PersonDto;
import com.example.demo.service.PersonService;
import lombok.AllArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/persons")
@AllArgsConstructor
public class PersonController {
    private final PersonService personService;

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<PersonDto>> getAllPeople() {
        return ResponseEntity.ok(personService.getAllPeople());
    }

    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<PersonDto> createPerson(PersonDto personDto) {
        personDto = personService.createPerson(personDto);
        return ResponseEntity.created(URI.create(String.format("/api/persons/%s", personDto.getId())))
                .body(personDto);
    }

    @PutMapping(produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<PersonDto> updatePerson(PersonDto personDto) {
        return ResponseEntity.ok(personService.updatePerson(personDto));
    }

    @DeleteMapping
    public ResponseEntity deletePerson(String id) {
        personService.delete(personService.findById(id));
        return ResponseEntity.noContent().build();
    }
}
