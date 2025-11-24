package ru.otus.java.springframework.yampolskiy.ttuserservice.services;

import ru.otus.java.springframework.yampolskiy.ttuserservice.entities.Permission;

import java.util.List;
import java.util.Set;
import java.util.UUID;

public interface PermissionService {

    Permission createPermission(Permission permission);

    Permission getPermissionById(UUID id);

    Permission getPermissionByName(String name);

    List<Permission> getAllPermissions();

    Permission updatePermission(UUID id, Permission permissionDetails);

    void deletePermission(UUID id);

    boolean existsByName(String name);

    Set<Permission> fetchOrCreatePermissions(Set<Permission> permissions);
}