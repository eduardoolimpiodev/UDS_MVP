package com.ged.application.mapper;

import com.ged.application.dto.response.DocumentResponse;
import com.ged.domain.model.Document;
import org.springframework.stereotype.Component;

@Component
public class DocumentMapper {
    public DocumentResponse toResponse(Document document) {
        return DocumentResponse.builder()
                .id(document.getId())
                .title(document.getTitle())
                .description(document.getDescription())
                .tags(document.getTags())
                .ownerUsername(document.getOwner() != null ? document.getOwner().getUsername() : null)
                .status(document.getStatus().name())
                .createdAt(document.getCreatedAt())
                .updatedAt(document.getUpdatedAt())
                .build();
    }
}
