package com.fao.flashcards.api.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DeckCardDTO {
    
    @NotBlank(message = "Die Deck-ID darf nicht leer sein")
    private String deckId;
    
    @NotBlank(message = "Die Karten-ID darf nicht leer sein")
    private String cardId;
}
