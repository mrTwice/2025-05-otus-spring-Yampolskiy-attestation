package ru.otus.java.springframework.yampolskiy.ttuserservice.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.java.springframework.yampolskiy.ttuserservice.entities.Permission;
import ru.otus.java.springframework.yampolskiy.ttuserservice.entities.Role;
import ru.otus.java.springframework.yampolskiy.ttuserservice.exceptions.ResourceNotFoundException;
import ru.otus.java.springframework.yampolskiy.ttuserservice.repositories.RoleRepository;
import ru.otus.java.springframework.yampolskiy.ttuserservice.validators.Validator;

import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class RoleServiceImpl implements RoleService {

    private final RoleRepository roleRepository;

    private final PermissionService permissionService;

    private final Validator<Role> commonRoleValidator;

    private final Validator<Role> roleUniqueValidator;

    @Override
    public Role createRole(Role role) {
        commonRoleValidator.validate(role);
        roleUniqueValidator.validate(role);
        //TODO надо бы вставлять пермиссии
        return roleRepository.save(role);
    }

    @Override
    public Role getRoleById(UUID id) {
        return roleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Role not found with id: " + id));
    }

    @Override
    public Role getRoleByName(String name) {
        return roleRepository.findByName(name)
                .orElseThrow(() -> new ResourceNotFoundException("Role not found with name: " + name));
    }

    @Override
    public List<Role> getAllRoles() {
        return roleRepository.findAll();
    }

    @Override
    public Role updateRole(UUID id, Role role) {
        commonRoleValidator.validate(role);
        Role existingRole = getRoleById(id);

        if (!existingRole.getName().equals(role.getName()) && roleRepository.existsByName(role.getName())) {
            throw new IllegalArgumentException("Role with name " + role.getName() + " already exists.");
        }

        Set<Permission> validatedPermissions = permissionService.fetchOrCreatePermissions(role.getPermissions());
        existingRole.setName(role.getName());
        existingRole.setPermissions(validatedPermissions);
        return roleRepository.save(existingRole);
    }

    @Override
    public void deleteRole(UUID id) {
        Role role = getRoleById(id);
        roleRepository.delete(role);
    }

    @Override
    public Set<Role> validateRoles(Set<Role> roles) {
        roles.forEach(commonRoleValidator::validate);
        return roles.stream()
                .map(role -> roleRepository.findByName(role.getName())
                        .orElseThrow(() -> new ResourceNotFoundException("Role not found with name: " + role.getName()))
                )
                .collect(Collectors.toSet());
    }

    @Override
    public Role createOrUpdateRoleWithPermissions(String roleName, Set<Permission> permissions) {
        return roleRepository.findByName(roleName).orElseGet(() -> {
            Role newRole = Role.builder()
                    .name(roleName)
                    .permissions(permissionService.fetchOrCreatePermissions(permissions))
                    .build();

            commonRoleValidator.validate(newRole);
            roleUniqueValidator.validate(newRole);

            return roleRepository.save(newRole);
        });
    }
}