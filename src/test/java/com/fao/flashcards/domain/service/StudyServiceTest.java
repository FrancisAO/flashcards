package com.fao.flashcards.domain.service;

import com.fao.flashcards.api.dto.StudyCardDTO;
import com.fao.flashcards.api.dto.StudyDeckDTO;
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
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class StudyServiceTest {

    @Mock
    private DeckRepository deckRepository;

    @Mock
    private CardRepository cardRepository;

    @Mock
    private DeckCardRepository deckCardRepository;

    @InjectMocks
    private StudyServiceImpl studyService;

    private Deck testDeck;
    private Card testCard1;
    private Card testCard2;
    private DeckCard testDeckCard1;
    private DeckCard testDeckCard2;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        testDeck = new Deck("Testdeck", "Testbeschreibung");
        testDeck.setId("deck-id");
        testDeck.setTags(new HashSet<>(Arrays.asList("Test", "Unit")));
        testDeck.setCreatedAt(LocalDateTime.now());
        testDeck.setUpdatedAt(LocalDateTime.now());

        testCard1 = new Card("Testfrage 1", "Testantwort 1");
        testCard1.setId("card-id-1");
        testCard1.setTags(new HashSet<>(Arrays.asList("Test")));
        testCard1.setCreatedAt(LocalDateTime.now());
        testCard1.setUpdatedAt(LocalDateTime.now());

        testCard2 = new Card("Testfrage 2", "Testantwort 2");
        testCard2.setId("card-id-2");
        testCard2.setTags(new HashSet<>(Arrays.asList("Test")));
        testCard2.setCreatedAt(LocalDateTime.now());
        testCard2.setUpdatedAt(LocalDateTime.now());

        testDeckCard1 = new DeckCard("deck-id", "card-id-1");
        testDeckCard1.setId("deck-card-id-1");
        testDeckCard1.setAddedAt(LocalDateTime.now());

        testDeckCard2 = new DeckCard("deck-id", "card-id-2");
        testDeckCard2.setId("deck-card-id-2");
        testDeckCard2.setAddedAt(LocalDateTime.now());
    }

    @Test
    void getRandomizedDeck_ShouldReturnDeckWithRandomizedCards() {
        // Arrange
        when(deckRepository.findById("deck-id")).thenReturn(Optional.of(testDeck));
        when(deckCardRepository.findByDeckId("deck-id")).thenReturn(Arrays.asList(testDeckCard1, testDeckCard2));
        when(cardRepository.findById("card-id-1")).thenReturn(Optional.of(testCard1));
        when(cardRepository.findById("card-id-2")).thenReturn(Optional.of(testCard2));

        // Act
        Optional<StudyDeckDTO> resultOptional = studyService.getRandomizedDeck("deck-id");

        // Assert
        assertTrue(resultOptional.isPresent(), "Result should be present");
        StudyDeckDTO result = resultOptional.get();
        
        assertEquals(testDeck.getId(), result.getId());
        assertEquals(testDeck.getName(), result.getName());
        assertEquals(testDeck.getDescription(), result.getDescription());
        assertEquals(2, result.getCards().size());
        
        // Verify that the cards are in the result (order may be random)
        boolean foundCard1 = false;
        boolean foundCard2 = false;
        
        for (StudyCardDTO card : result.getCards()) {
            if (card.getId().equals(testCard1.getId())) {
                foundCard1 = true;
                assertEquals(testCard1.getFront(), card.getFront());
                assertEquals(testCard1.getBack(), card.getBack());
            } else if (card.getId().equals(testCard2.getId())) {
                foundCard2 = true;
                assertEquals(testCard2.getFront(), card.getFront());
                assertEquals(testCard2.getBack(), card.getBack());
            }
        }
        
        assertTrue(foundCard1, "Card 1 should be in the result");
        assertTrue(foundCard2, "Card 2 should be in the result");
        
        verify(deckRepository, times(1)).findById("deck-id");
        verify(deckCardRepository, times(1)).findByDeckId("deck-id");
        verify(cardRepository, times(1)).findById("card-id-1");
        verify(cardRepository, times(1)).findById("card-id-2");
    }

    @Test
    void getRandomizedDeck_ShouldReturnEmptyWhenDeckNotFound() {
        // Arrange
        when(deckRepository.findById("deck-id")).thenReturn(Optional.empty());

        // Act
        Optional<StudyDeckDTO> result = studyService.getRandomizedDeck("deck-id");

        // Assert
        assertTrue(result.isEmpty(), "Result should be empty when deck is not found");
        verify(deckRepository, times(1)).findById("deck-id");
    }
    
    @Test
    void getRandomizedDeck_ShouldIgnoreMissingCards() {
        // Arrange
        when(deckRepository.findById("deck-id")).thenReturn(Optional.of(testDeck));
        when(deckCardRepository.findByDeckId("deck-id")).thenReturn(Arrays.asList(testDeckCard1, testDeckCard2));
        when(cardRepository.findById("card-id-1")).thenReturn(Optional.of(testCard1));
        when(cardRepository.findById("card-id-2")).thenReturn(Optional.empty()); // Second card is missing

        // Act
        Optional<StudyDeckDTO> resultOptional = studyService.getRandomizedDeck("deck-id");

        // Assert
        assertTrue(resultOptional.isPresent(), "Result should be present");
        StudyDeckDTO result = resultOptional.get();
        
        assertEquals(1, result.getCards().size(), "Should only include the cards that were found");
        assertEquals(testCard1.getId(), result.getCards().get(0).getId(), "Only card1 should be included");
        
        verify(deckRepository, times(1)).findById("deck-id");
        verify(deckCardRepository, times(1)).findByDeckId("deck-id");
        verify(cardRepository, times(1)).findById("card-id-1");
        verify(cardRepository, times(1)).findById("card-id-2");
    }

    @Test
    void doesDeckExist_ShouldReturnTrueWhenDeckExists() {
        // Arrange
        when(deckRepository.existsById("deck-id")).thenReturn(true);

        // Act
        boolean result = studyService.doesDeckExist("deck-id");

        // Assert
        assertTrue(result);
        verify(deckRepository, times(1)).existsById("deck-id");
    }

    @Test
    void doesDeckExist_ShouldReturnFalseWhenDeckDoesNotExist() {
        // Arrange
        when(deckRepository.existsById("deck-id")).thenReturn(false);

        // Act
        boolean result = studyService.doesDeckExist("deck-id");

        // Assert
        assertFalse(result);
        verify(deckRepository, times(1)).existsById("deck-id");
    }
}