package ru.otus.java.springframework.yampolskiy.ttuserservice.initializer;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import ru.otus.java.springframework.yampolskiy.ttuserservice.entities.Permission;
import ru.otus.java.springframework.yampolskiy.ttuserservice.services.PermissionService;
import ru.otus.java.springframework.yampolskiy.ttuserservice.services.RoleService;

import java.util.Map;
import java.util.Set;
import java.util.HashMap;
import java.util.stream.Collectors;

@Slf4j
@Component
@Order(1)
@RequiredArgsConstructor
public class RolePermissionInitializer implements CommandLineRunner {

    private final PermissionService permissionService;

    private final RoleService roleService;

    @Override
    public void run(String... args) {
        log.info("🔐 Initializing default roles and permissions...");

        Map<String, Set<String>> rolePermissions = defaultRolePermissions();
        Map<String, Permission> createdPermissions = initPermissions(rolePermissions);
        initRoles(rolePermissions, createdPermissions);

        log.info("🎉 Default roles and permissions initialized successfully.");
    }

    private Map<String, Set<String>> defaultRolePermissions() {
        return Map.of(
                "ADMIN", PermissionGroup.ADMIN.permissions(),
                "MANAGER", PermissionGroup.MANAGER.permissions(),
                "USER", PermissionGroup.USER.permissions(),
                "GUEST", PermissionGroup.GUEST.permissions()
        );
    }

    private Map<String, Permission> initPermissions(Map<String, Set<String>> rolePermissions) {
        Set<String> allPermissions = rolePermissions.values().stream()
                .flatMap(Set::stream)
                .collect(Collectors.toSet());

        Map<String, Permission> createdPermissions = new HashMap<>();
        for (String name : allPermissions) {
            Permission permission = findOrCreatePermission(name);
            createdPermissions.put(name, permission);
        }
        return createdPermissions;
    }

    private Permission findOrCreatePermission(String name) {
        if (permissionService.existsByName(name)) {
            log.debug("✅ Permission '{}' already exists", name);
            return permissionService.getPermissionByName(name);
        }

        Permission permission = permissionService.createPermission(
                Permission.builder()
                        .name(name)
                        .description("Allows " + name.replace(":", " ") + " action")
                        .build()
        );
        log.info("➕ Created permission: {}", name);
        return permission;
    }

    private void initRoles(Map<String, Set<String>> rolePermissions,
                           Map<String, Permission> createdPermissions) {
        for (Map.Entry<String, Set<String>> entry : rolePermissions.entrySet()) {
            String roleName = entry.getKey();
            Set<Permission> permissions = entry.getValue().stream()
                    .map(createdPermissions::get)
                    .collect(Collectors.toSet());

            roleService.createOrUpdateRoleWithPermissions(roleName, permissions);

            log.info("✅ Role '{}' initialized with permissions: {}",
                    roleName,
                    permissions.stream()
                            .map(Permission::getName)
                            .collect(Collectors.joining(", "))
            );
        }
    }
}

