package ru.otus.java.springframework.yampolskiy.ttuserservice.services;

import ru.otus.java.springframework.yampolskiy.ttuserservice.entities.User;

import java.util.List;
import java.util.UUID;

public interface UserService {

    User createUser(User user);

    User getUserById(UUID id);

    User getUserByUsername(String username);

    User getUserByEmail(String email);

    boolean existsByUsername(String username);

    boolean existsByEmail(String email);

    List<User> getAllActiveUsers();

    User addRoleToUser(UUID userId, String roleName);

    User removeRoleFromUser(UUID userId, String roleName);

    User updateUser(UUID userId, User user);

    void deleteUser(UUID userId);

    User getUserByOidcSubject(String oidcSubject);

    boolean existsByOidcSubject(String oidcSubject);

    List<User> getUsersByOidcProvider(String oidcProvider);

    User getUserByOidcSubjectAndProvider(String subject, String provider);

    void updatePasswordHash(UUID userId, String newPasswordHash);
}