package ru.otus.java.springframework.yampolskiy.tttaskservice.tasks.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.otus.java.springframework.yampolskiy.tttaskservice.tasks.entities.TaskPriority;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface TaskPriorityRepository extends JpaRepository<TaskPriority, UUID> {

    Optional<TaskPriority> findByCodeAndDeletedAtIsNull(String code);

    Optional<TaskPriority> findByIdAndDeletedAtIsNull(UUID id);

    List<TaskPriority> findAllByDeletedAtIsNull();

    List<TaskPriority> findAllByDeletedAtIsNullOrderBySortOrderAsc();

    List<TaskPriority> findByIsDefaultTrueAndDeletedAtIsNull();
}