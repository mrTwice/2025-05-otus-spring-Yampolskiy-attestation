package ru.otus.java.springframework.yampolskiy.ttoauth2authorizationserver.auth.core.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.java.springframework.yampolskiy.ttoauth2authorizationserver.auth.core.entity.Oauth2Authorization;

import java.util.List;
import java.util.Optional;

@Repository
public interface AuthorizationRepository extends JpaRepository<Oauth2Authorization, String> {

    @Transactional(readOnly = true)
    Optional<Oauth2Authorization> findByAccessTokenValue(String accessTokenValue);

    @Transactional(readOnly = true)
    Optional<Oauth2Authorization> findByRefreshTokenValue(String refreshTokenValue);

    @Transactional(readOnly = true)
    Optional<Oauth2Authorization> findByAuthorizationCodeValue(String authorizationCodeValue);

    @Transactional(readOnly = true)
    List<Oauth2Authorization> findByRegisteredClientId(String registeredClientId);

    @Modifying
    @Transactional
    void deleteByAccessTokenValue(String accessTokenValue);

    @Modifying
    @Transactional
    void deleteByRefreshTokenValue(String refreshTokenValue);

    @Transactional(readOnly = true)
    List<Oauth2Authorization> findByPrincipalName(String principal);

    @Query("""
                SELECT a FROM Oauth2Authorization a 
                WHERE cast(a.accessTokenValue as string) = :token 
                   OR cast(a.refreshTokenValue as string) = :token 
                   OR cast(a.authorizationCodeValue as string) = :token
            """)
    Optional<Oauth2Authorization> findByTokenValue(@Param("token") String token);

    Optional<Oauth2Authorization> findByState(String state);
}