package com.fao.flashcards.domain.model;

/**
 * Enum für den Status einer OCR-Verarbeitung.
 */
public enum OCRStatus {
    /**
     * OCR-Verarbeitung ist in der Warteschlange
     */
    PENDING,
    
    /**
     * OCR-Verarbeitung läuft gerade
     */
    PROCESSING,
    
    /**
     * OCR-Verarbeitung erfolgreich abgeschlossen
     */
    SUCCESS,
    
    /**
     * OCR-Verarbeitung fehlgeschlagen
     */
    FAILED
}