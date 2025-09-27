package com.fao.flashcards.adapter.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.fao.flashcards.application.model.DeckCard;

import java.util.List;
import java.util.Optional;

@Repository
public interface DeckCardRepository extends JpaRepository<DeckCard, String> {
    List<DeckCard> findByDeckId(String deckId);
    List<DeckCard> findByCardId(String cardId);
    Optional<DeckCard> findByDeckIdAndCardId(String deckId, String cardId);
    void deleteByDeckIdAndCardId(String deckId, String cardId);
}
