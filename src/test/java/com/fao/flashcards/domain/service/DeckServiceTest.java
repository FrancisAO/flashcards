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
import java.util.NoSuchElementException;
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

    @Test
    void createDeck_ShouldCreateAndReturnDeck() {
        // Arrange
        DeckDTO deckDTO = new DeckDTO();
        deckDTO.setName("New Deck");
        deckDTO.setDescription("New Description");
        when(deckRepository.save(any(Deck.class))).thenAnswer(invocation -> {
            Deck deck = invocation.getArgument(0);
            deck.setId("new-deck-id");
            return deck;
        });

        // Act
        DeckDTO result = deckService.createDeck(deckDTO);

        // Assert
        assertNotNull(result);
        assertEquals("new-deck-id", result.getId());
        assertEquals("New Deck", result.getName());
        assertEquals("New Description", result.getDescription());
        verify(deckRepository, times(1)).save(any(Deck.class));
    }

    @Test
    void updateDeck_ShouldUpdateAndReturnDeck() {
        // Arrange
        DeckDTO deckDTO = new DeckDTO();
        deckDTO.setName("Updated Deck");
        deckDTO.setDescription("Updated Description");
        when(deckRepository.findById("deck-id")).thenReturn(Optional.of(testDeck));
        when(deckRepository.save(any(Deck.class))).thenReturn(testDeck);

        // Act
        DeckDTO result = deckService.updateDeck("deck-id", deckDTO);

        // Assert
        assertNotNull(result);
        assertEquals("Updated Deck", result.getName());
        assertEquals("Updated Description", result.getDescription());
        verify(deckRepository, times(1)).findById("deck-id");
        verify(deckRepository, times(1)).save(any(Deck.class));
    }

    @Test
    void updateDeck_ShouldThrowExceptionWhenDeckNotFound() {
        // Arrange
        DeckDTO deckDTO = new DeckDTO();
        deckDTO.setName("Updated Deck");
        deckDTO.setDescription("Updated Description");
        when(deckRepository.findById("deck-id")).thenReturn(Optional.empty());

        // Act & Assert
        NoSuchElementException exception = assertThrows(NoSuchElementException.class, () -> {
            deckService.updateDeck("deck-id", deckDTO);
        });

        assertEquals("Deck mit ID deck-id wurde nicht gefunden", exception.getMessage());
        verify(deckRepository, times(1)).findById("deck-id");
    }

    @Test
    void updateDeck_ShouldOnlySetTagsWhenPresent() {
        // Arrange
        DeckDTO deckDTO = new DeckDTO();
        deckDTO.setName("Updated Deck");
        deckDTO.setDescription("Updated Description");
        deckDTO.setTags(null); // Tags are not provided
        when(deckRepository.findById("deck-id")).thenReturn(Optional.of(testDeck));
        when(deckRepository.save(any(Deck.class))).thenReturn(testDeck);

        // Act
        DeckDTO result = deckService.updateDeck("deck-id", deckDTO);

        // Assert
        assertNotNull(result);
        assertEquals("Updated Deck", result.getName());
        assertEquals("Updated Description", result.getDescription());
        assertEquals(testDeck.getTags(), result.getTags()); // Tags should remain unchanged
        verify(deckRepository, times(1)).findById("deck-id");
        verify(deckRepository, times(1)).save(any(Deck.class));
    }

    @Test
    void deleteDeck_ShouldDeleteDeck() {
        // Arrange
        when(deckRepository.existsById("deck-id")).thenReturn(true);
        when(deckCardRepository.findByDeckId("deck-id")).thenReturn(Arrays.asList(testDeckCard));

        // Act
        deckService.deleteDeck("deck-id");

        // Assert
        verify(deckRepository, times(1)).existsById("deck-id");
        verify(deckCardRepository, times(1)).findByDeckId("deck-id");
        verify(deckCardRepository, times(1)).deleteAll(anyList());
        verify(deckRepository, times(1)).deleteById("deck-id");
    }

    @Test
    void deleteDeck_ShouldThrowExceptionWhenDeckNotFound() {
        // Arrange
        when(deckRepository.existsById("deck-id")).thenReturn(false);

        // Act & Assert
        NoSuchElementException exception = assertThrows(NoSuchElementException.class, () -> {
            deckService.deleteDeck("deck-id");
        });

        assertEquals("Deck mit ID deck-id wurde nicht gefunden", exception.getMessage());
        verify(deckRepository, times(1)).existsById("deck-id");
    }

    @Test
    void addCardToDeck_ShouldAddCardToDeck() {
        // Arrange
        when(deckRepository.existsById("deck-id")).thenReturn(true);
        when(cardRepository.existsById("card-id")).thenReturn(true);
        when(deckCardRepository.findByDeckIdAndCardId("deck-id", "card-id")).thenReturn(Optional.empty());

        // Act
        deckService.addCardToDeck("deck-id", "card-id");

        // Assert
        verify(deckRepository, times(1)).existsById("deck-id");
        verify(cardRepository, times(1)).existsById("card-id");
        verify(deckCardRepository, times(1)).findByDeckIdAndCardId("deck-id", "card-id");
        verify(deckCardRepository, times(1)).save(any(DeckCard.class));
    }

    @Test
    void addCardToDeck_ShouldThrowExceptionWhenDeckNotFound() {
        // Arrange
        when(deckRepository.existsById("deck-id")).thenReturn(false);

        // Act & Assert
        NoSuchElementException exception = assertThrows(NoSuchElementException.class, () -> {
            deckService.addCardToDeck("deck-id", "card-id");
        });

        assertEquals("Deck mit ID deck-id wurde nicht gefunden", exception.getMessage());
        verify(deckRepository, times(1)).existsById("deck-id");
    }

    @Test
    void addCardToDeck_ShouldThrowExceptionWhenCardNotFound() {
        // Arrange
        when(deckRepository.existsById("deck-id")).thenReturn(true);
        when(cardRepository.existsById("card-id")).thenReturn(false);

        // Act & Assert
        NoSuchElementException exception = assertThrows(NoSuchElementException.class, () -> {
            deckService.addCardToDeck("deck-id", "card-id");
        });

        assertEquals("Karte mit ID card-id wurde nicht gefunden", exception.getMessage());
        verify(deckRepository, times(1)).existsById("deck-id");
        verify(cardRepository, times(1)).existsById("card-id");
    }

    @Test
    void addCardToDeck_ShouldThrowExceptionWhenCardAlreadyInDeck() {
        // Arrange
        when(deckRepository.existsById("deck-id")).thenReturn(true);
        when(cardRepository.existsById("card-id")).thenReturn(true);
        when(deckCardRepository.findByDeckIdAndCardId("deck-id", "card-id")).thenReturn(Optional.of(testDeckCard));

        // Act & Assert
        IllegalStateException exception = assertThrows(IllegalStateException.class, () -> {
            deckService.addCardToDeck("deck-id", "card-id");
        });

        assertEquals("Die Karte ist bereits im Deck vorhanden", exception.getMessage());
        verify(deckRepository, times(1)).existsById("deck-id");
        verify(cardRepository, times(1)).existsById("card-id");
        verify(deckCardRepository, times(1)).findByDeckIdAndCardId("deck-id", "card-id");
    }

    @Test
    void removeCardFromDeck_ShouldRemoveCardFromDeck() {
        // Arrange
        when(deckCardRepository.findByDeckIdAndCardId("deck-id", "card-id")).thenReturn(Optional.of(testDeckCard));

        // Act
        deckService.removeCardFromDeck("deck-id", "card-id");

        // Assert
        verify(deckCardRepository, times(1)).findByDeckIdAndCardId("deck-id", "card-id");
        verify(deckCardRepository, times(1)).delete(testDeckCard);
    }

    @Test
    void removeCardFromDeck_ShouldThrowExceptionWhenCardNotInDeck() {
        // Arrange
        when(deckCardRepository.findByDeckIdAndCardId("deck-id", "card-id")).thenReturn(Optional.empty());

        // Act & Assert
        NoSuchElementException exception = assertThrows(NoSuchElementException.class, () -> {
            deckService.removeCardFromDeck("deck-id", "card-id");
        });

        assertEquals("Die Karte ist nicht im Deck vorhanden", exception.getMessage());
        verify(deckCardRepository, times(1)).findByDeckIdAndCardId("deck-id", "card-id");
    }

    @Test
    void getDecksByTag_ShouldReturnDecksByTag() {
        // Arrange
        when(deckRepository.findByTagsContaining("Test")).thenReturn(Arrays.asList(testDeck));

        // Act
        List<DeckDTO> result = deckService.getDecksByTag("Test");

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(testDeck.getId(), result.get(0).getId());
        verify(deckRepository, times(1)).findByTagsContaining("Test");
    }
}
