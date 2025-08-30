package com.fao.flashcards.domain.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Temporäres Modell für generierte Karteikarten vor dem Speichern.
 * Enthält Vorder- und Rückseite sowie Status-Informationen.
 */
@Entity
@Table(name = "ai_generated_cards")
@Data
@NoArgsConstructor
public class AIGeneratedCard {
    
    @Id
    private String id;
    
    @ManyToOne
    @JoinColumn(name = "generation_request_id", nullable = false)
    private AIGenerationRequest generationRequest;
    
    @Column(nullable = false, columnDefinition = "TEXT")
    private String front;
    
    @Column(nullable = false, columnDefinition = "TEXT")
    private String back;
    
    @Column(name = "is_edited")
    private boolean edited;
    
    @Column(name = "is_saved")
    private boolean saved;
    
    @Column(name = "card_id")
    private String cardId;
    
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
    
    /**
     * Erstellt eine neue AIGeneratedCard-Instanz mit den angegebenen Werten.
     * 
     * @param front Die Vorderseite der Karteikarte
     * @param back Die Rückseite der Karteikarte
     * @return Eine neue AIGeneratedCard-Instanz
     */
    public static AIGeneratedCard create(String front, String back) {
        AIGeneratedCard card = new AIGeneratedCard();
        card.setFront(front);
        card.setBack(back);
        card.setEdited(false);
        card.setSaved(false);
        return card;
    }
    
    /**
     * Markiert die Karteikarte als bearbeitet.
     */
    public void markAsEdited() {
        this.edited = true;
    }
    
    /**
     * Markiert die Karteikarte als gespeichert und setzt die Card-ID.
     * 
     * @param cardId Die ID der gespeicherten Karteikarte
     */
    public void markAsSaved(String cardId) {
        this.saved = true;
        this.cardId = cardId;
    }
}