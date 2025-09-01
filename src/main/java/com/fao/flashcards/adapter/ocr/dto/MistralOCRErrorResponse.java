package com.fao.flashcards.adapter.ocr.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * DTO für Mistral OCR API Error Response (422 Validation Error).
 * Repräsentiert die JSON-Struktur für Fehlermeldungen von der Mistral API.
 */
public class MistralOCRErrorResponse {
    
    @JsonProperty("detail")
    private List<ValidationError> detail;
    
    /**
     * Standard-Konstruktor.
     */
    public MistralOCRErrorResponse() {
    }
    
    public List<ValidationError> getDetail() {
        return detail;
    }
    
    public void setDetail(List<ValidationError> detail) {
        this.detail = detail;
    }
    
    /**
     * DTO für einzelne Validierungsfehler.
     */
    public static class ValidationError {
        
        @JsonProperty("loc")
        private List<String> location;
        
        @JsonProperty("msg")
        private String message;
        
        @JsonProperty("type")
        private String type;
        
        /**
         * Standard-Konstruktor.
         */
        public ValidationError() {
        }
        
        public List<String> getLocation() {
            return location;
        }
        
        public void setLocation(List<String> location) {
            this.location = location;
        }
        
        public String getMessage() {
            return message;
        }
        
        public void setMessage(String message) {
            this.message = message;
        }
        
        public String getType() {
            return type;
        }
        
        public void setType(String type) {
            this.type = type;
        }
        
        /**
         * Gibt eine formatierte Fehlermeldung zurück.
         *
         * @return Formatierte Fehlermeldung
         */
        public String getFormattedMessage() {
            StringBuilder sb = new StringBuilder();
            if (location != null && !location.isEmpty()) {
                sb.append("Location: ").append(String.join(".", location)).append(" - ");
            }
            sb.append(message);
            if (type != null) {
                sb.append(" (Type: ").append(type).append(")");
            }
            return sb.toString();
        }
    }
    
    /**
     * Gibt alle Fehlermeldungen als formatierten String zurück.
     *
     * @return Formatierte Fehlermeldungen
     */
    public String getFormattedErrorMessage() {
        if (detail == null || detail.isEmpty()) {
            return "Unbekannter Validierungsfehler";
        }
        
        StringBuilder sb = new StringBuilder("Validierungsfehler: ");
        for (int i = 0; i < detail.size(); i++) {
            if (i > 0) {
                sb.append("; ");
            }
            sb.append(detail.get(i).getFormattedMessage());
        }
        
        return sb.toString();
    }
}