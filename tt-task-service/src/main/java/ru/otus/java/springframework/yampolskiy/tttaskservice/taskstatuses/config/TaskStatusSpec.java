package ru.otus.java.springframework.yampolskiy.tttaskservice.taskstatuses.config;

public record TaskStatusSpec(
        String code,
        String name,
        String description,
        boolean closedState,
        boolean initialState,
        int orderNumber,
        String color
) {}

