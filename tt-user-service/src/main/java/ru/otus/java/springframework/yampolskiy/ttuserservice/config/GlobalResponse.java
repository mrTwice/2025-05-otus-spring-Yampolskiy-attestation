package ru.otus.java.springframework.yampolskiy.ttuserservice.config;



public enum GlobalResponse {
    BAD_REQUEST("400", "Bad Request (ошибка запроса)", "#/components/schemas/ErrorDTO"),
    UNAUTHORIZED("401", "Unauthorized (неавторизован)", "#/components/schemas/ErrorDTO"),
    FORBIDDEN("403", "Forbidden (нет доступа)", "#/components/schemas/ErrorDTO"),
    NOT_FOUND("404", "Not Found", "#/components/schemas/ErrorDTO"),
    VALIDATION("422", "Unprocessable Entity (валидация email)", "#/components/schemas/ValidationEmailErrorDTO"),
    INTERNAL("500", "Internal Server Error", "#/components/schemas/ErrorDTO");

    private final String code;

    private final String description;

    private final String schema;

    GlobalResponse(String code, String description, String schema) {
        this.code = code;
        this.description = description;
        this.schema = schema;
    }

    public String getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }

    public String getSchema() {
        return schema;
    }
}
