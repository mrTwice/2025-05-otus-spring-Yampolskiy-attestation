package ru.otus.java.springframework.yampolskiy.ttuserservice.initializer;

import java.util.Set;

public enum PermissionGroup {

    ADMIN(Set.of(
            "task:create", "task:view", "task:update", "task:delete", "task:assign",
            "comment:create", "comment:view", "comment:update", "comment:delete",
            "attachment:create", "attachment:view", "attachment:update", "attachment:delete",
            "task-type:create", "task-type:view", "task-type:update", "task-type:delete",
            "task-status:create", "task-status:view", "task-status:update", "task-status:delete",
            "task-priority:create", "task-priority:view", "task-priority:update", "task-priority:delete",
            "user:view", "user:update", "user:delete", "user:assign-roles", "user:manage",
            "role:create", "role:view", "role:update", "role:delete",
            "permission:create", "permission:view", "permission:update", "permission:delete"
    )),

    MANAGER(Set.of(
            "task:create", "task:view", "task:update", "task:delete", "task:assign",
            "comment:create", "comment:view", "comment:update", "comment:delete",
            "attachment:create", "attachment:view", "attachment:update", "attachment:delete",
            "task-type:view", "task-type:update",
            "task-status:view", "task-status:update",
            "task-priority:view", "task-priority:update",
            "user:view", "user:update",
            "role:view", "permission:view"
    )),

    USER(Set.of(
            "task:create", "task:view", "task:update",
            "comment:create", "comment:view",
            "attachment:create", "attachment:view",
            "task-type:view",
            "task-status:view",
            "task-priority:view",
            "user:view"
    )),

    GUEST(Set.of(
            "task:view",
            "comment:view",
            "attachment:view",
            "task-type:view",
            "task-status:view",
            "task-priority:view",
            "project:view"
    ));

    private final Set<String> permissions;

    PermissionGroup(Set<String> permissions) {
        this.permissions = permissions;
    }

    public Set<String> permissions() {
        return permissions;
    }
}
