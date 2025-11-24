package ru.otus.java.springframework.yampolskiy.tttaskservice.taskstatuses.dtos;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Список статусов задач")
public class TaskStatusListResponse {

    @Schema(description = "Список статусов")
    private List<TaskStatusResponse> items;

    @Schema(description = "Всего", example = "5")
    private int total;
}

