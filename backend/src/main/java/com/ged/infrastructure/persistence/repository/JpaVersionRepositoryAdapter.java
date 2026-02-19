package com.ged.infrastructure.persistence.repository;

import com.ged.infrastructure.persistence.entity.VersionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface JpaVersionRepositoryAdapter extends JpaRepository<VersionEntity, Long> {
    List<VersionEntity> findByDocumentIdOrderByVersionNumberDesc(Long documentId);
    
    Optional<VersionEntity> findByDocumentIdAndIsCurrent(Long documentId, Boolean isCurrent);
    
    @Query("SELECT COALESCE(MAX(v.versionNumber), 0) + 1 FROM VersionEntity v WHERE v.documentId = :documentId")
    Integer getNextVersionNumber(@Param("documentId") Long documentId);
    
    @Modifying
    @Query("UPDATE VersionEntity v SET v.isCurrent = false WHERE v.documentId = :documentId")
    void markAllAsNotCurrent(@Param("documentId") Long documentId);
}
