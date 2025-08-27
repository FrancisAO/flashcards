package com.fao.flashcards.domain.repository;

import com.fao.flashcards.domain.model.AIGenerationRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository für AIGenerationRequest-Entitäten.
 */
@Repository
public interface AIGenerationRequestRepository extends JpaRepository<AIGenerationRequest, String> {
    
    /**
     * Findet alle Generierungsanfragen für ein bestimmtes Deck.
     * 
     * @param deckId Die ID des Decks
     * @return Eine Liste von Generierungsanfragen
     */
    List<AIGenerationRequest> findByDeckId(String deckId);
}