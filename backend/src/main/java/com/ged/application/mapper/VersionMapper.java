package com.ged.application.mapper;

import com.ged.application.dto.response.VersionResponse;
import com.ged.domain.model.DocumentVersion;
import org.springframework.stereotype.Component;

@Component
public class VersionMapper {
    public VersionResponse toResponse(DocumentVersion version) {
        return VersionResponse.builder()
                .id(version.getId())
                .documentId(version.getDocumentId())
                .versionNumber(version.getVersionNumber())
                .fileName(version.getFileName())
                .fileSize(version.getFileSize())
                .contentType(version.getContentType())
                .uploadedAt(version.getUploadedAt())
                .uploadedBy(version.getUploadedBy() != null ? version.getUploadedBy().getUsername() : null)
                .isCurrent(version.getIsCurrent())
                .build();
    }
}
