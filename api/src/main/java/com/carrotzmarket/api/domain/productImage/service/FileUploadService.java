package com.carrotzmarket.api.domain.productImage.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
public class FileUploadService {

    @Value("${file.dir}")
    private String uploadDirectory;

    public String uploadFile(MultipartFile file) throws IOException {

        String safeFileName = file.getOriginalFilename().replaceAll("[^a-zA-Z0-9\\.\\-]", "_");

        Path uploadPath = Paths.get(uploadDirectory);
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }

        Path filePath = uploadPath.resolve(safeFileName);
        Files.write(filePath, file.getBytes());

        return filePath.toString();
    }
}

