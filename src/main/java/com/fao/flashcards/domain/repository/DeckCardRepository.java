package com.fao.flashcards.domain.repository;

import com.fao.flashcards.domain.model.DeckCard;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DeckCardRepository extends JpaRepository<DeckCard, String> {
    List<DeckCard> findByDeckId(String deckId);
    List<DeckCard> findByCardId(String cardId);
    Optional<DeckCard> findByDeckIdAndCardId(String deckId, String cardId);
    void deleteByDeckIdAndCardId(String deckId, String cardId);
}
