package ru.otus.java.springframework.yampolskiy.tttaskservice.comments.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import ru.otus.java.springframework.yampolskiy.tttaskservice.comments.dtos.CommentCreateRequest;
import ru.otus.java.springframework.yampolskiy.tttaskservice.comments.dtos.CommentResponse;
import ru.otus.java.springframework.yampolskiy.tttaskservice.comments.dtos.CommentUpdateRequest;
import ru.otus.java.springframework.yampolskiy.tttaskservice.comments.entities.Comment;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING,
        unmappedTargetPolicy = ReportingPolicy.WARN)
public interface CommentMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    Comment toEntity(CommentCreateRequest request);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "taskId", ignore = true)
    @Mapping(target = "authorId", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    void updateEntity(@MappingTarget Comment target, CommentUpdateRequest update);

    CommentResponse toResponse(Comment entity);

    List<CommentResponse> toResponseList(List<Comment> comments);
}
