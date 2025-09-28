package com.fao.flashcards.ocr.application.port.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Request DTO für OCR-Verarbeitungsoperationen.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class OCRProcessRequest {
    
    @NotEmpty(message = "Mindestens eine Datei-ID ist erforderlich")
    private List<String> fileIds;
    
    private String modelPreference;
    private Integer maxPages;
    private Double confidenceThreshold;
    private String language;
    private Boolean extractTables;
    private Boolean extractImages;
    
    /**
     * Konstruktor für einzelne Datei-Verarbeitung.
     */
    public OCRProcessRequest(String fileId) {
        this.fileIds = List.of(fileId);
    }
    
    /**
     * Konstruktor für Batch-Verarbeitung mit Standard-Optionen.
     */
    public OCRProcessRequest(List<String> fileIds) {
        this.fileIds = fileIds;
    }
    
    /**
     * Prüft ob es sich um eine Einzeldatei-Verarbeitung handelt.
     */
    public boolean isSingleFile() {
        return fileIds != null && fileIds.size() == 1;
    }
    
    /**
     * Prüft ob es sich um eine Batch-Verarbeitung handelt.
     */
    public boolean isBatch() {
        return fileIds != null && fileIds.size() > 1;
    }
    
    /**
     * Gibt die erste Datei-ID zurück (für Einzeldatei-Verarbeitung).
     */
    public String getFirstFileId() {
        return fileIds != null && !fileIds.isEmpty() ? fileIds.get(0) : null;
    }
    
    /**
     * Anzahl der zu verarbeitenden Dateien.
     */
    public int getFileCount() {
        return fileIds != null ? fileIds.size() : 0;
    }
    
    /**
     * Validiert die Request-Daten.
     */
    public boolean isValid() {
        return fileIds != null && !fileIds.isEmpty() && 
               fileIds.stream().allMatch(id -> id != null && !id.trim().isEmpty());
    }
    
    /**
     * Erstellt Standard-OCR-Optionen basierend auf den Request-Parametern.
     */
    public OCROptionsDTO toOCROptions() {
        OCROptionsDTO options = new OCROptionsDTO();
        options.setModelPreference(modelPreference);
        options.setMaxPages(maxPages);
        options.setConfidenceThreshold(confidenceThreshold);
        options.setLanguage(language);
        options.setExtractTables(extractTables);
        options.setExtractImages(extractImages);
        return options;
    }
    
    /**
     * Hilfsmethode: OCR-Optionen DTO.
     */
    @Data
    public static class OCROptionsDTO {
        private String modelPreference;
        private Integer maxPages;
        private Double confidenceThreshold;
        private String language;
        private Boolean extractTables;
        private Boolean extractImages;
    }
}