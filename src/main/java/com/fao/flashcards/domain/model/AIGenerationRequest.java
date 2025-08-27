package com.fao.flashcards.domain.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Repräsentiert eine Anfrage zur KI-gestützten Karteikartengenerierung.
 * Enthält Hinweise für das KI-Modell und Referenzen zu hochgeladenen Dokumenten und generierten Karten.
 */
@Entity
@Table(name = "ai_generation_requests")
@Data
@NoArgsConstructor
public class AIGenerationRequest {
    
    @Id
    private String id;
    
    @Column(name = "deck_id", nullable = false)
    private String deckId;
    
    @Column(name = "prompt", columnDefinition = "TEXT")
    private String prompt;
    
    @Column(name = "number_of_cards")
    private Integer numberOfCards;
    
    @Column(name = "status", nullable = false)
    @Enumerated(EnumType.STRING)
    private GenerationStatus status;
    
    @OneToMany(mappedBy = "generationRequest", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<DocumentUpload> documents = new ArrayList<>();
    
    @OneToMany(mappedBy = "generationRequest", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<AIGeneratedCard> generatedCards = new ArrayList<>();
    
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
        if (status == null) {
            status = GenerationStatus.PENDING;
        }
    }
    
    /**
     * Fügt ein Dokument zur Generierungsanfrage hinzu.
     * 
     * @param document Das hinzuzufügende Dokument
     */
    public void addDocument(DocumentUpload document) {
        documents.add(document);
        document.setGenerationRequest(this);
    }
    
    /**
     * Fügt eine generierte Karteikarte zur Generierungsanfrage hinzu.
     * 
     * @param card Die hinzuzufügende Karteikarte
     */
    public void addGeneratedCard(AIGeneratedCard card) {
        generatedCards.add(card);
        card.setGenerationRequest(this);
    }
    
    /**
     * Status einer Generierungsanfrage.
     */
    public enum GenerationStatus {
        PENDING,
        PROCESSING,
        COMPLETED,
        FAILED
    }
}