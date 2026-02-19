package com.ged.infrastructure.persistence.repository;

import com.ged.domain.model.Document;
import com.ged.domain.model.enums.DocumentStatus;
import com.ged.domain.repository.DocumentRepository;
import com.ged.infrastructure.persistence.entity.DocumentEntity;
import com.ged.infrastructure.persistence.mapper.DocumentEntityMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class DocumentRepositoryImpl implements DocumentRepository {
    private final JpaDocumentRepositoryAdapter jpaRepository;
    private final DocumentEntityMapper mapper;

    @Override
    public Document save(Document document) {
        DocumentEntity entity = mapper.toEntity(document);
        DocumentEntity saved = jpaRepository.save(entity);
        return mapper.toDomain(saved);
    }

    @Override
    public Optional<Document> findById(Long id) {
        return jpaRepository.findById(id)
                .map(mapper::toDomain);
    }

    @Override
    public Page<Document> findAll(Pageable pageable) {
        return jpaRepository.findAll(pageable)
                .map(mapper::toDomain);
    }

    @Override
    public Page<Document> findByStatus(DocumentStatus status, Pageable pageable) {
        return jpaRepository.findByStatus(status, pageable)
                .map(mapper::toDomain);
    }

    @Override
    public Page<Document> findByTitleContaining(String title, Pageable pageable) {
        return jpaRepository.findByTitleContainingIgnoreCase(title, pageable)
                .map(mapper::toDomain);
    }

    @Override
    public Page<Document> findByStatusAndTitleContaining(DocumentStatus status, String title, Pageable pageable) {
        return jpaRepository.findByStatusAndTitleContainingIgnoreCase(status, title, pageable)
                .map(mapper::toDomain);
    }

    @Override
    public void deleteById(Long id) {
        jpaRepository.deleteById(id);
    }
}
