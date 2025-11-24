package ru.otus.java.springframework.yampolskiy.ttuserservice.repositories;

import lombok.NonNull;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.otus.java.springframework.yampolskiy.ttuserservice.entities.User;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<User, UUID> {

    @EntityGraph(attributePaths = {"roles", "roles.permissions"})
    Optional<User> findByUsername(String username);

    @EntityGraph(attributePaths = {"roles", "roles.permissions"})
    Optional<User> findByEmail(String email);

    boolean existsByUsername(String username);

    boolean existsByEmail(String email);

    @EntityGraph(attributePaths = {"roles", "roles.permissions"})
    List<User> findAllByActiveTrue();

    @EntityGraph(attributePaths = {"roles", "roles.permissions"})
    Optional<User> findByOidcSubject(String oidcSubject);

    boolean existsByOidcSubject(String oidcSubject);

    @EntityGraph(attributePaths = {"roles", "roles.permissions"})
    List<User> findByOidcProvider(String oidcProvider);

    @EntityGraph(attributePaths = {"roles", "roles.permissions"})
    Optional<User> findByOidcSubjectAndOidcProvider(String oidcSubject, String oidcProvider);

    @Override
    @EntityGraph(attributePaths = {"roles", "roles.permissions"})
    @NonNull
    Optional<User> findById(@NonNull UUID uuid);
}
