package com.fao.flashcards.api.dto;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * DTO für ein hochgeladenes Dokument.
 */
@Data
public class DocumentUploadDTO {
    
    /**
     * Die ID des hochgeladenen Dokuments.
     */
    private String id;
    
    /**
     * Der ursprüngliche Dateiname des Dokuments.
     */
    private String originalFilename;
    
    /**
     * Der MIME-Typ des Dokuments.
     */
    private String contentType;
    
    /**
     * Die Größe des Dokuments in Bytes.
     */
    private Long fileSize;
    
    /**
     * Der Zeitpunkt des Hochladens.
     */
    private LocalDateTime uploadedAt;
}