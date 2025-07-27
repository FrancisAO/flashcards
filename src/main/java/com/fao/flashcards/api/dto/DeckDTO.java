package com.fao.flashcards.api.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DeckDTO {
    
    private String id;
    
    @NotBlank(message = "Der Name des Decks darf nicht leer sein")
    private String name;
    
    private String description;
    
    private Set<String> tags = new HashSet<>();
    
    private String createdAt;
    private String updatedAt;
}
