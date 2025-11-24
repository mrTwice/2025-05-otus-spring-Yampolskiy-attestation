package ru.otus.java.springframework.yampolskiy.ttuserservice.repositories;

import lombok.NonNull;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.otus.java.springframework.yampolskiy.ttuserservice.entities.Role;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface RoleRepository extends JpaRepository<Role, UUID> {

    @EntityGraph(attributePaths = {"permissions"})
    Optional<Role> findByName(String name);

    @EntityGraph(attributePaths = {"permissions"})
    @NonNull
    Optional<Role> findById(@NonNull UUID id);

    boolean existsByName(String name);

    @NonNull
    @Override
    @EntityGraph(attributePaths = {"permissions"})
    List<Role> findAll();
}

