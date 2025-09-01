package com.fao.flashcards.api.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Request DTO für die Bearbeitung extrahierter Texte.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateTextRequest {
    
    @NotBlank(message = "Bearbeiteter Inhalt ist erforderlich")
    private String editedContent;
    
    private String editComment;
    
    /**
     * Konstruktor nur mit Inhalt.
     */
    public UpdateTextRequest(String editedContent) {
        this.editedContent = editedContent;
    }
    
    /**
     * Validiert den Request.
     */
    public boolean isValid() {
        return editedContent != null && !editedContent.trim().isEmpty();
    }
    
    /**
     * Gibt die Anzahl der Zeichen im bearbeiteten Text zurück.
     */
    public int getContentLength() {
        return editedContent != null ? editedContent.length() : 0;
    }
    
    /**
     * Gibt die Anzahl der Wörter im bearbeiteten Text zurück (grobe Schätzung).
     */
    public int getWordCount() {
        if (editedContent == null || editedContent.trim().isEmpty()) {
            return 0;
        }
        return editedContent.trim().split("\\s+").length;
    }
    
    /**
     * Prüft ob ein Kommentar zur Bearbeitung vorhanden ist.
     */
    public boolean hasComment() {
        return editComment != null && !editComment.trim().isEmpty();
    }
    
    /**
     * Normalisiert den Text (entfernt überflüssige Leerzeichen).
     */
    public void normalizeContent() {
        if (editedContent != null) {
            editedContent = editedContent.trim().replaceAll("\\s+", " ");
        }
    }
}