package com.fao.flashcards.api.controller;

import com.fao.flashcards.api.dto.StudyDeckDTO;
import com.fao.flashcards.domain.service.StudyService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Controller für den Lernmodus.
 * Stellt die API-Endpunkte für den Lernmodus bereit.
 */
@RestController
@RequestMapping("/api/v1/study")
@CrossOrigin(origins = "*")
public class StudyController {

    private static final Logger logger = LoggerFactory.getLogger(StudyController.class);
    private final StudyService studyService;

    @Autowired
    public StudyController(StudyService studyService) {
        this.studyService = studyService;
    }

    /**
     * Gibt alle Karten eines Decks in zufälliger Reihenfolge zurück.
     * 
     * @param deckId Die ID des Decks
     * @return Ein StudyDeckDTO mit den Karten in zufälliger Reihenfolge
     */
    @GetMapping("/decks/{deckId}/cards")
    public ResponseEntity<StudyDeckDTO> getRandomizedDeck(@PathVariable String deckId) {
        logger.info("GET /api/v1/study/decks/{}/cards - Hole Karten für Deck in zufälliger Reihenfolge", deckId);
        
        if (deckId == null || deckId.trim().isEmpty()) {
            logger.warn("Ungültige Deck-ID: {}", deckId);
            return ResponseEntity.badRequest().build();
        }
        
        if (!studyService.doesDeckExist(deckId)) {
            logger.warn("Deck mit ID {} nicht gefunden", deckId);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        
        return studyService.getRandomizedDeck(deckId)
                .map(ResponseEntity::ok)
                .orElseGet(() -> {
                    logger.warn("Deck mit ID {} nicht gefunden oder konnte nicht geladen werden", deckId);
                    return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
                });
    }
}