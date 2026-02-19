package com.ged.application.usecase.document;

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
public class DeleteDocumentUseCase {
    private final DocumentRepository documentRepository;
    private final SecurityContextHelper securityContextHelper;

    @Transactional
    public void execute(Long id) {
        User currentUser = securityContextHelper.getCurrentUser();

        if (currentUser.getRole() != UserRole.ADMIN) {
            throw new UnauthorizedAccessException("Only admins can delete documents");
        }

        Document document = documentRepository.findById(id)
                .orElseThrow(() -> new DocumentNotFoundException("Document not found"));

        documentRepository.deleteById(id);
    }
}
