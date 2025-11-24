package ru.otus.java.springframework.yampolskiy.tttaskservice.tasks.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.otus.java.springframework.yampolskiy.tttaskservice.tasks.entities.Task;
import ru.otus.java.springframework.yampolskiy.tttaskservice.tasks.entities.TaskPriority;
import ru.otus.java.springframework.yampolskiy.tttaskservice.tasks.exceptions.task.InvalidTaskException;
import ru.otus.java.springframework.yampolskiy.tttaskservice.tasks.services.taskpriority.TaskPriorityService;
import ru.otus.java.springframework.yampolskiy.tttaskservice.taskstatuses.entities.TaskStatus;
import ru.otus.java.springframework.yampolskiy.tttaskservice.taskstatuses.services.TaskStatusService;
import ru.otus.java.springframework.yampolskiy.tttaskservice.tasktypes.entities.TaskType;
import ru.otus.java.springframework.yampolskiy.tttaskservice.tasktypes.services.TaskTypeService;

@Component
@RequiredArgsConstructor
public class TaskCodeResolver {

    private final TaskStatusService taskStatusService;

    private final TaskTypeService taskTypeService;

    private final TaskPriorityService taskPriorityService;

    public void resolve(Task task) {
        resolveStatus(task);
        resolveType(task);
        resolvePriority(task);
    }

    private void resolveStatus(Task task) {
        TaskStatus status;
        if (task.getStatusId() != null) {
            status = taskStatusService.findById(task.getStatusId());
        } else {
            status = taskStatusService.findDefaults().stream()
                    .findFirst()
                    .orElseThrow(() -> new InvalidTaskException("Default task status is not configured"));
            task.setStatusId(status.getId());
        }
        task.setStatusCode(status.getCode());
    }

    private void resolveType(Task task) {
        TaskType type;
        if (task.getTypeId() != null) {
            type = taskTypeService.findById(task.getTypeId());
        } else {
            type = taskTypeService.findDefaults().stream()
                    .findFirst()
                    .orElseThrow(() -> new InvalidTaskException("Default task type is not configured"));
            task.setTypeId(type.getId());
        }
        task.setTypeCode(type.getCode());
    }

    private void resolvePriority(Task task) {
        if (task.getPriorityId() != null) {
            TaskPriority priority = taskPriorityService.findById(task.getPriorityId());
            task.setPriorityCode(priority.getCode());
        } else {
            TaskPriority defaultPriority = taskPriorityService.findDefaults().stream()
                    .findFirst()
                    .orElse(null);

            if (defaultPriority != null) {
                task.setPriorityId(defaultPriority.getId());
                task.setPriorityCode(defaultPriority.getCode());
            } else {
                task.setPriorityCode(null);
            }
        }
    }
}

