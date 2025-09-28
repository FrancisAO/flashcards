package com.fao.flashcards.ocr.adapter.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * DTO für Mistral OCR API Response.
 * Repräsentiert die JSON-Struktur für OCR-Antworten von der Mistral API.
 */
public class MistralOCRResponse {
    
    @JsonProperty("pages")
    private List<PageResult> pages;
    
    @JsonProperty("model")
    private String model;
    
    @JsonProperty("document_annotation")
    private String documentAnnotation;
    
    @JsonProperty("usage_info")
    private UsageInfo usageInfo;
    
    /**
     * Standard-Konstruktor.
     */
    public MistralOCRResponse() {
    }
    
    public List<PageResult> getPages() {
        return pages;
    }
    
    public void setPages(List<PageResult> pages) {
        this.pages = pages;
    }
    
    public String getModel() {
        return model;
    }
    
    public void setModel(String model) {
        this.model = model;
    }
    
    public String getDocumentAnnotation() {
        return documentAnnotation;
    }
    
    public void setDocumentAnnotation(String documentAnnotation) {
        this.documentAnnotation = documentAnnotation;
    }
    
    public UsageInfo getUsageInfo() {
        return usageInfo;
    }
    
    public void setUsageInfo(UsageInfo usageInfo) {
        this.usageInfo = usageInfo;
    }
    
    /**
     * DTO für Seiten-Ergebnisse.
     */
    public static class PageResult {
        
        @JsonProperty("index")
        private Integer index;
        
        @JsonProperty("markdown")
        private String markdown;
        
        @JsonProperty("images")
        private List<ImageResult> images;
        
        @JsonProperty("dimensions")
        private PageDimensions dimensions;
        
        /**
         * Standard-Konstruktor.
         */
        public PageResult() {
        }
        
        public Integer getIndex() {
            return index;
        }
        
        public void setIndex(Integer index) {
            this.index = index;
        }
        
        public String getMarkdown() {
            return markdown;
        }
        
        public void setMarkdown(String markdown) {
            this.markdown = markdown;
        }
        
        public List<ImageResult> getImages() {
            return images;
        }
        
        public void setImages(List<ImageResult> images) {
            this.images = images;
        }
        
        public PageDimensions getDimensions() {
            return dimensions;
        }
        
        public void setDimensions(PageDimensions dimensions) {
            this.dimensions = dimensions;
        }
    }
    
    /**
     * DTO für Bild-Ergebnisse.
     */
    public static class ImageResult {
        
        @JsonProperty("id")
        private String id;
        
        @JsonProperty("top_left_x")
        private Integer topLeftX;
        
        @JsonProperty("top_left_y")
        private Integer topLeftY;
        
        @JsonProperty("bottom_right_x")
        private Integer bottomRightX;
        
        @JsonProperty("bottom_right_y")
        private Integer bottomRightY;
        
        @JsonProperty("image_base64")
        private String imageBase64;
        
        @JsonProperty("image_annotation")
        private String imageAnnotation;
        
        /**
         * Standard-Konstruktor.
         */
        public ImageResult() {
        }
        
        public String getId() {
            return id;
        }
        
        public void setId(String id) {
            this.id = id;
        }
        
        public Integer getTopLeftX() {
            return topLeftX;
        }
        
        public void setTopLeftX(Integer topLeftX) {
            this.topLeftX = topLeftX;
        }
        
        public Integer getTopLeftY() {
            return topLeftY;
        }
        
        public void setTopLeftY(Integer topLeftY) {
            this.topLeftY = topLeftY;
        }
        
        public Integer getBottomRightX() {
            return bottomRightX;
        }
        
        public void setBottomRightX(Integer bottomRightX) {
            this.bottomRightX = bottomRightX;
        }
        
        public Integer getBottomRightY() {
            return bottomRightY;
        }
        
        public void setBottomRightY(Integer bottomRightY) {
            this.bottomRightY = bottomRightY;
        }
        
        public String getImageBase64() {
            return imageBase64;
        }
        
        public void setImageBase64(String imageBase64) {
            this.imageBase64 = imageBase64;
        }
        
        public String getImageAnnotation() {
            return imageAnnotation;
        }
        
        public void setImageAnnotation(String imageAnnotation) {
            this.imageAnnotation = imageAnnotation;
        }
    }
    
    /**
     * DTO für Seiten-Dimensionen.
     */
    public static class PageDimensions {
        
        @JsonProperty("dpi")
        private Integer dpi;
        
        @JsonProperty("height")
        private Integer height;
        
        @JsonProperty("width")
        private Integer width;
        
        /**
         * Standard-Konstruktor.
         */
        public PageDimensions() {
        }
        
        public Integer getDpi() {
            return dpi;
        }
        
        public void setDpi(Integer dpi) {
            this.dpi = dpi;
        }
        
        public Integer getHeight() {
            return height;
        }
        
        public void setHeight(Integer height) {
            this.height = height;
        }
        
        public Integer getWidth() {
            return width;
        }
        
        public void setWidth(Integer width) {
            this.width = width;
        }
    }
    
    /**
     * DTO für Nutzungsinformationen.
     */
    public static class UsageInfo {
        
        @JsonProperty("pages_processed")
        private Integer pagesProcessed;
        
        @JsonProperty("doc_size_bytes")
        private Long docSizeBytes;
        
        /**
         * Standard-Konstruktor.
         */
        public UsageInfo() {
        }
        
        public Integer getPagesProcessed() {
            return pagesProcessed;
        }
        
        public void setPagesProcessed(Integer pagesProcessed) {
            this.pagesProcessed = pagesProcessed;
        }
        
        public Long getDocSizeBytes() {
            return docSizeBytes;
        }
        
        public void setDocSizeBytes(Long docSizeBytes) {
            this.docSizeBytes = docSizeBytes;
        }
    }
}