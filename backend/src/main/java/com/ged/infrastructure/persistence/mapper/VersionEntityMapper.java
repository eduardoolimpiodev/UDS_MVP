package com.ged.infrastructure.persistence.mapper;

import com.ged.domain.model.DocumentVersion;
import com.ged.infrastructure.persistence.entity.VersionEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class VersionEntityMapper {
    private final UserEntityMapper userMapper;

    public DocumentVersion toDomain(VersionEntity entity) {
        if (entity == null) return null;
        
        return DocumentVersion.builder()
                .id(entity.getId())
                .documentId(entity.getDocumentId())
                .versionNumber(entity.getVersionNumber())
                .fileKey(entity.getFileKey())
                .fileName(entity.getFileName())
                .fileSize(entity.getFileSize())
                .contentType(entity.getContentType())
                .uploadedAt(entity.getUploadedAt())
                .uploadedBy(userMapper.toDomain(entity.getUploadedBy()))
                .isCurrent(entity.getIsCurrent())
                .build();
    }

    public VersionEntity toEntity(DocumentVersion version) {
        if (version == null) return null;
        
        return VersionEntity.builder()
                .id(version.getId())
                .documentId(version.getDocumentId())
                .versionNumber(version.getVersionNumber())
                .fileKey(version.getFileKey())
                .fileName(version.getFileName())
                .fileSize(version.getFileSize())
                .contentType(version.getContentType())
                .uploadedAt(version.getUploadedAt())
                .uploadedBy(userMapper.toEntity(version.getUploadedBy()))
                .isCurrent(version.getIsCurrent())
                .build();
    }
}
