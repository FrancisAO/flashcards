package com.fao.flashcards.adapter.ocr.dto;

import com.fao.flashcards.application.model.OCROptions;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * DTO für Mistral OCR API Request.
 * Repräsentiert die JSON-Struktur für OCR-Anfragen an die Mistral API.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class MistralOCRRequest {
    
    @JsonProperty("model")
    private String model;
    
    @JsonProperty("id")
    private String id;
    
    @JsonProperty("document")
    private DocumentChunk document;
    
    @JsonProperty("pages")
    private List<Integer> pages;
    
    @JsonProperty("include_image_base64")
    private Boolean includeImageBase64;
    
    @JsonProperty("image_limit")
    private Integer imageLimit;
    
    @JsonProperty("image_min_size")
    private Integer imageMinSize;
    
    @JsonProperty("bbox_annotation_format")
    private ResponseFormat bboxAnnotationFormat;
    
    @JsonProperty("document_annotation_format")
    private ResponseFormat documentAnnotationFormat;
    
    /**
     * Standard-Konstruktor.
     */
    public MistralOCRRequest() {
    }
    
    /**
     * Konstruktor mit OCROptions.
     *
     * @param options OCR-Konfigurationsoptionen
     * @param document Dokument-Chunk
     */
    public MistralOCRRequest(OCROptions options, DocumentChunk document) {
        this.model = options.getModel();
        this.id = options.getId();
        this.document = document;
        this.pages = options.getPages();
        this.includeImageBase64 = options.getIncludeImageBase64();
        this.imageLimit = options.getImageLimit();
        this.imageMinSize = options.getImageMinSize();
        
        if (options.getBboxAnnotationFormat() != null) {
            this.bboxAnnotationFormat = ResponseFormat.fromOCROptions(options.getBboxAnnotationFormat());
        }
        
        if (options.getDocumentAnnotationFormat() != null) {
            this.documentAnnotationFormat = ResponseFormat.fromOCROptions(options.getDocumentAnnotationFormat());
        }
    }
    
    // Getter und Setter
    public String getModel() {
        return model;
    }
    
    public void setModel(String model) {
        this.model = model;
    }
    
    public String getId() {
        return id;
    }
    
    public void setId(String id) {
        this.id = id;
    }
    
    public DocumentChunk getDocument() {
        return document;
    }
    
    public void setDocument(DocumentChunk document) {
        this.document = document;
    }
    
    public List<Integer> getPages() {
        return pages;
    }
    
    public void setPages(List<Integer> pages) {
        this.pages = pages;
    }
    
    public Boolean getIncludeImageBase64() {
        return includeImageBase64;
    }
    
    public void setIncludeImageBase64(Boolean includeImageBase64) {
        this.includeImageBase64 = includeImageBase64;
    }
    
    public Integer getImageLimit() {
        return imageLimit;
    }
    
    public void setImageLimit(Integer imageLimit) {
        this.imageLimit = imageLimit;
    }
    
    public Integer getImageMinSize() {
        return imageMinSize;
    }
    
    public void setImageMinSize(Integer imageMinSize) {
        this.imageMinSize = imageMinSize;
    }
    
    public ResponseFormat getBboxAnnotationFormat() {
        return bboxAnnotationFormat;
    }
    
    public void setBboxAnnotationFormat(ResponseFormat bboxAnnotationFormat) {
        this.bboxAnnotationFormat = bboxAnnotationFormat;
    }
    
    public ResponseFormat getDocumentAnnotationFormat() {
        return documentAnnotationFormat;
    }
    
    public void setDocumentAnnotationFormat(ResponseFormat documentAnnotationFormat) {
        this.documentAnnotationFormat = documentAnnotationFormat;
    }
}