package com.fao.flashcards.domain.service;

import com.fao.flashcards.api.dto.CardDTO;
import com.fao.flashcards.domain.model.Card;
import com.fao.flashcards.domain.repository.CardRepository;
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

class CardServiceTest {

    @Mock
    private CardRepository cardRepository;

    @InjectMocks
    private CardService cardService;

    private Card testCard;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        testCard = new Card("Testfrage", "Testantwort");
        testCard.setId("test-id");
        testCard.setTags(new HashSet<>(Arrays.asList("Test", "Unit")));
        testCard.setCreatedAt(LocalDateTime.now());
        testCard.setUpdatedAt(LocalDateTime.now());
    }

    @Test
    void getAllCards_ShouldReturnAllCards() {
        // Arrange
        when(cardRepository.findAll()).thenReturn(Arrays.asList(testCard));

        // Act
        List<CardDTO> result = cardService.getAllCards();

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(testCard.getId(), result.get(0).getId());
        assertEquals(testCard.getFront(), result.get(0).getFront());
        assertEquals(testCard.getBack(), result.get(0).getBack());
        
        verify(cardRepository, times(1)).findAll();
    }

    @Test
    void getCardById_ShouldReturnCardById() {
        // Arrange
        when(cardRepository.findById("test-id")).thenReturn(Optional.of(testCard));

        // Act
        CardDTO result = cardService.getCardById("test-id");

        // Assert
        assertNotNull(result);
        assertEquals(testCard.getId(), result.getId());
        assertEquals(testCard.getFront(), result.getFront());
        assertEquals(testCard.getBack(), result.getBack());
        
        verify(cardRepository, times(1)).findById("test-id");
    }

    @Test
    void createCard_ShouldCreateAndReturnNewCard() {
        // Arrange
        CardDTO cardDTO = new CardDTO();
        cardDTO.setFront("Neue Frage");
        cardDTO.setBack("Neue Antwort");
        cardDTO.setTags(new HashSet<>(Arrays.asList("Neu")));

        when(cardRepository.save(any(Card.class))).thenAnswer(invocation -> {
            Card savedCard = invocation.getArgument(0);
            savedCard.setId("new-id");
            savedCard.setCreatedAt(LocalDateTime.now());
            savedCard.setUpdatedAt(LocalDateTime.now());
            return savedCard;
        });

        // Act
        CardDTO result = cardService.createCard(cardDTO);

        // Assert
        assertNotNull(result);
        assertEquals("new-id", result.getId());
        assertEquals(cardDTO.getFront(), result.getFront());
        assertEquals(cardDTO.getBack(), result.getBack());
        assertEquals(cardDTO.getTags(), result.getTags());
        
        verify(cardRepository, times(1)).save(any(Card.class));
    }

    @Test
    void updateCard_ShouldUpdateAndReturnUpdatedCard() {
        // Arrange
        CardDTO cardDTO = new CardDTO();
        cardDTO.setFront("Updated Frage");
        cardDTO.setBack("Updated Antwort");
        cardDTO.setTags(new HashSet<>(Arrays.asList("Updated")));

        when(cardRepository.findById("test-id")).thenReturn(Optional.of(testCard));
        when(cardRepository.save(any(Card.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        CardDTO result = cardService.updateCard("test-id", cardDTO);

        // Assert
        assertNotNull(result);
        assertEquals("Updated Frage", result.getFront());
        assertEquals("Updated Antwort", result.getBack());
        assertEquals(cardDTO.getTags(), result.getTags());

        verify(cardRepository, times(1)).findById("test-id");
        verify(cardRepository, times(1)).save(any(Card.class));
    }

    @Test
    void deleteCard_ShouldDeleteCardById() {
        // Arrange
        when(cardRepository.existsById("test-id")).thenReturn(true);

        // Act
        cardService.deleteCard("test-id");

        // Assert
        verify(cardRepository, times(1)).existsById("test-id");
        verify(cardRepository, times(1)).deleteById("test-id");
    }

    @Test
    void getCardsByTag_ShouldReturnCardsWithTag() {
        // Arrange
        when(cardRepository.findByTagsContaining("Test"))
                .thenReturn(Arrays.asList(testCard));

        // Act
        List<CardDTO> result = cardService.getCardsByTag("Test");

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(testCard.getId(), result.get(0).getId());

        verify(cardRepository, times(1)).findByTagsContaining("Test");
    }

    @Test
    void getCardsByTags_ShouldReturnCardsWithAllTags() {
        // Arrange
        when(cardRepository.findAll()).thenReturn(Arrays.asList(testCard));

        // Act
        List<CardDTO> result = cardService.getCardsByTags(Arrays.asList("Test", "Unit"));

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(testCard.getId(), result.get(0).getId());

        verify(cardRepository, times(1)).findAll();
    }

    @Test
    void getAllTags_ShouldReturnAllUniqueTags() {
        // Arrange
        when(cardRepository.findAll()).thenReturn(Arrays.asList(testCard));

        // Act
        List<String> result = cardService.getAllTags();

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        assertTrue(result.contains("Test"));
        assertTrue(result.contains("Unit"));

        verify(cardRepository, times(1)).findAll();
    }
}
