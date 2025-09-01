package com.fao.flashcards.api.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Set;

/**
 * DTO für Project Entity.
 * Enthält alle relevanten Projekt-Informationen für die API-Schicht.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProjectDTO {
    
    private String id;
    
    @NotBlank(message = "Projektname ist erforderlich")
    @Size(max = 255, message = "Projektname darf maximal 255 Zeichen lang sein")
    private String name;
    
    @Size(max = 1000, message = "Beschreibung darf maximal 1000 Zeichen lang sein")
    private String description;
    
    private Set<String> tags;
    
    private Integer fileCount;
    
    private Integer extractedTextCount;
    
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime createdAt;
    
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime updatedAt;
    
    /**
     * Konstruktor für die Erstellung neuer Projekte ohne ID und Timestamps.
     */
    public ProjectDTO(String name, String description, Set<String> tags) {
        this.name = name;
        this.description = description;
        this.tags = tags;
        this.fileCount = 0;
        this.extractedTextCount = 0;
    }
    
    /**
     * Prüft ob das Projekt Dateien enthält.
     */
    public boolean hasFiles() {
        return fileCount != null && fileCount > 0;
    }
    
    /**
     * Prüft ob das Projekt extrahierte Texte enthält.
     */
    public boolean hasExtractedTexts() {
        return extractedTextCount != null && extractedTextCount > 0;
    }
    
    /**
     * Berechnet den Verarbeitungsfortschritt in Prozent.
     */
    public double getProcessingProgress() {
        if (fileCount == null || fileCount == 0) {
            return 0.0;
        }
        if (extractedTextCount == null) {
            return 0.0;
        }
        return Math.min(100.0, (extractedTextCount * 100.0) / fileCount);
    }
}