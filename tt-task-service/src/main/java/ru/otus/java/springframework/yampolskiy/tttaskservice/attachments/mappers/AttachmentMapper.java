package ru.otus.java.springframework.yampolskiy.tttaskservice.attachments.mappers;

import org.mapstruct.Mapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.MappingTarget;
import ru.otus.java.springframework.yampolskiy.tttaskservice.attachments.dtos.AttachmentResponse;
import ru.otus.java.springframework.yampolskiy.tttaskservice.attachments.dtos.AttachmentUpdateRequest;
import ru.otus.java.springframework.yampolskiy.tttaskservice.attachments.dtos.AttachmentUploadRequest;
import ru.otus.java.springframework.yampolskiy.tttaskservice.attachments.entities.Attachment;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING,
        unmappedTargetPolicy = ReportingPolicy.WARN)
public interface AttachmentMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "fileUrl", ignore = true)
    @Mapping(target = "fileSize", ignore = true)
    @Mapping(target = "contentType", ignore = true)
    @Mapping(target = "uploadedBy", ignore = true)
    @Mapping(target = "uploadedAt", ignore = true)
    Attachment toEntity(AttachmentUploadRequest request);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "taskId", ignore = true)
    @Mapping(target = "fileUrl", ignore = true)
    @Mapping(target = "fileSize", ignore = true)
    @Mapping(target = "contentType", ignore = true)
    @Mapping(target = "uploadedBy", ignore = true)
    @Mapping(target = "uploadedAt", ignore = true)
    void updateEntity(@MappingTarget Attachment entity, AttachmentUpdateRequest update);

    AttachmentResponse toResponse(Attachment attachment);

    List<AttachmentResponse> toResponseList(List<Attachment> attachments);
}
