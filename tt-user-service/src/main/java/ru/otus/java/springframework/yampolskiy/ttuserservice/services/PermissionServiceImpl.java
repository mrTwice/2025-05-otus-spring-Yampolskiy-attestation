package ru.otus.java.springframework.yampolskiy.ttuserservice.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.java.springframework.yampolskiy.ttuserservice.entities.Permission;
import ru.otus.java.springframework.yampolskiy.ttuserservice.exceptions.ResourceNotFoundException;
import ru.otus.java.springframework.yampolskiy.ttuserservice.repositories.PermissionRepository;
import ru.otus.java.springframework.yampolskiy.ttuserservice.validators.Validator;

import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class PermissionServiceImpl implements PermissionService {

    private final PermissionRepository permissionRepository;

    private final Validator<Permission> permissionValidator;

    private final Validator<Permission> permissionUniqueValidator;

    @Override
    public Permission createPermission(Permission permission) {
        permissionValidator.validate(permission);
        permissionUniqueValidator.validate(permission);
        return permissionRepository.save(permission);
    }

    @Override
    public Permission getPermissionById(UUID id) {
        return permissionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Permission with id " + id + " not found"));
    }

    @Override
    public Permission getPermissionByName(String name) {
        return permissionRepository.findByName(name)
                .orElseThrow(() -> new ResourceNotFoundException("Permission with name " + name + " not found"));
    }

    @Override
    public List<Permission> getAllPermissions() {
        return permissionRepository.findAll();
    }

    @Override
    public Permission updatePermission(UUID id, Permission permissionDetails) {
        permissionValidator.validate(permissionDetails);

        return permissionRepository.findById(id)
                .map(permission -> {
                    permission.setName(permissionDetails.getName());
                    permission.setDescription(permissionDetails.getDescription());
                    return permissionRepository.save(permission);
                })
                .orElseThrow(() -> new ResourceNotFoundException("Permission with ID " + id + " not found"));
    }

    @Override
    public void deletePermission(UUID id) {
        if (!permissionRepository.existsById(id)) {
            throw new ResourceNotFoundException("Permission with ID " + id + " not found");
        }
        permissionRepository.deleteById(id);
    }

    @Override
    public boolean existsByName(String name) {
        return permissionRepository.existsByName(name);
    }

    @Override
    public Set<Permission> fetchOrCreatePermissions(Set<Permission> permissions) {
        if (permissions == null || permissions.isEmpty()) {
            return Set.of();
        }

        permissions.forEach(permissionValidator::validate);

        return permissions.stream()
                .map(permission -> permissionRepository.findByName(
                        permission.getName())
                        .orElseThrow(
                                () -> new ResourceNotFoundException("Permission not found: " + permission.getName())
                        )
                )
                .collect(Collectors.toSet());
    }
}