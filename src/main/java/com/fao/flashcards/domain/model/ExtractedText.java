package com.fao.flashcards.domain.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Domain Model für extrahierte Texte aus Dateien.
 * Speichert sowohl den ursprünglich extrahierten als auch den bearbeiteten Text.
 */
@Entity
@Table(name = "extracted_texts")
@Data
@NoArgsConstructor
public class ExtractedText {
    
    @Id
    private String id;
    
    @NotNull(message = "Projektdatei ist erforderlich")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_file_id", nullable = false)
    private ProjectFile projectFile;
    
    @Column(name = "extracted_content", columnDefinition = "TEXT")
    private String extractedContent;
    
    @Column(name = "edited_content", columnDefinition = "TEXT")
    private String editedContent;
    
    @NotNull(message = "Extraktionsquelle ist erforderlich")
    @Enumerated(EnumType.STRING)
    @Column(name = "extraction_source", nullable = false)
    private ExtractionSource extractionSource;
    
    @Column(name = "is_edited", nullable = false)
    private Boolean isEdited = false;
    
    @Column(name = "metadata", columnDefinition = "JSON")
    private String metadata;
    
    @CreationTimestamp
    @Column(name = "extracted_at")
    private LocalDateTime extractedAt;
    
    @UpdateTimestamp
    @Column(name = "last_edited_at")
    private LocalDateTime lastEditedAt;
    
    @PrePersist
    protected void onCreate() {
        if (id == null) {
            id = UUID.randomUUID().toString();
        }
    }
    
    public ExtractedText(ProjectFile projectFile, String extractedContent, 
                        ExtractionSource extractionSource) {
        this.projectFile = projectFile;
        this.extractedContent = extractedContent;
        this.extractionSource = extractionSource;
    }
    
    /**
     * Aktualisiert den bearbeiteten Inhalt und markiert den Text als bearbeitet.
     */
    public void updateEditedContent(String editedContent) {
        this.editedContent = editedContent;
        this.isEdited = true;
    }
    
    /**
     * Gibt den aktuell relevanten Text zurück (bearbeitet oder extrahiert).
     */
    public String getCurrentContent() {
        return isEdited && editedContent != null ? editedContent : extractedContent;
    }
}