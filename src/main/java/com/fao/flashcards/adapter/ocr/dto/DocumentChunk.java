package com.fao.flashcards.adapter.ocr.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * DTO für Document-Chunk in Mistral OCR API Requests.
 * Repräsentiert verschiedene Arten von Dokumenten (File, DocumentURL, ImageURL).
 */
public abstract class DocumentChunk {
    
    @JsonProperty("type")
    protected String type;
    
    /**
     * Standard-Konstruktor.
     */
    protected DocumentChunk() {
    }
    
    /**
     * Konstruktor mit Type.
     *
     * @param type Der Typ des Document-Chunks
     */
    protected DocumentChunk(String type) {
        this.type = type;
    }
    
    public String getType() {
        return type;
    }
    
    public void setType(String type) {
        this.type = type;
    }
    
    /**
     * File-Chunk für hochgeladene Dateien.
     */
    public static class FileChunk extends DocumentChunk {
        
        @JsonProperty("file_id")
        private String fileId;
        
        /**
         * Standard-Konstruktor.
         */
        public FileChunk() {
            super("file");
        }
        
        /**
         * Konstruktor mit File-ID.
         *
         * @param fileId ID der hochgeladenen Datei
         */
        public FileChunk(String fileId) {
            super("file");
            this.fileId = fileId;
        }
        
        public String getFileId() {
            return fileId;
        }
        
        public void setFileId(String fileId) {
            this.fileId = fileId;
        }
    }
    
    /**
     * Document-URL-Chunk für Dokumente über URL.
     */
    public static class DocumentURLChunk extends DocumentChunk {
        
        @JsonProperty("document_url")
        private String documentUrl;
        
        /**
         * Standard-Konstruktor.
         */
        public DocumentURLChunk() {
            super("document_url");
        }
        
        /**
         * Konstruktor mit Document-URL.
         *
         * @param documentUrl URL des Dokuments
         */
        public DocumentURLChunk(String documentUrl) {
            super("document_url");
            this.documentUrl = documentUrl;
        }
        
        public String getDocumentUrl() {
            return documentUrl;
        }
        
        public void setDocumentUrl(String documentUrl) {
            this.documentUrl = documentUrl;
        }
    }
    
    /**
     * Image-URL-Chunk für Bilder über URL (Base64 Data-URL).
     */
    public static class ImageURLChunk extends DocumentChunk {
        
        @JsonProperty("image_url")
        private String imageUrl;
        
        /**
         * Standard-Konstruktor.
         */
        public ImageURLChunk() {
            super("image_url");
        }
        
        /**
         * Konstruktor mit Image-URL.
         *
         * @param imageUrl Base64 Data-URL des Bildes
         */
        public ImageURLChunk(String imageUrl) {
            super("image_url");
            this.imageUrl = imageUrl;
        }
        
        public String getImageUrl() {
            return imageUrl;
        }
        
        public void setImageUrl(String imageUrl) {
            this.imageUrl = imageUrl;
        }
    }
}