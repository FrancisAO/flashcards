package com.fao.flashcards.cards.adapter.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.fao.flashcards.cards.application.port.out.repository.DeckRepository;
import com.fao.flashcards.cards.model.Deck;

/**
 * Adapter der das DeckOutputPort Interface implementiert und
 * die Spring Boot Repository Funktionalität bereitstellt.
 */
@Component
public class JpaDeckRepository implements DeckRepository {

    private final JpaDeckSpringDataRepository deckRepository;

    public JpaDeckRepository(JpaDeckSpringDataRepository deckRepository) {
        this.deckRepository = deckRepository;
    }

    @Override
    @Transactional
    public Deck save(Deck deck) {
        return deckRepository.save(deck);
    }

    @Override
    @Transactional
    public List<Deck> saveAll(Iterable<Deck> decks) {
        return deckRepository.saveAll(decks);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Deck> findById(String id) {
        return deckRepository.findById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Deck> findAll() {
        return deckRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existsById(String id) {
        return deckRepository.existsById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public long count() {
        return deckRepository.count();
    }

    @Override
    @Transactional
    public void deleteById(String id) {
        deckRepository.deleteById(id);
    }

    @Override
    @Transactional
    public void delete(Deck deck) {
        deckRepository.delete(deck);
    }

    @Override
    @Transactional
    public void deleteAll() {
        deckRepository.deleteAll();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Deck> findByTagsContaining(String tag) {
        return deckRepository.findByTagsContaining(tag);
    }
}