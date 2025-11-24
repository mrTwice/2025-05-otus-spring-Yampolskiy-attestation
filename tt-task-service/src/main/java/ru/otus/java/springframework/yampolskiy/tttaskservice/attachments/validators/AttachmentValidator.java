package ru.otus.java.springframework.yampolskiy.tttaskservice.attachments.validators;

import ru.otus.java.springframework.yampolskiy.tttaskservice.attachments.entities.Attachment;
import ru.otus.java.springframework.yampolskiy.tttaskservice.attachments.exceptions.InvalidAttachmentException;
import ru.otus.java.springframework.yampolskiy.tttaskservice.common.DomainValidator;

public class AttachmentValidator implements DomainValidator<Attachment> {

    @Override
    public void validateForCreate(Attachment attachment) {
        validateCommon(attachment);
    }

    @Override
    public void validateForUpdate(Attachment existing, Attachment updated) {
        if (!existing.getTaskId().equals(updated.getTaskId())) {
            throw new InvalidAttachmentException("Task ID cannot be changed");
        }
        if (!existing.getUploadedBy().equals(updated.getUploadedBy())) {
            throw new InvalidAttachmentException("Uploader cannot be changed");
        }
        validateCommon(updated);
    }

    private void validateCommon(Attachment attachment) {
        if (attachment.getFileName() == null || attachment.getFileName().isBlank()) {
            throw new InvalidAttachmentException("File name is required");
        }
        if (attachment.getFileSize() != null && attachment.getFileSize() > 100_000_000) {
            throw new InvalidAttachmentException("File size exceeds 100MB limit");
        }
    }
}

