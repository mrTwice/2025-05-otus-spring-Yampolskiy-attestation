package ru.otus.java.springframework.yampolskiy.ttuserservice.validators;

import lombok.RequiredArgsConstructor;
import ru.otus.java.springframework.yampolskiy.ttuserservice.entities.User;
import ru.otus.java.springframework.yampolskiy.ttuserservice.exceptions.DuplicateResourceException;
import ru.otus.java.springframework.yampolskiy.ttuserservice.repositories.UserRepository;

@RequiredArgsConstructor
public class UserUniqueValidator implements Validator<User> {

    private final UserRepository userRepository;

    public void validate(User user) {
        if (userRepository.existsByUsername(user.getUsername())) {
            throw new DuplicateResourceException("Username is already taken: " + user.getUsername());
        }

        if (userRepository.existsByEmail(user.getEmail())) {
            throw new DuplicateResourceException("Email is already taken: " + user.getEmail());
        }
    }
}
