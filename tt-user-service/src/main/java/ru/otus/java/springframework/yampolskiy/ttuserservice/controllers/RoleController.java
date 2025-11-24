package ru.otus.java.springframework.yampolskiy.ttuserservice.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import ru.otus.java.springframework.yampolskiy.ttuserservice.dtos.role.RoleDTO;
import ru.otus.java.springframework.yampolskiy.ttuserservice.dtos.role.RoleResponseDTO;
import ru.otus.java.springframework.yampolskiy.ttuserservice.entities.Role;
import ru.otus.java.springframework.yampolskiy.ttuserservice.mappers.RoleMapper;
import ru.otus.java.springframework.yampolskiy.ttuserservice.services.RoleService;

import java.util.List;
import java.util.UUID;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/v1/roles")
@RequiredArgsConstructor
@Tag(name = "Role API", description = "Управление ролями и их разрешениями")
public class RoleController {

    private final RoleService roleService;

    private final RoleMapper roleMapper;

    @Operation(summary = "Создать роль")
    @PreAuthorize("@accessPolicy.canAdminUpdateUsers(authentication)")
    @PostMapping
    public ResponseEntity<RoleResponseDTO> createRole(@RequestBody @Valid RoleDTO dto) {
        Role role = roleMapper.toEntityFromRoleDTO(dto);
        Role created = roleService.createRole(role);
        return ResponseEntity.status(HttpStatus.CREATED).body(roleMapper.toResponseDTOFromRole(created));
    }

    @Operation(summary = "Получить роль по ID")
    @PreAuthorize("@accessPolicy.canViewRoles(authentication)")
    @GetMapping("/{id}")
    public ResponseEntity<RoleResponseDTO> getRoleById(@PathVariable UUID id) {
        Role role = roleService.getRoleById(id);
        return ResponseEntity.ok(roleMapper.toResponseDTOFromRole(role));
    }

    @Operation(summary = "Получить роль по имени")
    @PreAuthorize("@accessPolicy.canViewRoles(authentication)")
    @GetMapping("/by-name/{name}")
    public ResponseEntity<RoleResponseDTO> getRoleByName(@PathVariable String name) {
        Role role = roleService.getRoleByName(name);
        return ResponseEntity.ok(roleMapper.toResponseDTOFromRole(role));
    }

    @Operation(summary = "Получить список всех ролей")
    @PreAuthorize("@accessPolicy.canViewRoles(authentication)")
    @GetMapping
    public ResponseEntity<List<RoleResponseDTO>> getAllRoles() {
        List<Role> roles = roleService.getAllRoles();
        List<RoleResponseDTO> dtos = roles.stream()
                .map(roleMapper::toResponseDTOFromRole)
                .toList();
        return ResponseEntity.ok(dtos);
    }

    @Operation(summary = "Обновить роль")
    @PreAuthorize("@accessPolicy.canAdminUpdateUsers(authentication)")
    @PutMapping("/{id}")
    public ResponseEntity<RoleResponseDTO> updateRole(@PathVariable UUID id, @RequestBody @Valid RoleDTO dto) {
        Role role = roleMapper.toEntityFromRoleDTO(dto);
        Role updated = roleService.updateRole(id, role);
        return ResponseEntity.ok(roleMapper.toResponseDTOFromRole(updated));
    }

    @Operation(summary = "Удалить роль")
    @PreAuthorize("@accessPolicy.canAdminUpdateUsers(authentication)")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRole(@PathVariable UUID id) {
        roleService.deleteRole(id);
        return ResponseEntity.noContent().build();
    }
}
