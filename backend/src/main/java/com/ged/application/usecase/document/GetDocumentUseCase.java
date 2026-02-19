package com.ged.application.usecase.document;

import com.ged.application.dto.response.DocumentResponse;
import com.ged.application.mapper.DocumentMapper;
import com.ged.domain.exception.DocumentNotFoundException;
import com.ged.domain.model.Document;
import com.ged.domain.repository.DocumentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class GetDocumentUseCase {
    private final DocumentRepository documentRepository;
    private final DocumentMapper mapper;

    @Transactional(readOnly = true)
    public DocumentResponse execute(Long id) {
        Document document = documentRepository.findById(id)
                .orElseThrow(() -> new DocumentNotFoundException("Document not found"));

        return mapper.toResponse(document);
    }
}
