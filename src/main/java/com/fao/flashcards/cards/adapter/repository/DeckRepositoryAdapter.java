package com.fao.flashcards.cards.adapter.repository;

import com.fao.flashcards.cards.application.port.out.repository.DeckOutputPort;
import com.fao.flashcards.cards.model.Deck;

import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

/**
 * Adapter der das DeckOutputPort Interface implementiert und 
 * die Spring Boot Repository Funktionalität bereitstellt.
 */
@Component
public class DeckRepositoryAdapter implements DeckOutputPort {
    
    private final DeckRepository deckRepository;
    
    public DeckRepositoryAdapter(DeckRepository deckRepository) {
        this.deckRepository = deckRepository;
    }
    
    @Override
    public Deck save(Deck deck) {
        return deckRepository.save(deck);
    }
    
    @Override
    public List<Deck> saveAll(Iterable<Deck> decks) {
        return deckRepository.saveAll(decks);
    }
    
    @Override
    public Optional<Deck> findById(String id) {
        return deckRepository.findById(id);
    }
    
    @Override
    public List<Deck> findAll() {
        return deckRepository.findAll();
    }
    
    @Override
    public boolean existsById(String id) {
        return deckRepository.existsById(id);
    }
    
    @Override
    public long count() {
        return deckRepository.count();
    }
    
    @Override
    public void deleteById(String id) {
        deckRepository.deleteById(id);
    }
    
    @Override
    public void delete(Deck deck) {
        deckRepository.delete(deck);
    }
    
    @Override
    public void deleteAll() {
        deckRepository.deleteAll();
    }
    
    @Override
    public List<Deck> findByTagsContaining(String tag) {
        return deckRepository.findByTagsContaining(tag);
    }
}