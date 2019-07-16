package com.example.demo.repository.jpa;

import com.example.demo.entity.Person;
import com.example.demo.repository.PersonRepository;
import org.springframework.context.annotation.Profile;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Check more details at https://docs.spring.io/spring-data/jpa/docs/current/reference/html/#repositories.query-methods.query-creation
 */
@Profile({"default", "jpa"})
public interface PersonJpaRepository extends JpaRepository<Person, String>, PersonRepository {
}
