package ru.otus.java.springframework.yampolskiy.tttaskservice.tasks.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import ru.otus.java.springframework.yampolskiy.tttaskservice.tasks.dtos.task.TaskCreateRequest;
import ru.otus.java.springframework.yampolskiy.tttaskservice.tasks.dtos.task.TaskResponse;
import ru.otus.java.springframework.yampolskiy.tttaskservice.tasks.dtos.task.TaskShortResponse;
import ru.otus.java.springframework.yampolskiy.tttaskservice.tasks.dtos.task.TaskUpdateRequest;
import ru.otus.java.springframework.yampolskiy.tttaskservice.tasks.entities.Task;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING,
        unmappedTargetPolicy = ReportingPolicy.WARN)
public interface TaskMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "statusCode", ignore = true)
    @Mapping(target = "typeCode", ignore = true)
    @Mapping(target = "deletedAt", ignore = true)
    @Mapping(target = "completedAt", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "parent", ignore = true)
    @Mapping(target = "subtasks", ignore = true)
    @Mapping(target = "priorityCode", ignore = true)
    Task toEntity(TaskCreateRequest request);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "deletedAt", ignore = true)
    @Mapping(target = "priorityId", ignore = true)
    @Mapping(target = "priorityCode", ignore = true)
    @Mapping(target = "statusCode", ignore = true)
    @Mapping(target = "typeCode", ignore = true)
    @Mapping(target = "creatorId", ignore = true)
    @Mapping(target = "parent", ignore = true)
    @Mapping(target = "subtasks", ignore = true)
    void updateEntity(@MappingTarget Task task, TaskUpdateRequest request);

    @Mapping(source = "parent.id", target = "parentId")
    TaskResponse toResponse(Task task);

    @Mapping(source = "id", target = "id")
    @Mapping(source = "title", target = "title")
    @Mapping(source = "statusCode", target = "statusCode")
    @Mapping(source = "typeCode", target = "typeCode")
    TaskShortResponse toShortResponse(Task task);

    List<TaskShortResponse> toShortResponseList(List<Task> tasks);
}

