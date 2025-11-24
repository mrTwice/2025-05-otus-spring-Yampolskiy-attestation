package ru.otus.java.springframework.yampolskiy.tttaskservice.tasks.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.otus.java.springframework.yampolskiy.tttaskservice.tasks.entities.Task;
import ru.otus.java.springframework.yampolskiy.tttaskservice.tasks.entities.TaskPriority;
import ru.otus.java.springframework.yampolskiy.tttaskservice.common.DomainValidator;
import ru.otus.java.springframework.yampolskiy.tttaskservice.tasks.validators.TaskPriorityValidator;
import ru.otus.java.springframework.yampolskiy.tttaskservice.tasks.validators.TaskValidator;

@Configuration
public class ValidatorTaskConfig {

    @Bean(name = "taskValidator")
    public DomainValidator<Task> taskValidator() {
        return new TaskValidator();
    }

    @Bean(name = "taskPriorityValidator")
    public DomainValidator<TaskPriority> taskPriorityValidator() {
        return new TaskPriorityValidator();
    }
}