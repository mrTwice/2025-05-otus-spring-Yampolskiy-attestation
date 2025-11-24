package ru.otus.java.springframework.yampolskiy.ttoauth2authorizationserver.client.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import ru.otus.java.springframework.yampolskiy.ttoauth2authorizationserver.client.entity.RegisteredClientEntity;
import ru.otus.java.springframework.yampolskiy.ttoauth2authorizationserver.client.service.ClientManagementService;

import java.util.List;

//@RestController
//@RequestMapping("/api/v1/clients")
@RequiredArgsConstructor
public class ClientController {

    private final ClientManagementService clientService;

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<RegisteredClientEntity>> getAllClients() {
        return ResponseEntity.ok(clientService.getAllClients());
    }

    @GetMapping("/{clientId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<RegisteredClientEntity> getClientByClientId(@PathVariable String clientId) {
        return ResponseEntity.ok(clientService.getClientByClientId(clientId));
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<RegisteredClientEntity> createClient(@RequestBody RegisteredClientEntity client) {
        return ResponseEntity.ok(clientService.saveClient(client));
    }

    @DeleteMapping("/{clientId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteClient(@PathVariable String clientId) {
        clientService.deleteClientByClientId(clientId);
        return ResponseEntity.noContent().build();
    }
}