package com.fao.flashcards.cards.adapter.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.fao.flashcards.cards.model.DeckCard;

@Repository
public interface JpaDeckCardSpringDataRepository extends JpaRepository<DeckCard, String> {
    List<DeckCard> findByDeckId(String deckId);

    List<DeckCard> findByCardId(String cardId);

    Optional<DeckCard> findByDeckIdAndCardId(String deckId, String cardId);

    void deleteByDeckIdAndCardId(String deckId, String cardId);
}
