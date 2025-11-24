package ru.otus.java.springframework.yampolskiy.tttaskservice.taskstatuses.services;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.java.springframework.yampolskiy.tttaskservice.common.DomainValidator;
import ru.otus.java.springframework.yampolskiy.tttaskservice.taskstatuses.entities.TaskStatus;
import ru.otus.java.springframework.yampolskiy.tttaskservice.taskstatuses.exceptions.TaskStatusNotFoundException;
import ru.otus.java.springframework.yampolskiy.tttaskservice.taskstatuses.repositories.TaskStatusRepository;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class TaskStatusServiceImpl implements TaskStatusService {

    private final TaskStatusRepository taskStatusRepository;

    private final DomainValidator<TaskStatus> taskStatusValidator;

    @Override
    @Transactional(readOnly = true)
    public TaskStatus findById(UUID id) {
        return taskStatusRepository.findById(id)
                .orElseThrow(() -> new TaskStatusNotFoundException(id));
    }

    @Override
    @Transactional(readOnly = true)
    public TaskStatus findByCode(String code) {
        return taskStatusRepository.findByCode(code)
                .orElseThrow(() -> new TaskStatusNotFoundException(code));
    }

    @Override
    @Transactional(readOnly = true)
    public List<TaskStatus> findAll() {
        return taskStatusRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public List<TaskStatus> findAllSorted() {
        return taskStatusRepository.findAllByOrderBySortOrderAsc();
    }

    @Override
    @Transactional(readOnly = true)
    public List<TaskStatus> findDefaults() {
        return taskStatusRepository.findByIsDefault(true);
    }

    @Override
    @Transactional(readOnly = true)
    public List<TaskStatus> findFinal(boolean isFinal) {
        return taskStatusRepository.findByIsFinal(isFinal);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<TaskStatus> search(String search, Pageable pageable) {
        return taskStatusRepository.searchByName(search, pageable);
    }

    @Override
    public TaskStatus create(TaskStatus status) {
        taskStatusValidator.validateForCreate(status);
        return taskStatusRepository.save(status);
    }

    @Override
    public TaskStatus update(UUID id, TaskStatus updated) {
        TaskStatus existing = findById(id);
        taskStatusValidator.validateForUpdate(existing, updated);

        existing.setName(updated.getName());
        existing.setDescription(updated.getDescription());
        existing.setFinal(updated.isFinal());
        existing.setDefault(updated.isDefault());
        existing.setSortOrder(updated.getSortOrder());
        existing.setColor(updated.getColor());

        return taskStatusRepository.save(existing);
    }

    @Override
    public void delete(UUID id) {
        TaskStatus status = findById(id);
        // TODO  добавить soft delete
        taskStatusRepository.delete(status);
    }
}
