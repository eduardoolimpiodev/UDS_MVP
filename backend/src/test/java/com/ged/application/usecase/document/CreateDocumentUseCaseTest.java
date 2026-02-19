package com.ged.application.usecase.document;

import com.ged.application.dto.request.CreateDocumentRequest;
import com.ged.application.dto.response.DocumentResponse;
import com.ged.application.mapper.DocumentMapper;
import com.ged.domain.model.Document;
import com.ged.domain.model.User;
import com.ged.domain.model.enums.DocumentStatus;
import com.ged.domain.model.enums.UserRole;
import com.ged.domain.repository.DocumentRepository;
import com.ged.infrastructure.security.SecurityContextHelper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CreateDocumentUseCaseTest {

    @Mock
    private DocumentRepository documentRepository;

    @Mock
    private DocumentMapper mapper;

    @Mock
    private SecurityContextHelper securityContextHelper;

    @InjectMocks
    private CreateDocumentUseCase useCase;

    private User testUser;
    private CreateDocumentRequest request;

    @BeforeEach
    void setUp() {
        testUser = User.builder()
                .id(1L)
                .username("testuser")
                .role(UserRole.USER)
                .build();

        request = new CreateDocumentRequest(
                "Test Document",
                "Test Description",
                List.of("tag1", "tag2")
        );
    }

    @Test
    void execute_ValidRequest_CreatesDocument() {
        when(securityContextHelper.getCurrentUser()).thenReturn(testUser);

        Document savedDocument = Document.builder()
                .id(1L)
                .title(request.getTitle())
                .description(request.getDescription())
                .tags(request.getTags())
                .owner(testUser)
                .status(DocumentStatus.DRAFT)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        when(documentRepository.save(any(Document.class))).thenReturn(savedDocument);

        DocumentResponse expectedResponse = DocumentResponse.builder()
                .id(1L)
                .title(request.getTitle())
                .status("DRAFT")
                .build();

        when(mapper.toResponse(savedDocument)).thenReturn(expectedResponse);

        DocumentResponse response = useCase.execute(request);

        assertNotNull(response);
        assertEquals(1L, response.getId());
        assertEquals("Test Document", response.getTitle());
        assertEquals("DRAFT", response.getStatus());

        verify(securityContextHelper).getCurrentUser();
        verify(documentRepository).save(any(Document.class));
        verify(mapper).toResponse(savedDocument);
    }

    @Test
    void execute_ValidRequest_SetsCorrectOwner() {
        when(securityContextHelper.getCurrentUser()).thenReturn(testUser);

        Document savedDocument = Document.builder()
                .id(1L)
                .owner(testUser)
                .status(DocumentStatus.DRAFT)
                .build();

        when(documentRepository.save(any(Document.class))).thenReturn(savedDocument);
        when(mapper.toResponse(any())).thenReturn(new DocumentResponse());

        useCase.execute(request);

        verify(documentRepository).save(argThat(doc ->
                doc.getOwner().getId().equals(testUser.getId())
        ));
    }

    @Test
    void execute_ValidRequest_SetsStatusToDraft() {
        when(securityContextHelper.getCurrentUser()).thenReturn(testUser);

        Document savedDocument = Document.builder()
                .id(1L)
                .status(DocumentStatus.DRAFT)
                .build();

        when(documentRepository.save(any(Document.class))).thenReturn(savedDocument);
        when(mapper.toResponse(any())).thenReturn(new DocumentResponse());

        useCase.execute(request);

        verify(documentRepository).save(argThat(doc ->
                doc.getStatus() == DocumentStatus.DRAFT
        ));
    }
}
