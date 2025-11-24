package ru.otus.java.springframework.yampolskiy.tttaskservice.tasktypes.services;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.java.springframework.yampolskiy.tttaskservice.common.DomainValidator;
import ru.otus.java.springframework.yampolskiy.tttaskservice.tasktypes.entities.TaskType;
import ru.otus.java.springframework.yampolskiy.tttaskservice.tasktypes.exceptions.TaskTypeAlreadyDeletedException;
import ru.otus.java.springframework.yampolskiy.tttaskservice.tasktypes.exceptions.TaskTypeNotFoundException;
import ru.otus.java.springframework.yampolskiy.tttaskservice.tasktypes.repositories.TaskTypeRepository;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class TaskTypeServiceImpl implements TaskTypeService {

    private final TaskTypeRepository taskTypeRepository;

    private final DomainValidator<TaskType> taskTypeValidator;

    @Override
    @Transactional(readOnly = true)
    public TaskType findById(UUID id) {
        return taskTypeRepository.findByIdAndDeletedAtIsNull(id)
                .orElseThrow(() -> new TaskTypeNotFoundException(id));
    }

    @Override
    @Transactional(readOnly = true)
    public TaskType findByCode(String code) {
        return taskTypeRepository.findByCodeAndDeletedAtIsNull(code)
                .orElseThrow(() -> new TaskTypeNotFoundException(code));
    }

    @Override
    @Transactional(readOnly = true)
    public List<TaskType> findAll() {
        return taskTypeRepository.findAllByDeletedAtIsNull();
    }

    @Override
    @Transactional(readOnly = true)
    public List<TaskType> findAllSorted() {
        return taskTypeRepository.findAllByDeletedAtIsNullOrderBySortOrderAsc();
    }

    @Override
    @Transactional(readOnly = true)
    public List<TaskType> findDefaults() {
        return taskTypeRepository.findByIsDefaultTrueAndDeletedAtIsNull();
    }

    @Override
    @Transactional(readOnly = true)
    public Page<TaskType> search(String search, Pageable pageable) {
        return taskTypeRepository.searchByNameOrDescription(search, pageable);
    }

    @Override
    public TaskType create(TaskType type) {
        taskTypeValidator.validateForCreate(type);
        return taskTypeRepository.save(type);
    }

    @Override
    public TaskType update(UUID id, TaskType updated) {
        TaskType existing = findById(id);
        taskTypeValidator.validateForUpdate(existing, updated);

        existing.setName(updated.getName());
        existing.setDescription(updated.getDescription());
        existing.setSortOrder(updated.getSortOrder());
        existing.setDefault(updated.isDefault());
        existing.setIcon(updated.getIcon());

        return taskTypeRepository.save(existing);
    }

    @Override
    public void delete(UUID id) {
        TaskType type = findById(id);
        if (type.getDeletedAt() != null) {
            throw new TaskTypeAlreadyDeletedException(id);
        }
        type.setDeletedAt(Instant.now());
        taskTypeRepository.save(type);
    }
}


