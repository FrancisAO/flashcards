package com.fao.flashcards.domain.model;

/**
 * Enum für die Quelle der Textextraktion.
 */
public enum ExtractionSource {
    /**
     * Text wurde aus einem Bild extrahiert (OCR)
     */
    IMAGE,
    
    /**
     * Text wurde aus einem Dokument extrahiert
     */
    DOCUMENT
}