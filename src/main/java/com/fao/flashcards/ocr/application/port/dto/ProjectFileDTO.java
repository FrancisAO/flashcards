package com.fao.flashcards.ocr.application.port.dto;

import com.fao.flashcards.cards.model.FileType;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * DTO für ProjectFile Entity.
 * Enthält alle relevanten Datei-Informationen für die API-Schicht.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProjectFileDTO {
    
    private String id;
    
    @NotBlank(message = "Projekt-ID ist erforderlich")
    private String projectId;
    
    @NotBlank(message = "Original-Dateiname ist erforderlich")
    @Size(max = 500, message = "Dateiname darf maximal 500 Zeichen lang sein")
    private String originalFilename;
    
    @NotBlank(message = "Content-Type ist erforderlich")
    @Size(max = 100, message = "Content-Type darf maximal 100 Zeichen lang sein")
    private String contentType;
    
    @NotNull(message = "Dateigröße ist erforderlich")
    @Positive(message = "Dateigröße muss positiv sein")
    private Long fileSize;
    
    @NotNull(message = "Dateityp ist erforderlich")
    private FileType fileType;
    
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime uploadedAt;
    
    private Boolean hasExtractedText;
    
    /**
     * Konstruktor für die Erstellung ohne ID und Timestamps.
     */
    public ProjectFileDTO(String projectId, String originalFilename, String contentType, 
                         Long fileSize, FileType fileType) {
        this.projectId = projectId;
        this.originalFilename = originalFilename;
        this.contentType = contentType;
        this.fileSize = fileSize;
        this.fileType = fileType;
        this.hasExtractedText = false;
    }
    
    /**
     * Gibt die Dateigröße in menschenlesbarem Format zurück.
     */
    public String getFormattedFileSize() {
        if (fileSize == null) {
            return "0 B";
        }
        
        long bytes = fileSize;
        if (bytes < 1024) {
            return bytes + " B";
        }
        
        int exp = (int) (Math.log(bytes) / Math.log(1024));
        String pre = "KMGTPE".charAt(exp - 1) + "";
        
        return String.format("%.1f %sB", bytes / Math.pow(1024, exp), pre);
    }
    
    /**
     * Gibt die Dateiendung zurück.
     */
    public String getFileExtension() {
        if (originalFilename == null || originalFilename.isEmpty()) {
            return "";
        }
        
        int lastDotIndex = originalFilename.lastIndexOf('.');
        if (lastDotIndex == -1 || lastDotIndex == originalFilename.length() - 1) {
            return "";
        }
        
        return originalFilename.substring(lastDotIndex + 1).toLowerCase();
    }
    
    /**
     * Prüft ob es sich um eine Bilddatei handelt.
     */
    public boolean isImage() {
        return fileType == FileType.IMAGE;
    }
    
    /**
     * Prüft ob es sich um ein Dokument handelt.
     */
    public boolean isDocument() {
        return fileType == FileType.DOCUMENT;
    }
    
    /**
     * Prüft ob die Datei bereits verarbeitet wurde.
     */
    public boolean isProcessed() {
        return hasExtractedText != null && hasExtractedText;
    }
}