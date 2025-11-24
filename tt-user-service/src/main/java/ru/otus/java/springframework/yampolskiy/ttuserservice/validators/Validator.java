package ru.otus.java.springframework.yampolskiy.ttuserservice.validators;

public interface Validator<T> {
    void validate(T object);
}
