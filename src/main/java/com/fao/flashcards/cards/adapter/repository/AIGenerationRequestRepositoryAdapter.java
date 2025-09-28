package com.fao.flashcards.cards.adapter.repository;

import com.fao.flashcards.cards.application.port.out.repository.AIGenerationRequestOutputPort;
import com.fao.flashcards.cards.model.AIGenerationRequest;

import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

/**
 * Adapter der das AIGenerationRequestOutputPort Interface implementiert und 
 * die Spring Boot Repository Funktionalität bereitstellt.
 */
@Component
public class AIGenerationRequestRepositoryAdapter implements AIGenerationRequestOutputPort {
    
    private final AIGenerationRequestRepository aiGenerationRequestRepository;
    
    public AIGenerationRequestRepositoryAdapter(AIGenerationRequestRepository aiGenerationRequestRepository) {
        this.aiGenerationRequestRepository = aiGenerationRequestRepository;
    }
    
    @Override
    public AIGenerationRequest save(AIGenerationRequest aiGenerationRequest) {
        return aiGenerationRequestRepository.save(aiGenerationRequest);
    }
    
    @Override
    public List<AIGenerationRequest> saveAll(Iterable<AIGenerationRequest> aiGenerationRequests) {
        return aiGenerationRequestRepository.saveAll(aiGenerationRequests);
    }
    
    @Override
    public Optional<AIGenerationRequest> findById(String id) {
        return aiGenerationRequestRepository.findById(id);
    }
    
    @Override
    public List<AIGenerationRequest> findAll() {
        return aiGenerationRequestRepository.findAll();
    }
    
    @Override
    public boolean existsById(String id) {
        return aiGenerationRequestRepository.existsById(id);
    }
    
    @Override
    public long count() {
        return aiGenerationRequestRepository.count();
    }
    
    @Override
    public void deleteById(String id) {
        aiGenerationRequestRepository.deleteById(id);
    }
    
    @Override
    public void delete(AIGenerationRequest aiGenerationRequest) {
        aiGenerationRequestRepository.delete(aiGenerationRequest);
    }
    
    @Override
    public void deleteAll() {
        aiGenerationRequestRepository.deleteAll();
    }
    
    @Override
    public List<AIGenerationRequest> findByDeckId(String deckId) {
        return aiGenerationRequestRepository.findByDeckId(deckId);
    }
}