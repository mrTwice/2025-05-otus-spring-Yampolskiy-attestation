package ru.otus.java.springframework.yampolskiy.ttuserservice.validators;

import ru.otus.java.springframework.yampolskiy.ttuserservice.entities.Role;

public class CommonRoleValidator  implements Validator<Role> {

    public void validate(Role role) {
        if (role == null) {
            throw new IllegalArgumentException("Role cannot be null");
        }

        if (role.getName() == null || role.getName().isEmpty()) {
            throw new IllegalArgumentException("Role name cannot be null or empty");
        }
    }
}