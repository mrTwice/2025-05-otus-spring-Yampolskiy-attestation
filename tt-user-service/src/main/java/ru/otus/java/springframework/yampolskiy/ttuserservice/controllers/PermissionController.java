package ru.otus.java.springframework.yampolskiy.ttuserservice.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import ru.otus.java.springframework.yampolskiy.ttuserservice.dtos.permissions.PermissionDTO;
import ru.otus.java.springframework.yampolskiy.ttuserservice.dtos.permissions.PermissionResponseDTO;
import ru.otus.java.springframework.yampolskiy.ttuserservice.entities.Permission;
import ru.otus.java.springframework.yampolskiy.ttuserservice.mappers.PermissionMapper;
import ru.otus.java.springframework.yampolskiy.ttuserservice.services.PermissionService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;

import jakarta.validation.Valid;
import java.util.List;
import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/api/v1/permissions")
@RequiredArgsConstructor
@Tag(name = "Permission API", description = "Операции над разрешениями (permissions)")
public class PermissionController {

    private final PermissionService permissionService;

    private final PermissionMapper permissionMapper;

    @Operation(summary = "Создать разрешение")
    @PreAuthorize("@accessPolicy.canManagePermissions(authentication)")
    @PostMapping
    public ResponseEntity<PermissionResponseDTO> createPermission(@RequestBody @Valid PermissionDTO dto) {
        Permission permission = permissionMapper.toEntityFromPermissionDTO(dto);
        Permission saved = permissionService.createPermission(permission);
        return ResponseEntity.status(HttpStatus.CREATED).body(permissionMapper.toResponseDTOFromEntity(saved));
    }

    @Operation(summary = "Получить разрешение по ID")
    @PreAuthorize("@accessPolicy.canViewRoles(authentication)")
    @GetMapping("/{id}")
    public ResponseEntity<PermissionResponseDTO> getPermissionById(@PathVariable UUID id) {
        Permission permission = permissionService.getPermissionById(id);
        return ResponseEntity.ok(permissionMapper.toResponseDTOFromEntity(permission));
    }

    @Operation(summary = "Получить разрешение по имени")
    @PreAuthorize("@accessPolicy.canViewRoles(authentication)")
    @GetMapping("/by-name/{name}")
    public ResponseEntity<PermissionResponseDTO> getPermissionByName(@PathVariable String name) {
        Permission permission = permissionService.getPermissionByName(name);
        return ResponseEntity.ok(permissionMapper.toResponseDTOFromEntity(permission));
    }

    @Operation(summary = "Получить список всех разрешений")
    @PreAuthorize("@accessPolicy.canViewRoles(authentication)")
    @GetMapping
    public ResponseEntity<List<PermissionResponseDTO>> getAllPermissions() {
        List<Permission> permissions = permissionService.getAllPermissions();
        List<PermissionResponseDTO> dtos = permissions.stream()
                .map(permissionMapper::toResponseDTOFromEntity)
                .toList();
        return ResponseEntity.ok(dtos);
    }

    @Operation(summary = "Обновить разрешение")
    @PreAuthorize("@accessPolicy.canManagePermissions(authentication)")
    @PutMapping("/{id}")
    public ResponseEntity<PermissionResponseDTO> updatePermission(
            @PathVariable UUID id,
            @RequestBody @Valid PermissionDTO dto
    ) {
        Permission toUpdate = permissionMapper.toEntityFromPermissionDTO(dto);
        Permission updated = permissionService.updatePermission(id, toUpdate);
        return ResponseEntity.ok(permissionMapper.toResponseDTOFromEntity(updated));
    }

    @Operation(summary = "Удалить разрешение")
    @PreAuthorize("@accessPolicy.canManagePermissions(authentication)")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePermission(@PathVariable UUID id) {
        permissionService.deletePermission(id);
        return ResponseEntity.noContent().build();
    }
}


