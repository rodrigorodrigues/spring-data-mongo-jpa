package com.example.demo;

import com.example.demo.entity.CamisaTamanho;
import org.springframework.stereotype.Component;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter
@Component
public class CamisaTamanhoConverter implements AttributeConverter<CamisaTamanho, Integer> {
    @Override
    public Integer convertToDatabaseColumn(CamisaTamanho camisaTamanho) {
        return (camisaTamanho != null ? camisaTamanho.getId() : null);
    }

    @Override
    public CamisaTamanho convertToEntityAttribute(Integer id) {
        return (id != null ? CamisaTamanho.valueOf(id) : null);
    }
}
