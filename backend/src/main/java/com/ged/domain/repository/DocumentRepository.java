package com.ged.domain.repository;

import com.ged.domain.model.Document;
import com.ged.domain.model.enums.DocumentStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface DocumentRepository {
    Document save(Document document);
    Optional<Document> findById(Long id);
    Page<Document> findAll(Pageable pageable);
    Page<Document> findByStatus(DocumentStatus status, Pageable pageable);
    Page<Document> findByTitleContaining(String title, Pageable pageable);
    Page<Document> findByStatusAndTitleContaining(DocumentStatus status, String title, Pageable pageable);
    void deleteById(Long id);
}
