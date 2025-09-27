package com.fao.flashcards.application.port;

import java.io.InputStream;
import java.util.List;

import com.fao.flashcards.application.model.ExtractedText;
import com.fao.flashcards.application.model.OCROptions;
import com.fao.flashcards.application.model.OCRResult;
import com.fao.flashcards.application.model.ProjectFile;
import com.fao.flashcards.application.port.exception.OCRProcessingException;

/**
 * Port-Interface für OCR-Verarbeitung.
 * Definiert die Schnittstelle für OCR-Services und ermöglicht verschiedene Implementierungen
 * (z.B. Mistral, Google Vision, etc.) ohne Änderungen am Domain-Code.
 */
public interface OCRProcessingPort {
    
    /**
     * Führt OCR-Verarbeitung für eine einzelne Datei durch.
     * 
     * @param projectFile Die zu verarbeitende Projektdatei
     * @param fileStream  InputStream der Datei
     * @param options     OCR-Konfigurationsoptionen
     * @return OCRResult mit Verarbeitungsergebnis
     * @throws OCRProcessingException bei Verarbeitungsfehlern
     */
    OCRResult processFile(ProjectFile projectFile, InputStream fileStream, OCROptions options) 
            throws OCRProcessingException;
    
    /**
     * Führt OCR-Verarbeitung für eine einzelne Datei mit Standard-Optionen durch.
     * 
     * @param projectFile Die zu verarbeitende Projektdatei
     * @param fileStream  InputStream der Datei
     * @return OCRResult mit Verarbeitungsergebnis
     * @throws OCRProcessingException bei Verarbeitungsfehlern
     */
    default OCRResult processFile(ProjectFile projectFile, InputStream fileStream) 
            throws OCRProcessingException {
        return processFile(projectFile, fileStream, OCROptions.defaultOptions());
    }
    
    /**
     * Führt Batch-OCR-Verarbeitung für mehrere Dateien durch.
     * 
     * @param fileData Liste von FileProcessingData mit Dateien und Streams
     * @param options  OCR-Konfigurationsoptionen
     * @return Liste von OCRResults für jede verarbeitete Datei
     * @throws OCRProcessingException bei Verarbeitungsfehlern
     */
    List<OCRResult> processBatch(List<FileProcessingData> fileData, OCROptions options) 
            throws OCRProcessingException;
    
    /**
     * Führt Batch-OCR-Verarbeitung für mehrere Dateien mit Standard-Optionen durch.
     * 
     * @param fileData Liste von FileProcessingData mit Dateien und Streams
     * @return Liste von OCRResults für jede verarbeitete Datei
     * @throws OCRProcessingException bei Verarbeitungsfehlern
     */
    default List<OCRResult> processBatch(List<FileProcessingData> fileData) 
            throws OCRProcessingException {
        return processBatch(fileData, OCROptions.defaultOptions());
    }
    
    /**
     * Konvertiert Bilddaten zu Base64-String.
     * 
     * @param imageStream InputStream des Bildes
     * @param mimeType    MIME-Type der Bilddatei
     * @return Base64-codierter String im Data-URL-Format
     * @throws OCRProcessingException bei Konvertierungsfehlern
     */
    String convertToBase64(InputStream imageStream, String mimeType) throws OCRProcessingException;
    
    /**
     * Validiert, ob eine Datei für OCR-Verarbeitung geeignet ist.
     * 
     * @param projectFile Die zu validierende Projektdatei
     * @param fileSize    Dateigröße in Bytes
     * @throws OCRProcessingException wenn die Datei nicht geeignet ist
     */
    void validateFile(ProjectFile projectFile, long fileSize) throws OCRProcessingException;
    
    /**
     * Gibt die maximale unterstützte Dateigröße in Bytes zurück.
     * 
     * @return Maximale Dateigröße in Bytes
     */
    long getMaxFileSize();
    
    /**
     * Gibt die unterstützten Dateiformate zurück.
     * 
     * @return Liste der unterstützten MIME-Types
     */
    List<String> getSupportedMimeTypes();
    
    /**
     * Erstellt ein ExtractedText-Objekt aus einem OCRResult.
     * 
     * @param ocrResult   Das OCRResult mit den Verarbeitungsergebnissen
     * @param projectFile Die zugehörige Projektdatei
     * @return ExtractedText-Objekt
     */
    ExtractedText createExtractedText(OCRResult ocrResult, ProjectFile projectFile);
    
    /**
     * Datenklasse für Batch-Verarbeitung.
     * Kapselt ProjectFile und zugehörigen InputStream.
     */
    class FileProcessingData {
        private final ProjectFile projectFile;
        private final InputStream fileStream;
        private final long fileSize;
        
        /**
         * Konstruktor für FileProcessingData.
         * 
         * @param projectFile Die Projektdatei
         * @param fileStream  InputStream der Datei
         * @param fileSize    Größe der Datei in Bytes
         */
        public FileProcessingData(ProjectFile projectFile, InputStream fileStream, long fileSize) {
            this.projectFile = projectFile;
            this.fileStream = fileStream;
            this.fileSize = fileSize;
        }
        
        /**
         * Gibt die Projektdatei zurück.
         * 
         * @return ProjectFile
         */
        public ProjectFile getProjectFile() {
            return projectFile;
        }
        
        /**
         * Gibt den InputStream zurück.
         * 
         * @return InputStream der Datei
         */
        public InputStream getFileStream() {
            return fileStream;
        }
        
        /**
         * Gibt die Dateigröße zurück.
         * 
         * @return Dateigröße in Bytes
         */
        public long getFileSize() {
            return fileSize;
        }
    }
}