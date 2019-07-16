package com.example.demo.controller;

import com.example.demo.dto.PersonDto;
import com.example.demo.service.PersonService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URI;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/persons") // path at class level will apply for all methods
@AllArgsConstructor
public class PersonController {
    private final PersonService personService;

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE) // GET - /api/persons
    public ResponseEntity<List<PersonDto>> getAllPeople() {
        return ResponseEntity.ok(personService.getAllPeople());
    }

    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE) // POST - /api/persons
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

    @GetMapping("/names/total/endingWith")
    public ResponseEntity<Integer> totalByNameContaining(@RequestParam String name) {
        return ResponseEntity.ok(personService.totalByNameContaining(name));
    }

    @GetMapping("/names/endingWith")
    public ResponseEntity<List<PersonDto>> getByNameEndingWithOrderByName(@RequestParam String name) {
        return ResponseEntity.ok(personService.getByNameEndingWithOrderByName(name));
    }

    @PostMapping("/uploadCSV") //TODO Juninho
    public ResponseEntity<String> uploadCsv(@RequestParam("file") MultipartFile file) {
        try {
            personService.createPeople(file.getInputStream());
            return ResponseEntity.ok("Successful!");
        } catch (IOException e) {
            log.error("Error on method uploadCsv");
            return ResponseEntity.badRequest().body(String.format("Error: %s", e.getLocalizedMessage()));
        }
    }
}
