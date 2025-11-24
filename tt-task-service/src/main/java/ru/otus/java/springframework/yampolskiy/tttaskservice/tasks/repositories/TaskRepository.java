package ru.otus.java.springframework.yampolskiy.tttaskservice.tasks.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.otus.java.springframework.yampolskiy.tttaskservice.tasks.entities.Task;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface TaskRepository extends JpaRepository<Task, UUID>, JpaSpecificationExecutor<Task> {

    List<Task> findActiveTasksByStatusCodeAndAssigneeId(String statusCode, UUID assigneeId);

    long countByAssigneeIdAndDeletedAtIsNull(UUID assigneeId);

    List<Task> findByParentId(UUID parentId);

    List<Task> findByAssigneeIdAndDeletedAtIsNull(UUID assigneeId);

    List<Task> findByCreatorIdAndDeletedAtIsNull(UUID creatorId);

    @Query("""
                SELECT t FROM Task t
                LEFT JOIN FETCH t.subtasks
                WHERE t.id = :id
            """)
    Optional<Task> findById(@Param("id") UUID id);

}
