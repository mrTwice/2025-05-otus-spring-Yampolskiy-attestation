package ru.otus.java.springframework.yampolskiy.ttuserservice.services;

import ru.otus.java.springframework.yampolskiy.ttuserservice.entities.Permission;
import ru.otus.java.springframework.yampolskiy.ttuserservice.entities.Role;

import java.util.List;
import java.util.Set;
import java.util.UUID;

public interface RoleService {

    Role createRole(Role role);

    Role getRoleById(UUID id);

    Role getRoleByName(String name);

    List<Role> getAllRoles();

    Role updateRole(UUID id, Role role);

    void deleteRole(UUID id);

    Set<Role> validateRoles(Set<Role> roles);

    Role createOrUpdateRoleWithPermissions(String roleName, Set<Permission> permissions);

}
