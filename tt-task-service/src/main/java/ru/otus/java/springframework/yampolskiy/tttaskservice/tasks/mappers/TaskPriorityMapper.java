package ru.otus.java.springframework.yampolskiy.tttaskservice.tasks.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import ru.otus.java.springframework.yampolskiy.tttaskservice.tasks.dtos.taskpriority.TaskPriorityCreateRequest;
import ru.otus.java.springframework.yampolskiy.tttaskservice.tasks.dtos.taskpriority.TaskPriorityResponse;
import ru.otus.java.springframework.yampolskiy.tttaskservice.tasks.dtos.taskpriority.TaskPriorityShortResponse;
import ru.otus.java.springframework.yampolskiy.tttaskservice.tasks.dtos.taskpriority.TaskPriorityUpdateRequest;
import ru.otus.java.springframework.yampolskiy.tttaskservice.tasks.entities.TaskPriority;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING,
        unmappedTargetPolicy = ReportingPolicy.WARN)
public interface TaskPriorityMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "deletedAt", ignore = true)
    TaskPriority toEntity(TaskPriorityCreateRequest request);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "code", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "deletedAt", ignore = true)
    void updateEntity(@MappingTarget TaskPriority existing, TaskPriorityUpdateRequest update);

    TaskPriorityResponse toResponse(TaskPriority priority);


    TaskPriorityShortResponse toShortResponse(TaskPriority priority);

    List<TaskPriorityResponse> toResponseList(List<TaskPriority> priorities);

    List<TaskPriorityShortResponse> toShortResponseList(List<TaskPriority> priorities);
}

