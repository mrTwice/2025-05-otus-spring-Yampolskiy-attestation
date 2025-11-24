package ru.otus.java.springframework.yampolskiy.ttuserservice.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.java.springframework.yampolskiy.ttuserservice.entities.Role;
import ru.otus.java.springframework.yampolskiy.ttuserservice.entities.User;
import ru.otus.java.springframework.yampolskiy.ttuserservice.exceptions.ResourceNotFoundException;
import ru.otus.java.springframework.yampolskiy.ttuserservice.repositories.UserRepository;
import ru.otus.java.springframework.yampolskiy.ttuserservice.validators.Validator;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    private final RoleServiceImpl roleService;

    private final Validator<User> commonUserValidator;

    private final Validator<User> userUniqueValidator;

    private final Validator<String> userEmailValidator;

    @Override
    public User createUser(User user) {
        commonUserValidator.validate(user);
        userEmailValidator.validate(user.getEmail());
        userUniqueValidator.validate(user);

        if (user.getRoles() == null || user.getRoles().isEmpty()) {
            Role defaultRole = roleService.getRoleByName("USER");
            user.setRoles(Set.of(defaultRole));
        }

        user.setRoles(roleService.validateRoles(user.getRoles()));

        return userRepository.save(user);
    }

    @Override
    public User getUserById(UUID id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));
    }

    @Override
    public User getUserByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with username: " + username));
    }

    @Override
    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with email: " + email));
    }

    @Override
    public boolean existsByUsername(String username) {
        return userRepository.existsByUsername(username);
    }

    @Override
    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    @Override
    public List<User> getAllActiveUsers() {
        return userRepository.findAllByActiveTrue();
    }

    @Override
    public User addRoleToUser(UUID userId, String roleName) {
        User user = getUserById(userId);
        Role role = roleService.getRoleByName(roleName);
        user.getRoles().add(role);
        return userRepository.save(user);
    }

    @Override
    public User removeRoleFromUser(UUID userId, String roleName) {
        User user = getUserById(userId);
        Role role = roleService.getRoleByName(roleName);
        user.getRoles().remove(role);
        return userRepository.save(user);
    }

    @Override
    public User updateUser(UUID userId, User user) {
        User existingUser = getUserById(userId);
        existingUser.setUsername(user.getUsername());
        existingUser.setEmail(user.getEmail());
        existingUser.setFirstName(user.getFirstName());
        existingUser.setLastName(user.getLastName());
        existingUser.setLocale(user.getLocale());
        existingUser.setPictureUrl(user.getPictureUrl());
        return userRepository.save(existingUser);
    }

    @Override
    public void deleteUser(UUID userId) {
        User user = getUserById(userId);
        user.setActive(false);
        user.setDeletedAt(LocalDateTime.now());
        userRepository.save(user);
    }

    @Override
    public User getUserByOidcSubject(String oidcSubject) {
        return userRepository.findByOidcSubject(oidcSubject)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with OIDC subject: " + oidcSubject));
    }

    @Override
    public boolean existsByOidcSubject(String oidcSubject) {
        return userRepository.existsByOidcSubject(oidcSubject);
    }

    @Override
    public List<User> getUsersByOidcProvider(String oidcProvider) {
        return userRepository.findByOidcProvider(oidcProvider);
    }

    @Override
    public User getUserByOidcSubjectAndProvider(String subject, String provider) {
        return userRepository.findByOidcSubjectAndOidcProvider(subject, provider)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "User not found with OIDC provider " + provider + " and subject " + subject));
    }

    @Override
    public void updatePasswordHash(UUID userId, String newPasswordHash) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        user.setPassword(newPasswordHash);
        userRepository.save(user);
    }

}