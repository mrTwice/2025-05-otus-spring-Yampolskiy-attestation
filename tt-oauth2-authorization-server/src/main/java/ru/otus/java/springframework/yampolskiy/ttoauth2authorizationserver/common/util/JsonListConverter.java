package ru.otus.java.springframework.yampolskiy.ttoauth2authorizationserver.common.util;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

import java.util.Collections;
import java.util.List;

@Converter(autoApply = true)
public class JsonListConverter implements AttributeConverter<List<String>, String> {
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    @Override
    public String convertToDatabaseColumn(List<String> attribute) {
        try {
            return (attribute != null) ? OBJECT_MAPPER.writeValueAsString(attribute) : "[]";
        } catch (Exception e) {
            throw new RuntimeException("Ошибка сериализации JSON", e);
        }
    }

    @Override
    public List<String> convertToEntityAttribute(String dbData) {
        try {
            return (dbData != null && !dbData.isEmpty()) ?
                    OBJECT_MAPPER.readValue(dbData, new TypeReference<List<String>>() {}) :
                    Collections.emptyList();
        } catch (Exception e) {
            throw new RuntimeException("Ошибка десериализации JSON", e);
        }
    }
}