package com.fao.flashcards.ocr.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

/**
 * Domain Model für OCR-Projekte.
 * Ein Projekt kann mehrere Dateien enthalten und dient als Container für OCR-Operationen.
 */
@Entity
@Table(name = "projects")
@Data
@NoArgsConstructor
public class Project {
    
    @Id
    private String id;
    
    @NotBlank(message = "Projektname ist erforderlich")
    @Size(max = 255, message = "Projektname darf maximal 255 Zeichen lang sein")
    @Column(nullable = false)
    private String name;
    
    @Size(max = 1000, message = "Beschreibung darf maximal 1000 Zeichen lang sein")
    @Column(columnDefinition = "TEXT")
    private String description;
    
    @ElementCollection
    @CollectionTable(name = "project_tags", joinColumns = @JoinColumn(name = "project_id"))
    @Column(name = "tag")
    private Set<String> tags = new HashSet<>();
    
    @Column(name = "file_count", nullable = false)
    private Integer fileCount = 0;
    
    @Column(name = "extracted_text_count", nullable = false)
    private Integer extractedTextCount = 0;
    
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
    
    public Project(String name, String description) {
        this.name = name;
        this.description = description;
    }
}