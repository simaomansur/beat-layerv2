// src/main/java/com/beatlayer/api/LocalAudioStorageService.java
package com.beatlayer.api;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.*;
import java.time.Instant;

@Service
public class LocalAudioStorageService implements AudioStorageService {

    private final Path rootDir = Paths.get("uploads/audio");

    public LocalAudioStorageService() throws Exception {
        Files.createDirectories(rootDir);
    }

    @Override
    public String storeBaseLayerAudio(MultipartFile file, String suggestedName) throws Exception {
        String originalName = file.getOriginalFilename();
        String ext = ".webm";
        if (originalName != null && originalName.contains(".")) {
            ext = originalName.substring(originalName.lastIndexOf("."));
        }

        String fileName = suggestedName + "_" + Instant.now().toEpochMilli() + ext;
        Path target = rootDir.resolve(fileName);

        Files.copy(file.getInputStream(), target, StandardCopyOption.REPLACE_EXISTING);

        // This assumes you're serving "uploads" as static files from the backend.
        return "/audio/" + fileName;
    }
}
