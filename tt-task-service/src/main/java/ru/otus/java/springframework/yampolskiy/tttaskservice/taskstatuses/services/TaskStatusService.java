package ru.otus.java.springframework.yampolskiy.tttaskservice.taskstatuses.services;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ru.otus.java.springframework.yampolskiy.tttaskservice.taskstatuses.entities.TaskStatus;

import java.util.List;
import java.util.UUID;

public interface TaskStatusService {

    TaskStatus findById(UUID id);

    TaskStatus findByCode(String code);

    List<TaskStatus> findAll();

    List<TaskStatus> findAllSorted();

    List<TaskStatus> findDefaults();

    List<TaskStatus> findFinal(boolean isFinal);

    Page<TaskStatus> search(String search, Pageable pageable);

    TaskStatus create(TaskStatus status);

    TaskStatus update(UUID id, TaskStatus updated);

    void delete(UUID id);
}
