package ru.otus.java.springframework.yampolskiy.ttoauth2authorizationserver.client.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.java.springframework.yampolskiy.ttoauth2authorizationserver.client.entity.RegisteredClientEntity;
import ru.otus.java.springframework.yampolskiy.ttoauth2authorizationserver.common.exception.EntityNotFoundException;
import ru.otus.java.springframework.yampolskiy.ttoauth2authorizationserver.client.repository.RegisteredClientJpaRepository;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ClientManagementService {

    private final RegisteredClientJpaRepository clientRepository;

    @Transactional(readOnly = true)
    public List<RegisteredClientEntity> getAllClients() {
        return clientRepository.findAll();
    }

    @Transactional
    public RegisteredClientEntity saveClient(RegisteredClientEntity client) {
        if (client.getId() == null) {
            client.setId(UUID.randomUUID().toString());
        }
        if (client.getClientIdIssuedAt() == null) {
            client.setClientIdIssuedAt(Instant.now());
        }
        return clientRepository.save(client);
    }

    @Transactional(readOnly = true)
    public RegisteredClientEntity getClientByClientId(String clientId) {
        return clientRepository.findByClientId(clientId)
                .orElseThrow(() -> new EntityNotFoundException("Client not found with clientId: " + clientId));
    }

    @Transactional(readOnly = true)
    public boolean existsByClientId(String clientId) {
        return clientRepository.existsByClientId(clientId);
    }

    @Transactional
    public void deleteClientByClientId(String clientId) {
        RegisteredClientEntity client = getClientByClientId(clientId);
        clientRepository.delete(client);
    }
}