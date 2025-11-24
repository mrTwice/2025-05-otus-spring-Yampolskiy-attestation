package ru.otus.java.springframework.yampolskiy.tttaskservice.tasktypes.services;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ru.otus.java.springframework.yampolskiy.tttaskservice.tasktypes.entities.TaskType;

import java.util.List;
import java.util.UUID;

public interface TaskTypeService {

    TaskType findById(UUID id);

    TaskType findByCode(String code);

    List<TaskType> findAll();

    List<TaskType> findAllSorted();

    List<TaskType> findDefaults();

    Page<TaskType> search(String search, Pageable pageable);

    TaskType create(TaskType type);

    TaskType update(UUID id, TaskType updated);

    void delete(UUID id);
}
