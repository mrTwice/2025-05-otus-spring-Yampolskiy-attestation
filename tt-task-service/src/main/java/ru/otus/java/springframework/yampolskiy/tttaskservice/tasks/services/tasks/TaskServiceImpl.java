package ru.otus.java.springframework.yampolskiy.tttaskservice.tasks.services.tasks;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.java.springframework.yampolskiy.tttaskservice.tasks.dtos.task.TaskFilterRequest;
import ru.otus.java.springframework.yampolskiy.tttaskservice.tasks.entities.Task;
import ru.otus.java.springframework.yampolskiy.tttaskservice.tasks.exceptions.task.TaskAlreadyDeletedException;
import ru.otus.java.springframework.yampolskiy.tttaskservice.tasks.exceptions.task.TaskNotFoundException;
import ru.otus.java.springframework.yampolskiy.tttaskservice.tasks.repositories.TaskRepository;
import ru.otus.java.springframework.yampolskiy.tttaskservice.tasks.services.TaskCodeResolver;
import ru.otus.java.springframework.yampolskiy.tttaskservice.tasks.services.tasks.filtering.TaskSpecifications;
import ru.otus.java.springframework.yampolskiy.tttaskservice.common.DomainValidator;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class TaskServiceImpl implements TaskService {

    private final TaskRepository taskRepository;

    private final TaskCodeResolver taskCodeResolver;

    private final DomainValidator<Task> taskValidator;

    @Override
    public Task create(Task task) {
        taskCodeResolver.resolve(task);
        taskValidator.validateForCreate(task);
        return taskRepository.save(task);
    }


    @Override
    public Task update(UUID taskId, Task updatedTask) {
        Task existingTask = getByIdOrThrow(taskId);
        taskValidator.validateForUpdate(existingTask, updatedTask);

        existingTask.setTitle(updatedTask.getTitle());
        existingTask.setDescription(updatedTask.getDescription());
        existingTask.setStatusId(updatedTask.getStatusId());
        existingTask.setAssigneeId(updatedTask.getAssigneeId());
        existingTask.setDueDate(updatedTask.getDueDate());
        existingTask.setCompletedAt(updatedTask.getCompletedAt());
        existingTask.setPriorityId(updatedTask.getPriorityId());
        existingTask.setTypeId(updatedTask.getTypeId());

        taskCodeResolver.resolve(existingTask);

        return taskRepository.save(existingTask);
    }



    @Override
    @Transactional(readOnly = true)
    public Task findById(UUID id) {
        return getByIdOrThrow(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Task> findAll(TaskFilterRequest filter) {
        return taskRepository.findAll(TaskSpecifications.fromFilter(filter));
    }

    @Override
    @Transactional(readOnly = true)
    public List<Task> findAll() {
        return taskRepository.findAll(TaskSpecifications.isNotDeleted());
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Task> findAll(TaskFilterRequest filter, Pageable pageable) {
        return taskRepository.findAll(TaskSpecifications.fromFilter(filter), pageable);
    }

    @Override
    public void delete(UUID taskId) {
        Task task = getByIdOrThrow(taskId);
        if (task.getDeletedAt() != null) {
            throw new TaskAlreadyDeletedException(taskId);
        }
        task.setDeletedAt(Instant.now());
        taskRepository.save(task);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Task> findSubtasks(UUID parentId) {
        return taskRepository.findByParentId(parentId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Task> findByAssignee(UUID assigneeId) {
        return taskRepository.findByAssigneeIdAndDeletedAtIsNull(assigneeId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Task> findByCreator(UUID creatorId) {
        return taskRepository.findByCreatorIdAndDeletedAtIsNull(creatorId);
    }

    @Override
    @Transactional(readOnly = true)
    public long countByAssignee(UUID assigneeId) {
        return taskRepository.countByAssigneeIdAndDeletedAtIsNull(assigneeId);
    }

    private Task getByIdOrThrow(UUID id) {
        return taskRepository.findById(id)
                .filter(task -> task.getDeletedAt() == null)
                .orElseThrow(() -> new TaskNotFoundException(id));
    }
}
