package ru.otus.java.springframework.yampolskiy.tttaskservice.tasktypes.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import ru.otus.java.springframework.yampolskiy.tttaskservice.tasktypes.dtos.TaskTypeCreateRequest;
import ru.otus.java.springframework.yampolskiy.tttaskservice.tasktypes.dtos.TaskTypeResponse;
import ru.otus.java.springframework.yampolskiy.tttaskservice.tasktypes.dtos.TaskTypeShortResponse;
import ru.otus.java.springframework.yampolskiy.tttaskservice.tasktypes.dtos.TaskTypeUpdateRequest;
import ru.otus.java.springframework.yampolskiy.tttaskservice.tasktypes.entities.TaskType;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING,
        unmappedTargetPolicy = ReportingPolicy.WARN)
public interface TaskTypeMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "deletedAt", ignore = true)
    TaskType toEntity(TaskTypeCreateRequest request);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "code", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "deletedAt", ignore = true)
    void updateEntity(@MappingTarget TaskType target, TaskTypeUpdateRequest update);

    TaskTypeResponse toResponse(TaskType type);

    TaskTypeShortResponse toShortResponse(TaskType type);

    List<TaskTypeResponse> toResponseList(List<TaskType> types);

    List<TaskTypeShortResponse> toShortResponseList(List<TaskType> types);
}
