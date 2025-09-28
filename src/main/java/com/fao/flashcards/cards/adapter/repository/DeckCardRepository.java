package com.fao.flashcards.cards.adapter.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.fao.flashcards.cards.model.DeckCard;

import java.util.List;
import java.util.Optional;

@Repository
public interface DeckCardRepository extends JpaRepository<DeckCard, String> {
    List<DeckCard> findByDeckId(String deckId);
    List<DeckCard> findByCardId(String cardId);
    Optional<DeckCard> findByDeckIdAndCardId(String deckId, String cardId);
    void deleteByDeckIdAndCardId(String deckId, String cardId);
}
