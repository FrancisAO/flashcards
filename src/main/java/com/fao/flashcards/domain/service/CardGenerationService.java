package com.fao.flashcards.domain.service;

import com.fao.flashcards.domain.model.AIGeneratedCard;
import com.fao.flashcards.domain.model.AIGenerationRequest;
import com.fao.flashcards.domain.model.Card;
import com.fao.flashcards.domain.model.DocumentUpload;
import com.fao.flashcards.domain.port.AICardGenerationPort;
import com.fao.flashcards.domain.port.DocumentProcessingPort;
import com.fao.flashcards.domain.repository.AIGeneratedCardRepository;
import com.fao.flashcards.domain.repository.AIGenerationRequestRepository;
import com.fao.flashcards.domain.repository.CardRepository;
import com.fao.flashcards.domain.repository.DocumentUploadRepository;
import jakarta.persistence.EntityNotFoundException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Service für die Karteikartengenerierung.
 * Koordiniert die Dokumentenverarbeitung und KI-Anfragen.
 */
@Service
public class CardGenerationService {
    
    private final AIGenerationRequestRepository generationRequestRepository;
    private final DocumentUploadRepository documentUploadRepository;
    private final AIGeneratedCardRepository generatedCardRepository;
    private final CardRepository cardRepository;
    private final DeckService deckService;
    private final AICardGenerationPort aiCardGenerationPort;
    private final DocumentProcessingPort documentProcessingPort;
    private static final Logger LOGGER = LoggerFactory.getLogger(CardGenerationService.class);

    @Autowired
    public CardGenerationService(
            AIGenerationRequestRepository generationRequestRepository,
            DocumentUploadRepository documentUploadRepository,
            AIGeneratedCardRepository generatedCardRepository,
            CardRepository cardRepository,
            DeckService deckService,
            AICardGenerationPort aiCardGenerationPort,
            DocumentProcessingPort documentProcessingPort) {
        this.generationRequestRepository = generationRequestRepository;
        this.documentUploadRepository = documentUploadRepository;
        this.generatedCardRepository = generatedCardRepository;
        this.cardRepository = cardRepository;
        this.deckService = deckService;
        this.aiCardGenerationPort = aiCardGenerationPort;
        this.documentProcessingPort = documentProcessingPort;
    }
    
    /**
     * Erstellt eine neue Generierungsanfrage für ein Deck.
     * 
     * @param deckId Die ID des Decks
     * @param prompt Hinweise für das KI-Modell
     * @param numberOfCards Die gewünschte Anzahl an Karteikarten (optional)
     * @return Die erstellte Generierungsanfrage
     */
    @Transactional
    public AIGenerationRequest createGenerationRequest(String deckId, String prompt, Integer numberOfCards) {
        AIGenerationRequest request = new AIGenerationRequest();
        request.setDeckId(deckId);
        request.setPrompt(prompt);
        request.setNumberOfCards(numberOfCards);
        request.setStatus(AIGenerationRequest.GenerationStatus.PENDING);
        
        return generationRequestRepository.save(request);
    }
    
    /**
     * Lädt ein Dokument für eine Generierungsanfrage hoch und extrahiert den Text.
     * 
     * @param requestId Die ID der Generierungsanfrage
     * @param file Die hochzuladende Datei
     * @return Das hochgeladene Dokument
     * @throws EntityNotFoundException wenn die Generierungsanfrage nicht gefunden wird
     * @throws IOException wenn beim Lesen der Datei ein Fehler auftritt
     * @throws DocumentProcessingPort.DocumentProcessingException wenn bei der Textextraktion ein Fehler auftritt
     */
    @Transactional
    public DocumentUpload uploadDocument(String requestId, MultipartFile file) 
            throws EntityNotFoundException, IOException, DocumentProcessingPort.DocumentProcessingException {
        
        AIGenerationRequest request = generationRequestRepository.findById(requestId)
                .orElseThrow(() -> new EntityNotFoundException("Generierungsanfrage nicht gefunden: " + requestId));
        
        DocumentUpload document = DocumentUpload.create(
                file.getOriginalFilename(),
                file.getContentType(),
                file.getSize()
        );
        
        // Text aus dem Dokument extrahieren
        String extractedText = documentProcessingPort.extractTextFromDocument(
                file.getInputStream(),
                file.getOriginalFilename()
        );

        LOGGER.debug("Extrahierter Text: {}", extractedText);

        document.setExtractedText(extractedText);
        request.addDocument(document);
        
        generationRequestRepository.save(request);
        return document;
    }
    
    /**
     * Verarbeitet eine Generierungsanfrage und generiert Karteikarten.
     * 
     * @param requestId Die ID der Generierungsanfrage
     * @return Die aktualisierte Generierungsanfrage
     * @throws EntityNotFoundException wenn die Generierungsanfrage nicht gefunden wird
     */
    @Transactional
    public AIGenerationRequest processGenerationRequest(String requestId) throws EntityNotFoundException {
        AIGenerationRequest request = generationRequestRepository.findById(requestId)
                .orElseThrow(() -> new EntityNotFoundException("Generierungsanfrage nicht gefunden: " + requestId));
        
        // Status auf "PROCESSING" setzen
        request.setStatus(AIGenerationRequest.GenerationStatus.PROCESSING);
        generationRequestRepository.save(request);
        
        try {
            // Alle Dokumente für die Anfrage laden
            List<DocumentUpload> documents = documentUploadRepository.findByGenerationRequestId(requestId);
            
            // Text aus allen Dokumenten sammeln
            StringBuilder contentTextBuilder = new StringBuilder();
            for (DocumentUpload document : documents) {
                if (document.getExtractedText() != null && !document.getExtractedText().isEmpty()) {
                    contentTextBuilder.append(document.getExtractedText()).append("\n\n");
                }
            }
            
            String contentText = contentTextBuilder.toString().trim();
            
            // Wenn kein Text vorhanden ist, Fehler werfen
            if (contentText.isEmpty()) {
                throw new IllegalStateException("Kein Text zum Generieren von Karteikarten vorhanden");
            }
            
            // Karteikarten generieren
            List<AICardGenerationPort.GeneratedCard> generatedCards = aiCardGenerationPort.generateCardsFromText(
                    contentText,
                    request.getPrompt(),
                    request.getNumberOfCards()
            );

            LOGGER.debug("Generierte Karten: {}", generatedCards);
            // Generierte Karteikarten speichern
            for (AICardGenerationPort.GeneratedCard generatedCard : generatedCards) {
                AIGeneratedCard card = AIGeneratedCard.create(generatedCard.getFront(), generatedCard.getBack());
                request.addGeneratedCard(card);
            }
            
            // Status auf "COMPLETED" setzen
            request.setStatus(AIGenerationRequest.GenerationStatus.COMPLETED);
            
        } catch (Exception e) {
            // Bei Fehler Status auf "FAILED" setzen
            request.setStatus(AIGenerationRequest.GenerationStatus.FAILED);
            throw new RuntimeException("Fehler bei der Karteikartengenerierung: " + e.getMessage(), e);
        }
        
        return generationRequestRepository.save(request);
    }
    
    /**
     * Ruft eine Generierungsanfrage ab.
     * 
     * @param requestId Die ID der Generierungsanfrage
     * @return Die Generierungsanfrage
     * @throws EntityNotFoundException wenn die Generierungsanfrage nicht gefunden wird
     */
    public AIGenerationRequest getGenerationRequest(String requestId) throws EntityNotFoundException {
        return generationRequestRepository.findById(requestId)
                .orElseThrow(() -> new EntityNotFoundException("Generierungsanfrage nicht gefunden: " + requestId));
    }
    
    /**
     * Ruft alle generierten Karteikarten für eine Anfrage ab.
     * 
     * @param requestId Die ID der Generierungsanfrage
     * @return Eine Liste von generierten Karteikarten
     * @throws EntityNotFoundException wenn die Generierungsanfrage nicht gefunden wird
     */
    public List<AIGeneratedCard> getGeneratedCards(String requestId) throws EntityNotFoundException {
        if (!generationRequestRepository.existsById(requestId)) {
            throw new EntityNotFoundException("Generierungsanfrage nicht gefunden: " + requestId);
        }
        
        return generatedCardRepository.findByGenerationRequestId(requestId);
    }
    
    /**
     * Aktualisiert eine generierte Karteikarte.
     * 
     * @param requestId Die ID der Generierungsanfrage
     * @param cardId Die ID der generierten Karteikarte
     * @param front Die neue Vorderseite
     * @param back Die neue Rückseite
     * @return Die aktualisierte Karteikarte
     * @throws EntityNotFoundException wenn die Karteikarte nicht gefunden wird
     */
    @Transactional
    public AIGeneratedCard updateGeneratedCard(String requestId, String cardId, String front, String back) 
            throws EntityNotFoundException {
        
        AIGeneratedCard card = generatedCardRepository.findById(cardId)
                .orElseThrow(() -> new EntityNotFoundException("Generierte Karteikarte nicht gefunden: " + cardId));
        
        // Prüfen, ob die Karteikarte zur Anfrage gehört
        if (!card.getGenerationRequest().getId().equals(requestId)) {
            throw new IllegalArgumentException("Die Karteikarte gehört nicht zur angegebenen Generierungsanfrage");
        }
        
        card.setFront(front);
        card.setBack(back);
        card.markAsEdited();
        
        return generatedCardRepository.save(card);
    }
    
    /**
     * Speichert alle generierten Karteikarten im Deck.
     * 
     * @param deckId Die ID des Decks
     * @param requestId Die ID der Generierungsanfrage
     * @return Eine Liste der gespeicherten Karteikarten
     * @throws EntityNotFoundException wenn die Generierungsanfrage oder das Deck nicht gefunden wird
     */
    @Transactional
    public List<Card> saveGeneratedCards(String deckId, String requestId) throws EntityNotFoundException {
        AIGenerationRequest request = generationRequestRepository.findById(requestId)
                .orElseThrow(() -> new EntityNotFoundException("Generierungsanfrage nicht gefunden: " + requestId));
        
        // Prüfen, ob die Anfrage zum Deck gehört
        if (!request.getDeckId().equals(deckId)) {
            throw new IllegalArgumentException("Die Generierungsanfrage gehört nicht zum angegebenen Deck");
        }
        
        // Alle nicht gespeicherten Karteikarten laden
        List<AIGeneratedCard> generatedCards = generatedCardRepository.findByGenerationRequestIdAndSavedFalse(requestId);
        List<Card> savedCards = new ArrayList<>();
        
        // Karteikarten speichern
        for (AIGeneratedCard generatedCard : generatedCards) {
            Card card = new Card();
            card.setFront(generatedCard.getFront());
            card.setBack(generatedCard.getBack());
            
            Card savedCard = cardRepository.save(card);
            deckService.addCardToDeck(deckId, savedCard.getId());
            
            // Generierte Karteikarte als gespeichert markieren
            generatedCard.markAsSaved(savedCard.getId());
            generatedCardRepository.save(generatedCard);
            
            savedCards.add(savedCard);
        }
        
        return savedCards;
    }
}