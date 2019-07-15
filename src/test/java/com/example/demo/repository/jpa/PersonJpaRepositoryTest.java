package com.example.demo.repository.jpa;

import com.example.demo.entity.Person;
import com.example.demo.service.PersonService;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@DataJpaTest
public class PersonJpaRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private PersonJpaRepository repository;

    @MockBean
    PersonService personService;

    @Before
    public void setup() {
        entityManager.persist(Person.builder().id("c").name("Rodrigo Santos").build());
        entityManager.persist(Person.builder().id("a").name("Anna Santos").build());
        entityManager.persist(Person.builder().id("b").name("Elias Junior").build());
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