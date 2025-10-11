package com.fao.flashcards.cards.adapter.rest;

import com.fao.flashcards.cards.application.port.dto.CardDTO;
import com.fao.flashcards.cards.application.port.in.CardInputPort;

import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/cards")
@CrossOrigin(origins = "*")
public class CardController {

    private static final Logger logger = LoggerFactory.getLogger(CardController.class);
    private final CardInputPort cardService;

    @Autowired
    public CardController(CardInputPort cardService) {
        this.cardService = cardService;
    }

    @GetMapping
    public ResponseEntity<List<CardDTO>> getAllCards() {
        logger.info("GET /api/cards - Fetching all cards");
        List<CardDTO> cards = cardService.getAllCards();
        return ResponseEntity.ok(cards);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CardDTO> getCardById(@PathVariable("id") String id) {
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
    public ResponseEntity<CardDTO> updateCard(@PathVariable("id") String id, @Valid @RequestBody CardDTO cardDTO) {
        logger.info("PUT /api/cards/{} - Updating card", id);
        
        if (id == null || id.trim().isEmpty()) {
            logger.warn("Invalid card ID: {}", id);
            return ResponseEntity.badRequest().build();
        }
        
        CardDTO updatedCard = cardService.updateCard(id, cardDTO);
        return ResponseEntity.ok(updatedCard);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCard(@PathVariable("id") String id) {
        logger.info("DELETE /api/cards/{} - Deleting card", id);
        
        if (id == null || id.trim().isEmpty()) {
            logger.warn("Invalid card ID: {}", id);
            return ResponseEntity.badRequest().build();
        }
        
        cardService.deleteCard(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/tag/{tag}")
    public ResponseEntity<List<CardDTO>> getCardsByTag(@PathVariable("tag") String tag) {
        logger.info("GET /api/cards/tag/{} - Fetching cards by tag", tag);
        
        if (tag == null || tag.trim().isEmpty()) {
            logger.warn("Invalid tag: {}", tag);
            return ResponseEntity.badRequest().build();
        }
        
        List<CardDTO> cards = cardService.getCardsByTag(tag);
        return ResponseEntity.ok(cards);
    }
    
    @GetMapping("/tags")
    public ResponseEntity<List<String>> getAllTags() {
        logger.info("GET /api/cards/tags - Fetching all available tags");
        List<String> tags = cardService.getAllTags();
        return ResponseEntity.ok(tags);
    }
    
    @PostMapping("/search")
    public ResponseEntity<List<CardDTO>> getCardsByTags(@RequestBody List<String> tags) {
        logger.info("POST /api/cards/search - Fetching cards by tags: {}", tags);
        
        if (tags == null) {
            logger.warn("Invalid tags list: null");
            return ResponseEntity.badRequest().build();
        }
        
        List<CardDTO> cards = cardService.getCardsByTags(tags);
        return ResponseEntity.ok(cards);
    }
}
