package com.ged.infrastructure.persistence.repository;

import com.ged.domain.model.enums.DocumentStatus;
import com.ged.infrastructure.persistence.entity.DocumentEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaDocumentRepositoryAdapter extends JpaRepository<DocumentEntity, Long> {
    Page<DocumentEntity> findByStatus(DocumentStatus status, Pageable pageable);
    Page<DocumentEntity> findByTitleContainingIgnoreCase(String title, Pageable pageable);
    Page<DocumentEntity> findByStatusAndTitleContainingIgnoreCase(DocumentStatus status, String title, Pageable pageable);
}
