// src/main/java/com/beatlayer/api/AudioStorageService.java
package com.beatlayer.api;

import org.springframework.web.multipart.MultipartFile;

public interface AudioStorageService {
    /**
     * Save the audio file and return a URL that the frontend can use to access it.
     */
    String storeBaseLayerAudio(MultipartFile file, String suggestedName) throws Exception;
}
