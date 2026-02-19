package com.ged.application.usecase.document;

import com.ged.application.dto.request.CreateDocumentRequest;
import com.ged.application.dto.response.DocumentResponse;
import com.ged.application.mapper.DocumentMapper;
import com.ged.domain.model.Document;
import com.ged.domain.model.User;
import com.ged.domain.model.enums.DocumentStatus;
import com.ged.domain.repository.DocumentRepository;
import com.ged.infrastructure.security.SecurityContextHelper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class CreateDocumentUseCase {
    private final DocumentRepository documentRepository;
    private final DocumentMapper mapper;
    private final SecurityContextHelper securityContextHelper;

    @Transactional
    public DocumentResponse execute(CreateDocumentRequest request) {
        User currentUser = securityContextHelper.getCurrentUser();

        Document document = Document.builder()
                .title(request.getTitle())
                .description(request.getDescription())
                .tags(request.getTags())
                .owner(currentUser)
                .status(DocumentStatus.DRAFT)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        document.validate();

        Document saved = documentRepository.save(document);
        return mapper.toResponse(saved);
    }
}
