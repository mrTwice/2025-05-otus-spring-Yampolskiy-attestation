package ru.otus.java.springframework.yampolskiy.tttaskservice.tasktypes.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.otus.java.springframework.yampolskiy.tttaskservice.tasktypes.entities.TaskType;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface TaskTypeRepository extends JpaRepository<TaskType, UUID> {

    Optional<TaskType> findByCodeAndDeletedAtIsNull(String code);

    Optional<TaskType> findByIdAndDeletedAtIsNull(UUID id);

    List<TaskType> findAllByDeletedAtIsNull();

    List<TaskType> findAllByDeletedAtIsNullOrderBySortOrderAsc();

    List<TaskType> findByIsDefaultTrueAndDeletedAtIsNull();

    @Query("""
        SELECT t FROM TaskType t
        WHERE t.deletedAt IS NULL AND
                (:search IS NULL OR LOWER(t.name) LIKE LOWER(CONCAT('%', :search, '%'))
                       OR LOWER(t.description) LIKE LOWER(CONCAT('%', :search, '%')))
        ORDER BY t.sortOrder ASC
    """)
    Page<TaskType> searchByNameOrDescription(@Param("search") String search, Pageable pageable);
}

