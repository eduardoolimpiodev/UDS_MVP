package com.ged.application.usecase.version;

import com.ged.domain.exception.VersionNotFoundException;
import com.ged.domain.model.DocumentVersion;
import com.ged.domain.repository.VersionRepository;
import com.ged.domain.service.StorageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class DownloadVersionUseCase {
    private final VersionRepository versionRepository;
    private final StorageService storageService;

    @Transactional(readOnly = true)
    public byte[] execute(Long versionId) {
        DocumentVersion version = versionRepository.findById(versionId)
                .orElseThrow(() -> new VersionNotFoundException("Version not found"));

        return storageService.retrieve(version.getFileKey());
    }

    public DocumentVersion getVersionMetadata(Long versionId) {
        return versionRepository.findById(versionId)
                .orElseThrow(() -> new VersionNotFoundException("Version not found"));
    }
}
