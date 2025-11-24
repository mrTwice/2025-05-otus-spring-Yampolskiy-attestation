package ru.otus.java.springframework.yampolskiy.tttaskservice.tasks.security;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import ru.otus.java.springframework.yampolskiy.tttaskservice.common.securiry.AbstractAccessPolicy;
import ru.otus.java.springframework.yampolskiy.tttaskservice.tasks.exceptions.taskpriority.TaskPriorityAccessDeniedException;

@Slf4j
@Component("taskPriorityAccessPolicy")
public class TaskPriorityAccessPolicy extends AbstractAccessPolicy {

    public boolean canViewTaskPriority(Authentication auth) {
        return hasPermission(auth, "task-priority:view");
    }

    public void checkViewTaskPriority(Authentication auth) {
        if (!canViewTaskPriority(auth)) {
            throw new TaskPriorityAccessDeniedException();
        }
    }

    public boolean canCreateTaskPriority(Authentication auth) {
        return hasPermission(auth, "task-priority:create");
    }

    public void checkCreateTaskPriority(Authentication auth) {
        if (!canCreateTaskPriority(auth)) {
            throw new TaskPriorityAccessDeniedException();
        }
    }

    public boolean canUpdateTaskPriority(Authentication auth) {
        return hasPermission(auth, "task-priority:update");
    }

    public void checkUpdateTaskPriority(Authentication auth) {
        if (!canUpdateTaskPriority(auth)) {
            throw new TaskPriorityAccessDeniedException();
        }
    }

    public boolean canDeleteTaskPriority(Authentication auth) {
        return hasPermission(auth, "task-priority:delete");
    }

    public void checkDeleteTaskPriority(Authentication auth) {
        if (!canDeleteTaskPriority(auth)) {
            throw new TaskPriorityAccessDeniedException();
        }
    }
}

