package com.ged.presentation.controller;

import com.ged.application.dto.request.CreateDocumentRequest;
import com.ged.application.dto.request.UpdateDocumentRequest;
import com.ged.application.dto.response.DocumentResponse;
import com.ged.application.usecase.document.*;
import com.ged.domain.model.enums.DocumentStatus;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/documents")
@RequiredArgsConstructor
public class DocumentController {
    private final CreateDocumentUseCase createDocumentUseCase;
    private final UpdateDocumentUseCase updateDocumentUseCase;
    private final GetDocumentUseCase getDocumentUseCase;
    private final SearchDocumentsUseCase searchDocumentsUseCase;
    private final PublishDocumentUseCase publishDocumentUseCase;
    private final ArchiveDocumentUseCase archiveDocumentUseCase;
    private final DeleteDocumentUseCase deleteDocumentUseCase;

    @PostMapping
    public ResponseEntity<DocumentResponse> create(@Valid @RequestBody CreateDocumentRequest request) {
        DocumentResponse response = createDocumentUseCase.execute(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<DocumentResponse> getById(@PathVariable Long id) {
        DocumentResponse response = getDocumentUseCase.execute(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<Page<DocumentResponse>> search(
            @RequestParam(required = false) DocumentStatus status,
            @RequestParam(required = false) String title,
            @PageableDefault(size = 10, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {
        Page<DocumentResponse> response = searchDocumentsUseCase.execute(status, title, pageable);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<DocumentResponse> update(
            @PathVariable Long id,
            @Valid @RequestBody UpdateDocumentRequest request) {
        DocumentResponse response = updateDocumentUseCase.execute(id, request);
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{id}/publish")
    public ResponseEntity<DocumentResponse> publish(@PathVariable Long id) {
        DocumentResponse response = publishDocumentUseCase.execute(id);
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{id}/archive")
    public ResponseEntity<DocumentResponse> archive(@PathVariable Long id) {
        DocumentResponse response = archiveDocumentUseCase.execute(id);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        deleteDocumentUseCase.execute(id);
        return ResponseEntity.noContent().build();
    }
}
