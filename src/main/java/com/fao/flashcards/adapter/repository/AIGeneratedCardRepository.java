package com.fao.flashcards.adapter.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.fao.flashcards.application.model.AIGeneratedCard;

import java.util.List;

/**
 * Repository für AIGeneratedCard-Entitäten.
 */
@Repository
public interface AIGeneratedCardRepository extends JpaRepository<AIGeneratedCard, String> {
    
    /**
     * Findet alle generierten Karteikarten für eine bestimmte Generierungsanfrage.
     * 
     * @param generationRequestId Die ID der Generierungsanfrage
     * @return Eine Liste von generierten Karteikarten
     */
    List<AIGeneratedCard> findByGenerationRequestId(String generationRequestId);
    
    /**
     * Findet alle generierten Karteikarten, die noch nicht gespeichert wurden.
     * 
     * @param generationRequestId Die ID der Generierungsanfrage
     * @return Eine Liste von nicht gespeicherten Karteikarten
     */
    List<AIGeneratedCard> findByGenerationRequestIdAndSavedFalse(String generationRequestId);
}