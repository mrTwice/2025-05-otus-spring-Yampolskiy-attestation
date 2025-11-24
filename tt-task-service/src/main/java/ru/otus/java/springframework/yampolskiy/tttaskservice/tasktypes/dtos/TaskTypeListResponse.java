package ru.otus.java.springframework.yampolskiy.tttaskservice.tasktypes.dtos;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Список типов задач с метаинформацией")
public class TaskTypeListResponse {

    @Schema(description = "Элементы списка")
    private List<TaskTypeResponse> items;

    @Schema(description = "Всего элементов", example = "5")
    private int total;
}

