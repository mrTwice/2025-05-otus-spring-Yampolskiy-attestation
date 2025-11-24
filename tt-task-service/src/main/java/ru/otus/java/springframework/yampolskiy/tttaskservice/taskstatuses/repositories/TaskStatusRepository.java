package ru.otus.java.springframework.yampolskiy.tttaskservice.taskstatuses.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.otus.java.springframework.yampolskiy.tttaskservice.taskstatuses.entities.TaskStatus;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface TaskStatusRepository extends JpaRepository<TaskStatus, UUID> {

    Optional<TaskStatus> findByCode(String code);

    List<TaskStatus> findAllByOrderBySortOrderAsc();

    List<TaskStatus> findByIsFinal(boolean isFinal);

    List<TaskStatus> findByIsDefault(boolean isDefault);

    @Query("""
        SELECT s FROM TaskStatus s
        WHERE (:search IS NULL OR LOWER(s.name) LIKE LOWER(CONCAT('%', :search, '%')))
        ORDER BY s.sortOrder ASC
    """)
    Page<TaskStatus> searchByName(
            @Param("search") String search,
            Pageable pageable
    );
}