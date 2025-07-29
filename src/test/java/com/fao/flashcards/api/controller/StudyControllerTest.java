package com.fao.flashcards.api.controller;

import com.fao.flashcards.api.dto.StudyCardDTO;
import com.fao.flashcards.api.dto.StudyDeckDTO;
import com.fao.flashcards.domain.service.StudyService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.HashSet;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class StudyControllerTest {

    @Mock
    private StudyService studyService;

    @InjectMocks
    private StudyController studyController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getRandomizedDeck_ValidId_ShouldReturnDeck() {
        // Arrange
        String deckId = "deck-id";
        StudyDeckDTO mockStudyDeck = new StudyDeckDTO();
        mockStudyDeck.setId(deckId);
        mockStudyDeck.setName("Test Deck");
        mockStudyDeck.setDescription("Test Description");
        
        StudyCardDTO card1 = new StudyCardDTO("card-id-1", "Front 1", "Back 1", new HashSet<>());
        StudyCardDTO card2 = new StudyCardDTO("card-id-2", "Front 2", "Back 2", new HashSet<>());
        mockStudyDeck.setCards(Arrays.asList(card1, card2));
        
        when(studyService.doesDeckExist(deckId)).thenReturn(true);
        when(studyService.getRandomizedDeck(deckId)).thenReturn(mockStudyDeck);

        // Act
        ResponseEntity<StudyDeckDTO> response = studyController.getRandomizedDeck(deckId);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(mockStudyDeck, response.getBody());
        verify(studyService, times(1)).doesDeckExist(deckId);
        verify(studyService, times(1)).getRandomizedDeck(deckId);
    }

    @Test
    void getRandomizedDeck_InvalidId_ShouldReturnBadRequest() {
        // Arrange
        String deckId = " ";

        // Act
        ResponseEntity<StudyDeckDTO> response = studyController.getRandomizedDeck(deckId);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        verify(studyService, never()).getRandomizedDeck(anyString());
    }

    @Test
    void getRandomizedDeck_NullId_ShouldReturnBadRequest() {
        // Arrange
        String deckId = null;

        // Act
        ResponseEntity<StudyDeckDTO> response = studyController.getRandomizedDeck(deckId);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        verify(studyService, never()).getRandomizedDeck(anyString());
    }

    @Test
    void getRandomizedDeck_DeckNotFound_ShouldReturnNotFound() {
        // Arrange
        String deckId = "deck-id";
        when(studyService.doesDeckExist(deckId)).thenReturn(false);

        // Act
        ResponseEntity<StudyDeckDTO> response = studyController.getRandomizedDeck(deckId);

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        verify(studyService, times(1)).doesDeckExist(deckId);
        verify(studyService, never()).getRandomizedDeck(anyString());
    }
}