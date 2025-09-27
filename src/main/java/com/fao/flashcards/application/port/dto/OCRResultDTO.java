package com.fao.flashcards.application.port.dto;

import com.fao.flashcards.application.model.OCRStatus;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * DTO für OCRResult Entity.
 * Enthält alle relevanten Informationen über OCR-Verarbeitungsergebnisse für die API-Schicht.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class OCRResultDTO {
    
    private String id;
    
    @NotNull(message = "OCR-Status ist erforderlich")
    private OCRStatus status;
    
    private Integer pagesProcessed;
    
    private Long processingTimeMs;
    
    private String modelUsed;
    
    private BigDecimal confidenceScore;
    
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime processedAt;
    
    private String errorDetails;
    
    /**
     * Konstruktor für die Erstellung mit minimalem Status.
     */
    public OCRResultDTO(OCRStatus status) {
        this.status = status;
    }
    
    /**
     * Konstruktor für erfolgreiche OCR-Ergebnisse.
     */
    public OCRResultDTO(OCRStatus status, Integer pagesProcessed, Long processingTimeMs,
                       String modelUsed, BigDecimal confidenceScore) {
        this.status = status;
        this.pagesProcessed = pagesProcessed;
        this.processingTimeMs = processingTimeMs;
        this.modelUsed = modelUsed;
        this.confidenceScore = confidenceScore;
    }
    
    /**
     * Prüft ob die OCR-Verarbeitung erfolgreich war.
     */
    public boolean isSuccessful() {
        return status == OCRStatus.SUCCESS;
    }
    
    /**
     * Prüft ob die OCR-Verarbeitung fehlgeschlagen ist.
     */
    public boolean isFailed() {
        return status == OCRStatus.FAILED;
    }
    
    /**
     * Prüft ob die OCR-Verarbeitung noch läuft.
     */
    public boolean isProcessing() {
        return status == OCRStatus.PROCESSING;
    }
    
    /**
     * Prüft ob die OCR-Verarbeitung noch nicht gestartet wurde.
     */
    public boolean isPending() {
        return status == OCRStatus.PENDING;
    }
    
    /**
     * Gibt die Verarbeitungszeit in Sekunden zurück.
     */
    public Double getProcessingTimeSeconds() {
        if (processingTimeMs == null) {
            return null;
        }
        return processingTimeMs / 1000.0;
    }
    
    /**
     * Gibt eine menschenlesbare Beschreibung der Verarbeitungszeit zurück.
     */
    public String getFormattedProcessingTime() {
        if (processingTimeMs == null) {
            return "N/A";
        }
        
        if (processingTimeMs < 1000) {
            return processingTimeMs + " ms";
        } else if (processingTimeMs < 60000) {
            return String.format("%.1f s", processingTimeMs / 1000.0);
        } else {
            long minutes = processingTimeMs / 60000;
            long seconds = (processingTimeMs % 60000) / 1000;
            return String.format("%d min %d s", minutes, seconds);
        }
    }
    
    /**
     * Gibt den Confidence Score als Prozentangabe zurück.
     */
    public String getFormattedConfidenceScore() {
        if (confidenceScore == null) {
            return "N/A";
        }
        return String.format("%.1f%%", confidenceScore.multiply(new BigDecimal("100")).doubleValue());
    }
    
    /**
     * Gibt eine Zusammenfassung des OCR-Ergebnisses zurück.
     */
    public String getSummary() {
        if (status == OCRStatus.SUCCESS) {
            StringBuilder summary = new StringBuilder();
            summary.append("Erfolgreich verarbeitet");
            
            if (pagesProcessed != null && pagesProcessed > 0) {
                summary.append(" (").append(pagesProcessed).append(" Seite");
                if (pagesProcessed > 1) {
                    summary.append("n");
                }
                summary.append(")");
            }
            
            if (processingTimeMs != null) {
                summary.append(" in ").append(getFormattedProcessingTime());
            }
            
            if (confidenceScore != null) {
                summary.append(", Konfidenz: ").append(getFormattedConfidenceScore());
            }
            
            return summary.toString();
        } else if (status == OCRStatus.FAILED) {
            return "Verarbeitung fehlgeschlagen" + 
                   (errorDetails != null ? ": " + errorDetails : "");
        } else if (status == OCRStatus.PROCESSING) {
            return "Verarbeitung läuft...";
        } else {
            return "Warten auf Verarbeitung";
        }
    }
    
    /**
     * Prüft ob Fehlerdetails vorhanden sind.
     */
    public boolean hasErrorDetails() {
        return errorDetails != null && !errorDetails.trim().isEmpty();
    }
}