package com.example.demo.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.persistence.Entity;
import javax.validation.constraints.NotEmpty;

@Data
@Entity
@Document
@NoArgsConstructor
public class Person {
    @Id
    @javax.persistence.Id
    private String id;

    @NotEmpty
    private String name;
}
