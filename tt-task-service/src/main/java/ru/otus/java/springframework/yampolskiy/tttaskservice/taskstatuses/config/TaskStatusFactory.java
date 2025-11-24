package ru.otus.java.springframework.yampolskiy.tttaskservice.taskstatuses.config;

import org.springframework.stereotype.Component;
import ru.otus.java.springframework.yampolskiy.tttaskservice.taskstatuses.entities.TaskStatus;

import java.util.List;

@Component
public class TaskStatusFactory {

    public TaskStatus create(TaskStatusSpec spec) {
        return new TaskStatus(
                null,
                spec.code(),
                spec.name(),
                spec.description(),
                spec.closedState(),
                spec.initialState(),
                spec.orderNumber(),
                spec.color(),
                null,
                null
        );
    }

    public TaskStatus todo() {
        return create(new TaskStatusSpec(
                "todo", "To Do", "Task is not yet started",
                false, true, 1, "#D3D3D3"
        ));
    }

    public TaskStatus inProgress() {
        return create(new TaskStatusSpec(
                "in_progress", "In Progress", "Work is ongoing",
                false, false, 2, "#87CEEB"
        ));
    }

    public TaskStatus review() {
        return create(new TaskStatusSpec(
                "review", "Review", "Awaiting review",
                false, false, 3, "#FFD700"
        ));
    }

    public TaskStatus done() {
        return create(new TaskStatusSpec(
                "done", "Done", "Task is completed",
                true, false, 4, "#32CD32"
        ));
    }

    public TaskStatus cancelled() {
        return create(new TaskStatusSpec(
                "cancelled", "Cancelled", "Task was cancelled",
                true, false, 5, "#A9A9A9"
        ));
    }

    public List<TaskStatus> defaultStatuses() {
        return List.of(todo(), inProgress(), review(), done(), cancelled());
    }
}
