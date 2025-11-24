package ru.otus.java.springframework.yampolskiy.tttaskservice.comments.dtos;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Data;

import java.util.List;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Список комментариев по задаче")
public class CommentListResponse {

    @Schema(description = "ID задачи", example = "d960f4a5-55a4-4d89-9248-41712b42e0f9")
    private UUID taskId;

    @Schema(description = "Комментарии по задаче")
    private List<CommentResponse> comments;
}
