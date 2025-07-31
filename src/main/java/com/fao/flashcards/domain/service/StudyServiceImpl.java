package com.fao.flashcards.domain.service;

import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fao.flashcards.api.dto.StudyCardDTO;
import com.fao.flashcards.api.dto.StudyDeckDTO;
import com.fao.flashcards.domain.model.Card;
import com.fao.flashcards.domain.model.Deck;
import com.fao.flashcards.domain.model.DeckCard;
import com.fao.flashcards.domain.repository.CardRepository;
import com.fao.flashcards.domain.repository.DeckCardRepository;
import com.fao.flashcards.domain.repository.DeckRepository;

/**
 * Implementierung des StudyService-Interfaces.
 * Enthält die Geschäftslogik für den Lernmodus.
 */
@Service
public class StudyServiceImpl implements StudyService {

    private static final Logger logger = LoggerFactory.getLogger(StudyServiceImpl.class);
    
    private final DeckRepository deckRepository;
    private final CardRepository cardRepository;
    private final DeckCardRepository deckCardRepository;
    
    @Autowired
    public StudyServiceImpl(DeckRepository deckRepository, CardRepository cardRepository, DeckCardRepository deckCardRepository) {
        this.deckRepository = deckRepository;
        this.cardRepository = cardRepository;
        this.deckCardRepository = deckCardRepository;
    }
    
    @Override
    @Transactional(readOnly = true)
    public Optional<StudyDeckDTO> getRandomizedDeck(String deckId) {
        assert deckId != null : "Deck ID darf nicht null sein";
        assert !deckId.isBlank() : "Deck ID darf nicht leer sein";
        
        logger.info("Hole Karten für Deck {} in zufälliger Reihenfolge", deckId);
        
        // Deck aus der Datenbank laden
        Optional<Deck> deckOptional = deckRepository.findById(deckId);
        if (deckOptional.isEmpty()) {
            logger.warn("Deck mit ID {} wurde nicht gefunden", deckId);
            return Optional.empty();
        }
        
        Deck deck = deckOptional.get();
        
        // Verknüpfungen zwischen Deck und Karten laden
        List<DeckCard> deckCards = deckCardRepository.findByDeckId(deckId);
        
        // Karten laden (ignoriere Karten, die nicht gefunden werden können)
        List<Card> cards = deckCards.stream()
                .map(dc -> cardRepository.findById(dc.getCardId()))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toList());
        
        // Karten mischen
        Collections.shuffle(cards);
        
        // StudyDeckDTO erstellen
        StudyDeckDTO studyDeckDTO = new StudyDeckDTO();
        studyDeckDTO.setId(deck.getId());
        studyDeckDTO.setName(deck.getName());
        studyDeckDTO.setDescription(deck.getDescription());
        
        // Karten in StudyCardDTOs umwandeln
        List<StudyCardDTO> studyCardDTOs = cards.stream()
                .map(this::convertToStudyCardDTO)
                .collect(Collectors.toList());
        
        studyDeckDTO.setCards(studyCardDTOs);
        
        logger.info("Deck {} mit {} Karten in zufälliger Reihenfolge zurückgegeben", deckId, studyCardDTOs.size());
        
        return Optional.of(studyDeckDTO);
    }
    
    @Override
    @Transactional(readOnly = true)
    public boolean doesDeckExist(String deckId) {
        assert deckId != null : "Deck ID darf nicht null sein";
        assert !deckId.trim().isEmpty() : "Deck ID darf nicht leer sein";
        
        return deckRepository.existsById(deckId);
    }
    
    /**
     * Konvertiert eine Card-Entität in ein StudyCardDTO.
     * 
     * @param card Die Card-Entität
     * @return Das StudyCardDTO
     */
    private StudyCardDTO convertToStudyCardDTO(Card card) {
        StudyCardDTO dto = new StudyCardDTO();
        dto.setId(card.getId());
        dto.setFront(card.getFront());
        dto.setBack(card.getBack());
        dto.setTags(card.getTags());
        
        return dto;
    }
}