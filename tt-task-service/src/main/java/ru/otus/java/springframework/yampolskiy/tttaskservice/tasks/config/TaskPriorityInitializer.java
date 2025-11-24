package ru.otus.java.springframework.yampolskiy.tttaskservice.tasks.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import ru.otus.java.springframework.yampolskiy.tttaskservice.tasks.entities.TaskPriority;
import ru.otus.java.springframework.yampolskiy.tttaskservice.tasks.services.taskpriority.TaskPriorityService;

import java.util.List;

@Component
@RequiredArgsConstructor
@Order(12)
@Slf4j
public class TaskPriorityInitializer implements CommandLineRunner {

    private final TaskPriorityService taskPriorityService;

    @Override
    public void run(String... args) {
        log.info("🔧 Checking for default task priorities...");

        if (!taskPriorityService.findAll().isEmpty()) {
            log.info("✅ Task priorities already exist. Skipping initialization.");
            return;
        }

        log.info("➕ Inserting default task priorities...");

        List<TaskPriority> defaults = List.of(
                new TaskPriority(null, "high", "High", "High urgency", 1, "#FF3B30", true, null, null, null),
                new TaskPriority(null, "medium", "Medium", "Normal urgency", 2, "#FF9500", false, null, null, null),
                new TaskPriority(null, "low", "Low", "Low urgency", 3, "#34C759", false, null, null, null)
        );

        defaults.forEach(taskPriorityService::create);

        log.info("✅ Default task priorities initialized.");
    }
}