package ru.otus.java.springframework.yampolskiy.tttaskservice.tasks.dtos.task;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Запрос на смену статуса задачи")
public class TaskStatusChangeRequest {

    @NotNull
    @Schema(description = "ID нового статуса", example = "09a23780-9917-4060-9ec5-bf63df174178")
    private UUID statusId;
}
