package com.fao.flashcards.domain.service;

import com.fao.flashcards.api.dto.CardDTO;
import com.fao.flashcards.domain.model.Card;
import com.fao.flashcards.domain.repository.CardRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Service
public class CardService {
    
    private final CardRepository cardRepository;
    private static final DateTimeFormatter formatter = DateTimeFormatter.ISO_DATE_TIME;
    
    @Autowired
    public CardService(CardRepository cardRepository) {
        this.cardRepository = cardRepository;
    }
    
    @Transactional(readOnly = true)
    public List<CardDTO> getAllCards() {
        return cardRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    @Transactional(readOnly = true)
    public CardDTO getCardById(String id) {
        Card card = cardRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Karte mit ID " + id + " wurde nicht gefunden"));
        return convertToDTO(card);
    }
    
    @Transactional
    public CardDTO createCard(CardDTO cardDTO) {
        Card card = new Card();
        card.setFront(cardDTO.getFront());
        card.setBack(cardDTO.getBack());
        
        if (cardDTO.getTags() != null) {
            card.setTags(cardDTO.getTags());
        }
        
        Card savedCard = cardRepository.save(card);
        return convertToDTO(savedCard);
    }
    
    @Transactional
    public CardDTO updateCard(String id, CardDTO cardDTO) {
        Card card = cardRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Karte mit ID " + id + " wurde nicht gefunden"));
        
        card.setFront(cardDTO.getFront());
        card.setBack(cardDTO.getBack());
        
        if (cardDTO.getTags() != null) {
            card.setTags(cardDTO.getTags());
        }
        
        Card updatedCard = cardRepository.save(card);
        return convertToDTO(updatedCard);
    }
    
    @Transactional
    public void deleteCard(String id) {
        if (!cardRepository.existsById(id)) {
            throw new NoSuchElementException("Karte mit ID " + id + " wurde nicht gefunden");
        }
        cardRepository.deleteById(id);
    }
    
    @Transactional(readOnly = true)
    public List<CardDTO> getCardsByTag(String tag) {
        return cardRepository.findByTagsContaining(tag).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    private CardDTO convertToDTO(Card card) {
        CardDTO dto = new CardDTO();
        dto.setId(card.getId());
        dto.setFront(card.getFront());
        dto.setBack(card.getBack());
        dto.setTags(card.getTags());
        
        if (card.getCreatedAt() != null) {
            dto.setCreatedAt(card.getCreatedAt().format(formatter));
        }
        
        if (card.getUpdatedAt() != null) {
            dto.setUpdatedAt(card.getUpdatedAt().format(formatter));
        }
        
        return dto;
    }
}
