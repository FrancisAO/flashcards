package com.fao.flashcards.cards.adapter.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.fao.flashcards.cards.application.port.out.repository.AIGeneratedCardRepository;
import com.fao.flashcards.cards.model.AIGeneratedCard;

/**
 * Adapter der das AIGeneratedCardOutputPort Interface implementiert und
 * die Spring Boot Repository Funktionalität bereitstellt.
 */
@Component
public class JpaAIGeneratedCardRepository implements AIGeneratedCardRepository {

    private final JpaAIGeneratedCardSpringDataRepository aiGeneratedCardRepository;

    public JpaAIGeneratedCardRepository(JpaAIGeneratedCardSpringDataRepository aiGeneratedCardRepository) {
        this.aiGeneratedCardRepository = aiGeneratedCardRepository;
    }

    @Override
    @Transactional
    public AIGeneratedCard save(AIGeneratedCard aiGeneratedCard) {
        return aiGeneratedCardRepository.save(aiGeneratedCard);
    }

    @Override
    @Transactional
    public List<AIGeneratedCard> saveAll(Iterable<AIGeneratedCard> aiGeneratedCards) {
        return aiGeneratedCardRepository.saveAll(aiGeneratedCards);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<AIGeneratedCard> findById(String id) {
        return aiGeneratedCardRepository.findById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<AIGeneratedCard> findAll() {
        return aiGeneratedCardRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existsById(String id) {
        return aiGeneratedCardRepository.existsById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public long count() {
        return aiGeneratedCardRepository.count();
    }

    @Override
    @Transactional
    public void deleteById(String id) {
        aiGeneratedCardRepository.deleteById(id);
    }

    @Override
    @Transactional
    public void delete(AIGeneratedCard aiGeneratedCard) {
        aiGeneratedCardRepository.delete(aiGeneratedCard);
    }

    @Override
    @Transactional
    public void deleteAll() {
        aiGeneratedCardRepository.deleteAll();
    }

    @Override
    @Transactional(readOnly = true)
    public List<AIGeneratedCard> findByGenerationRequestId(String generationRequestId) {
        return aiGeneratedCardRepository.findByGenerationRequestId(generationRequestId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<AIGeneratedCard> findByGenerationRequestIdAndSavedFalse(String generationRequestId) {
        return aiGeneratedCardRepository.findByGenerationRequestIdAndSavedFalse(generationRequestId);
    }
}