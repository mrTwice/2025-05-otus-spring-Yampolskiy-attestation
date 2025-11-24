package ru.otus.java.springframework.yampolskiy.tttaskservice.taskstatuses.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.otus.java.springframework.yampolskiy.tttaskservice.common.DomainValidator;
import ru.otus.java.springframework.yampolskiy.tttaskservice.taskstatuses.entities.TaskStatus;
import ru.otus.java.springframework.yampolskiy.tttaskservice.taskstatuses.validators.TaskStatusValidator;

@Configuration
public class ValidatorTaskStatusConfig {

    @Bean(name = "taskStatusValidator")
    public DomainValidator<TaskStatus> taskStatusValidator() {
        return new TaskStatusValidator();
    }
}
