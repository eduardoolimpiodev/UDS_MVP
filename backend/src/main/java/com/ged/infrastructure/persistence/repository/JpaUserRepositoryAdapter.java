package com.ged.infrastructure.persistence.repository;

import com.ged.infrastructure.persistence.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface JpaUserRepositoryAdapter extends JpaRepository<UserEntity, Long> {
    Optional<UserEntity> findByUsername(String username);
}
