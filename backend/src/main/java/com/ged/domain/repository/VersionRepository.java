package com.ged.domain.repository;

import com.ged.domain.model.DocumentVersion;

import java.util.List;
import java.util.Optional;

public interface VersionRepository {
    DocumentVersion save(DocumentVersion version);
    Optional<DocumentVersion> findById(Long id);
    List<DocumentVersion> findByDocumentId(Long documentId);
    Optional<DocumentVersion> findCurrentByDocumentId(Long documentId);
    Integer getNextVersionNumber(Long documentId);
    void markAllAsNotCurrent(Long documentId);
}
