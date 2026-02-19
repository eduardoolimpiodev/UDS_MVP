package com.ged.application.usecase.version;

import com.ged.application.dto.response.VersionResponse;
import com.ged.application.mapper.VersionMapper;
import com.ged.domain.model.DocumentVersion;
import com.ged.domain.repository.VersionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class GetVersionsUseCase {
    private final VersionRepository versionRepository;
    private final VersionMapper mapper;

    @Transactional(readOnly = true)
    public List<VersionResponse> execute(Long documentId) {
        List<DocumentVersion> versions = versionRepository.findByDocumentId(documentId);
        return versions.stream()
                .map(mapper::toResponse)
                .collect(Collectors.toList());
    }
}
