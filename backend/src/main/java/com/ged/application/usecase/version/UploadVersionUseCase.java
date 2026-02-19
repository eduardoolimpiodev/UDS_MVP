package com.ged.application.usecase.version;

import com.ged.application.dto.response.VersionResponse;
import com.ged.application.mapper.VersionMapper;
import com.ged.domain.exception.DocumentNotFoundException;
import com.ged.domain.exception.UnauthorizedAccessException;
import com.ged.domain.model.Document;
import com.ged.domain.model.DocumentVersion;
import com.ged.domain.model.User;
import com.ged.domain.model.enums.UserRole;
import com.ged.domain.repository.DocumentRepository;
import com.ged.domain.repository.VersionRepository;
import com.ged.domain.service.StorageService;
import com.ged.infrastructure.security.SecurityContextHelper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class UploadVersionUseCase {
    private final DocumentRepository documentRepository;
    private final VersionRepository versionRepository;
    private final StorageService storageService;
    private final VersionMapper mapper;
    private final SecurityContextHelper securityContextHelper;

    @Transactional
    public VersionResponse execute(Long documentId, MultipartFile file) {
        User currentUser = securityContextHelper.getCurrentUser();

        Document document = documentRepository.findById(documentId)
                .orElseThrow(() -> new DocumentNotFoundException("Document not found"));

        if (!document.isOwnedBy(currentUser) && currentUser.getRole() != UserRole.ADMIN) {
            throw new UnauthorizedAccessException("You don't have permission to upload versions for this document");
        }

        Integer nextVersion = versionRepository.getNextVersionNumber(documentId);
        String path = String.format("%d/%d", documentId, nextVersion);
        String fileKey = storageService.store(file, path);

        versionRepository.markAllAsNotCurrent(documentId);

        DocumentVersion version = DocumentVersion.builder()
                .documentId(documentId)
                .versionNumber(nextVersion)
                .fileKey(fileKey)
                .fileName(file.getOriginalFilename())
                .fileSize(file.getSize())
                .contentType(file.getContentType())
                .uploadedAt(LocalDateTime.now())
                .uploadedBy(currentUser)
                .isCurrent(true)
                .build();

        DocumentVersion saved = versionRepository.save(version);
        return mapper.toResponse(saved);
    }
}
