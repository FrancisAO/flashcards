package com.fao.flashcards.ocr.application.port.dto;

import com.fao.flashcards.ocr.model.ExtractionSource;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * DTO für ExtractedText Entity.
 * Enthält alle relevanten Informationen über extrahierte Texte für die API-Schicht.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ExtractedTextDTO {
    
    private String id;
    
    @NotBlank(message = "Projektdatei-ID ist erforderlich")
    private String sourceFileId;
    
    private String extractedContent;
    
    private String editedContent;
    
    @NotNull(message = "Extraktionsquelle ist erforderlich")
    private ExtractionSource extractionSource;
    
    private Boolean isEdited;
    
    private String metadata;
    
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime extractedAt;
    
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime lastEditedAt;
    
    /**
     * Konstruktor für die Erstellung ohne ID und Timestamps.
     */
    public ExtractedTextDTO(String sourceFileId, String extractedContent, 
                           ExtractionSource extractionSource) {
        this.sourceFileId = sourceFileId;
        this.extractedContent = extractedContent;
        this.extractionSource = extractionSource;
        this.isEdited = false;
    }
    
    /**
     * Gibt den aktuell relevanten Text zurück (bearbeitet oder extrahiert).
     * Entspricht der getCurrentContent() Methode aus der Entity.
     */
    public String getCurrentContent() {
        return isEdited != null && isEdited && editedContent != null ? 
               editedContent : extractedContent;
    }
    
    /**
     * Prüft ob Text vorhanden ist.
     */
    public boolean hasContent() {
        String content = getCurrentContent();
        return content != null && !content.trim().isEmpty();
    }
    
    /**
     * Gibt die Anzahl der Zeichen im aktuellen Text zurück.
     */
    public int getContentLength() {
        String content = getCurrentContent();
        return content != null ? content.length() : 0;
    }
    
    /**
     * Gibt die Anzahl der Wörter im aktuellen Text zurück (grobe Schätzung).
     */
    public int getWordCount() {
        String content = getCurrentContent();
        if (content == null || content.trim().isEmpty()) {
            return 0;
        }
        return content.trim().split("\\s+").length;
    }
    
    /**
     * Prüft ob der Text bearbeitet wurde.
     */
    public boolean wasEdited() {
        return isEdited != null && isEdited;
    }
    
    /**
     * Prüft ob es sich um einen aus einem Bild extrahierten Text handelt.
     */
    public boolean isFromImage() {
        return extractionSource == ExtractionSource.IMAGE;
    }
    
    /**
     * Prüft ob es sich um einen aus einem Dokument extrahierten Text handelt.
     */
    public boolean isFromDocument() {
        return extractionSource == ExtractionSource.DOCUMENT;
    }
    
    /**
     * Gibt eine Vorschau des Textes zurück (erste 100 Zeichen).
     */
    public String getPreview() {
        String content = getCurrentContent();
        if (content == null || content.isEmpty()) {
            return "";
        }
        
        String preview = content.trim();
        if (preview.length() <= 100) {
            return preview;
        }
        
        // An Wortgrenze abschneiden wenn möglich
        String truncated = preview.substring(0, 100);
        int lastSpaceIndex = truncated.lastIndexOf(' ');
        
        if (lastSpaceIndex > 50) { // Nur wenn genug Text vorhanden
            return truncated.substring(0, lastSpaceIndex) + "...";
        }
        
        return truncated + "...";
    }
}