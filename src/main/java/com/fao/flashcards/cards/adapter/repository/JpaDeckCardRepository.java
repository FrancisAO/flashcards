package com.fao.flashcards.cards.adapter.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.fao.flashcards.cards.application.port.out.repository.DeckCardRepository;
import com.fao.flashcards.cards.model.DeckCard;

/**
 * Adapter der das DeckCardOutputPort Interface implementiert und
 * die Spring Boot Repository Funktionalität bereitstellt.
 */
@Component
public class JpaDeckCardRepository implements DeckCardRepository {

    private final JpaDeckCardSpringDataRepository deckCardRepository;

    public JpaDeckCardRepository(JpaDeckCardSpringDataRepository deckCardRepository) {
        this.deckCardRepository = deckCardRepository;
    }

    @Override
    @Transactional
    public DeckCard save(DeckCard deckCard) {
        return deckCardRepository.save(deckCard);
    }

    @Override
    @Transactional
    public List<DeckCard> saveAll(Iterable<DeckCard> deckCards) {
        return deckCardRepository.saveAll(deckCards);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<DeckCard> findById(String id) {
        return deckCardRepository.findById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<DeckCard> findAll() {
        return deckCardRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existsById(String id) {
        return deckCardRepository.existsById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public long count() {
        return deckCardRepository.count();
    }

    @Override
    @Transactional
    public void deleteById(String id) {
        deckCardRepository.deleteById(id);
    }

    @Override
    @Transactional
    public void delete(DeckCard deckCard) {
        deckCardRepository.delete(deckCard);
    }

    @Override
    @Transactional
    public void deleteAll() {
        deckCardRepository.deleteAll();
    }

    @Override
    @Transactional(readOnly = true)
    public List<DeckCard> findByDeckId(String deckId) {
        return deckCardRepository.findByDeckId(deckId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<DeckCard> findByCardId(String cardId) {
        return deckCardRepository.findByCardId(cardId);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<DeckCard> findByDeckIdAndCardId(String deckId, String cardId) {
        return deckCardRepository.findByDeckIdAndCardId(deckId, cardId);
    }

    @Override
    @Transactional
    public void deleteByDeckIdAndCardId(String deckId, String cardId) {
        deckCardRepository.deleteByDeckIdAndCardId(deckId, cardId);
    }
}