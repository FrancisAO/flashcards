package com.fao.flashcards.ocr.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import com.fao.flashcards.cards.model.FileType;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Domain Model für Dateien in OCR-Projekten.
 * Repräsentiert eine hochgeladene Datei mit ihren Metadaten.
 */
@Entity
@Table(name = "project_files")
@Data
@NoArgsConstructor
public class ProjectFile {
    
    @Id
    private String id;
    
    @NotNull(message = "Projekt ist erforderlich")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_id", nullable = false)
    private Project project;
    
    @NotBlank(message = "Original-Dateiname ist erforderlich")
    @Size(max = 500, message = "Dateiname darf maximal 500 Zeichen lang sein")
    @Column(name = "original_filename", nullable = false)
    private String originalFilename;
    
    @NotBlank(message = "Content-Type ist erforderlich")
    @Size(max = 100, message = "Content-Type darf maximal 100 Zeichen lang sein")
    @Column(name = "content_type", nullable = false)
    private String contentType;
    
    @NotNull(message = "Dateigröße ist erforderlich")
    @Positive(message = "Dateigröße muss positiv sein")
    @Column(name = "file_size", nullable = false)
    private Long fileSize;
    
    @NotBlank(message = "Speicherpfad ist erforderlich")
    @Size(max = 1000, message = "Speicherpfad darf maximal 1000 Zeichen lang sein")
    @Column(name = "storage_path", nullable = false)
    private String storagePath;
    
    @NotNull(message = "Dateityp ist erforderlich")
    @Enumerated(EnumType.STRING)
    @Column(name = "file_type", nullable = false)
    private FileType fileType;
    
    @CreationTimestamp
    @Column(name = "uploaded_at")
    private LocalDateTime uploadedAt;
    
    @PrePersist
    protected void onCreate() {
        if (id == null) {
            id = UUID.randomUUID().toString();
        }
    }
    
    public ProjectFile(Project project, String originalFilename, String contentType, 
                      Long fileSize, String storagePath, FileType fileType) {
        this.project = project;
        this.originalFilename = originalFilename;
        this.contentType = contentType;
        this.fileSize = fileSize;
        this.storagePath = storagePath;
        this.fileType = fileType;
    }
}