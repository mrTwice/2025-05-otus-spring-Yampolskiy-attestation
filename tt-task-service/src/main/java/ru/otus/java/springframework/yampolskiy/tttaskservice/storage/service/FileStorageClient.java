package ru.otus.java.springframework.yampolskiy.tttaskservice.storage.service;

import ru.otus.java.springframework.yampolskiy.tttaskservice.storage.dto.PresignedUploadResponse;

public interface FileStorageClient {
    PresignedUploadResponse generateUploadUrl(String fileName);
}
