package com.ged.application.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateDocumentRequest {
    private String title;
    private String description;
    private List<String> tags;
}
