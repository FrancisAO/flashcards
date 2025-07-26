package com.fao.flashcards.api.controller;

import com.fao.flashcards.api.dto.CardDTO;
import com.fao.flashcards.domain.service.CardService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cards")
@CrossOrigin(origins = "*")
public class CardController {

    private static final Logger logger = LoggerFactory.getLogger(CardController.class);
    private final CardService cardService;

    @Autowired
    public CardController(CardService cardService) {
        this.cardService = cardService;
    }

    @GetMapping
    public ResponseEntity<List<CardDTO>> getAllCards() {
        logger.info("GET /api/cards - Fetching all cards");
        List<CardDTO> cards = cardService.getAllCards();
        return ResponseEntity.ok(cards);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CardDTO> getCardById(@PathVariable String id) {
        logger.info("GET /api/cards/{} - Fetching card by ID", id);
        
        if (id == null || id.trim().isEmpty()) {
            logger.warn("Invalid card ID: {}", id);
            return ResponseEntity.badRequest().build();
        }
        
        CardDTO card = cardService.getCardById(id);
        return ResponseEntity.ok(card);
    }

    @PostMapping
    public ResponseEntity<CardDTO> createCard(@Valid @RequestBody CardDTO cardDTO) {
        logger.info("POST /api/cards - Creating new card");
        CardDTO createdCard = cardService.createCard(cardDTO);
        return new ResponseEntity<>(createdCard, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CardDTO> updateCard(@PathVariable String id, @Valid @RequestBody CardDTO cardDTO) {
        logger.info("PUT /api/cards/{} - Updating card", id);
        
        if (id == null || id.trim().isEmpty()) {
            logger.warn("Invalid card ID: {}", id);
            return ResponseEntity.badRequest().build();
        }
        
        CardDTO updatedCard = cardService.updateCard(id, cardDTO);
        return ResponseEntity.ok(updatedCard);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCard(@PathVariable String id) {
        logger.info("DELETE /api/cards/{} - Deleting card", id);
        
        if (id == null || id.trim().isEmpty()) {
            logger.warn("Invalid card ID: {}", id);
            return ResponseEntity.badRequest().build();
        }
        
        cardService.deleteCard(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/tag/{tag}")
    public ResponseEntity<List<CardDTO>> getCardsByTag(@PathVariable String tag) {
        logger.info("GET /api/cards/tag/{} - Fetching cards by tag", tag);
        
        if (tag == null || tag.trim().isEmpty()) {
            logger.warn("Invalid tag: {}", tag);
            return ResponseEntity.badRequest().build();
        }
        
        List<CardDTO> cards = cardService.getCardsByTag(tag);
        return ResponseEntity.ok(cards);
    }
}
