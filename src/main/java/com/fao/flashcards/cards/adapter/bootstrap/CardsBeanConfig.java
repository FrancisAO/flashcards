package com.fao.flashcards.cards.adapter.bootstrap;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.fao.flashcards.cards.adapter.ai.DirectOpenRouterAIAdapter;
import com.fao.flashcards.cards.adapter.document.DocumentProcessingAdapter;
import com.fao.flashcards.cards.adapter.repository.JpaAIGeneratedCardRepository;
import com.fao.flashcards.cards.adapter.repository.JpaAIGenerationRequestRepository;
import com.fao.flashcards.cards.adapter.repository.JpaCardRepository;
import com.fao.flashcards.cards.adapter.repository.JpaDeckCardRepository;
import com.fao.flashcards.cards.adapter.repository.JpaDeckRepository;
import com.fao.flashcards.cards.adapter.repository.JpaDocumentUploadRepository;
import com.fao.flashcards.cards.application.port.in.CardGenerationInputPort;
import com.fao.flashcards.cards.application.port.in.CardInputPort;
import com.fao.flashcards.cards.application.port.in.DeckInputPort;
import com.fao.flashcards.cards.application.port.out.AICardGenerationPort;
import com.fao.flashcards.cards.application.port.out.DocumentProcessingPort;
import com.fao.flashcards.cards.application.port.out.repository.AIGeneratedCardRepository;
import com.fao.flashcards.cards.application.port.out.repository.AIGenerationRequestRepository;
import com.fao.flashcards.cards.application.port.out.repository.CardRepository;
import com.fao.flashcards.cards.application.port.out.repository.DeckCardRepository;
import com.fao.flashcards.cards.application.port.out.repository.DeckRepository;
import com.fao.flashcards.cards.application.port.out.repository.DocumentUploadRepository;
import com.fao.flashcards.cards.application.service.CardGenerationService;
import com.fao.flashcards.cards.application.service.CardService;
import com.fao.flashcards.cards.application.service.DeckService;
import com.fao.flashcards.ocr.adapter.bootstrap.FileUploadConfigImpl;
import com.fao.flashcards.ocr.application.port.in.FileUploadConfig;


@Configuration
public class CardsBeanConfig {

    // ============================================================================
    // Repository Adapters (Output Ports)
    // ============================================================================

    @Bean
    public CardRepository cardRepository(JpaCardRepository jpaCardRepository) {
        return jpaCardRepository;
    }

    @Bean
    public DeckRepository deckRepository(JpaDeckRepository jpaDeckRepository) {
        return jpaDeckRepository;
    }

    @Bean
    public DeckCardRepository deckCardRepository(JpaDeckCardRepository jpaDeckCardRepository) {
        return jpaDeckCardRepository;
    }

    @Bean
    public AIGenerationRequestRepository aiGenerationRequestRepository(
            JpaAIGenerationRequestRepository jpaAIGenerationRequestRepository) {
        return jpaAIGenerationRequestRepository;
    }

    @Bean
    public DocumentUploadRepository documentUploadRepository(
            JpaDocumentUploadRepository jpaDocumentUploadRepository) {
        return jpaDocumentUploadRepository;
    }

    @Bean
    public AIGeneratedCardRepository aiGeneratedCardRepository(
            JpaAIGeneratedCardRepository jpaAIGeneratedCardRepository) {
        return jpaAIGeneratedCardRepository;
    }

    // ============================================================================
    // External Service Adapters (Output Ports)
    // ============================================================================

    @Bean
    public AICardGenerationPort aiCardGenerationPort(DirectOpenRouterAIAdapter directOpenRouterAIAdapter) {
        return directOpenRouterAIAdapter;
    }

    @Bean
    public DocumentProcessingPort documentProcessingPort(DocumentProcessingAdapter documentProcessingAdapter) {
        return documentProcessingAdapter;
    }

    // ============================================================================
    // Service Configurations
    // ============================================================================

    @Bean
    public FileUploadConfig fileUploadConfig(FileUploadConfigImpl fileUploadConfigImpl) {
        return fileUploadConfigImpl;
    }

    // ============================================================================
    // Application Services (Input Ports)
    // ============================================================================

    @Bean
    public CardInputPort cardInputPort(
            CardRepository cardRepository) {
        return new CardService(cardRepository);
    }

    @Bean
    public DeckInputPort deckInputPort(
            DeckRepository deckRepository,
            CardRepository cardRepository,
            DeckCardRepository deckCardRepository) {
        return new DeckService(deckRepository, cardRepository, deckCardRepository);
    }

    @Bean
    public CardGenerationInputPort cardGenerationInputPort(
            AIGenerationRequestRepository aiGenerationRequestRepository,
            DocumentUploadRepository documentUploadRepository,
            AIGeneratedCardRepository aiGeneratedCardRepository,
            CardRepository cardRepository,
            DeckInputPort deckInputPort,
            AICardGenerationPort aiCardGenerationPort,
            DocumentProcessingPort documentProcessingPort) {
        return new CardGenerationService(
                aiGenerationRequestRepository,
                documentUploadRepository,
                aiGeneratedCardRepository,
                cardRepository,
                deckInputPort,
                aiCardGenerationPort,
                documentProcessingPort);
    }
}
