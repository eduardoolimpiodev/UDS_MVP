package com.ged.presentation.controller;

import com.ged.application.dto.response.VersionResponse;
import com.ged.application.usecase.version.DownloadVersionUseCase;
import com.ged.application.usecase.version.GetVersionsUseCase;
import com.ged.application.usecase.version.UploadVersionUseCase;
import com.ged.domain.model.DocumentVersion;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/documents/{documentId}/versions")
@RequiredArgsConstructor
public class VersionController {
    private final UploadVersionUseCase uploadVersionUseCase;
    private final GetVersionsUseCase getVersionsUseCase;
    private final DownloadVersionUseCase downloadVersionUseCase;

    @PostMapping
    public ResponseEntity<VersionResponse> upload(
            @PathVariable Long documentId,
            @RequestParam("file") MultipartFile file) {
        VersionResponse response = uploadVersionUseCase.execute(documentId, file);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    public ResponseEntity<List<VersionResponse>> getVersions(@PathVariable Long documentId) {
        List<VersionResponse> response = getVersionsUseCase.execute(documentId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{versionId}/download")
    public ResponseEntity<byte[]> download(@PathVariable Long documentId, @PathVariable Long versionId) {
        byte[] fileContent = downloadVersionUseCase.execute(versionId);
        DocumentVersion version = downloadVersionUseCase.getVersionMetadata(versionId);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType(version.getContentType()));
        headers.setContentDispositionFormData("attachment", version.getFileName());
        headers.setContentLength(fileContent.length);

        return ResponseEntity.ok()
                .headers(headers)
                .body(fileContent);
    }
}
