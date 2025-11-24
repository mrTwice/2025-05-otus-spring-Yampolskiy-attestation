package ru.otus.java.springframework.yampolskiy.tttaskservice.comments.dtos;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Data;

import java.time.Instant;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Ответ с информацией о комментарии")
public class CommentResponse {

    @Schema(description = "ID комментария", example = "a3f1e2b9-4fc2-4022-8a6a-3190c9f9ba89")
    private UUID id;

    @Schema(description = "ID задачи", example = "d960f4a5-55a4-4d89-9248-41712b42e0f9")
    private UUID taskId;

    @Schema(description = "ID автора", example = "c1d2f1e3-7d90-487b-b2f9-b8470ac2b246")
    private UUID authorId;

    @Schema(description = "Текст комментария", example = "Комментарий к задаче")
    private String content;

    @Schema(description = "Дата создания", example = "2024-04-01T12:00:00Z")
    private Instant createdAt;

    @Schema(description = "Дата обновления", example = "2024-04-02T12:00:00Z")
    private Instant updatedAt;
}
