package ru.otus.java.springframework.yampolskiy.ttoauth2authorizationserver.infrastructure.dto;

import java.time.LocalDateTime;

public class ErrorDTO {
    private String code;

    private String message;

    private LocalDateTime dateTime;

    public ErrorDTO(String code, String message) {
        this.code = code;
        this.message = message;
        this.dateTime = LocalDateTime.now();
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public void setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }

}

