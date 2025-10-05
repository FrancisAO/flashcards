package com.fao.flashcards.cards.adapter.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.fao.flashcards.cards.application.port.out.repository.AIGenerationRequestRepository;
import com.fao.flashcards.cards.model.AIGenerationRequest;

/**
 * Adapter der das AIGenerationRequestOutputPort Interface implementiert und
 * die Spring Boot Repository Funktionalität bereitstellt.
 */
@Component
public class JpaAIGenerationRequestRepository implements AIGenerationRequestRepository {

    private final JpaAIGenerationRequestSpringDataRepository aiGenerationRequestRepository;

    public JpaAIGenerationRequestRepository(
            JpaAIGenerationRequestSpringDataRepository aiGenerationRequestRepository) {
        this.aiGenerationRequestRepository = aiGenerationRequestRepository;
    }

    @Override
    @Transactional
    public AIGenerationRequest save(AIGenerationRequest aiGenerationRequest) {
        return aiGenerationRequestRepository.save(aiGenerationRequest);
    }

    @Override
    @Transactional
    public List<AIGenerationRequest> saveAll(Iterable<AIGenerationRequest> aiGenerationRequests) {
        return aiGenerationRequestRepository.saveAll(aiGenerationRequests);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<AIGenerationRequest> findById(String id) {
        return aiGenerationRequestRepository.findById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<AIGenerationRequest> findAll() {
        return aiGenerationRequestRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existsById(String id) {
        return aiGenerationRequestRepository.existsById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public long count() {
        return aiGenerationRequestRepository.count();
    }

    @Override
    @Transactional
    public void deleteById(String id) {
        aiGenerationRequestRepository.deleteById(id);
    }

    @Override
    @Transactional
    public void delete(AIGenerationRequest aiGenerationRequest) {
        aiGenerationRequestRepository.delete(aiGenerationRequest);
    }

    @Override
    @Transactional
    public void deleteAll() {
        aiGenerationRequestRepository.deleteAll();
    }

    @Override
    @Transactional(readOnly = true)
    public List<AIGenerationRequest> findByDeckId(String deckId) {
        return aiGenerationRequestRepository.findByDeckId(deckId);
    }
}