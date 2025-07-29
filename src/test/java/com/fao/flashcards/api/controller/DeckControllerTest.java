package com.fao.flashcards.api.controller;

import com.fao.flashcards.api.dto.DeckCardDTO;
import com.fao.flashcards.api.dto.DeckDTO;
import com.fao.flashcards.api.dto.DeckWithCardsDTO;
import com.fao.flashcards.domain.service.DeckService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class DeckControllerTest {

    @Mock
    private DeckService deckService;

    @InjectMocks
    private DeckController deckController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAllDecks() {
        List<DeckDTO> mockDecks = Arrays.asList(
            new DeckDTO("1", "Deck1", "Description1", new HashSet<>(), null, null),
            new DeckDTO("2", "Deck2", "Description2", new HashSet<>(), null, null)
        );
        when(deckService.getAllDecks()).thenReturn(mockDecks);

        ResponseEntity<List<DeckDTO>> response = deckController.getAllDecks();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(mockDecks, response.getBody());
    }

    @Test
    void testGetDeckById_ValidId() {
        String deckId = "1";
        DeckDTO mockDeck = new DeckDTO(deckId, "Deck1", "Description1", new HashSet<>(), null, null);
        when(deckService.doesDeckExist(deckId)).thenReturn(true);
        when(deckService.getDeckById(deckId)).thenReturn(mockDeck);

        ResponseEntity<DeckDTO> response = deckController.getDeckById(deckId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(mockDeck, response.getBody());
    }

    @Test
    void testGetDeckById_InvalidId() {
        String deckId = " ";

        ResponseEntity<DeckDTO> response = deckController.getDeckById(deckId);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    void testGetDeckById_NullId() {
        String deckId = null;

        ResponseEntity<DeckDTO> response = deckController.getDeckById(deckId);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    void testGetDeckById_DeckNotFound() {
        String deckId = "1";
        when(deckService.doesDeckExist(deckId)).thenReturn(false);

        ResponseEntity<DeckDTO> response = deckController.getDeckById(deckId);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void testAddCardToDeck() {
        DeckCardDTO deckCardDTO = new DeckCardDTO("1", "101");

        ResponseEntity<Void> response = deckController.addCardToDeck(deckCardDTO);

        verify(deckService, times(1)).addCardToDeck(deckCardDTO.getDeckId(), deckCardDTO.getCardId());
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    }

    @Test
    void testRemoveCardFromDeck_ValidIds() {
        String deckId = "1";
        String cardId = "101";
        when(deckService.doesDeckExist(deckId)).thenReturn(true);

        ResponseEntity<Void> response = deckController.removeCardFromDeck(deckId, cardId);

        verify(deckService, times(1)).removeCardFromDeck(deckId, cardId);
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    }

    @Test
    void testRemoveCardFromDeck_InvalidDeckId() {
        String deckId = " ";
        String cardId = "101";

        ResponseEntity<Void> response = deckController.removeCardFromDeck(deckId, cardId);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    void testRemoveCardFromDeck_NullDeckId() {
        String deckId = null;
        String cardId = "101";

        ResponseEntity<Void> response = deckController.removeCardFromDeck(deckId, cardId);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    void testRemoveCardFromDeck_InvalidCardId() {
        String deckId = "1";
        String cardId = " ";

        ResponseEntity<Void> response = deckController.removeCardFromDeck(deckId, cardId);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    void testRemoveCardFromDeck_NullCardId() {
        String deckId = "1";
        String cardId = null;

        ResponseEntity<Void> response = deckController.removeCardFromDeck(deckId, cardId);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    void testRemoveCardFromDeck_DeckNotFound() {
        String deckId = "1";
        String cardId = "101";
        when(deckService.doesDeckExist(deckId)).thenReturn(false);

        ResponseEntity<Void> response = deckController.removeCardFromDeck(deckId, cardId);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void testGetDeckWithCards_ValidId() {
        String deckId = "1";
        DeckWithCardsDTO mockDeckWithCards = new DeckWithCardsDTO(
            deckId, "Deck1", "Description1", new HashSet<>(), Arrays.asList(), null, null
        );
        when(deckService.doesDeckExist(deckId)).thenReturn(true);
        when(deckService.getDeckWithCards(deckId)).thenReturn(mockDeckWithCards);

        ResponseEntity<DeckWithCardsDTO> response = deckController.getDeckWithCards(deckId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(mockDeckWithCards, response.getBody());
    }

    @Test
    void testGetDeckWithCards_InvalidId() {
        String deckId = " ";

        ResponseEntity<DeckWithCardsDTO> response = deckController.getDeckWithCards(deckId);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    void testGetDeckWithCards_NullId() {
        String deckId = null;

        ResponseEntity<DeckWithCardsDTO> response = deckController.getDeckWithCards(deckId);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    void testGetDeckWithCards_DeckNotFound() {
        String deckId = "1";
        when(deckService.doesDeckExist(deckId)).thenReturn(false);

        ResponseEntity<DeckWithCardsDTO> response = deckController.getDeckWithCards(deckId);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void testCreateDeck_ValidDeck() {
        DeckDTO newDeck = new DeckDTO(null, "Deck1", "Description1", new HashSet<>(), null, null);
        DeckDTO createdDeck = new DeckDTO("1", "Deck1", "Description1", new HashSet<>(), null, null);
        when(deckService.createDeck(newDeck)).thenReturn(createdDeck);

        ResponseEntity<DeckDTO> response = deckController.createDeck(newDeck);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(createdDeck, response.getBody());
    }

    @Test
    void testCreateDeck_InvalidDeck() {
        DeckDTO invalidDeck = new DeckDTO(null, "", "Description1", new HashSet<>(), null, null);

        ResponseEntity<DeckDTO> response = deckController.createDeck(invalidDeck);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    void testCreateDeck_NullName() {
        DeckDTO invalidDeck = new DeckDTO(null, null, "Description1", new HashSet<>(), null, null);

        ResponseEntity<DeckDTO> response = deckController.createDeck(invalidDeck);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    void testUpdateDeck_ValidDeck() {
        String deckId = "1";
        DeckDTO updatedDeck = new DeckDTO(deckId, "Updated Deck", "Updated Description", new HashSet<>(), null, null);
        when(deckService.doesDeckExist(deckId)).thenReturn(true);
        when(deckService.updateDeck(deckId, updatedDeck)).thenReturn(updatedDeck);

        ResponseEntity<DeckDTO> response = deckController.updateDeck(deckId, updatedDeck);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(updatedDeck, response.getBody());
    }

    @Test
    void testUpdateDeck_InvalidDeckId() {
        String deckId = " ";
        DeckDTO updatedDeck = new DeckDTO(deckId, "Updated Deck", "Updated Description", new HashSet<>(), null, null);

        ResponseEntity<DeckDTO> response = deckController.updateDeck(deckId, updatedDeck);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    void testUpdateDeck_NullId() {
        String deckId = null;
        DeckDTO updatedDeck = new DeckDTO(deckId, "Updated Deck", "Updated Description", new HashSet<>(), null, null);

        ResponseEntity<DeckDTO> response = deckController.updateDeck(deckId, updatedDeck);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    void testUpdateDeck_DeckNotFound() {
        String deckId = "1";
        DeckDTO updatedDeck = new DeckDTO(deckId, "Updated Deck", "Updated Description", new HashSet<>(), null, null);
        when(deckService.doesDeckExist(deckId)).thenReturn(false);

        ResponseEntity<DeckDTO> response = deckController.updateDeck(deckId, updatedDeck);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void testDeleteDeck_ValidId() {
        String deckId = "1";
        when(deckService.doesDeckExist(deckId)).thenReturn(true);

        ResponseEntity<Void> response = deckController.deleteDeck(deckId);

        verify(deckService, times(1)).deleteDeck(deckId);
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    }

    @Test
    void testDeleteDeck_InvalidId() {
        String deckId = " ";

        ResponseEntity<Void> response = deckController.deleteDeck(deckId);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    void testDeleteDeck_NullId() {
        String deckId = null;

        ResponseEntity<Void> response = deckController.deleteDeck(deckId);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    void testDeleteDeck_DeckNotFound() {
        String deckId = "1";
        when(deckService.doesDeckExist(deckId)).thenReturn(false);

        ResponseEntity<Void> response = deckController.deleteDeck(deckId);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void testGetDecksByTag_ValidTag() {
        String tag = "tag1";
        List<DeckDTO> mockDecks = Arrays.asList(
            new DeckDTO("1", "Deck1", "Description1", new HashSet<>(), null, null),
            new DeckDTO("2", "Deck2", "Description2", new HashSet<>(), null, null)
        );
        when(deckService.getDecksByTag(tag)).thenReturn(mockDecks);

        ResponseEntity<List<DeckDTO>> response = deckController.getDecksByTag(tag);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(mockDecks, response.getBody());
    }

    @Test
    void testGetDecksByTag_InvalidTag() {
        String tag = " ";

        ResponseEntity<List<DeckDTO>> response = deckController.getDecksByTag(tag);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    void testGetDecksByTag_NullTag() {
        String tag = null;

        ResponseEntity<List<DeckDTO>> response = deckController.getDecksByTag(tag);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }
}
