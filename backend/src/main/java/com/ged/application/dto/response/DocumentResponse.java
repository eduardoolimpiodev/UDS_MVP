package com.ged.application.dto.response;

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
public class DocumentResponse {
    private Long id;
    private String title;
    private String description;
    private List<String> tags;
    private String ownerUsername;
    private String status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
