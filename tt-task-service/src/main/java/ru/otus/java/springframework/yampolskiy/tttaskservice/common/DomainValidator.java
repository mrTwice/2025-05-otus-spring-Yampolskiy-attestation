package ru.otus.java.springframework.yampolskiy.tttaskservice.common;

public interface DomainValidator<T> {

    void validateForCreate(T entity);

    void validateForUpdate(T existingEntity, T updatedEntity);
}
