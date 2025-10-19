package com.fao.flashcards.ocr.application.port.in;

public interface FileUploadConfig {
    
    String getUploadBasePath();

    long getMaxFileSizeBytes();

    String getAllowedExtensions();
}
