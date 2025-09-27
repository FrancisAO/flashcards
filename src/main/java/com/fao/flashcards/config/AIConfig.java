package com.fao.flashcards.config;

import com.fao.flashcards.adapter.ocr.MistralOCRAdapter;
import com.fao.flashcards.application.port.OCRProcessingPort;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.BufferingClientHttpRequestFactory;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

/**
 * Konfiguration für die KI-Integration mit OpenRouter.ai und OCR-Services.
 * Die eigentliche Konfiguration erfolgt über die application.properties-Datei.
 */
@Configuration
public class AIConfig {
    
    @Value("${mistral.api.key}")
    private String mistralApiKey;
    
    @Value("${mistral.api.base-url}")
    private String mistralApiUrl;
    
    @Value("${mistral.ocr.model}")
    private String mistralOcrModel;
    
    @Value("${mistral.ocr.max-tokens:4096}")
    private Integer mistralOcrMaxTokens;
    
    @Value("${mistral.ocr.temperature:0.0}")
    private Double mistralOcrTemperature;
    
    /**
     * Bean für RestTemplate speziell für OCR-Requests.
     * Konfiguriert mit angemessenen Timeouts für OCR-Verarbeitung.
     */
    @Bean("ocrRestTemplate")
    public RestTemplate ocrRestTemplate() {
        RestTemplate restTemplate = new RestTemplate(new BufferingClientHttpRequestFactory(new SimpleClientHttpRequestFactory()));
        
        
        
        return restTemplate;
    }
    
    /**
     * Primary Bean für OCRProcessingPort.
     * Verwendet den MistralOCRAdapter als Standard-Implementation.
     */
    @Bean
    public OCRProcessingPort ocrProcessingPort() {
        return new MistralOCRAdapter(ocrRestTemplate());
    }
}