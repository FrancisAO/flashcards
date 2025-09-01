package com.fao.flashcards.api.dto;

import com.fao.flashcards.domain.model.OCRStatus;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Response DTO für OCR-Verarbeitungsoperationen.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class OCRProcessResponse {
    
    private boolean success;
    private String message;
    private OCRStatus overallStatus;
    
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime processedAt;
    
    private List<OCRFileResult> results;
    private OCRProcessingSummary summary;
    
    /**
     * Einzelnes OCR-Datei-Ergebnis.
     */
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class OCRFileResult {
        private String fileId;
        private String originalFilename;
        private String extractedTextId;
        private OCRStatus status;
        private String errorMessage;
        private Integer pagesProcessed;
        private Long processingTimeMs;
        private BigDecimal confidenceScore;
        
        /**
         * Erstellt ein erfolgreiches Ergebnis.
         */
        public static OCRFileResult success(String fileId, String originalFilename,
                                          String extractedTextId, Integer pagesProcessed,
                                          Long processingTimeMs, BigDecimal confidenceScore) {
            return new OCRFileResult(fileId, originalFilename, extractedTextId,
                                   OCRStatus.SUCCESS, null, pagesProcessed,
                                   processingTimeMs, confidenceScore);
        }
        
        /**
         * Erstellt ein fehlerhaftes Ergebnis.
         */
        public static OCRFileResult error(String fileId, String originalFilename, String errorMessage) {
            return new OCRFileResult(fileId, originalFilename, null, 
                                   OCRStatus.FAILED, errorMessage, null, null, null);
        }
        
        /**
         * Prüft ob die Verarbeitung erfolgreich war.
         */
        public boolean isSuccessful() {
            return status == OCRStatus.SUCCESS;
        }
    }
    
    /**
     * Zusammenfassung der OCR-Verarbeitung.
     */
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class OCRProcessingSummary {
        private int totalFiles;
        private int successfulFiles;
        private int failedFiles;
        private int totalPagesProcessed;
        private Long totalProcessingTimeMs;
        private BigDecimal averageConfidenceScore;
        
        /**
         * Berechnet die Erfolgsrate in Prozent.
         */
        public double getSuccessRate() {
            if (totalFiles == 0) return 0.0;
            return (successfulFiles * 100.0) / totalFiles;
        }
        
        /**
         * Gibt die gesamte Verarbeitungszeit in Sekunden zurück.
         */
        public Double getTotalProcessingTimeSeconds() {
            if (totalProcessingTimeMs == null) return null;
            return totalProcessingTimeMs / 1000.0;
        }
        
        /**
         * Gibt eine menschenlesbare Zusammenfassung zurück.
         */
        public String getFormattedSummary() {
            return String.format("%d von %d Dateien erfolgreich verarbeitet (%.1f%%), " +
                               "%d Seiten insgesamt", 
                               successfulFiles, totalFiles, getSuccessRate(), totalPagesProcessed);
        }
    }
    
    /**
     * Erstellt eine erfolgreiche Response für Einzeldatei-Verarbeitung.
     */
    public static OCRProcessResponse singleFileSuccess(String fileId, String originalFilename,
                                                     String extractedTextId, Integer pagesProcessed,
                                                     Long processingTimeMs, BigDecimal confidenceScore) {
        OCRFileResult result = OCRFileResult.success(fileId, originalFilename, extractedTextId,
                                                   pagesProcessed, processingTimeMs, confidenceScore);
        
        OCRProcessingSummary summary = new OCRProcessingSummary(1, 1, 0,
                                                              pagesProcessed != null ? pagesProcessed : 0,
                                                              processingTimeMs, confidenceScore);
        
        return new OCRProcessResponse(true, "OCR-Verarbeitung erfolgreich abgeschlossen",
                                    OCRStatus.SUCCESS, LocalDateTime.now(), List.of(result), summary);
    }
    
    /**
     * Erstellt eine Fehler-Response für Einzeldatei-Verarbeitung.
     */
    public static OCRProcessResponse singleFileError(String fileId, String originalFilename, String errorMessage) {
        OCRFileResult result = OCRFileResult.error(fileId, originalFilename, errorMessage);
        
        OCRProcessingSummary summary = new OCRProcessingSummary(1, 0, 1, 0, null, null);
        
        return new OCRProcessResponse(false, "OCR-Verarbeitung fehlgeschlagen", 
                                    OCRStatus.FAILED, LocalDateTime.now(), List.of(result), summary);
    }
    
    /**
     * Erstellt eine Response für Batch-Verarbeitung.
     */
    public static OCRProcessResponse batchResult(List<OCRFileResult> results) {
        int totalFiles = results.size();
        int successfulFiles = (int) results.stream().filter(OCRFileResult::isSuccessful).count();
        int failedFiles = totalFiles - successfulFiles;
        
        int totalPages = results.stream()
                .filter(r -> r.getPagesProcessed() != null)
                .mapToInt(OCRFileResult::getPagesProcessed)
                .sum();
        
        Long totalTime = results.stream()
                .filter(r -> r.getProcessingTimeMs() != null)
                .mapToLong(OCRFileResult::getProcessingTimeMs)
                .sum();
        
        BigDecimal avgConfidence = null;
        long confidenceCount = results.stream()
            .filter(r -> r.getConfidenceScore() != null)
            .count();
        
        if (confidenceCount > 0) {
            BigDecimal sum = results.stream()
                .filter(r -> r.getConfidenceScore() != null)
                .map(OCRFileResult::getConfidenceScore)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
            avgConfidence = sum.divide(BigDecimal.valueOf(confidenceCount), 4, BigDecimal.ROUND_HALF_UP);
        }
        
        OCRProcessingSummary summary = new OCRProcessingSummary(totalFiles, successfulFiles, failedFiles,
                                                              totalPages, totalTime > 0 ? totalTime : null,
                                                              avgConfidence);
        
        boolean allSuccessful = failedFiles == 0;
        OCRStatus overallStatus = allSuccessful ? OCRStatus.SUCCESS : 
                                (successfulFiles > 0 ? OCRStatus.SUCCESS : OCRStatus.FAILED);
        
        String message = allSuccessful ? "Alle Dateien erfolgreich verarbeitet" : 
                        summary.getFormattedSummary();
        
        return new OCRProcessResponse(allSuccessful, message, overallStatus, 
                                    LocalDateTime.now(), results, summary);
    }
    
    /**
     * Prüft ob alle Verarbeitungen erfolgreich waren.
     */
    public boolean isAllSuccessful() {
        return success && (summary == null || summary.getFailedFiles() == 0);
    }
    
    /**
     * Prüft ob mindestens eine Verarbeitung erfolgreich war.
     */
    public boolean hasAnySuccess() {
        return summary != null && summary.getSuccessfulFiles() > 0;
    }
}