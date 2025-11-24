package ru.otus.java.springframework.yampolskiy.tttaskservice.tasktypes.security;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import ru.otus.java.springframework.yampolskiy.tttaskservice.common.securiry.AbstractAccessPolicy;
import ru.otus.java.springframework.yampolskiy.tttaskservice.tasktypes.exceptions.TaskTypeAccessDeniedException;

@Slf4j
@Component("taskTypeAccessPolicy")
public class TaskTypeAccessPolicy extends AbstractAccessPolicy {

    public boolean canViewTaskType(Authentication authentication) {
        return hasPermission(authentication, "task-type:view");
    }

    public void checkViewTaskType(Authentication authentication) {
        if (!canViewTaskType(authentication)) {
            throw new TaskTypeAccessDeniedException();
        }
    }

    public boolean canCreateTaskType(Authentication authentication) {
        return hasPermission(authentication, "task-type:create");
    }

    public void checkCreateTaskType(Authentication authentication) {
        if (!canCreateTaskType(authentication)) {
            throw new TaskTypeAccessDeniedException();
        }
    }

    public boolean canUpdateTaskType(Authentication authentication) {
        return hasPermission(authentication, "task-type:update");
    }

    public void checkUpdateTaskType(Authentication authentication) {
        if (!canUpdateTaskType(authentication)) {
            throw new TaskTypeAccessDeniedException();
        }
    }

    public boolean canDeleteTaskType(Authentication authentication) {
        return hasPermission(authentication, "task-type:delete");
    }

    public void checkDeleteTaskType(Authentication authentication) {
        if (!canDeleteTaskType(authentication)) {
            throw new TaskTypeAccessDeniedException();
        }
    }
}

