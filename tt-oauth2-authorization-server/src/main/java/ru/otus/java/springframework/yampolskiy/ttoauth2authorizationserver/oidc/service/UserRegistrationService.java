package ru.otus.java.springframework.yampolskiy.ttoauth2authorizationserver.oidc.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.otus.java.springframework.yampolskiy.ttoauth2authorizationserver.integrations.exceptions.IntegrationException;
import ru.otus.java.springframework.yampolskiy.ttoauth2authorizationserver.integrations.users.client.UserRegistrationClient;
import ru.otus.java.springframework.yampolskiy.ttoauth2authorizationserver.integrations.users.dto.UserCreateDTO;

import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserRegistrationService {

    private final UserRegistrationClient userRegistrationClient;

    private final PasswordEncoder passwordEncoder;


    //TODO регистрация + авто-логин
    //TODO регистрацию + выдачу токена выделить в отдельный фасадный слой
    public void registerLocalUser(UserCreateDTO userCreateDTO) {
        UUID id = UUID.randomUUID();
        userCreateDTO.setId(id);
        userCreateDTO.setPassword(passwordEncoder.encode(userCreateDTO.getPassword()));
        userCreateDTO.setOidcProvider("local");
        userCreateDTO.setOidcSubject(id.toString());
        try {
            userRegistrationClient.createUser(userCreateDTO);
            log.info("Пользователь {} успешно зарегистрирован", userCreateDTO.getUsername());
        } catch (IntegrationException e) {
            log.warn(
                    "Ошибка при регистрации пользователя {}: [{}] {}",
                    userCreateDTO.getUsername(),
                    e.getCode(),
                    e.getMessage());
            throw e;
        }
    }
}
