package com.example.demo.repository.mongo;

import com.example.demo.entity.Person;
import com.example.demo.service.PersonService;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@DataMongoTest
@ActiveProfiles("mongo")
public class PersonMongoRepositoryTest {

    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    private PersonMongoRepository repository;

    @MockBean
    PersonService personService;

    @Before
    public void setup() {
        mongoTemplate.save(Person.builder().id("c").name("Rodrigo Santos").build());
        mongoTemplate.save(Person.builder().id("a").name("Anna Santos").build());
        mongoTemplate.save(Person.builder().id("b").name("Elias Junior").build());
    }

    @After
    public void tearDown() {
        repository.deleteAll();
    }

    @Test
    public void testFindByNameEndingWithOrderByName() {
        List<Person> people = repository.findByNameEndingWithOrderByName("Santos");

        assertThat(people.size()).isEqualTo(2);

        assertThat(people.stream().map(Person::getId)
                .collect(Collectors.toList()))
                .containsExactly("a", "c");
    }

    @Test
    public void testCountByNameContaining() {
        Integer count = repository.countByNameContaining("r");

        assertThat(count).isEqualTo(2);
    }
}