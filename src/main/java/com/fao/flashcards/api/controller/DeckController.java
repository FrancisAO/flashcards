package com.fao.flashcards.api.controller;

import com.fao.flashcards.api.dto.DeckCardDTO;
import com.fao.flashcards.api.dto.DeckDTO;
import com.fao.flashcards.api.dto.DeckWithCardsDTO;
import com.fao.flashcards.domain.service.DeckService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/decks")
@CrossOrigin(origins = "*")
public class DeckController {

    private static final Logger logger = LoggerFactory.getLogger(DeckController.class);
    private final DeckService deckService;

    @Autowired
    public DeckController(DeckService deckService) {
        this.deckService = deckService;
    }

    @GetMapping
    public ResponseEntity<List<DeckDTO>> getAllDecks() {
        logger.info("GET /api/decks - Fetching all decks");
        List<DeckDTO> decks = deckService.getAllDecks();
        return ResponseEntity.ok(decks);
    }

    @GetMapping("/{id}")
    public ResponseEntity<DeckDTO> getDeckById(@PathVariable String id) {
        logger.info("GET /api/decks/{} - Fetching deck by ID", id);
        
        if (id == null || id.trim().isEmpty()) {
            logger.warn("Invalid deck ID: {}", id);
            return ResponseEntity.badRequest().build();
        }
        
        DeckDTO deck = deckService.getDeckById(id);
        return ResponseEntity.ok(deck);
    }

    @GetMapping("/{id}/cards")
    public ResponseEntity<DeckWithCardsDTO> getDeckWithCards(@PathVariable String id) {
        logger.info("GET /api/decks/{}/cards - Fetching deck with cards", id);
        
        if (id == null || id.trim().isEmpty()) {
            logger.warn("Invalid deck ID: {}", id);
            return ResponseEntity.badRequest().build();
        }
        
        DeckWithCardsDTO deck = deckService.getDeckWithCards(id);
        return ResponseEntity.ok(deck);
    }

    @PostMapping
    public ResponseEntity<DeckDTO> createDeck(@Valid @RequestBody DeckDTO deckDTO) {
        logger.info("POST /api/decks - Creating new deck");
        DeckDTO createdDeck = deckService.createDeck(deckDTO);
        return new ResponseEntity<>(createdDeck, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<DeckDTO> updateDeck(@PathVariable String id, @Valid @RequestBody DeckDTO deckDTO) {
        logger.info("PUT /api/decks/{} - Updating deck", id);
        
        if (id == null || id.trim().isEmpty()) {
            logger.warn("Invalid deck ID: {}", id);
            return ResponseEntity.badRequest().build();
        }
        
        DeckDTO updatedDeck = deckService.updateDeck(id, deckDTO);
        return ResponseEntity.ok(updatedDeck);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDeck(@PathVariable String id) {
        logger.info("DELETE /api/decks/{} - Deleting deck", id);
        
        if (id == null || id.trim().isEmpty()) {
            logger.warn("Invalid deck ID: {}", id);
            return ResponseEntity.badRequest().build();
        }
        
        deckService.deleteDeck(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/add-card")
    public ResponseEntity<Void> addCardToDeck(@Valid @RequestBody DeckCardDTO deckCardDTO) {
        logger.info("POST /api/decks/add-card - Adding card to deck");
        deckService.addCardToDeck(deckCardDTO.getDeckId(), deckCardDTO.getCardId());
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{deckId}/cards/{cardId}")
    public ResponseEntity<Void> removeCardFromDeck(@PathVariable String deckId, @PathVariable String cardId) {
        logger.info("DELETE /api/decks/{}/cards/{} - Removing card from deck", deckId, cardId);
        
        if (deckId == null || deckId.trim().isEmpty()) {
            logger.warn("Invalid deck ID: {}", deckId);
            return ResponseEntity.badRequest().build();
        }
        
        if (cardId == null || cardId.trim().isEmpty()) {
            logger.warn("Invalid card ID: {}", cardId);
            return ResponseEntity.badRequest().build();
        }
        
        deckService.removeCardFromDeck(deckId, cardId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/tag/{tag}")
    public ResponseEntity<List<DeckDTO>> getDecksByTag(@PathVariable String tag) {
        logger.info("GET /api/decks/tag/{} - Fetching decks by tag", tag);
        
        if (tag == null || tag.trim().isEmpty()) {
            logger.warn("Invalid tag: {}", tag);
            return ResponseEntity.badRequest().build();
        }
        
        List<DeckDTO> decks = deckService.getDecksByTag(tag);
        return ResponseEntity.ok(decks);
    }
}
