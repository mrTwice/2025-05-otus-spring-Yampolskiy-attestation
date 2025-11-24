package ru.otus.java.springframework.yampolskiy.tttaskservice.attachments.services;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.java.springframework.yampolskiy.tttaskservice.attachments.entities.Attachment;
import ru.otus.java.springframework.yampolskiy.tttaskservice.attachments.exceptions.AttachmentNotFoundException;
import ru.otus.java.springframework.yampolskiy.tttaskservice.attachments.repositories.AttachmentRepository;
import ru.otus.java.springframework.yampolskiy.tttaskservice.common.DomainValidator;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class AttachmentServiceImpl implements AttachmentService {

    private final AttachmentRepository attachmentRepository;

    private final DomainValidator<Attachment> attachmentValidator;

    @Override
    public Attachment upload(Attachment attachment) {
        attachmentValidator.validateForCreate(attachment);
        return attachmentRepository.save(attachment);
    }

    @Override
    public Attachment update(UUID id, Attachment updated) {
        Attachment existing = findById(id);
        attachmentValidator.validateForUpdate(existing, updated);
        existing.setFileName(updated.getFileName());
        return attachmentRepository.save(existing);
    }

    @Override
    public void delete(UUID id) {
        Attachment attachment = findById(id);
        attachmentRepository.delete(attachment);
    }

    @Override
    public void deleteMany(List<UUID> ids) {
        ids.forEach(this::delete);
    }

    @Override
    @Transactional(readOnly = true)
    public Attachment findById(UUID id) {
        return attachmentRepository.findById(id)
                .orElseThrow(() -> new AttachmentNotFoundException(id));
    }

    @Override
    @Transactional(readOnly = true)
    public List<Attachment> findByTaskId(UUID taskId) {
        return attachmentRepository.findByTaskId(taskId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Attachment> findByUploader(UUID uploaderId) {
        return attachmentRepository.findByUploadedBy(uploaderId);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Attachment> search(UUID taskId, UUID uploadedBy, String fileName, Pageable pageable) {
        return attachmentRepository.search(taskId, uploadedBy, fileName, pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public long countByTaskId(UUID taskId) {
        return attachmentRepository.countByTaskId(taskId);
    }

    @Override
    @Transactional(readOnly = true)
    public Long getTotalSizeByTask(UUID taskId) {
        return attachmentRepository.sumFileSizeByTaskId(taskId);
    }
}
