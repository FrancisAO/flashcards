package com.fao.flashcards.cards.adapter.repository;

import com.fao.flashcards.cards.application.port.out.repository.DeckCardOutputPort;
import com.fao.flashcards.cards.model.DeckCard;

import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

/**
 * Adapter der das DeckCardOutputPort Interface implementiert und 
 * die Spring Boot Repository Funktionalität bereitstellt.
 */
@Component
public class DeckCardRepositoryAdapter implements DeckCardOutputPort {
    
    private final DeckCardRepository deckCardRepository;
    
    public DeckCardRepositoryAdapter(DeckCardRepository deckCardRepository) {
        this.deckCardRepository = deckCardRepository;
    }
    
    @Override
    public DeckCard save(DeckCard deckCard) {
        return deckCardRepository.save(deckCard);
    }
    
    @Override
    public List<DeckCard> saveAll(Iterable<DeckCard> deckCards) {
        return deckCardRepository.saveAll(deckCards);
    }
    
    @Override
    public Optional<DeckCard> findById(String id) {
        return deckCardRepository.findById(id);
    }
    
    @Override
    public List<DeckCard> findAll() {
        return deckCardRepository.findAll();
    }
    
    @Override
    public boolean existsById(String id) {
        return deckCardRepository.existsById(id);
    }
    
    @Override
    public long count() {
        return deckCardRepository.count();
    }
    
    @Override
    public void deleteById(String id) {
        deckCardRepository.deleteById(id);
    }
    
    @Override
    public void delete(DeckCard deckCard) {
        deckCardRepository.delete(deckCard);
    }
    
    @Override
    public void deleteAll() {
        deckCardRepository.deleteAll();
    }
    
    @Override
    public List<DeckCard> findByDeckId(String deckId) {
        return deckCardRepository.findByDeckId(deckId);
    }
    
    @Override
    public List<DeckCard> findByCardId(String cardId) {
        return deckCardRepository.findByCardId(cardId);
    }
    
    @Override
    public Optional<DeckCard> findByDeckIdAndCardId(String deckId, String cardId) {
        return deckCardRepository.findByDeckIdAndCardId(deckId, cardId);
    }
    
    @Override
    public void deleteByDeckIdAndCardId(String deckId, String cardId) {
        deckCardRepository.deleteByDeckIdAndCardId(deckId, cardId);
    }
}