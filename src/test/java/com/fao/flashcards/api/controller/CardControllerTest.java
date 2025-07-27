package com.fao.flashcards.api.controller;

import com.fao.flashcards.api.dto.CardDTO;
import com.fao.flashcards.domain.service.CardService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CardControllerTest {

    @Mock
    private CardService cardService;

    @InjectMocks
    private CardController cardController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAllCards() {
        List<CardDTO> mockCards = Arrays.asList(new CardDTO(), new CardDTO());
        when(cardService.getAllCards()).thenReturn(mockCards);

        ResponseEntity<List<CardDTO>> response = cardController.getAllCards();

        assertEquals(200, response.getStatusCode().value());
        assertEquals(mockCards, response.getBody());
    }

    @Test
    void testGetCardById() {
        String cardId = "1";
        CardDTO mockCard = new CardDTO();
        when(cardService.getCardById(cardId)).thenReturn(mockCard);

        ResponseEntity<CardDTO> response = cardController.getCardById(cardId);

        assertEquals(200, response.getStatusCode().value());
        assertEquals(mockCard, response.getBody());
    }

    @Test
    void testGetCardById_NullId() {
        ResponseEntity<CardDTO> response = cardController.getCardById(null);

        assertEquals(400, response.getStatusCode().value());
        assertNull(response.getBody());
    }

    @Test
    void testGetCardById_EmptyId() {
        ResponseEntity<CardDTO> response = cardController.getCardById(" ");

        assertEquals(400, response.getStatusCode().value());
        assertNull(response.getBody());
    }

    @Test
    void testCreateCard() {
        CardDTO newCard = new CardDTO();
        CardDTO createdCard = new CardDTO();
        when(cardService.createCard(newCard)).thenReturn(createdCard);

        ResponseEntity<CardDTO> response = cardController.createCard(newCard);

        assertEquals(201, response.getStatusCode().value());
        assertEquals(createdCard, response.getBody());
    }

    @Test
    void testUpdateCard() {
        String cardId = "1";
        CardDTO updatedCard = new CardDTO();
        when(cardService.updateCard(eq(cardId), any(CardDTO.class))).thenReturn(updatedCard);

        ResponseEntity<CardDTO> response = cardController.updateCard(cardId, new CardDTO());

        assertEquals(200, response.getStatusCode().value());
        assertEquals(updatedCard, response.getBody());
    }

    @Test
    void testUpdateCard_NullId() {
        ResponseEntity<CardDTO> response = cardController.updateCard(null, new CardDTO());

        assertEquals(400, response.getStatusCode().value());
        assertNull(response.getBody());
    }

    @Test
    void testUpdateCard_EmptyId() {
        ResponseEntity<CardDTO> response = cardController.updateCard(" ", new CardDTO());

        assertEquals(400, response.getStatusCode().value());
        assertNull(response.getBody());
    }

    @Test
    void testDeleteCard() {
        String cardId = "1";
        doNothing().when(cardService).deleteCard(cardId);

        ResponseEntity<Void> response = cardController.deleteCard(cardId);

        assertEquals(204, response.getStatusCode().value());
        assertNull(response.getBody());
    }

    @Test
    void testDeleteCard_NullId() {
        ResponseEntity<Void> response = cardController.deleteCard(null);

        assertEquals(400, response.getStatusCode().value());
        assertNull(response.getBody());
    }

    @Test
    void testDeleteCard_EmptyId() {
        ResponseEntity<Void> response = cardController.deleteCard(" ");

        assertEquals(400, response.getStatusCode().value());
        assertNull(response.getBody());
    }

    @Test
    void testGetCardsByTag() {
        String tag = "tag1";
        List<CardDTO> mockCards = Arrays.asList(new CardDTO(), new CardDTO());
        when(cardService.getCardsByTag(tag)).thenReturn(mockCards);

        ResponseEntity<List<CardDTO>> response = cardController.getCardsByTag(tag);

        assertEquals(200, response.getStatusCode().value());
        assertEquals(mockCards, response.getBody());
    }

    @Test
    void testGetCardsByTag_NullTag() {
        ResponseEntity<List<CardDTO>> response = cardController.getCardsByTag(null);

        assertEquals(400, response.getStatusCode().value());
        assertNull(response.getBody());
    }

    @Test
    void testGetCardsByTag_EmptyTag() {
        ResponseEntity<List<CardDTO>> response = cardController.getCardsByTag(" ");

        assertEquals(400, response.getStatusCode().value());
        assertNull(response.getBody());
    }

    @Test
    void testGetAllTags() {
        List<String> mockTags = Arrays.asList("tag1", "tag2");
        when(cardService.getAllTags()).thenReturn(mockTags);

        ResponseEntity<List<String>> response = cardController.getAllTags();

        assertEquals(200, response.getStatusCode().value());
        assertEquals(mockTags, response.getBody());
    }

    @Test
    void testGetCardsByTags() {
        List<String> tags = Arrays.asList("tag1", "tag2");
        List<CardDTO> mockCards = Arrays.asList(new CardDTO(), new CardDTO());
        when(cardService.getCardsByTags(tags)).thenReturn(mockCards);

        ResponseEntity<List<CardDTO>> response = cardController.getCardsByTags(tags);

        assertEquals(200, response.getStatusCode().value());
        assertEquals(mockCards, response.getBody());
    }

    @Test
    void testGetCardsByTags_NullTags() {
        ResponseEntity<List<CardDTO>> response = cardController.getCardsByTags(null);

        assertEquals(400, response.getStatusCode().value());
        assertNull(response.getBody());
    }
}
