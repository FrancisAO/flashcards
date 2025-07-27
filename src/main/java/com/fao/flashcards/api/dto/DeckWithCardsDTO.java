package com.fao.flashcards.api.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DeckWithCardsDTO {
    
    private String id;
    private String name;
    private String description;
    private Set<String> tags = new HashSet<>();
    private List<CardDTO> cards = new ArrayList<>();
    private String createdAt;
    private String updatedAt;
}
