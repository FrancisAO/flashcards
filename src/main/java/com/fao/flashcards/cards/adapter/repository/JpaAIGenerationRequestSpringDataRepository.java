package com.fao.flashcards.cards.adapter.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.fao.flashcards.cards.model.AIGenerationRequest;

import java.util.List;

/**
 * Repository für AIGenerationRequest-Entitäten.
 */
@Repository
public interface JpaAIGenerationRequestSpringDataRepository extends JpaRepository<AIGenerationRequest, String> {

    /**
     * Findet alle Generierungsanfragen für ein bestimmtes Deck.
     * 
     * @param deckId Die ID des Decks
     * @return Eine Liste von Generierungsanfragen
     */
    List<AIGenerationRequest> findByDeckId(String deckId);
}