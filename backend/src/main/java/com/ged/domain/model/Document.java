package com.ged.domain.model;

import com.ged.domain.exception.InvalidDocumentException;
import com.ged.domain.model.enums.DocumentStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Document {
    private Long id;
    private String title;
    private String description;
    private List<String> tags;
    private User owner;
    private DocumentStatus status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public void validate() {
        if (title == null || title.trim().isEmpty()) {
            throw new InvalidDocumentException("Title is required");
        }
        if (status == null) {
            throw new InvalidDocumentException("Status is required");
        }
    }

    public void publish() {
        if (status == DocumentStatus.ARCHIVED) {
            throw new InvalidDocumentException("Cannot publish archived document");
        }
        this.status = DocumentStatus.PUBLISHED;
        this.updatedAt = LocalDateTime.now();
    }

    public void archive() {
        this.status = DocumentStatus.ARCHIVED;
        this.updatedAt = LocalDateTime.now();
    }

    public boolean isOwnedBy(User user) {
        return this.owner != null && this.owner.getId().equals(user.getId());
    }
}
