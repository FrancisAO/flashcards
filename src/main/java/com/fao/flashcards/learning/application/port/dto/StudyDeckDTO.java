package com.fao.flashcards.learning.application.port.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

/**
 * DTO für ein Deck im Lernmodus.
 * Enthält die Informationen über das Deck und eine Liste der Karteikarten in zufälliger Reihenfolge.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class StudyDeckDTO {
    
    private String id;
    private String name;
    private String description;
    private List<StudyCardDTO> cards = new ArrayList<>();
}