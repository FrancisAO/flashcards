package com.fao.flashcards.domain.service;

import com.fao.flashcards.api.dto.DeckDTO;
import com.fao.flashcards.api.dto.DeckWithCardsDTO;
import com.fao.flashcards.domain.model.Card;
import com.fao.flashcards.domain.model.Deck;
import com.fao.flashcards.domain.model.DeckCard;
import com.fao.flashcards.domain.repository.CardRepository;
import com.fao.flashcards.domain.repository.DeckCardRepository;
import com.fao.flashcards.domain.repository.DeckRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class DeckServiceTest {

    @Mock
    private DeckRepository deckRepository;

    @Mock
    private CardRepository cardRepository;

    @Mock
    private DeckCardRepository deckCardRepository;

    @InjectMocks
    private DeckService deckService;

    private Deck testDeck;
    private Card testCard;
    private DeckCard testDeckCard;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        testDeck = new Deck("Testdeck", "Testbeschreibung");
        testDeck.setId("deck-id");
        testDeck.setTags(new HashSet<>(Arrays.asList("Test", "Unit")));
        testDeck.setCreatedAt(LocalDateTime.now());
        testDeck.setUpdatedAt(LocalDateTime.now());

        testCard = new Card("Testfrage", "Testantwort");
        testCard.setId("card-id");
        testCard.setTags(new HashSet<>(Arrays.asList("Test")));
        testCard.setCreatedAt(LocalDateTime.now());
        testCard.setUpdatedAt(LocalDateTime.now());

        testDeckCard = new DeckCard("deck-id", "card-id");
        testDeckCard.setId("deck-card-id");
        testDeckCard.setAddedAt(LocalDateTime.now());
    }

    @Test
    void getAllDecks_ShouldReturnAllDecks() {
        // Arrange
        when(deckRepository.findAll()).thenReturn(Arrays.asList(testDeck));

        // Act
        List<DeckDTO> result = deckService.getAllDecks();

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(testDeck.getId(), result.get(0).getId());
        assertEquals(testDeck.getName(), result.get(0).getName());
        assertEquals(testDeck.getDescription(), result.get(0).getDescription());
        
        verify(deckRepository, times(1)).findAll();
    }

    @Test
    void getDeckById_ShouldReturnDeckById() {
        // Arrange
        when(deckRepository.findById("deck-id")).thenReturn(Optional.of(testDeck));

        // Act
        DeckDTO result = deckService.getDeckById("deck-id");

        // Assert
        assertNotNull(result);
        assertEquals(testDeck.getId(), result.getId());
        assertEquals(testDeck.getName(), result.getName());
        assertEquals(testDeck.getDescription(), result.getDescription());
        
        verify(deckRepository, times(1)).findById("deck-id");
    }

    @Test
    void getDeckWithCards_ShouldReturnDeckWithCards() {
        // Arrange
        when(deckRepository.findById("deck-id")).thenReturn(Optional.of(testDeck));
        when(deckCardRepository.findByDeckId("deck-id")).thenReturn(Arrays.asList(testDeckCard));
        when(cardRepository.findById("card-id")).thenReturn(Optional.of(testCard));

        // Act
        DeckWithCardsDTO result = deckService.getDeckWithCards("deck-id");

        // Assert
        assertNotNull(result);
        assertEquals(testDeck.getId(), result.getId());
        assertEquals(testDeck.getName(), result.getName());
        assertEquals(testDeck.getDescription(), result.getDescription());
        assertEquals(1, result.getCards().size());
        assertEquals(testCard.getId(), result.getCards().get(0).getId());
        
        verify(deckRepository, times(1)).findById("deck-id");
        verify(deckCardRepository, times(1)).findByDeckId("deck-id");
        verify(cardRepository, times(1)).findById("card-id");
    }
}
