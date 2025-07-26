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
public class CardDTO {
    
    private String id;
    
    @NotBlank(message = "Die Vorderseite der Karte darf nicht leer sein")
    private String front;
    
    @NotBlank(message = "Die Rückseite der Karte darf nicht leer sein")
    private String back;
    
    private Set<String> tags = new HashSet<>();
    
    private String createdAt;
    private String updatedAt;
}
