package com.fao.flashcards.application.model;

import java.util.List;

/**
 * Konfigurationsklasse für OCR-Verarbeitung mit der Mistral API.
 * Verwendet das Builder Pattern für einfache und flexible Instanziierung.
 */
public class OCROptions {
    
    private final String model;
    private final String id;
    private final List<Integer> pages;
    private final Boolean includeImageBase64;
    private final Integer imageLimit;
    private final Integer imageMinSize;
    private final ResponseFormat bboxAnnotationFormat;
    private final ResponseFormat documentAnnotationFormat;
    
    private OCROptions(Builder builder) {
        this.model = builder.model;
        this.id = builder.id;
        this.pages = builder.pages;
        this.includeImageBase64 = builder.includeImageBase64;
        this.imageLimit = builder.imageLimit;
        this.imageMinSize = builder.imageMinSize;
        this.bboxAnnotationFormat = builder.bboxAnnotationFormat;
        this.documentAnnotationFormat = builder.documentAnnotationFormat;
    }
    
    /**
     * Gibt das zu verwendende OCR-Model zurück.
     *
     * @return Model-Name
     */
    public String getModel() {
        return model;
    }
    
    /**
     * Gibt die ID für die OCR-Anfrage zurück.
     *
     * @return ID
     */
    public String getId() {
        return id;
    }
    
    /**
     * Gibt die zu verarbeitenden Seiten zurück.
     *
     * @return Liste der Seitennummern (0-basiert)
     */
    public List<Integer> getPages() {
        return pages;
    }
    
    /**
     * Gibt zurück, ob Base64-Bilder in der Antwort enthalten sein sollen.
     *
     * @return true wenn Base64-Bilder enthalten sein sollen
     */
    public Boolean getIncludeImageBase64() {
        return includeImageBase64;
    }
    
    /**
     * Gibt das Limit für zu extrahierende Bilder zurück.
     *
     * @return Maximale Anzahl der Bilder
     */
    public Integer getImageLimit() {
        return imageLimit;
    }
    
    /**
     * Gibt die minimale Bildgröße für die Extraktion zurück.
     *
     * @return Minimale Höhe und Breite der Bilder
     */
    public Integer getImageMinSize() {
        return imageMinSize;
    }
    
    /**
     * Gibt das Format für Bounding-Box-Annotationen zurück.
     *
     * @return ResponseFormat für Bounding-Boxes
     */
    public ResponseFormat getBboxAnnotationFormat() {
        return bboxAnnotationFormat;
    }
    
    /**
     * Gibt das Format für Dokument-Annotationen zurück.
     *
     * @return ResponseFormat für Dokumente
     */
    public ResponseFormat getDocumentAnnotationFormat() {
        return documentAnnotationFormat;
    }
    
    /**
     * Erstellt einen neuen Builder für OCROptions.
     *
     * @return Builder-Instanz
     */
    public static Builder builder() {
        return new Builder();
    }
    
    /**
     * Erstellt Standard-OCROptions für die Mistral API.
     *
     * @return OCROptions mit Standard-Konfiguration
     */
    public static OCROptions defaultOptions() {
        return builder()
                .model("mistral-ocr-latest")
                .includeImageBase64(true)
                .build();
    }
    
    /**
     * Builder-Klasse für OCROptions.
     */
    public static class Builder {
        private String model = "mistral-ocr-latest";
        private String id;
        private List<Integer> pages;
        private Boolean includeImageBase64;
        private Integer imageLimit;
        private Integer imageMinSize;
        private ResponseFormat bboxAnnotationFormat;
        private ResponseFormat documentAnnotationFormat;
        
        private Builder() {
        }
        
        /**
         * Setzt das zu verwendende OCR-Model.
         *
         * @param model Model-Name
         * @return Builder
         */
        public Builder model(String model) {
            this.model = model;
            return this;
        }
        
        /**
         * Setzt die ID für die OCR-Anfrage.
         *
         * @param id ID
         * @return Builder
         */
        public Builder id(String id) {
            this.id = id;
            return this;
        }
        
        /**
         * Setzt die zu verarbeitenden Seiten.
         *
         * @param pages Liste der Seitennummern (0-basiert)
         * @return Builder
         */
        public Builder pages(List<Integer> pages) {
            this.pages = pages;
            return this;
        }
        
        /**
         * Setzt, ob Base64-Bilder in der Antwort enthalten sein sollen.
         *
         * @param includeImageBase64 true wenn Base64-Bilder enthalten sein sollen
         * @return Builder
         */
        public Builder includeImageBase64(Boolean includeImageBase64) {
            this.includeImageBase64 = includeImageBase64;
            return this;
        }
        
        /**
         * Setzt das Limit für zu extrahierende Bilder.
         *
         * @param imageLimit Maximale Anzahl der Bilder
         * @return Builder
         */
        public Builder imageLimit(Integer imageLimit) {
            this.imageLimit = imageLimit;
            return this;
        }
        
        /**
         * Setzt die minimale Bildgröße für die Extraktion.
         *
         * @param imageMinSize Minimale Höhe und Breite der Bilder
         * @return Builder
         */
        public Builder imageMinSize(Integer imageMinSize) {
            this.imageMinSize = imageMinSize;
            return this;
        }
        
        /**
         * Setzt das Format für Bounding-Box-Annotationen.
         *
         * @param bboxAnnotationFormat ResponseFormat für Bounding-Boxes
         * @return Builder
         */
        public Builder bboxAnnotationFormat(ResponseFormat bboxAnnotationFormat) {
            this.bboxAnnotationFormat = bboxAnnotationFormat;
            return this;
        }
        
        /**
         * Setzt das Format für Dokument-Annotationen.
         *
         * @param documentAnnotationFormat ResponseFormat für Dokumente
         * @return Builder
         */
        public Builder documentAnnotationFormat(ResponseFormat documentAnnotationFormat) {
            this.documentAnnotationFormat = documentAnnotationFormat;
            return this;
        }
        
        /**
         * Erstellt die OCROptions-Instanz.
         *
         * @return OCROptions
         */
        public OCROptions build() {
            return new OCROptions(this);
        }
    }
    
    /**
     * Klasse für Response-Format-Konfiguration.
     */
    public static class ResponseFormat {
        private final String type;
        private final JsonSchema jsonSchema;
        
        public ResponseFormat(String type, JsonSchema jsonSchema) {
            this.type = type;
            this.jsonSchema = jsonSchema;
        }
        
        public String getType() {
            return type;
        }
        
        public JsonSchema getJsonSchema() {
            return jsonSchema;
        }
        
        /**
         * Erstellt ein Text-ResponseFormat.
         *
         * @return ResponseFormat für Text
         */
        public static ResponseFormat text() {
            return new ResponseFormat("text", null);
        }
        
        /**
         * Erstellt ein JSON-Object-ResponseFormat.
         *
         * @return ResponseFormat für JSON-Objekte
         */
        public static ResponseFormat jsonObject() {
            return new ResponseFormat("json_object", null);
        }
        
        /**
         * Erstellt ein JSON-Schema-ResponseFormat.
         *
         * @param jsonSchema JSON-Schema-Definition
         * @return ResponseFormat für JSON-Schema
         */
        public static ResponseFormat jsonSchema(JsonSchema jsonSchema) {
            return new ResponseFormat("json_schema", jsonSchema);
        }
    }
    
    /**
     * Klasse für JSON-Schema-Konfiguration.
     */
    public static class JsonSchema {
        private final String name;
        private final String description;
        private final Object schema;
        private final Boolean strict;
        
        public JsonSchema(String name, String description, Object schema, Boolean strict) {
            this.name = name;
            this.description = description;
            this.schema = schema;
            this.strict = strict;
        }
        
        public String getName() {
            return name;
        }
        
        public String getDescription() {
            return description;
        }
        
        public Object getSchema() {
            return schema;
        }
        
        public Boolean getStrict() {
            return strict;
        }
    }
}