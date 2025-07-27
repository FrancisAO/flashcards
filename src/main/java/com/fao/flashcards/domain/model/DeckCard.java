package com.fao.flashcards.domain.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "deck_cards")
@Data
@NoArgsConstructor
public class DeckCard {
    
    @Id
    private String id;
    
    @Column(name = "deck_id", nullable = false)
    private String deckId;
    
    @Column(name = "card_id", nullable = false)
    private String cardId;
    
    @Column(name = "added_at")
    private LocalDateTime addedAt;
    
    @PrePersist
    protected void onCreate() {
        if (id == null) {
            id = UUID.randomUUID().toString();
        }
        if (addedAt == null) {
            addedAt = LocalDateTime.now();
        }
    }
    
    public DeckCard(String deckId, String cardId) {
        this.deckId = deckId;
        this.cardId = cardId;
        this.addedAt = LocalDateTime.now();
    }
}
