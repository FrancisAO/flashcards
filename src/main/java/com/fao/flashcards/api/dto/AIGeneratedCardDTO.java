package com.fao.flashcards.api.dto;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * DTO für eine generierte Karteikarte.
 */
@Data
public class AIGeneratedCardDTO {
    
    /**
     * Die ID der generierten Karteikarte.
     */
    private String id;
    
    /**
     * Die Vorderseite der Karteikarte.
     */
    private String front;
    
    /**
     * Die Rückseite der Karteikarte.
     */
    private String back;
    
    /**
     * Gibt an, ob die Karteikarte bearbeitet wurde.
     */
    private boolean edited;
    
    /**
     * Gibt an, ob die Karteikarte gespeichert wurde.
     */
    private boolean saved;
    
    /**
     * Die ID der gespeicherten Karteikarte (falls gespeichert).
     */
    private String cardId;
    
    /**
     * Der Zeitpunkt der Erstellung der Karteikarte.
     */
    private LocalDateTime createdAt;
    
    /**
     * Der Zeitpunkt der letzten Aktualisierung der Karteikarte.
     */
    private LocalDateTime updatedAt;
}