package com.fao.flashcards.cards.adapter.repository;

import com.fao.flashcards.cards.application.port.out.repository.AIGeneratedCardOutputPort;
import com.fao.flashcards.cards.model.AIGeneratedCard;

import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

/**
 * Adapter der das AIGeneratedCardOutputPort Interface implementiert und 
 * die Spring Boot Repository Funktionalität bereitstellt.
 */
@Component
public class AIGeneratedCardRepositoryAdapter implements AIGeneratedCardOutputPort {
    
    private final AIGeneratedCardRepository aiGeneratedCardRepository;
    
    public AIGeneratedCardRepositoryAdapter(AIGeneratedCardRepository aiGeneratedCardRepository) {
        this.aiGeneratedCardRepository = aiGeneratedCardRepository;
    }
    
    @Override
    public AIGeneratedCard save(AIGeneratedCard aiGeneratedCard) {
        return aiGeneratedCardRepository.save(aiGeneratedCard);
    }
    
    @Override
    public List<AIGeneratedCard> saveAll(Iterable<AIGeneratedCard> aiGeneratedCards) {
        return aiGeneratedCardRepository.saveAll(aiGeneratedCards);
    }
    
    @Override
    public Optional<AIGeneratedCard> findById(String id) {
        return aiGeneratedCardRepository.findById(id);
    }
    
    @Override
    public List<AIGeneratedCard> findAll() {
        return aiGeneratedCardRepository.findAll();
    }
    
    @Override
    public boolean existsById(String id) {
        return aiGeneratedCardRepository.existsById(id);
    }
    
    @Override
    public long count() {
        return aiGeneratedCardRepository.count();
    }
    
    @Override
    public void deleteById(String id) {
        aiGeneratedCardRepository.deleteById(id);
    }
    
    @Override
    public void delete(AIGeneratedCard aiGeneratedCard) {
        aiGeneratedCardRepository.delete(aiGeneratedCard);
    }
    
    @Override
    public void deleteAll() {
        aiGeneratedCardRepository.deleteAll();
    }
    
    @Override
    public List<AIGeneratedCard> findByGenerationRequestId(String generationRequestId) {
        return aiGeneratedCardRepository.findByGenerationRequestId(generationRequestId);
    }
    
    @Override
    public List<AIGeneratedCard> findByGenerationRequestIdAndSavedFalse(String generationRequestId) {
        return aiGeneratedCardRepository.findByGenerationRequestIdAndSavedFalse(generationRequestId);
    }
}