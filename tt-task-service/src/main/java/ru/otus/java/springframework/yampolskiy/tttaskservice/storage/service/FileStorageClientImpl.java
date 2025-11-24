package ru.otus.java.springframework.yampolskiy.tttaskservice.storage.service;

import org.springframework.stereotype.Service;
import ru.otus.java.springframework.yampolskiy.tttaskservice.storage.dto.PresignedUploadResponse;

import java.util.UUID;

@Service
public class FileStorageClientImpl implements FileStorageClient {

    private static final String STORAGE_BASE_URL = "https://storage.localhost.local/bucket";

    private static final String CDN_BASE_URL = "https://cdn.localhost.local/files";

    @Override
    public PresignedUploadResponse generateUploadUrl(String originalFileName) {
        String extension = getExtension(originalFileName);
        String uuid = UUID.randomUUID().toString();
        String fileName = uuid + (extension.isBlank() ? "" : "." + extension);

        // Заглушка presigned URL — в реальности здесь будет сигнатура от MinIO
        String presignedUrl = STORAGE_BASE_URL + "/" + fileName + "?X-Fake-Signature=123";

        // Будущий URL файла
        String fileUrl = CDN_BASE_URL + "/" + fileName;

        return new PresignedUploadResponse(presignedUrl, fileUrl);
    }

    private String getExtension(String fileName) {
        if (fileName == null || !fileName.contains(".")) {
            return "";
        }
        return fileName.substring(fileName.lastIndexOf('.') + 1);
    }
}
