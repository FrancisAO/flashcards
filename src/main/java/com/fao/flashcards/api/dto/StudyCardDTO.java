package com.fao.flashcards.api.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;

/**
 * DTO für eine Karteikarte im Lernmodus.
 * Enthält die Informationen, die für das Lernen einer Karteikarte benötigt werden.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class StudyCardDTO {
    
    private String id;
    private String front;
    private String back;
    private Set<String> tags = new HashSet<>();
}