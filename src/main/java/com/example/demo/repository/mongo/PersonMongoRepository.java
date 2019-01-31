package com.example.demo.repository.mongo;

import com.example.demo.entity.Person;
import com.example.demo.repository.PersonRepository;
import org.springframework.context.annotation.Profile;
import org.springframework.data.mongodb.repository.MongoRepository;

@Profile("mongo")
public interface PersonMongoRepository extends MongoRepository<Person, String>, PersonRepository {
}
