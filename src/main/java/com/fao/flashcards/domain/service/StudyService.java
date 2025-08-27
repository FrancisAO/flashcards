package com.fao.flashcards.domain.service;

import com.fao.flashcards.api.dto.StudyDeckDTO;
import java.util.Optional;

/**
 * Service-Interface für den Lernmodus.
 * Definiert die Methoden, die für den Lernmodus benötigt werden.
 */
public interface StudyService {
    
    /**
     * Gibt alle Karten eines Decks in zufälliger Reihenfolge zurück.
     * 
     * @param deckId Die ID des Decks
     * @return Ein Optional mit StudyDeckDTO mit den Karten in zufälliger Reihenfolge
     *         oder Optional.empty(), wenn das Deck nicht gefunden wurde
     */
    Optional<StudyDeckDTO> getRandomizedDeck(String deckId);
    
    /**
     * Prüft, ob ein Deck existiert.
     * 
     * @param deckId Die ID des Decks
     * @return true, wenn das Deck existiert, sonst false
     */
    boolean doesDeckExist(String deckId);
}