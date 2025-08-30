package com.fao.flashcards.api.controller;

import com.fao.flashcards.api.dto.AIGeneratedCardDTO;
import com.fao.flashcards.api.dto.AIGenerationRequestDTO;
import com.fao.flashcards.api.dto.CardDTO;
import com.fao.flashcards.api.dto.CardGenerationRequestDTO;
import com.fao.flashcards.api.dto.DocumentUploadDTO;
import com.fao.flashcards.domain.model.AIGeneratedCard;
import com.fao.flashcards.domain.model.AIGenerationRequest;
import com.fao.flashcards.domain.model.Card;
import com.fao.flashcards.domain.model.DocumentUpload;
import com.fao.flashcards.domain.port.DocumentProcessingPort;
import com.fao.flashcards.domain.service.CardGenerationService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Controller für die Karteikartengenerierung.
 * Stellt Endpunkte für die Karteikartengenerierung bereit.
 */
@RestController
@RequestMapping("/api/decks/{deckId}/generate")
public class CardGenerationController {

    private final CardGenerationService cardGenerationService;

    @Autowired
    public CardGenerationController(CardGenerationService cardGenerationService) {
        this.cardGenerationService = cardGenerationService;
    }

    /**
     * Startet eine neue Karteikartengenerierung für ein Deck.
     * 
     * @param deckId Die ID des Decks
     * @param requestDTO Die Anfrage mit Hinweisen für die KI
     * @return Die erstellte Generierungsanfrage
     */
    @PostMapping
    public ResponseEntity<AIGenerationRequestDTO> startGeneration(
            @PathVariable String deckId,
            @RequestBody CardGenerationRequestDTO requestDTO) {
        
        AIGenerationRequest generationRequest = cardGenerationService.createGenerationRequest(
                deckId, 
                requestDTO.getPrompt(), 
                requestDTO.getNumberOfCards()
        );
        
        return new ResponseEntity<>(mapToAIGenerationRequestDTO(generationRequest), HttpStatus.CREATED);
    }

    /**
     * Lädt ein Dokument für eine Generierungsanfrage hoch.
     * 
     * @param deckId Die ID des Decks
     * @param requestId Die ID der Generierungsanfrage
     * @param file Die hochzuladende Datei
     * @return Das hochgeladene Dokument
     */
    @PostMapping("/{requestId}/documents")
    public ResponseEntity<?> uploadDocument(
            @PathVariable String deckId,
            @PathVariable String requestId,
            @RequestParam("file") MultipartFile file) {
        
        try {
            DocumentUpload uploadedDocument = cardGenerationService.uploadDocument(requestId, file);
            return new ResponseEntity<>(mapToDocumentUploadDTO(uploadedDocument), HttpStatus.CREATED);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Fehler beim Lesen der Datei: " + e.getMessage());
        } catch (DocumentProcessingPort.DocumentProcessingException e) {
            return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY)
                    .body("Fehler bei der Textextraktion: " + e.getMessage());
        }
    }

    /**
     * Startet den Generierungsprozess für eine Anfrage.
     * 
     * @param deckId Die ID des Decks
     * @param requestId Die ID der Generierungsanfrage
     * @return Die aktualisierte Generierungsanfrage
     */
    @PostMapping("/{requestId}/process")
    public ResponseEntity<?> processGeneration(
            @PathVariable String deckId,
            @PathVariable String requestId) {
        
        try {
            AIGenerationRequest updatedRequest = cardGenerationService.processGenerationRequest(requestId);
            return ResponseEntity.ok(mapToAIGenerationRequestDTO(updatedRequest));
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Fehler bei der Karteikartengenerierung: " + e.getMessage());
        }
    }

    /**
     * Ruft den Status einer Generierungsanfrage ab.
     * 
     * @param deckId Die ID des Decks
     * @param requestId Die ID der Generierungsanfrage
     * @return Die Generierungsanfrage mit Status
     */
    @GetMapping("/{requestId}")
    public ResponseEntity<?> getGenerationStatus(
            @PathVariable String deckId,
            @PathVariable String requestId) {
        
        try {
            AIGenerationRequest generationRequest = cardGenerationService.getGenerationRequest(requestId);
            return ResponseEntity.ok(mapToAIGenerationRequestDTO(generationRequest));
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Ruft die generierten Karteikarten für eine Anfrage ab.
     * 
     * @param deckId Die ID des Decks
     * @param requestId Die ID der Generierungsanfrage
     * @return Die Liste der generierten Karteikarten
     */
    @GetMapping("/{requestId}/cards")
    public ResponseEntity<?> getGeneratedCards(
            @PathVariable String deckId,
            @PathVariable String requestId) {
        
        try {
            List<AIGeneratedCard> generatedCards = cardGenerationService.getGeneratedCards(requestId);
            List<AIGeneratedCardDTO> cardDTOs = generatedCards.stream()
                    .map(this::mapToAIGeneratedCardDTO)
                    .collect(Collectors.toList());
            
            return ResponseEntity.ok(cardDTOs);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Aktualisiert eine generierte Karteikarte.
     * 
     * @param deckId Die ID des Decks
     * @param requestId Die ID der Generierungsanfrage
     * @param cardId Die ID der generierten Karteikarte
     * @param cardDTO Die aktualisierten Daten der Karteikarte
     * @return Die aktualisierte Karteikarte
     */
    @PutMapping("/{requestId}/cards/{cardId}")
    public ResponseEntity<?> updateGeneratedCard(
            @PathVariable String deckId,
            @PathVariable String requestId,
            @PathVariable String cardId,
            @RequestBody AIGeneratedCardDTO cardDTO) {
        
        try {
            AIGeneratedCard updatedCard = cardGenerationService.updateGeneratedCard(
                    requestId,
                    cardId,
                    cardDTO.getFront(),
                    cardDTO.getBack()
            );
            
            return ResponseEntity.ok(mapToAIGeneratedCardDTO(updatedCard));
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    /**
     * Speichert alle generierten Karteikarten im Deck.
     * 
     * @param deckId Die ID des Decks
     * @param requestId Die ID der Generierungsanfrage
     * @return Die Liste der gespeicherten Karteikarten
     */
    @PostMapping("/{requestId}/save")
    public ResponseEntity<?> saveGeneratedCards(
            @PathVariable String deckId,
            @PathVariable String requestId) {
        
        try {
            List<Card> savedCards = cardGenerationService.saveGeneratedCards(deckId, requestId);
            List<CardDTO> cardDTOs = savedCards.stream()
                    .map(this::mapToCardDTO)
                    .collect(Collectors.toList());
            
            return ResponseEntity.ok(cardDTOs);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    
    /**
     * Konvertiert eine AIGenerationRequest-Entität in ein AIGenerationRequestDTO.
     * 
     * @param request Die zu konvertierende Entität
     * @return Das konvertierte DTO
     */
    private AIGenerationRequestDTO mapToAIGenerationRequestDTO(AIGenerationRequest request) {
        AIGenerationRequestDTO dto = new AIGenerationRequestDTO();
        dto.setId(request.getId());
        dto.setDeckId(request.getDeckId());
        dto.setPrompt(request.getPrompt());
        dto.setNumberOfCards(request.getNumberOfCards());
        dto.setStatus(request.getStatus().name());
        dto.setDocumentCount(request.getDocuments().size());
        dto.setGeneratedCardCount(request.getGeneratedCards().size());
        dto.setCreatedAt(request.getCreatedAt());
        dto.setUpdatedAt(request.getUpdatedAt());
        return dto;
    }
    
    /**
     * Konvertiert eine DocumentUpload-Entität in ein DocumentUploadDTO.
     * 
     * @param document Die zu konvertierende Entität
     * @return Das konvertierte DTO
     */
    private DocumentUploadDTO mapToDocumentUploadDTO(DocumentUpload document) {
        DocumentUploadDTO dto = new DocumentUploadDTO();
        dto.setId(document.getId());
        dto.setOriginalFilename(document.getOriginalFilename());
        dto.setContentType(document.getContentType());
        dto.setFileSize(document.getFileSize());
        dto.setUploadedAt(document.getUploadedAt());
        return dto;
    }
    
    /**
     * Konvertiert eine AIGeneratedCard-Entität in ein AIGeneratedCardDTO.
     * 
     * @param card Die zu konvertierende Entität
     * @return Das konvertierte DTO
     */
    private AIGeneratedCardDTO mapToAIGeneratedCardDTO(AIGeneratedCard card) {
        AIGeneratedCardDTO dto = new AIGeneratedCardDTO();
        dto.setId(card.getId());
        dto.setFront(card.getFront());
        dto.setBack(card.getBack());
        dto.setEdited(card.isEdited());
        dto.setSaved(card.isSaved());
        dto.setCardId(card.getCardId());
        dto.setCreatedAt(card.getCreatedAt());
        dto.setUpdatedAt(card.getUpdatedAt());
        return dto;
    }
    
    /**
     * Konvertiert eine Card-Entität in ein CardDTO.
     * 
     * @param card Die zu konvertierende Entität
     * @return Das konvertierte DTO
     */
    private CardDTO mapToCardDTO(Card card) {
        CardDTO dto = new CardDTO();
        dto.setId(card.getId());
        dto.setFront(card.getFront());
        dto.setBack(card.getBack());
        // Weitere Felder wie Tags könnten hier gesetzt werden
        return dto;
    }
}