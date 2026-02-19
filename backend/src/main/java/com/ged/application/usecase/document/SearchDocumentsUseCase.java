package com.ged.application.usecase.document;

import com.ged.application.dto.response.DocumentResponse;
import com.ged.application.mapper.DocumentMapper;
import com.ged.domain.model.Document;
import com.ged.domain.model.enums.DocumentStatus;
import com.ged.domain.repository.DocumentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class SearchDocumentsUseCase {
    private final DocumentRepository documentRepository;
    private final DocumentMapper mapper;

    @Transactional(readOnly = true)
    public Page<DocumentResponse> execute(DocumentStatus status, String title, Pageable pageable) {
        Page<Document> documents;

        if (status != null && title != null && !title.isEmpty()) {
            documents = documentRepository.findByStatusAndTitleContaining(status, title, pageable);
        } else if (status != null) {
            documents = documentRepository.findByStatus(status, pageable);
        } else if (title != null && !title.isEmpty()) {
            documents = documentRepository.findByTitleContaining(title, pageable);
        } else {
            documents = documentRepository.findAll(pageable);
        }

        return documents.map(mapper::toResponse);
    }
}
