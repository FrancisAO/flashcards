package com.fao.flashcards.cards.adapter.bootstrap;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import com.fao.flashcards.cards.application.port.in.FileUploadConfig;

@Configuration
public class FileUploadConfigImpl implements FileUploadConfig {

    // Konfigurierbare Werte
    @Value("${app.upload.base-path:uploads}")
    private String uploadBasePath;

    @Value("${app.upload.max-file-size:52428800}") // 50MB default
    private long maxFileSizeBytes;

    @Value("${app.upload.allowed-extensions:pdf,png,jpg,jpeg,gif,bmp,tiff,txt,docx,doc}")
    private String allowedExtensions;

    @Override
    public String getUploadBasePath() {
        return uploadBasePath;
    }

    @Override
    public long getMaxFileSizeBytes() {
        return maxFileSizeBytes;
    }

    @Override
    public String getAllowedExtensions() {
        return allowedExtensions;
    }
    
}
