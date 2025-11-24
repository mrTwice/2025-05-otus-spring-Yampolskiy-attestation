package ru.otus.java.springframework.yampolskiy.ttoauth2authorizationserver.infrastructure.dto;


import java.time.LocalDateTime;
import java.util.List;

public class ValidationEmailErrorDTO {

    private String code;

    private String message;

    private List<ValidationEmailPartErrorDTO> errors;

    private LocalDateTime dateTime;

    public ValidationEmailErrorDTO(String code, String message, List<ValidationEmailPartErrorDTO> errors) {
        this.code = code;
        this.message = message;
        this.errors = errors;
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

    public List<ValidationEmailPartErrorDTO> getErrors() {
        return errors;
    }

    public void setErrors(List<ValidationEmailPartErrorDTO> errors) {
        this.errors = errors;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public void setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }


}
