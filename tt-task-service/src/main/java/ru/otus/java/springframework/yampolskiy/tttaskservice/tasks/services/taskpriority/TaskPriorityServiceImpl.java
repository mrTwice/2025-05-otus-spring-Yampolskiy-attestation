package ru.otus.java.springframework.yampolskiy.tttaskservice.tasks.services.taskpriority;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.java.springframework.yampolskiy.tttaskservice.tasks.entities.TaskPriority;
import ru.otus.java.springframework.yampolskiy.tttaskservice.tasks.exceptions.taskpriority.TaskPriorityAlreadyDeletedException;
import ru.otus.java.springframework.yampolskiy.tttaskservice.tasks.exceptions.taskpriority.TaskPriorityNotFoundException;
import ru.otus.java.springframework.yampolskiy.tttaskservice.tasks.repositories.TaskPriorityRepository;
import ru.otus.java.springframework.yampolskiy.tttaskservice.common.DomainValidator;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class TaskPriorityServiceImpl implements TaskPriorityService {

    private final TaskPriorityRepository taskPriorityRepository;

    private final DomainValidator<TaskPriority> taskPriorityValidator;

    @Override
    @Transactional(readOnly = true)
    public TaskPriority findById(UUID id) {
        return taskPriorityRepository.findByIdAndDeletedAtIsNull(id)
                .orElseThrow(() -> new TaskPriorityNotFoundException(id));
    }

    @Override
    @Transactional(readOnly = true)
    public TaskPriority findByCode(String code) {
        return taskPriorityRepository.findByCodeAndDeletedAtIsNull(code)
                .orElseThrow(() -> new TaskPriorityNotFoundException(code));
    }

    @Override
    @Transactional(readOnly = true)
    public List<TaskPriority> findAll() {
        return taskPriorityRepository.findAllByDeletedAtIsNull();
    }

    @Override
    @Transactional(readOnly = true)
    public List<TaskPriority> findAllSorted() {
        return taskPriorityRepository.findAllByDeletedAtIsNullOrderBySortOrderAsc();
    }

    @Override
    @Transactional(readOnly = true)
    public List<TaskPriority> findDefaults() {
        return taskPriorityRepository.findByIsDefaultTrueAndDeletedAtIsNull();
    }

    @Override
    public TaskPriority create(TaskPriority priority) {
        taskPriorityValidator.validateForCreate(priority);
        return taskPriorityRepository.save(priority);
    }

    @Override
    public TaskPriority update(UUID id, TaskPriority updated) {
        TaskPriority existing = findById(id);
        taskPriorityValidator.validateForUpdate(existing, updated);

        existing.setName(updated.getName());
        existing.setDescription(updated.getDescription());
        existing.setColor(updated.getColor());
        existing.setSortOrder(updated.getSortOrder());
        existing.setDefault(updated.isDefault());

        return taskPriorityRepository.save(existing);
    }

    @Override
    public void delete(UUID id) {
        TaskPriority priority = findById(id);
        if (priority.getDeletedAt() != null) {
            throw new TaskPriorityAlreadyDeletedException(id);
        }
        priority.setDeletedAt(Instant.now());
        taskPriorityRepository.save(priority);
    }
}


