package ru.otus.java.springframework.yampolskiy.tttaskservice.taskstatuses.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import ru.otus.java.springframework.yampolskiy.tttaskservice.taskstatuses.services.TaskStatusService;

@Component
@RequiredArgsConstructor
@Order(11)
@Slf4j
public class TaskStatusInitializer implements CommandLineRunner {

    private final TaskStatusService taskStatusService;

    private final TaskStatusFactory taskStatusFactory;

    @Override
    public void run(String... args) {
        log.info("🔧 Checking for default task statuses...");

        if (!taskStatusService.findAll().isEmpty()) {
            log.info("✅ Task statuses already exist. Skipping initialization.");
            return;
        }

        log.info("➕ Inserting default task statuses...");

        taskStatusFactory.defaultStatuses()
                .forEach(taskStatusService::create);

        log.info("✅ Default task statuses initialized.");
    }
}
