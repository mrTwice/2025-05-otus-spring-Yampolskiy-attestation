package ru.otus.java.springframework.yampolskiy.tttaskservice.attachments.services;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ru.otus.java.springframework.yampolskiy.tttaskservice.attachments.entities.Attachment;

import java.util.List;
import java.util.UUID;

public interface AttachmentService {

    Attachment upload(Attachment attachment);

    Attachment update(UUID id, Attachment updated);

    void delete(UUID id);

    void deleteMany(List<UUID> ids);

    Attachment findById(UUID id);

    List<Attachment> findByTaskId(UUID taskId);

    List<Attachment> findByUploader(UUID uploaderId);

    Page<Attachment> search(UUID taskId, UUID uploadedBy, String fileName, Pageable pageable);

    long countByTaskId(UUID taskId);

    Long getTotalSizeByTask(UUID taskId);
}

