package com.fao.flashcards.domain.service;

import com.fao.flashcards.api.dto.StudyDeckDTO;

/**
 * Service-Interface für den Lernmodus.
 * Definiert die Methoden, die für den Lernmodus benötigt werden.
 */
public interface StudyService {
    
    /**
     * Gibt alle Karten eines Decks in zufälliger Reihenfolge zurück.
     * 
     * @param deckId Die ID des Decks
     * @return Ein StudyDeckDTO mit den Karten in zufälliger Reihenfolge
     * @throws java.util.NoSuchElementException wenn das Deck nicht gefunden wird
     */
    StudyDeckDTO getRandomizedDeck(String deckId);
    
    /**
     * Prüft, ob ein Deck existiert.
     * 
     * @param deckId Die ID des Decks
     * @return true, wenn das Deck existiert, sonst false
     */
    boolean doesDeckExist(String deckId);
}