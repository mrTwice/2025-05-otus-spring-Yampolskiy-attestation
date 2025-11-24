package ru.otus.java.springframework.yampolskiy.tttaskservice.tasktypes.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import ru.otus.java.springframework.yampolskiy.tttaskservice.tasktypes.entities.TaskType;
import ru.otus.java.springframework.yampolskiy.tttaskservice.tasktypes.services.TaskTypeService;

import java.util.List;

@Component
@RequiredArgsConstructor
@Order(10)
@Slf4j
public class TaskTypeInitializer implements CommandLineRunner {

    private final TaskTypeService taskTypeService;

    @Override
    public void run(String... args) {
        log.info("🔧 Checking for default task types...");

        if (!taskTypeService.findAll().isEmpty()) {
            log.info("✅ Task types already exist. Skipping initialization.");
            return;
        }

        log.info("➕ Inserting default task types...");

        List<TaskType> defaults = List.of(
                new TaskType(null, "bug", "Bug", "A bug or defect", true, 1, "🐞", null, null, null),
                new TaskType(null, "feature", "Feature", "New feature or improvement", false, 2, "✨", null, null, null),
                new TaskType(null, "task", "Task", "General task or action", false, 3, "📝", null, null, null)
        );

        defaults.forEach(taskTypeService::create);

        log.info("✅ Default task types initialized.");
    }
}