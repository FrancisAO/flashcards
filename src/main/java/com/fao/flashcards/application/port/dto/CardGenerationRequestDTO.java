package com.fao.flashcards.application.port.dto;

import jakarta.validation.constraints.Size;
import lombok.Data;

/**
 * DTO für die Anfrage zur Karteikartengenerierung.
 */
@Data
public class CardGenerationRequestDTO {
    
    /**
     * Hinweise für das KI-Modell zur Generierung der Karteikarten.
     */
    @Size(max = 1000, message = "Hinweise dürfen maximal 1000 Zeichen lang sein")
    private String prompt;
    
    /**
     * Die gewünschte Anzahl an Karteikarten (optional).
     */
    private Integer numberOfCards;
}