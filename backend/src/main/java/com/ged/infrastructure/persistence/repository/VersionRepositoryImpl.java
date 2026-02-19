package com.ged.infrastructure.persistence.repository;

import com.ged.domain.model.DocumentVersion;
import com.ged.domain.repository.VersionRepository;
import com.ged.infrastructure.persistence.entity.VersionEntity;
import com.ged.infrastructure.persistence.mapper.VersionEntityMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class VersionRepositoryImpl implements VersionRepository {
    private final JpaVersionRepositoryAdapter jpaRepository;
    private final VersionEntityMapper mapper;

    @Override
    public DocumentVersion save(DocumentVersion version) {
        VersionEntity entity = mapper.toEntity(version);
        VersionEntity saved = jpaRepository.save(entity);
        return mapper.toDomain(saved);
    }

    @Override
    public Optional<DocumentVersion> findById(Long id) {
        return jpaRepository.findById(id)
                .map(mapper::toDomain);
    }

    @Override
    public List<DocumentVersion> findByDocumentId(Long documentId) {
        return jpaRepository.findByDocumentIdOrderByVersionNumberDesc(documentId)
                .stream()
                .map(mapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<DocumentVersion> findCurrentByDocumentId(Long documentId) {
        return jpaRepository.findByDocumentIdAndIsCurrent(documentId, true)
                .map(mapper::toDomain);
    }

    @Override
    public Integer getNextVersionNumber(Long documentId) {
        return jpaRepository.getNextVersionNumber(documentId);
    }

    @Override
    @Transactional
    public void markAllAsNotCurrent(Long documentId) {
        jpaRepository.markAllAsNotCurrent(documentId);
    }
}
