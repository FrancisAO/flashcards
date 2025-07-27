package com.fao.flashcards.domain.service;

import com.fao.flashcards.api.dto.CardDTO;
import com.fao.flashcards.api.dto.DeckDTO;
import com.fao.flashcards.api.dto.DeckWithCardsDTO;
import com.fao.flashcards.domain.model.Card;
import com.fao.flashcards.domain.model.Deck;
import com.fao.flashcards.domain.model.DeckCard;
import com.fao.flashcards.domain.repository.CardRepository;
import com.fao.flashcards.domain.repository.DeckCardRepository;
import com.fao.flashcards.domain.repository.DeckRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Service
public class DeckService {
    
    private final DeckRepository deckRepository;
    private final CardRepository cardRepository;
    private final DeckCardRepository deckCardRepository;
    private static final DateTimeFormatter formatter = DateTimeFormatter.ISO_DATE_TIME;
    
    @Autowired
    public DeckService(DeckRepository deckRepository, CardRepository cardRepository, DeckCardRepository deckCardRepository) {
        this.deckRepository = deckRepository;
        this.cardRepository = cardRepository;
        this.deckCardRepository = deckCardRepository;
    }
    
    @Transactional(readOnly = true)
    public List<DeckDTO> getAllDecks() {
        return deckRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    @Transactional(readOnly = true)
    public DeckDTO getDeckById(String id) {
        assert id != null : "Deck ID darf nicht null sein";
        assert !id.trim().isEmpty() : "Deck ID darf nicht leer sein";
        Deck deck = deckRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Deck mit ID " + id + " wurde nicht gefunden"));
        return convertToDTO(deck);
    }
    
    @Transactional(readOnly = true)
    public DeckWithCardsDTO getDeckWithCards(String id) {
        assert id != null : "Deck ID darf nicht null sein";
        assert !id.trim().isEmpty() : "Deck ID darf nicht leer sein";
        Deck deck = deckRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Deck mit ID " + id + " wurde nicht gefunden"));
        
        List<DeckCard> deckCards = deckCardRepository.findByDeckId(id);
        List<Card> cards = deckCards.stream()
                .map(dc -> cardRepository.findById(dc.getCardId())
                        .orElseThrow(() -> new NoSuchElementException("Karte mit ID " + dc.getCardId() + " wurde nicht gefunden")))
                .collect(Collectors.toList());
        
        return convertToDTOWithCards(deck, cards);
    }
    
    @Transactional
    public DeckDTO createDeck(DeckDTO deckDTO) {
        Deck deck = new Deck();
        deck.setName(deckDTO.getName());
        deck.setDescription(deckDTO.getDescription());
        
        if (deckDTO.getTags() != null) {
            deck.setTags(deckDTO.getTags());
        }
        
        Deck savedDeck = deckRepository.save(deck);
        return convertToDTO(savedDeck);
    }
    
    @Transactional
    public DeckDTO updateDeck(String id, DeckDTO deckDTO) {
        assert id != null : "Deck ID darf nicht null sein";
        assert !id.trim().isEmpty() : "Deck ID darf nicht leer sein";
        Deck deck = deckRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Deck mit ID " + id + " wurde nicht gefunden"));
        
        deck.setName(deckDTO.getName());
        deck.setDescription(deckDTO.getDescription());
        
        if (deckDTO.getTags() != null) {
            deck.setTags(deckDTO.getTags());
        }
        
        Deck updatedDeck = deckRepository.save(deck);
        return convertToDTO(updatedDeck);
    }
    
    @Transactional
    public void deleteDeck(String id) {
        assert id != null : "Deck ID darf nicht null sein";
        assert !id.trim().isEmpty() : "Deck ID darf nicht leer sein";
        if (!deckRepository.existsById(id)) {
            throw new NoSuchElementException("Deck mit ID " + id + " wurde nicht gefunden");
        }
        
        // Erst die Verknüpfungen löschen
        List<DeckCard> deckCards = deckCardRepository.findByDeckId(id);
        deckCardRepository.deleteAll(deckCards);
        
        // Dann das Deck löschen
        deckRepository.deleteById(id);
    }
    
    @Transactional
    public void addCardToDeck(String deckId, String cardId) {
        assert deckId != null : "Deck ID darf nicht null sein";
        assert !deckId.trim().isEmpty() : "Deck ID darf nicht leer sein";
        assert cardId != null : "Card ID darf nicht null sein";
        assert !cardId.trim().isEmpty() : "Card ID darf nicht leer sein";
        // Prüfen, ob Deck und Karte existieren
        if (!deckRepository.existsById(deckId)) {
            throw new NoSuchElementException("Deck mit ID " + deckId + " wurde nicht gefunden");
        }
        
        if (!cardRepository.existsById(cardId)) {
            throw new NoSuchElementException("Karte mit ID " + cardId + " wurde nicht gefunden");
        }
        
        // Prüfen, ob die Verknüpfung bereits existiert
        if (deckCardRepository.findByDeckIdAndCardId(deckId, cardId).isPresent()) {
            throw new IllegalStateException("Die Karte ist bereits im Deck vorhanden");
        }
        
        // Neue Verknüpfung erstellen und speichern
        DeckCard deckCard = new DeckCard(deckId, cardId);
        deckCardRepository.save(deckCard);
    }
    
    @Transactional
    public void removeCardFromDeck(String deckId, String cardId) {
        assert deckId != null : "Deck ID darf nicht null sein";
        assert !deckId.trim().isEmpty() : "Deck ID darf nicht leer sein";
        assert cardId != null : "Card ID darf nicht null sein";
        assert !cardId.trim().isEmpty() : "Card ID darf nicht leer sein";
        // Prüfen, ob die Verknüpfung existiert
        DeckCard deckCard = deckCardRepository.findByDeckIdAndCardId(deckId, cardId)
                .orElseThrow(() -> new NoSuchElementException("Die Karte ist nicht im Deck vorhanden"));
        
        // Verknüpfung löschen
        deckCardRepository.delete(deckCard);
    }
    
    @Transactional(readOnly = true)
    public List<DeckDTO> getDecksByTag(String tag) {
        assert tag != null : "Tag darf nicht null sein";
        assert !tag.trim().isEmpty() : "Tag darf nicht leer sein";
        return deckRepository.findByTagsContaining(tag).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    @Transactional(readOnly = true)
    public boolean doesDeckExist(String id) {
        assert id != null : "Deck ID darf nicht null sein";
        assert !id.trim().isEmpty() : "Deck ID darf nicht leer sein";
        return deckRepository.existsById(id);
    }
    
    private DeckDTO convertToDTO(Deck deck) {
        DeckDTO dto = new DeckDTO();
        dto.setId(deck.getId());
        dto.setName(deck.getName());
        dto.setDescription(deck.getDescription());
        dto.setTags(deck.getTags());
        
        if (deck.getCreatedAt() != null) {
            dto.setCreatedAt(deck.getCreatedAt().format(formatter));
        }
        
        if (deck.getUpdatedAt() != null) {
            dto.setUpdatedAt(deck.getUpdatedAt().format(formatter));
        }
        
        return dto;
    }
    
    private DeckWithCardsDTO convertToDTOWithCards(Deck deck, List<Card> cards) {
        DeckWithCardsDTO dto = new DeckWithCardsDTO();
        dto.setId(deck.getId());
        dto.setName(deck.getName());
        dto.setDescription(deck.getDescription());
        dto.setTags(deck.getTags());
        
        if (deck.getCreatedAt() != null) {
            dto.setCreatedAt(deck.getCreatedAt().format(formatter));
        }
        
        if (deck.getUpdatedAt() != null) {
            dto.setUpdatedAt(deck.getUpdatedAt().format(formatter));
        }
        
        // Karten konvertieren und hinzufügen
        List<CardDTO> cardDTOs = cards.stream()
                .map(card -> {
                    CardDTO cardDTO = new CardDTO();
                    cardDTO.setId(card.getId());
                    cardDTO.setFront(card.getFront());
                    cardDTO.setBack(card.getBack());
                    cardDTO.setTags(card.getTags());
                    
                    if (card.getCreatedAt() != null) {
                        cardDTO.setCreatedAt(card.getCreatedAt().format(formatter));
                    }
                    
                    if (card.getUpdatedAt() != null) {
                        cardDTO.setUpdatedAt(card.getUpdatedAt().format(formatter));
                    }
                    
                    return cardDTO;
                })
                .collect(Collectors.toList());
        
        dto.setCards(cardDTOs);
        
        return dto;
    }
}
