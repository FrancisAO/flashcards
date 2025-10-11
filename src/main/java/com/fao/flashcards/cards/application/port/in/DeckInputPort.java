package com.fao.flashcards.cards.application.port.in;

import java.util.List;

import com.fao.flashcards.cards.application.port.dto.DeckDTO;
import com.fao.flashcards.cards.application.port.dto.DeckWithCardsDTO;

public interface DeckInputPort {

    List<DeckDTO> getAllDecks();

    DeckDTO getDeckById(String id);

    DeckWithCardsDTO getDeckWithCards(String id);

    DeckDTO createDeck(DeckDTO deckDTO);

    DeckDTO updateDeck(String id, DeckDTO deckDTO);

    void deleteDeck(String id);

    void addCardToDeck(String deckId, String cardId);

    void removeCardFromDeck(String deckId, String cardId);

    List<DeckDTO> getDecksByTag(String tag);

    boolean doesDeckExist(String id);

}