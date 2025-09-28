package com.fao.flashcards.cards.adapter.repository;

import com.fao.flashcards.cards.application.port.out.repository.CardOutputPort;
import com.fao.flashcards.cards.model.Card;

import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

/**
 * Adapter der das CardOutputPort Interface implementiert und 
 * die Spring Boot Repository Funktionalität bereitstellt.
 */
@Component
public class CardRepositoryAdapter implements CardOutputPort {
    
    private final CardRepository cardRepository;
    
    public CardRepositoryAdapter(CardRepository cardRepository) {
        this.cardRepository = cardRepository;
    }
    
    @Override
    public Card save(Card card) {
        return cardRepository.save(card);
    }
    
    @Override
    public List<Card> saveAll(Iterable<Card> cards) {
        return cardRepository.saveAll(cards);
    }
    
    @Override
    public Optional<Card> findById(String id) {
        return cardRepository.findById(id);
    }
    
    @Override
    public List<Card> findAll() {
        return cardRepository.findAll();
    }
    
    @Override
    public boolean existsById(String id) {
        return cardRepository.existsById(id);
    }
    
    @Override
    public long count() {
        return cardRepository.count();
    }
    
    @Override
    public void deleteById(String id) {
        cardRepository.deleteById(id);
    }
    
    @Override
    public void delete(Card card) {
        cardRepository.delete(card);
    }
    
    @Override
    public void deleteAll() {
        cardRepository.deleteAll();
    }
    
    @Override
    public List<Card> findByTagsContaining(String tag) {
        return cardRepository.findByTagsContaining(tag);
    }
}