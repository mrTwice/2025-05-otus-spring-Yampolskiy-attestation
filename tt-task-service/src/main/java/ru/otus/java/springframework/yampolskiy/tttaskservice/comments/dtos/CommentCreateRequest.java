package ru.otus.java.springframework.yampolskiy.tttaskservice.comments.dtos;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Data;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Запрос на создание комментария")
public class CommentCreateRequest {

    @NotNull
    @Schema(description = "ID задачи", example = "d960f4a5-55a4-4d89-9248-41712b42e0f9")
    private UUID taskId;

    @NotNull
    @Schema(description = "ID автора", example = "c1d2f1e3-7d90-487b-b2f9-b8470ac2b246")
    private UUID authorId;

    @NotBlank
    @Size(max = 5000)
    @Schema(description = "Текст комментария", example = "Комментарий к задаче")
    private String content;
}

