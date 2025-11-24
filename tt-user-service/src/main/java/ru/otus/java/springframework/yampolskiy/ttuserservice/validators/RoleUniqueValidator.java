package ru.otus.java.springframework.yampolskiy.ttuserservice.validators;

import lombok.RequiredArgsConstructor;
import ru.otus.java.springframework.yampolskiy.ttuserservice.entities.Role;
import ru.otus.java.springframework.yampolskiy.ttuserservice.exceptions.DuplicateResourceException;
import ru.otus.java.springframework.yampolskiy.ttuserservice.repositories.RoleRepository;

@RequiredArgsConstructor
public class RoleUniqueValidator implements Validator<Role> {

    private final RoleRepository roleRepository;

    public void validate(Role role) {
        if (roleRepository.existsByName(role.getName())) {
            throw new DuplicateResourceException("Role name is already taken: " + role.getName());
        }
    }
}