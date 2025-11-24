package ru.otus.java.springframework.yampolskiy.tttaskservice.common.controller;

import lombok.AllArgsConstructor;
import lombok.Getter;
import ru.otus.java.springframework.yampolskiy.tttaskservice.common.dto.ErrorDTO;

import java.util.Arrays;

@Getter
@AllArgsConstructor
public enum ErrorMapping {
    TASK("/api/v1/tasks", "TASK_INTERNAL_ERROR",
            "Внутренняя ошибка при работе с задачами"),
    STATUS("/api/v1/task-statuses", "TASK_STATUS_INTERNAL_ERROR",
            "Внутренняя ошибка сервиса статусов задач"),
    PRIORITY("/api/v1/task-priorities", "TASK_PRIORITY_INTERNAL_ERROR",
            "Внутренняя ошибка сервиса приоритетов задач"),
    TYPE("/api/v1/task-types", "TASK_TYPE_INTERNAL_ERROR",
            "Внутренняя ошибка сервиса TaskType"),
    COMMENT("/api/v1/comments", "COMMENT_INTERNAL_ERROR",
            "Внутренняя ошибка сервиса комментариев"),
    ATTACH("/api/v1/attachments", "ATTACHMENT_INTERNAL_ERROR",
            "Внутренняя ошибка сервиса вложений");

    private final String prefix;

    private final String code;

    private final String message;

    public static ErrorDTO resolve(String uri) {
        return Arrays.stream(values())
                .filter(e -> uri.startsWith(e.prefix))
                .findFirst()
                .map(e -> new ErrorDTO(e.code, e.message))
                .orElse(new ErrorDTO("INTERNAL_ERROR",
                        "Внутренняя ошибка сервиса"));
    }
}
