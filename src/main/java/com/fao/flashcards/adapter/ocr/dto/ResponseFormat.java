package com.fao.flashcards.adapter.ocr.dto;

import com.fao.flashcards.application.model.OCROptions;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * DTO für Response-Format in Mistral OCR API Requests.
 * Repräsentiert das Format für strukturierte Ausgaben.
 */
public class ResponseFormat {
    
    @JsonProperty("type")
    private String type;
    
    @JsonProperty("json_schema")
    private JsonSchema jsonSchema;
    
    /**
     * Standard-Konstruktor.
     */
    public ResponseFormat() {
    }
    
    /**
     * Konstruktor mit Type und JSON-Schema.
     *
     * @param type       Der Response-Typ
     * @param jsonSchema Das JSON-Schema (optional)
     */
    public ResponseFormat(String type, JsonSchema jsonSchema) {
        this.type = type;
        this.jsonSchema = jsonSchema;
    }
    
    public String getType() {
        return type;
    }
    
    public void setType(String type) {
        this.type = type;
    }
    
    public JsonSchema getJsonSchema() {
        return jsonSchema;
    }
    
    public void setJsonSchema(JsonSchema jsonSchema) {
        this.jsonSchema = jsonSchema;
    }
    
    /**
     * Konvertiert OCROptions.ResponseFormat zu DTO-ResponseFormat.
     *
     * @param ocrResponseFormat Das OCROptions.ResponseFormat
     * @return DTO-ResponseFormat
     */
    public static ResponseFormat fromOCROptions(OCROptions.ResponseFormat ocrResponseFormat) {
        if (ocrResponseFormat == null) {
            return null;
        }
        
        JsonSchema jsonSchema = null;
        if (ocrResponseFormat.getJsonSchema() != null) {
            jsonSchema = JsonSchema.fromOCROptions(ocrResponseFormat.getJsonSchema());
        }
        
        return new ResponseFormat(ocrResponseFormat.getType(), jsonSchema);
    }
    
    /**
     * DTO für JSON-Schema in Response-Format.
     */
    public static class JsonSchema {
        
        @JsonProperty("name")
        private String name;
        
        @JsonProperty("description")
        private String description;
        
        @JsonProperty("schema")
        private Object schema;
        
        @JsonProperty("strict")
        private Boolean strict;
        
        /**
         * Standard-Konstruktor.
         */
        public JsonSchema() {
        }
        
        /**
         * Konstruktor mit allen Parametern.
         *
         * @param name        Name des Schemas
         * @param description Beschreibung des Schemas
         * @param schema      Schema-Objekt
         * @param strict      Strict-Mode
         */
        public JsonSchema(String name, String description, Object schema, Boolean strict) {
            this.name = name;
            this.description = description;
            this.schema = schema;
            this.strict = strict;
        }
        
        public String getName() {
            return name;
        }
        
        public void setName(String name) {
            this.name = name;
        }
        
        public String getDescription() {
            return description;
        }
        
        public void setDescription(String description) {
            this.description = description;
        }
        
        public Object getSchema() {
            return schema;
        }
        
        public void setSchema(Object schema) {
            this.schema = schema;
        }
        
        public Boolean getStrict() {
            return strict;
        }
        
        public void setStrict(Boolean strict) {
            this.strict = strict;
        }
        
        /**
         * Konvertiert OCROptions.JsonSchema zu DTO-JsonSchema.
         *
         * @param ocrJsonSchema Das OCROptions.JsonSchema
         * @return DTO-JsonSchema
         */
        public static JsonSchema fromOCROptions(OCROptions.JsonSchema ocrJsonSchema) {
            if (ocrJsonSchema == null) {
                return null;
            }
            
            return new JsonSchema(
                ocrJsonSchema.getName(),
                ocrJsonSchema.getDescription(),
                ocrJsonSchema.getSchema(),
                ocrJsonSchema.getStrict()
            );
        }
    }
}