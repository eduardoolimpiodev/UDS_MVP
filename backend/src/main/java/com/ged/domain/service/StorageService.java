package com.ged.domain.service;

import org.springframework.web.multipart.MultipartFile;

public interface StorageService {
    String store(MultipartFile file, String path);
    byte[] retrieve(String fileKey);
    void delete(String fileKey);
}
