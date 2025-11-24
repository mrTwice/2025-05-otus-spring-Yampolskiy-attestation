package ru.otus.java.springframework.yampolskiy.ttuserservice.converters;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

import java.util.Collections;
import java.util.Map;

@Converter(autoApply = true)
public class JsonMapConverter implements AttributeConverter<Map<String, Object>, String> {
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper()
            .registerModule(new JavaTimeModule())
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
            .setSerializationInclusion(JsonInclude.Include.NON_NULL);

    @Override
    public String convertToDatabaseColumn(Map<String, Object> attribute) {
        try {
            return (attribute != null) ? OBJECT_MAPPER.writeValueAsString(attribute) : "{}";
        } catch (Exception e) {
            throw new RuntimeException("Ошибка сериализации JSON", e);
        }
    }

    @Override
    public Map<String, Object> convertToEntityAttribute(String dbData) {
        try {
            return (dbData != null && !dbData.isEmpty()) ?
                    OBJECT_MAPPER.readValue(dbData, new TypeReference<Map<String, Object>>() {}) :
                    Collections.emptyMap();
        } catch (Exception e) {
            throw new RuntimeException("Ошибка десериализации JSON", e);
        }
    }
}