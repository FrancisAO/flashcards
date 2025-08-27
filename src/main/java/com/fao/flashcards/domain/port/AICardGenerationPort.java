package com.fao.flashcards.domain.port;

import java.util.List;
import lombok.Data;

/**
 * Port für die KI-gestützte Generierung von Karteikarten.
 * Dieser Port wird vom Anwendungskern verwendet, um mit der KI zu kommunizieren.
 */
public interface AICardGenerationPort {
    
    /**
     * Generiert Karteikarten basierend auf dem übergebenen Text und den Hinweisen.
     * 
     * @param contentText Der Text, aus dem Karteikarten generiert werden sollen
     * @param prompt Hinweise für das KI-Modell zur Generierung der Karteikarten
     * @param numberOfCards Die gewünschte Anzahl an Karteikarten (optional)
     * @return Eine Liste von generierten Karteikarten mit Vorder- und Rückseite
     */
    List<GeneratedCard> generateCardsFromText(String contentText, String prompt, Integer numberOfCards);
    
    /**
     * Datenklasse für generierte Karteikarten.
     */
    @Data
    class GeneratedCard {
        private String front;
        private String back;
        
        public GeneratedCard() {
        }
        
        public GeneratedCard(String front, String back) {
            this.front = front;
            this.back = back;
        }
    }
}