package com.fao.flashcards.cards.adapter.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.fao.flashcards.cards.application.port.out.repository.CardRepository;
import com.fao.flashcards.cards.model.Card;

/**
 * Adapter der das CardOutputPort Interface implementiert und
 * die Spring Boot Repository Funktionalität bereitstellt.
 */
@Component
public class JpaCardRepository implements CardRepository {

    private final JpaCardSpringDataRepository cardRepository;

    public JpaCardRepository(JpaCardSpringDataRepository cardRepository) {
        this.cardRepository = cardRepository;
    }

    @Override
    @Transactional
    public Card save(Card card) {
        return cardRepository.save(card);
    }

    @Override
    @Transactional
    public List<Card> saveAll(Iterable<Card> cards) {
        return cardRepository.saveAll(cards);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Card> findById(String id) {
        return cardRepository.findById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Card> findAll() {
        return cardRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existsById(String id) {
        return cardRepository.existsById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public long count() {
        return cardRepository.count();
    }

    @Override
    @Transactional
    public void deleteById(String id) {
        cardRepository.deleteById(id);
    }

    @Override
    @Transactional
    public void delete(Card card) {
        cardRepository.delete(card);
    }

    @Override
    @Transactional
    public void deleteAll() {
        cardRepository.deleteAll();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Card> findByTagsContaining(String tag) {
        return cardRepository.findByTagsContaining(tag);
    }
}