package ru.otus.java.springframework.yampolskiy.ttoauth2authorizationserver.auth.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.DeleteMapping;
import ru.otus.java.springframework.yampolskiy.ttoauth2authorizationserver.auth.core.entity.Oauth2Authorization;
import ru.otus.java.springframework.yampolskiy.ttoauth2authorizationserver.auth.core.service.AuthorizationService;

import java.util.List;

//@RestController
//@RequestMapping("/api/v1/authorizations")
@RequiredArgsConstructor
public class AuthorizationController {

    private final AuthorizationService authorizationService;

    @GetMapping("/client/{clientId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<Oauth2Authorization>> getAuthorizationsByClientId(@PathVariable String clientId) {
        return ResponseEntity.ok(authorizationService.getAuthorizationsByClientId(clientId));
    }

    @DeleteMapping("/user/{username}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> revokeAllTokensForUser(@PathVariable String username) {
        authorizationService.revokeAllTokensForUser(username);
        return ResponseEntity.noContent().build();
    }
}
