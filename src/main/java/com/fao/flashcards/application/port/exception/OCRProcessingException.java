package com.fao.flashcards.application.port.exception;

/**
 * Spezifische Exception für OCR-Verarbeitungsfehler.
 * Diese Exception wird geworfen, wenn bei der OCR-Verarbeitung Fehler auftreten.
 */
public class OCRProcessingException extends RuntimeException {
    
    private final String errorCode;
    private final int httpStatus;
    
    /**
     * Konstruktor für OCRProcessingException mit Nachricht.
     *
     * @param message Fehlermeldung
     */
    public OCRProcessingException(String message) {
        super(message);
        this.errorCode = "OCR_ERROR";
        this.httpStatus = 500;
    }
    
    /**
     * Konstruktor für OCRProcessingException mit Nachricht und Ursache.
     *
     * @param message Fehlermeldung
     * @param cause   Ursache des Fehlers
     */
    public OCRProcessingException(String message, Throwable cause) {
        super(message, cause);
        this.errorCode = "OCR_ERROR";
        this.httpStatus = 500;
    }
    
    /**
     * Konstruktor für OCRProcessingException mit erweiterten Fehlerinformationen.
     *
     * @param message    Fehlermeldung
     * @param errorCode  Spezifischer Fehlercode
     * @param httpStatus HTTP-Statuscode
     */
    public OCRProcessingException(String message, String errorCode, int httpStatus) {
        super(message);
        this.errorCode = errorCode;
        this.httpStatus = httpStatus;
    }
    
    /**
     * Konstruktor für OCRProcessingException mit erweiterten Fehlerinformationen und Ursache.
     *
     * @param message    Fehlermeldung
     * @param cause      Ursache des Fehlers
     * @param errorCode  Spezifischer Fehlercode
     * @param httpStatus HTTP-Statuscode
     */
    public OCRProcessingException(String message, Throwable cause, String errorCode, int httpStatus) {
        super(message, cause);
        this.errorCode = errorCode;
        this.httpStatus = httpStatus;
    }
    
    /**
     * Gibt den Fehlercode zurück.
     *
     * @return Fehlercode
     */
    public String getErrorCode() {
        return errorCode;
    }
    
    /**
     * Gibt den HTTP-Statuscode zurück.
     *
     * @return HTTP-Statuscode
     */
    public int getHttpStatus() {
        return httpStatus;
    }
    
    /**
     * Factory-Methode für API-Fehler.
     *
     * @param message    Fehlermeldung
     * @param httpStatus HTTP-Statuscode der API
     * @return OCRProcessingException
     */
    public static OCRProcessingException apiError(String message, int httpStatus) {
        return new OCRProcessingException(message, "API_ERROR", httpStatus);
    }
    
    /**
     * Factory-Methode für Validierungsfehler.
     *
     * @param message Fehlermeldung
     * @return OCRProcessingException
     */
    public static OCRProcessingException validationError(String message) {
        return new OCRProcessingException(message, "VALIDATION_ERROR", 400);
    }
    
    /**
     * Factory-Methode für Dateigröße-Fehler.
     *
     * @param actualSize Tatsächliche Dateigröße
     * @param maxSize    Maximale Dateigröße
     * @return OCRProcessingException
     */
    public static OCRProcessingException fileSizeError(long actualSize, long maxSize) {
        String message = String.format("Datei zu groß: %d bytes. Maximum: %d bytes", actualSize, maxSize);
        return new OCRProcessingException(message, "FILE_SIZE_ERROR", 413);
    }
    
    /**
     * Factory-Methode für Netzwerk-Fehler.
     *
     * @param message Fehlermeldung
     * @param cause   Ursache des Fehlers
     * @return OCRProcessingException
     */
    public static OCRProcessingException networkError(String message, Throwable cause) {
        return new OCRProcessingException(message, cause, "NETWORK_ERROR", 503);
    }
    
    /**
     * Factory-Methode für Authentifizierungsfehler.
     *
     * @param message Fehlermeldung
     * @return OCRProcessingException
     */
    public static OCRProcessingException authenticationError(String message) {
        return new OCRProcessingException(message, "AUTH_ERROR", 401);
    }
}