package ru.otus.java.springframework.yampolskiy.ttuserservice.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;
import ru.otus.java.springframework.yampolskiy.ttuserservice.dtos.user.UserResponseDTO;
import ru.otus.java.springframework.yampolskiy.ttuserservice.dtos.user.UserCreateDTO;
import ru.otus.java.springframework.yampolskiy.ttuserservice.dtos.user.UserUpdateDTO;
import ru.otus.java.springframework.yampolskiy.ttuserservice.dtos.user.UserPrincipalDTO;
import ru.otus.java.springframework.yampolskiy.ttuserservice.dtos.user.UserAdminUpdateDTO;
import ru.otus.java.springframework.yampolskiy.ttuserservice.entities.Permission;
import ru.otus.java.springframework.yampolskiy.ttuserservice.entities.Role;
import ru.otus.java.springframework.yampolskiy.ttuserservice.entities.User;

import java.util.Collections;
import java.util.Set;
import java.util.stream.Collectors;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING,
        uses = RoleMapper.class,
        unmappedTargetPolicy = ReportingPolicy.WARN
)
public interface UserMapper {

    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "deletedAt", ignore = true)
    @Mapping(target = "roles", ignore = true)
    @Mapping(target = "updatedAtOidc", ignore = true)
    @Mapping(target = "credentialsExpireAt", ignore = true)
    @Mapping(target = "accountExpireAt",  ignore = true)
    User toEntityFromUserCreateDTO(UserCreateDTO dto);

    @Mapping(target = "roles", qualifiedByName = "rolesToStringSet")
    UserResponseDTO toResponseDTOFromEntity(User entity);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "username", ignore = true)
    @Mapping(target = "email", ignore = true)
    @Mapping(target = "password", ignore = true)
    @Mapping(target = "roles", qualifiedByName = "stringSetToRoles")
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "deletedAt", ignore = true)
    @Mapping(target = "credentialsExpireAt", ignore = true)
    @Mapping(target = "accountExpireAt", ignore = true)
    @Mapping(target = "firstName", ignore = true)
    @Mapping(target = "middleName", ignore = true)
    @Mapping(target = "lastName", ignore = true)
    @Mapping(target = "nickname", ignore = true)
    @Mapping(target = "locale", ignore = true)
    @Mapping(target = "gender", ignore = true)
    @Mapping(target = "birthdate", ignore = true)
    @Mapping(target = "zoneinfo", ignore = true)
    @Mapping(target = "phoneNumber", ignore = true)
    @Mapping(target = "phoneNumberVerified", ignore = true)
    @Mapping(target = "profile", ignore = true)
    @Mapping(target = "website", ignore = true)
    @Mapping(target = "pictureUrl", ignore = true)
    @Mapping(target = "address", ignore = true)
    @Mapping(target = "oidcProvider", ignore = true)
    @Mapping(target = "oidcSubject", ignore = true)
    @Mapping(target = "updatedAtOidc", ignore = true)
    @Mapping(target = "locked",  ignore = true)
    void updateEntityFromAdminDTO(UserAdminUpdateDTO dto, @MappingTarget User entity);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "password", ignore = true)
    @Mapping(target = "roles", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "deletedAt", ignore = true)
    @Mapping(target = "emailVerified",  ignore = true)
    @Mapping(target = "active",  ignore = true)
    @Mapping(target = "credentialsExpireAt",  ignore = true)
    @Mapping(target = "accountExpireAt",  ignore = true)
    @Mapping(target = "middleName",  ignore = true)
    @Mapping(target = "nickname",  ignore = true)
    @Mapping(target = "gender",  ignore = true)
    @Mapping(target = "birthdate",  ignore = true)
    @Mapping(target = "zoneinfo",  ignore = true)
    @Mapping(target = "phoneNumber",  ignore = true)
    @Mapping(target = "phoneNumberVerified",  ignore = true)
    @Mapping(target = "profile",  ignore = true)
    @Mapping(target = "website",  ignore = true)
    @Mapping(target = "address",  ignore = true)
    @Mapping(target = "oidcProvider",  ignore = true)
    @Mapping(target = "oidcSubject",  ignore = true)
    @Mapping(target = "updatedAtOidc", ignore = true)
    @Mapping(target = "locked",  ignore = true)
    void updateEntityFromUpdateDTO(UserUpdateDTO dto, @MappingTarget User entity);

    @Mapping(target = "roles", expression = "java(mapRoles(user))")
    @Mapping(target = "permissions", expression = "java(mapPermissions(user))")
    UserPrincipalDTO toPrincipalDTO(User user);

    default Set<String> mapRoles(User user) {
        return user.getRoles().stream()
                .map(Role::getName)
                .collect(Collectors.toSet());
    }

    default Set<String> mapPermissions(User user) {
        return user.getRoles().stream()
                .flatMap(role -> role.getPermissions().stream())
                .map(Permission::getName)
                .collect(Collectors.toSet());
    }

    @Named("rolesToStringSet")
    default Set<String> mapRoles(Set<Role> roles) {
        return roles != null
                ? roles.stream().map(Role::getName).collect(Collectors.toSet())
                : Collections.emptySet();
    }

    @Named("stringSetToRoles")
    default Set<Role> mapRoleNamesToRoles(Set<String> roleNames) {
        if (roleNames == null || roleNames.isEmpty()) {
            return Collections.emptySet();
        }
        return roleNames.stream()
                .map(name -> Role.builder().name(name).build())
                .collect(Collectors.toSet());
    }
}



