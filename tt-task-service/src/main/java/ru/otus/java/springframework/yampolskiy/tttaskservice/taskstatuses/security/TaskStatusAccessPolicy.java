package ru.otus.java.springframework.yampolskiy.tttaskservice.taskstatuses.security;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import ru.otus.java.springframework.yampolskiy.tttaskservice.common.securiry.AbstractAccessPolicy;
import ru.otus.java.springframework.yampolskiy.tttaskservice.taskstatuses.exceptions.TaskStatusAccessDeniedException;

@Slf4j
@Component("taskStatusAccessPolicy")
public class TaskStatusAccessPolicy extends AbstractAccessPolicy {

    public boolean canViewTaskStatus(Authentication auth) {
        return hasPermission(auth, "task-status:view");
    }

    public void checkViewTaskStatus(Authentication auth) {
        if (!canViewTaskStatus(auth)) {
            throw new TaskStatusAccessDeniedException();
        }
    }

    public boolean canCreateTaskStatus(Authentication auth) {
        return hasPermission(auth, "task-status:create");
    }

    public void checkCreateTaskStatus(Authentication auth) {
        if (!canCreateTaskStatus(auth)) {
            throw new TaskStatusAccessDeniedException();
        }
    }

    public boolean canUpdateTaskStatus(Authentication auth) {
        return hasPermission(auth, "task-status:update");
    }

    public void checkUpdateTaskStatus(Authentication auth) {
        if (!canUpdateTaskStatus(auth)) {
            throw new TaskStatusAccessDeniedException();
        }
    }

    public boolean canDeleteTaskStatus(Authentication auth) {
        return hasPermission(auth, "task-status:delete");
    }

    public void checkDeleteTaskStatus(Authentication auth) {
        if (!canDeleteTaskStatus(auth)) {
            throw new TaskStatusAccessDeniedException();
        }
    }
}

