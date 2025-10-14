package com.fao.flashcards.ocr.adapter;

/**
 * DTO-Record für Mistral OCR Konfigurationswerte.
 * Enthält alle Konfigurationsparameter die über @Value-Annotationen geladen
 * werden.
 */
public record MistralOCRConfigDTO(
        String mistralApiKey,
        String mistralApiUrl,
        String mistralOcrModel,
        Integer mistralOcrMaxTokens,
        Double mistralOcrTemperature) {
}