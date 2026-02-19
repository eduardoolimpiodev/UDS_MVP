package com.ged.application.usecase.document;

import com.ged.application.dto.response.DocumentResponse;
import com.ged.application.mapper.DocumentMapper;
import com.ged.domain.model.Document;
import com.ged.domain.model.enums.DocumentStatus;
import com.ged.domain.repository.DocumentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SearchDocumentsUseCaseTest {

    @Mock
    private DocumentRepository documentRepository;

    @Mock
    private DocumentMapper mapper;

    @InjectMocks
    private SearchDocumentsUseCase useCase;

    private Pageable pageable;
    private Document document;
    private DocumentResponse documentResponse;

    @BeforeEach
    void setUp() {
        pageable = PageRequest.of(0, 10);
        
        document = Document.builder()
                .id(1L)
                .title("Test Document")
                .status(DocumentStatus.PUBLISHED)
                .build();

        documentResponse = DocumentResponse.builder()
                .id(1L)
                .title("Test Document")
                .status("PUBLISHED")
                .build();
    }

    @Test
    void execute_WithStatusFilter_ReturnsFilteredDocuments() {
        Page<Document> documentPage = new PageImpl<>(List.of(document));
        when(documentRepository.findByStatus(eq(DocumentStatus.PUBLISHED), any(Pageable.class)))
                .thenReturn(documentPage);
        when(mapper.toResponse(document)).thenReturn(documentResponse);

        Page<DocumentResponse> result = useCase.execute(DocumentStatus.PUBLISHED, null, pageable);

        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        assertEquals("PUBLISHED", result.getContent().get(0).getStatus());

        verify(documentRepository).findByStatus(DocumentStatus.PUBLISHED, pageable);
        verify(documentRepository, never()).findAll(any(Pageable.class));
    }

    @Test
    void execute_WithTitleFilter_ReturnsFilteredDocuments() {
        Page<Document> documentPage = new PageImpl<>(List.of(document));
        when(documentRepository.findByTitleContaining(eq("Test"), any(Pageable.class)))
                .thenReturn(documentPage);
        when(mapper.toResponse(document)).thenReturn(documentResponse);

        Page<DocumentResponse> result = useCase.execute(null, "Test", pageable);

        assertNotNull(result);
        assertEquals(1, result.getTotalElements());

        verify(documentRepository).findByTitleContaining("Test", pageable);
    }

    @Test
    void execute_WithStatusAndTitleFilter_ReturnsFilteredDocuments() {
        Page<Document> documentPage = new PageImpl<>(List.of(document));
        when(documentRepository.findByStatusAndTitleContaining(
                eq(DocumentStatus.PUBLISHED), eq("Test"), any(Pageable.class)))
                .thenReturn(documentPage);
        when(mapper.toResponse(document)).thenReturn(documentResponse);

        Page<DocumentResponse> result = useCase.execute(DocumentStatus.PUBLISHED, "Test", pageable);

        assertNotNull(result);
        assertEquals(1, result.getTotalElements());

        verify(documentRepository).findByStatusAndTitleContaining(DocumentStatus.PUBLISHED, "Test", pageable);
    }

    @Test
    void execute_WithoutFilters_ReturnsAllDocuments() {
        Page<Document> documentPage = new PageImpl<>(List.of(document));
        when(documentRepository.findAll(any(Pageable.class))).thenReturn(documentPage);
        when(mapper.toResponse(document)).thenReturn(documentResponse);

        Page<DocumentResponse> result = useCase.execute(null, null, pageable);

        assertNotNull(result);
        assertEquals(1, result.getTotalElements());

        verify(documentRepository).findAll(pageable);
    }
}
