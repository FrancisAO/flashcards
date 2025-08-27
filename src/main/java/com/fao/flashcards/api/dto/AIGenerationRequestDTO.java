package com.fao.flashcards.api.dto;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * DTO für eine Generierungsanfrage.
 */
@Data
public class AIGenerationRequestDTO {
    
    /**
     * Die ID der Generierungsanfrage.
     */
    private String id;
    
    /**
     * Die ID des Decks, für das Karten generiert werden sollen.
     */
    private String deckId;
    
    /**
     * Hinweise für das KI-Modell zur Generierung der Karteikarten.
     */
    private String prompt;
    
    /**
     * Die gewünschte Anzahl an Karteikarten (optional).
     */
    private Integer numberOfCards;
    
    /**
     * Der Status der Generierungsanfrage.
     */
    private String status;
    
    /**
     * Die Anzahl der hochgeladenen Dokumente.
     */
    private int documentCount;
    
    /**
     * Die Anzahl der generierten Karteikarten.
     */
    private int generatedCardCount;
    
    /**
     * Der Zeitpunkt der Erstellung der Generierungsanfrage.
     */
    private LocalDateTime createdAt;
    
    /**
     * Der Zeitpunkt der letzten Aktualisierung der Generierungsanfrage.
     */
    private LocalDateTime updatedAt;
}