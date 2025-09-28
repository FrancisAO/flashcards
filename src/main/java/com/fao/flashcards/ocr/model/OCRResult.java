package com.fao.flashcards.ocr.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Domain Model für OCR-Verarbeitungsergebnisse.
 * Speichert Details über den OCR-Prozess und dessen Ergebnisse.
 */
@Entity
@Table(name = "ocr_results")
@Data
@NoArgsConstructor
public class OCRResult {
    
    @Id
    private String id;
    
    @NotNull(message = "ExtractedText ist erforderlich")
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "extracted_text_id", nullable = false)
    private ExtractedText extractedText;
    
    @NotNull(message = "OCR-Status ist erforderlich")
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private OCRStatus status;
    
    @Column(name = "pages_processed")
    private Integer pagesProcessed;
    
    @Column(name = "processing_time_ms")
    private Long processingTimeMs;
    
    @Column(name = "api_response", columnDefinition = "TEXT")
    private String apiResponse;
    
    @Column(name = "error_details", columnDefinition = "TEXT")
    private String errorDetails;
    
    @Size(max = 100, message = "Model-Information darf maximal 100 Zeichen lang sein")
    @Column(name = "model_used")
    private String modelUsed;
    
    @Column(name = "confidence_score", precision = 5, scale = 4)
    private BigDecimal confidenceScore;
    
    @CreationTimestamp
    @Column(name = "created_at")
    private LocalDateTime createdAt;
    
    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
    
    @PrePersist
    protected void onCreate() {
        if (id == null) {
            id = UUID.randomUUID().toString();
        }
    }
    
    public OCRResult(ExtractedText extractedText, OCRStatus status) {
        this.extractedText = extractedText;
        this.status = status;
    }
    
    /**
     * Markiert die OCR-Verarbeitung als erfolgreich abgeschlossen.
     */
    public void markAsSuccess(Integer pagesProcessed, Long processingTimeMs, 
                             String apiResponse, String modelUsed) {
        this.status = OCRStatus.SUCCESS;
        this.pagesProcessed = pagesProcessed;
        this.processingTimeMs = processingTimeMs;
        this.apiResponse = apiResponse;
        this.modelUsed = modelUsed;
        this.errorDetails = null;
    }
    
    /**
     * Markiert die OCR-Verarbeitung als fehlgeschlagen.
     */
    public void markAsFailed(String errorDetails) {
        this.status = OCRStatus.FAILED;
        this.errorDetails = errorDetails;
    }
    
    /**
     * Markiert die OCR-Verarbeitung als in Bearbeitung.
     */
    public void markAsProcessing() {
        this.status = OCRStatus.PROCESSING;
        this.errorDetails = null;
    }
}