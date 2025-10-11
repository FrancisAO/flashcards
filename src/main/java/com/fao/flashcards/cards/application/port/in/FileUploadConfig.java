package com.fao.flashcards.cards.application.port.in;

public interface FileUploadConfig {
    
    String getUploadBasePath();

    long getMaxFileSizeBytes();

    String getAllowedExtensions();
}
