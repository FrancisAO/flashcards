package com.fao.flashcards.domain.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Speichert Informationen über hochgeladene Dokumente.
 * Enthält Metadaten zum Dokument und den extrahierten Text.
 */
@Entity
@Table(name = "document_uploads")
@Data
@NoArgsConstructor
public class DocumentUpload {
    
    @Id
    private String id;
    
    @ManyToOne
    @JoinColumn(name = "generation_request_id", nullable = false)
    private AIGenerationRequest generationRequest;
    
    @Column(name = "original_filename", nullable = false)
    private String originalFilename;
    
    @Column(name = "content_type", nullable = false)
    private String contentType;
    
    @Column(name = "file_size")
    private Long fileSize;
    
    @Column(name = "storage_path")
    private String storagePath;
    
    @Column(name = "extracted_text", columnDefinition = "TEXT")
    private String extractedText;
    
    @CreationTimestamp
    @Column(name = "uploaded_at")
    private LocalDateTime uploadedAt;
    
    @PrePersist
    protected void onCreate() {
        if (id == null) {
            id = UUID.randomUUID().toString();
        }
    }
    
    /**
     * Erstellt eine neue DocumentUpload-Instanz mit den angegebenen Werten.
     * 
     * @param originalFilename Der ursprüngliche Dateiname
     * @param contentType Der MIME-Typ des Dokuments
     * @param fileSize Die Größe des Dokuments in Bytes
     * @return Eine neue DocumentUpload-Instanz
     */
    public static DocumentUpload create(String originalFilename, String contentType, Long fileSize) {
        DocumentUpload upload = new DocumentUpload();
        upload.setOriginalFilename(originalFilename);
        upload.setContentType(contentType);
        upload.setFileSize(fileSize);
        return upload;
    }
}