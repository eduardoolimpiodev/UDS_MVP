package com.ged.application.usecase.version;

import com.ged.application.dto.response.VersionResponse;
import com.ged.application.mapper.VersionMapper;
import com.ged.domain.exception.DocumentNotFoundException;
import com.ged.domain.model.Document;
import com.ged.domain.model.DocumentVersion;
import com.ged.domain.model.User;
import com.ged.domain.model.enums.DocumentStatus;
import com.ged.domain.model.enums.UserRole;
import com.ged.domain.repository.DocumentRepository;
import com.ged.domain.repository.VersionRepository;
import com.ged.domain.service.StorageService;
import com.ged.infrastructure.security.SecurityContextHelper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UploadVersionUseCaseTest {

    @Mock
    private DocumentRepository documentRepository;

    @Mock
    private VersionRepository versionRepository;

    @Mock
    private StorageService storageService;

    @Mock
    private VersionMapper mapper;

    @Mock
    private SecurityContextHelper securityContextHelper;

    @Mock
    private MultipartFile file;

    @InjectMocks
    private UploadVersionUseCase useCase;

    private User testUser;
    private Document document;

    @BeforeEach
    void setUp() {
        testUser = User.builder()
                .id(1L)
                .username("testuser")
                .role(UserRole.USER)
                .build();

        document = Document.builder()
                .id(1L)
                .title("Test Document")
                .owner(testUser)
                .status(DocumentStatus.DRAFT)
                .build();
    }

    @Test
    void execute_ValidUpload_CreatesNewVersion() {
        Long documentId = 1L;
        when(securityContextHelper.getCurrentUser()).thenReturn(testUser);
        when(documentRepository.findById(documentId)).thenReturn(Optional.of(document));
        when(versionRepository.getNextVersionNumber(documentId)).thenReturn(1);
        when(storageService.store(any(), any())).thenReturn("1/1/file.pdf");
        when(file.getOriginalFilename()).thenReturn("test.pdf");
        when(file.getSize()).thenReturn(1024L);
        when(file.getContentType()).thenReturn("application/pdf");

        DocumentVersion savedVersion = DocumentVersion.builder()
                .id(1L)
                .documentId(documentId)
                .versionNumber(1)
                .isCurrent(true)
                .build();

        when(versionRepository.save(any(DocumentVersion.class))).thenReturn(savedVersion);

        VersionResponse expectedResponse = VersionResponse.builder()
                .id(1L)
                .versionNumber(1)
                .isCurrent(true)
                .build();

        when(mapper.toResponse(savedVersion)).thenReturn(expectedResponse);

        VersionResponse response = useCase.execute(documentId, file);

        assertNotNull(response);
        assertEquals(1, response.getVersionNumber());
        assertTrue(response.getIsCurrent());

        verify(versionRepository).markAllAsNotCurrent(documentId);
        verify(storageService).store(file, "1/1");
        verify(versionRepository).save(any(DocumentVersion.class));
    }

    @Test
    void execute_DocumentNotFound_ThrowsException() {
        Long documentId = 999L;
        when(securityContextHelper.getCurrentUser()).thenReturn(testUser);
        when(documentRepository.findById(documentId)).thenReturn(Optional.empty());

        assertThrows(DocumentNotFoundException.class, () -> {
            useCase.execute(documentId, file);
        });

        verify(versionRepository, never()).save(any());
        verify(storageService, never()).store(any(), any());
    }

    @Test
    void execute_ValidUpload_MarksOldVersionsAsNotCurrent() {
        Long documentId = 1L;
        when(securityContextHelper.getCurrentUser()).thenReturn(testUser);
        when(documentRepository.findById(documentId)).thenReturn(Optional.of(document));
        when(versionRepository.getNextVersionNumber(documentId)).thenReturn(2);
        when(storageService.store(any(), any())).thenReturn("1/2/file.pdf");
        when(file.getOriginalFilename()).thenReturn("test.pdf");
        when(versionRepository.save(any())).thenReturn(new DocumentVersion());
        when(mapper.toResponse(any())).thenReturn(new VersionResponse());

        useCase.execute(documentId, file);

        verify(versionRepository).markAllAsNotCurrent(documentId);
    }
}
