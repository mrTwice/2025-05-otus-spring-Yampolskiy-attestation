package ru.otus.java.springframework.yampolskiy.tttaskservice.tasktypes.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.otus.java.springframework.yampolskiy.tttaskservice.common.DomainValidator;
import ru.otus.java.springframework.yampolskiy.tttaskservice.tasktypes.entities.TaskType;
import ru.otus.java.springframework.yampolskiy.tttaskservice.tasktypes.validators.TaskTypeValidator;

@Configuration
public class ValidatorsTaskTypeConfig {

    @Bean(name = "taskTypeValidator")
    public DomainValidator<TaskType> taskTypeValidator() {
        return new TaskTypeValidator();
    }
}
