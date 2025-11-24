package ru.otus.java.springframework.yampolskiy.ttoauth2authorizationserver.client.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.Id;
import jakarta.persistence.Column;
import jakarta.persistence.Index;
import jakarta.persistence.Convert;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnTransformer;
import ru.otus.java.springframework.yampolskiy.ttoauth2authorizationserver.common.util.JsonListConverter;
import ru.otus.java.springframework.yampolskiy.ttoauth2authorizationserver.common.util.JsonMapConverter;

import java.time.Instant;
import java.util.List;
import java.util.Map;

@Entity
@Table(name = "oauth2_client",
        indexes = {
                @Index(name = "idx_oauth2_client_client_id", columnList = "clientId", unique = true)
        })
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RegisteredClientEntity {

    @Id
    private String id;

    @Column(nullable = false, unique = true)
    private String clientId;

    @Column(nullable = false)
    private Instant clientIdIssuedAt;

    private String clientSecret;

    private Instant clientSecretExpiresAt;

    @Column(nullable = false)
    private String clientName;

    @Convert(converter = JsonListConverter.class)
    @Column(nullable = false, columnDefinition = "jsonb")
    @ColumnTransformer(write = "?::jsonb")
    private List<String> clientAuthenticationMethods;

    @Convert(converter = JsonListConverter.class)
    @Column(nullable = false, columnDefinition = "jsonb")
    @ColumnTransformer(write = "?::jsonb")
    private List<String> authorizationGrantTypes;

    @Convert(converter = JsonListConverter.class)
    @Column(columnDefinition = "jsonb")
    @ColumnTransformer(write = "?::jsonb")
    private List<String> redirectUris;

    @Convert(converter = JsonListConverter.class)
    @Column(nullable = false, columnDefinition = "jsonb")
    @ColumnTransformer(write = "?::jsonb")
    private List<String> scopes;

    @Convert(converter = JsonMapConverter.class)
    @Column(nullable = false, columnDefinition = "jsonb")
    @ColumnTransformer(write = "?::jsonb")
    private Map<String, Object> clientSettings;

    @Convert(converter = JsonMapConverter.class)
    @Column(nullable = false, columnDefinition = "jsonb")
    @ColumnTransformer(write = "?::jsonb")
    private Map<String, Object> tokenSettings;
}