package com.ged.infrastructure.persistence.mapper;

import com.ged.domain.model.Document;
import com.ged.infrastructure.persistence.entity.DocumentEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DocumentEntityMapper {
    private final UserEntityMapper userMapper;

    public Document toDomain(DocumentEntity entity) {
        if (entity == null) return null;
        
        return Document.builder()
                .id(entity.getId())
                .title(entity.getTitle())
                .description(entity.getDescription())
                .tags(entity.getTags())
                .owner(userMapper.toDomain(entity.getOwner()))
                .status(entity.getStatus())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .build();
    }

    public DocumentEntity toEntity(Document document) {
        if (document == null) return null;
        
        return DocumentEntity.builder()
                .id(document.getId())
                .title(document.getTitle())
                .description(document.getDescription())
                .tags(document.getTags())
                .owner(userMapper.toEntity(document.getOwner()))
                .status(document.getStatus())
                .createdAt(document.getCreatedAt())
                .updatedAt(document.getUpdatedAt())
                .build();
    }
}
