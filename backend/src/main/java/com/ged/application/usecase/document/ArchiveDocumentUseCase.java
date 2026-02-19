package com.ged.application.usecase.document;

import com.ged.application.dto.response.DocumentResponse;
import com.ged.application.mapper.DocumentMapper;
import com.ged.domain.exception.DocumentNotFoundException;
import com.ged.domain.exception.UnauthorizedAccessException;
import com.ged.domain.model.Document;
import com.ged.domain.model.User;
import com.ged.domain.model.enums.UserRole;
import com.ged.domain.repository.DocumentRepository;
import com.ged.infrastructure.security.SecurityContextHelper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ArchiveDocumentUseCase {
    private final DocumentRepository documentRepository;
    private final DocumentMapper mapper;
    private final SecurityContextHelper securityContextHelper;

    @Transactional
    public DocumentResponse execute(Long id) {
        User currentUser = securityContextHelper.getCurrentUser();

        Document document = documentRepository.findById(id)
                .orElseThrow(() -> new DocumentNotFoundException("Document not found"));

        if (!document.isOwnedBy(currentUser) && currentUser.getRole() != UserRole.ADMIN) {
            throw new UnauthorizedAccessException("You don't have permission to archive this document");
        }

        document.archive();
        Document updated = documentRepository.save(document);
        return mapper.toResponse(updated);
    }
}
