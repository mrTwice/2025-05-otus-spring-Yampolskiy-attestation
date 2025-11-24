package ru.otus.java.springframework.yampolskiy.tttaskservice.taskstatuses.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import ru.otus.java.springframework.yampolskiy.tttaskservice.taskstatuses.dtos.TaskStatusCreateRequest;
import ru.otus.java.springframework.yampolskiy.tttaskservice.taskstatuses.dtos.TaskStatusResponse;
import ru.otus.java.springframework.yampolskiy.tttaskservice.taskstatuses.dtos.TaskStatusShortResponse;
import ru.otus.java.springframework.yampolskiy.tttaskservice.taskstatuses.dtos.TaskStatusUpdateRequest;
import ru.otus.java.springframework.yampolskiy.tttaskservice.taskstatuses.entities.TaskStatus;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING,
        unmappedTargetPolicy = ReportingPolicy.WARN)
public interface TaskStatusMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    TaskStatus toEntity(TaskStatusCreateRequest request);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "code", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    void updateEntity(@MappingTarget TaskStatus existing, TaskStatusUpdateRequest update);

    TaskStatusResponse toResponse(TaskStatus entity);

    TaskStatusShortResponse toShortResponse(TaskStatus entity);

    List<TaskStatusResponse> toResponseList(List<TaskStatus> entities);

    List<TaskStatusShortResponse> toShortResponseList(List<TaskStatus> entities);
}
