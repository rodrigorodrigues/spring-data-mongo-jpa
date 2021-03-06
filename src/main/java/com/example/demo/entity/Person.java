package com.example.demo.entity;

import com.example.demo.CamisaTamanhoConverter;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.validation.constraints.NotEmpty;

@Data
@Entity
@Document
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class Person {
    @Id
    @javax.persistence.Id
    private String id;

    @NotEmpty
    private String name;

    @Convert(converter = CamisaTamanhoConverter.class)
    @Column(name = "camisa_tamanho")
    private CamisaTamanho camisaTamanho;
}
