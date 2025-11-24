package ru.otus.java.springframework.yampolskiy.ttuserservice.validators;

import ru.otus.java.springframework.yampolskiy.ttuserservice.entities.Permission;

public class PermissionValidator implements Validator<Permission> {

    @Override
    public void validate(Permission permission) {
        if (permission == null) {
            throw new IllegalArgumentException("Permission cannot be null");
        }

        if (permission.getName() == null || permission.getName().trim().isEmpty()) {
            throw new IllegalArgumentException("Permission name cannot be null or empty");
        }

        if (permission.getDescription() == null || permission.getDescription().trim().isEmpty()) {
            throw new IllegalArgumentException("Permission description cannot be null or empty");
        }
    }
}
