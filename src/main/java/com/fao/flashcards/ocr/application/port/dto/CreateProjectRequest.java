package com.fao.flashcards.ocr.application.port.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

/**
 * Request DTO für die Erstellung eines neuen Projekts.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateProjectRequest {
    
    @NotBlank(message = "Projektname ist erforderlich")
    @Size(max = 255, message = "Projektname darf maximal 255 Zeichen lang sein")
    private String name;
    
    @Size(max = 1000, message = "Beschreibung darf maximal 1000 Zeichen lang sein")
    private String description;
    
    private Set<String> tags;
    
    /**
     * Konstruktor für minimale Projekt-Erstellung.
     */
    public CreateProjectRequest(String name) {
        this.name = name;
    }
    
    /**
     * Konstruktor mit Name und Beschreibung.
     */
    public CreateProjectRequest(String name, String description) {
        this.name = name;
        this.description = description;
    }
    
    /**
     * Validiert die Request-Daten.
     */
    public boolean isValid() {
        return name != null && !name.trim().isEmpty() && name.length() <= 255 &&
               (description == null || description.length() <= 1000);
    }
    
    /**
     * Normalisiert die Tags (kleingeschrieben, getrimmt).
     */
    public void normalizeTags() {
        if (tags != null) {
            tags = tags.stream()
                    .filter(tag -> tag != null && !tag.trim().isEmpty())
                    .map(tag -> tag.trim().toLowerCase())
                    .collect(java.util.stream.Collectors.toSet());
        }
    }
}