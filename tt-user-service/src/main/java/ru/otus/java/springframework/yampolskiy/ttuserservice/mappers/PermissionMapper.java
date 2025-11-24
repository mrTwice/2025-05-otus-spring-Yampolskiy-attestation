package ru.otus.java.springframework.yampolskiy.ttuserservice.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;
import ru.otus.java.springframework.yampolskiy.ttuserservice.dtos.permissions.PermissionDTO;
import ru.otus.java.springframework.yampolskiy.ttuserservice.dtos.permissions.PermissionResponseDTO;
import ru.otus.java.springframework.yampolskiy.ttuserservice.entities.Permission;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING,
        unmappedTargetPolicy = ReportingPolicy.WARN)
public interface PermissionMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    Permission toEntityFromPermissionDTO(PermissionDTO dto);

    PermissionResponseDTO toResponseDTOFromEntity(Permission entity);

    Permission toEntityFromResponseDTO(PermissionResponseDTO dto);
}