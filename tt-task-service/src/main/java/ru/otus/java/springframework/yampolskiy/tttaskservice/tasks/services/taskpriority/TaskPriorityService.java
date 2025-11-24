package ru.otus.java.springframework.yampolskiy.tttaskservice.tasks.services.taskpriority;

import ru.otus.java.springframework.yampolskiy.tttaskservice.tasks.entities.TaskPriority;

import java.util.List;
import java.util.UUID;

public interface TaskPriorityService {

    TaskPriority findById(UUID id);

    TaskPriority findByCode(String code);

    List<TaskPriority> findAll();

    List<TaskPriority> findAllSorted();

    List<TaskPriority> findDefaults();

    TaskPriority create(TaskPriority priority);

    TaskPriority update(UUID id, TaskPriority updated);

    void delete(UUID id);
}



