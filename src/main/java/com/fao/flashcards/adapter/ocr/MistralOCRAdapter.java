package com.fao.flashcards.adapter.ocr;

import com.fao.flashcards.adapter.ocr.dto.*;
import com.fao.flashcards.domain.exception.OCRProcessingException;
import com.fao.flashcards.domain.model.*;
import com.fao.flashcards.domain.port.OCRProcessingPort;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Mistral OCR API Adapter.
 * Implementiert OCRProcessingPort für die Verwendung der Mistral OCR API.
 */
public class MistralOCRAdapter implements OCRProcessingPort {
    
    private static final Logger logger = LoggerFactory.getLogger(MistralOCRAdapter.class);
    
    private static final String MISTRAL_OCR_ENDPOINT = "https://api.mistral.ai/v1/ocr";
    private static final long MAX_FILE_SIZE = 50 * 1024 * 1024; // 50 MB
    private static final List<String> SUPPORTED_MIME_TYPES = Arrays.asList(
        "image/jpeg", "image/jpg", "image/png", "image/gif", "image/bmp", "image/tiff",
        "application/pdf", "application/msword", 
        "application/vnd.openxmlformats-officedocument.wordprocessingml.document"
    );
    
    @Value("${mistral.api.key}")
    private String apiKey;
    
    private final RestTemplate restTemplate;
    
    /**
     * Konstruktor mit RestTemplate.
     *
     * @param restTemplate HTTP-Client für API-Aufrufe
     */
    public MistralOCRAdapter(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }
    
    /**
     * Standard-Konstruktor mit neuem RestTemplate.
     */
    public MistralOCRAdapter() {
        this.restTemplate = new RestTemplate();
    }
    
    @Override
    public OCRResult processFile(ProjectFile projectFile, InputStream fileStream, OCROptions options)
            throws OCRProcessingException {
        
        logger.info("Starte OCR-Verarbeitung für Datei: {}", projectFile.getOriginalFilename());
        long startTime = System.currentTimeMillis();
        
        try {
            // Validiere Datei
            byte[] fileData = readInputStream(fileStream);
            validateFile(projectFile, fileData.length);
            
            // Erstelle OCRResult
            ExtractedText extractedText = new ExtractedText(projectFile, "", getExtractionSource(projectFile));
            OCRResult ocrResult = new OCRResult(extractedText, OCRStatus.PROCESSING);
            
            // Konvertiere zu Base64
            String mimeType = projectFile.getContentType();
            String base64Data = convertToBase64(fileData, mimeType);
            
            // Erstelle Request
            DocumentChunk.ImageURLChunk documentChunk = new DocumentChunk.ImageURLChunk(base64Data);
            MistralOCRRequest request = new MistralOCRRequest(options, documentChunk);
            
            // Führe API-Aufruf durch
            MistralOCRResponse response = callMistralAPI(request);
            
            // Verarbeite Response
            String extractedContent = extractContentFromResponse(response);
            extractedText.setExtractedContent(extractedContent);
            
            // Aktualisiere OCRResult
            long processingTime = System.currentTimeMillis() - startTime;
            ocrResult.markAsSuccess(
                response.getUsageInfo() != null ? response.getUsageInfo().getPagesProcessed() : 1,
                processingTime,
                serializeResponse(response),
                response.getModel()
            );
            
            logger.info("OCR-Verarbeitung erfolgreich abgeschlossen für Datei: {} in {}ms", 
                projectFile.getOriginalFilename(), processingTime);
            
            return ocrResult;
            
        } catch (OCRProcessingException e) {
            logger.error("OCR-Verarbeitungsfehler für Datei {}: {}", projectFile.getOriginalFilename(), e.getMessage());
            throw e;
        } catch (Exception e) {
            logger.error("Unerwarteter Fehler bei OCR-Verarbeitung für Datei {}", projectFile.getOriginalFilename(), e);
            throw new OCRProcessingException("Unerwarteter Fehler bei OCR-Verarbeitung: " + e.getMessage(), e);
        }
    }
    
    @Override
    public List<OCRResult> processBatch(List<FileProcessingData> fileData, OCROptions options) 
            throws OCRProcessingException {
        
        logger.info("Starte Batch-OCR-Verarbeitung für {} Dateien", fileData.size());
        
        List<OCRResult> results = new ArrayList<>();
        
        for (FileProcessingData data : fileData) {
            try {
                OCRResult result = processFile(data.getProjectFile(), data.getFileStream(), options);
                results.add(result);
            } catch (OCRProcessingException e) {
                logger.warn("Fehler bei Batch-Verarbeitung für Datei {}: {}", 
                    data.getProjectFile().getOriginalFilename(), e.getMessage());
                
                // Erstelle fehlgeschlagenes OCRResult
                ExtractedText extractedText = new ExtractedText(
                    data.getProjectFile(), "", getExtractionSource(data.getProjectFile()));
                OCRResult failedResult = new OCRResult(extractedText, OCRStatus.FAILED);
                failedResult.markAsFailed(e.getMessage());
                results.add(failedResult);
            }
        }
        
        logger.info("Batch-OCR-Verarbeitung abgeschlossen. {} von {} Dateien erfolgreich verarbeitet", 
            results.stream().mapToInt(r -> r.getStatus() == OCRStatus.SUCCESS ? 1 : 0).sum(),
            fileData.size());
        
        return results;
    }
    
    @Override
    public String convertToBase64(InputStream imageStream, String mimeType) throws OCRProcessingException {
        try {
            byte[] imageData = readInputStream(imageStream);
            return convertToBase64(imageData, mimeType);
        } catch (IOException e) {
            throw new OCRProcessingException("Fehler beim Lesen des InputStreams: " + e.getMessage(), e);
        }
    }
    
    @Override
    public void validateFile(ProjectFile projectFile, long fileSize) throws OCRProcessingException {
        // Validiere Dateigröße
        if (fileSize > MAX_FILE_SIZE) {
            throw OCRProcessingException.fileSizeError(fileSize, MAX_FILE_SIZE);
        }
        
        // Validiere MIME-Type
        String mimeType = projectFile.getContentType();
        if (mimeType == null || !SUPPORTED_MIME_TYPES.contains(mimeType.toLowerCase())) {
            throw OCRProcessingException.validationError(
                "Nicht unterstützter Dateityp: " + mimeType + ". Unterstützte Typen: " + 
                String.join(", ", SUPPORTED_MIME_TYPES));
        }
        
        logger.debug("Datei-Validierung erfolgreich für: {} ({})", projectFile.getOriginalFilename(), mimeType);
    }
    
    @Override
    public long getMaxFileSize() {
        return MAX_FILE_SIZE;
    }
    
    @Override
    public List<String> getSupportedMimeTypes() {
        return new ArrayList<>(SUPPORTED_MIME_TYPES);
    }
    
    @Override
    public ExtractedText createExtractedText(OCRResult ocrResult, ProjectFile projectFile) {
        ExtractedText extractedText = new ExtractedText(
            projectFile, 
            "", 
            getExtractionSource(projectFile)
        );
        
        if (ocrResult.getExtractedText() != null && 
            ocrResult.getExtractedText().getExtractedContent() != null) {
            extractedText.setExtractedContent(ocrResult.getExtractedText().getExtractedContent());
        }
        
        return extractedText;
    }
    
    /**
     * Führt den API-Aufruf zur Mistral OCR API durch.
     *
     * @param request OCR-Request
     * @return OCR-Response
     * @throws OCRProcessingException bei API-Fehlern
     */
    private MistralOCRResponse callMistralAPI(MistralOCRRequest request) throws OCRProcessingException {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.setBearerAuth(apiKey);
            
            HttpEntity<MistralOCRRequest> entity = new HttpEntity<>(request, headers);
            
            logger.debug("Sende OCR-Request an Mistral API: {}", MISTRAL_OCR_ENDPOINT);
            
            ResponseEntity<MistralOCRResponse> response = restTemplate.exchange(
                MISTRAL_OCR_ENDPOINT,
                HttpMethod.POST,
                entity,
                MistralOCRResponse.class
            );
            
            if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
                logger.debug("OCR-Response erfolgreich erhalten");
                return response.getBody();
            } else {
                throw new OCRProcessingException("Unerwartete API-Antwort: " + response.getStatusCode());
            }
            
        } catch (HttpClientErrorException e) {
            logger.error("Client-Fehler bei API-Aufruf: {}", e.getMessage());
            
            if (e.getStatusCode() == HttpStatus.UNAUTHORIZED) {
                throw OCRProcessingException.authenticationError("Ungültiger API-Key");
            } else if (e.getStatusCode() == HttpStatus.UNPROCESSABLE_ENTITY) {
                String errorMessage = parseValidationError(e.getResponseBodyAsString());
                throw OCRProcessingException.validationError(errorMessage);
            } else {
                throw OCRProcessingException.apiError(e.getMessage(), e.getRawStatusCode());
            }
            
        } catch (HttpServerErrorException e) {
            logger.error("Server-Fehler bei API-Aufruf: {}", e.getMessage());
            throw OCRProcessingException.networkError("Mistral API Server-Fehler: " + e.getMessage(), e);
            
        } catch (Exception e) {
            logger.error("Unerwarteter Fehler bei API-Aufruf", e);
            throw OCRProcessingException.networkError("Netzwerk-Fehler: " + e.getMessage(), e);
        }
    }
    
    /**
     * Konvertiert Byte-Array zu Base64 Data-URL.
     *
     * @param fileData Datei-Daten
     * @param mimeType MIME-Type
     * @return Base64 Data-URL
     */
    private String convertToBase64(byte[] fileData, String mimeType) {
        String base64 = Base64.getEncoder().encodeToString(fileData);
        return "data:" + mimeType + ";base64," + base64;
    }
    
    /**
     * Liest InputStream zu Byte-Array.
     *
     * @param inputStream InputStream
     * @return Byte-Array
     * @throws IOException bei IO-Fehlern
     */
    private byte[] readInputStream(InputStream inputStream) throws IOException {
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        byte[] data = new byte[8192];
        int bytesRead;
        
        while ((bytesRead = inputStream.read(data, 0, data.length)) != -1) {
            buffer.write(data, 0, bytesRead);
        }
        
        return buffer.toByteArray();
    }
    
    /**
     * Extrahiert Textinhalt aus OCR-Response.
     *
     * @param response OCR-Response
     * @return Extrahierter Text
     */
    private String extractContentFromResponse(MistralOCRResponse response) {
        if (response.getPages() == null || response.getPages().isEmpty()) {
            return "";
        }
        
        return response.getPages().stream()
            .map(page -> page.getMarkdown() != null ? page.getMarkdown() : "")
            .collect(Collectors.joining("\n\n"));
    }
    
    /**
     * Serialisiert Response zu JSON-String.
     *
     * @param response OCR-Response
     * @return JSON-String
     */
    private String serializeResponse(MistralOCRResponse response) {
        try {
            // Einfache JSON-Serialisierung (könnte mit ObjectMapper verbessert werden)
            return response.toString();
        } catch (Exception e) {
            logger.warn("Fehler beim Serialisieren der Response: {}", e.getMessage());
            return "Response serialization failed";
        }
    }
    
    /**
     * Ermittelt ExtractionSource basierend auf Dateityp.
     *
     * @param projectFile Projektdatei
     * @return ExtractionSource
     */
    private ExtractionSource getExtractionSource(ProjectFile projectFile) {
        String mimeType = projectFile.getContentType();
        if (mimeType != null && mimeType.startsWith("image/")) {
            return ExtractionSource.IMAGE;
        } else {
            return ExtractionSource.DOCUMENT;
        }
    }
    
    /**
     * Parst Validierungsfehler aus API-Response.
     *
     * @param responseBody Response-Body als String
     * @return Formatierte Fehlermeldung
     */
    private String parseValidationError(String responseBody) {
        try {
            // Hier könnte mit ObjectMapper die MistralOCRErrorResponse geparst werden
            // Für jetzt eine einfache Fallback-Lösung
            return "Validierungsfehler: " + responseBody;
        } catch (Exception e) {
            logger.warn("Fehler beim Parsen des Validierungsfehlers: {}", e.getMessage());
            return "Unbekannter Validierungsfehler";
        }
    }
}