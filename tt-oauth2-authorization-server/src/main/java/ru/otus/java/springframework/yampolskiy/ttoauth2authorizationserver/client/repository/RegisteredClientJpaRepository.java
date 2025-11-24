package ru.otus.java.springframework.yampolskiy.ttoauth2authorizationserver.client.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.java.springframework.yampolskiy.ttoauth2authorizationserver.client.entity.RegisteredClientEntity;

import java.util.Optional;

@Repository
public interface RegisteredClientJpaRepository extends JpaRepository<RegisteredClientEntity, String> {

    @Transactional(readOnly = true)
    Optional<RegisteredClientEntity> findByClientId(String clientId);

    @Transactional(readOnly = true)
    boolean existsByClientId(String clientId);

    @Modifying
    @Transactional
    void deleteByClientId(String clientId);
}
