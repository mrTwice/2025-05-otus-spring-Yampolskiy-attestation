package ru.otus.java.springframework.yampolskiy.ttoauth2authorizationserver.auth.core.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.java.springframework.yampolskiy.ttoauth2authorizationserver.auth.core.entity.JwkKey;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface JwkKeyRepository extends JpaRepository<JwkKey, UUID> {

    Optional<JwkKey> findFirstByIsActiveTrueOrderByCreatedAtDesc();

    Optional<JwkKey> findFirstByIsPrimaryTrueAndIsActiveTrueOrderByCreatedAtDesc();

    List<JwkKey> findAllByIsActiveTrueOrderByCreatedAtDesc();

    @Modifying
    @Transactional
    @Query("UPDATE JwkKey k SET k.isActive = false WHERE k.isActive = true")
    void updateAllSetInactive();

    @Modifying
    @Transactional
    @Query("UPDATE JwkKey k SET k.isPrimary = false")
    void resetPrimaryFlag();

    @Modifying
    @Transactional
    @Query("UPDATE JwkKey k SET k.isPrimary = true WHERE k.kid = :kid")
    void markKeyAsPrimary(@Param("kid") UUID kid);

    @Modifying
    @Query("UPDATE JwkKey k SET k.isPrimary = false WHERE k.isPrimary = true")
    void clearPrimaryFromAll();

}