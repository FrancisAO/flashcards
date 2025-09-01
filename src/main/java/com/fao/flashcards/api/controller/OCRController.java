package com.fao.flashcards.api.controller;

import com.fao.flashcards.api.dto.*;
import com.fao.flashcards.domain.exception.OCRProcessingException;
import com.fao.flashcards.domain.model.*;
import com.fao.flashcards.domain.service.OCRService;
import com.fao.flashcards.domain.service.ProjectService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

/**
 * REST Controller für OCR-Verarbeitung.
 * Stellt Endpoints für OCR-Operationen, Text-Extraktion und -Bearbeitung bereit.
 */
@RestController
@RequestMapping("/api/v1/ocr")
@Validated
@Slf4j
@CrossOrigin(origins = "*", maxAge = 3600)
public class OCRController {

    private final OCRService ocrService;
    private final ProjectService projectService;
    private final DTOMapper dtoMapper;

    @Autowired
    public OCRController(OCRService ocrService, 
                        ProjectService projectService,
                        DTOMapper dtoMapper) {
        this.ocrService = ocrService;
        this.projectService = projectService;
        this.dtoMapper = dtoMapper;
    }

    /**
     * POST /api/v1/ocr/extract/{fileId} - OCR für einzelne Datei
     */
    @PostMapping("/extract/{fileId}")
    public ResponseEntity<OCRProcessResponse> extractTextFromFile(
            @PathVariable("fileId") String fileId,
            @RequestParam(value = "async", defaultValue = "false") boolean async,
            @RequestBody(required = false) OCRProcessRequest.OCROptionsDTO options) {
        
        log.info("POST /api/v1/ocr/extract/{} - async: {}", fileId, async);
        
        try {
            // OCR-Optionen konvertieren
            OCROptions ocrOptions = options != null ? 
                dtoMapper.toOCROptions(options) : OCROptions.defaultOptions();
            
            if (async) {
                // Asynchrone Verarbeitung
                CompletableFuture<ExtractedText> future = ocrService.processFileAsync(fileId, ocrOptions);
                
                // Sofortige Antwort mit PROCESSING Status
                OCRProcessResponse response = OCRProcessResponse.singleFileSuccess(
                    fileId, "unknown", null, null, null, null);
                response.setOverallStatus(OCRStatus.PROCESSING);
                response.setMessage("OCR-Verarbeitung gestartet (asynchron)");
                
                log.info("Asynchrone OCR-Verarbeitung gestartet für Datei: {}", fileId);
                return ResponseEntity.accepted().body(response);
                
            } else {
                // Synchrone Verarbeitung
                ExtractedText extractedText = ocrService.processFile(fileId, ocrOptions);
                
                // OCR-Result für Response laden
                List<OCRResult> ocrResults = ocrService.getProjectOCRResults(
                    extractedText.getProjectFile().getProject().getId());
                
                OCRResult ocrResult = ocrResults.stream()
                    .filter(r -> r.getExtractedText().getId().equals(extractedText.getId()))
                    .findFirst()
                    .orElse(null);
                
                OCRProcessResponse response = dtoMapper.toOCRProcessResponse(extractedText, ocrResult);
                
                log.info("Synchrone OCR-Verarbeitung abgeschlossen für Datei: {}", fileId);
                return ResponseEntity.ok(response);
            }
            
        } catch (EntityNotFoundException e) {
            log.warn("Datei nicht gefunden für OCR-Verarbeitung: {}", fileId);
            return ResponseEntity.notFound().build();
        } catch (IllegalStateException e) {
            log.warn("OCR-Verarbeitung nicht möglich: {}", e.getMessage());
            return ResponseEntity.badRequest().body(
                OCRProcessResponse.singleFileError(fileId, "unknown", e.getMessage()));
        } catch (OCRProcessingException e) {
            log.error("OCR-Verarbeitungsfehler für Datei {}: {}", fileId, e.getMessage());
            return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(
                OCRProcessResponse.singleFileError(fileId, "unknown", e.getMessage()));
        } catch (Exception e) {
            log.error("Unerwarteter Fehler bei OCR-Verarbeitung für Datei: {}", fileId, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                OCRProcessResponse.singleFileError(fileId, "unknown", "Unerwarteter Fehler"));
        }
    }

    /**
     * POST /api/v1/ocr/extract/batch - Batch-OCR für mehrere Dateien
     */
    @PostMapping("/extract/batch")
    public ResponseEntity<OCRProcessResponse> extractTextFromBatch(
            @Valid @RequestBody OCRProcessRequest request,
            @RequestParam(value = "async", defaultValue = "false") boolean async) {
        
        log.info("POST /api/v1/ocr/extract/batch - {} Dateien, async: {}", 
                request.getFileCount(), async);
        
        try {
            if (!request.isValid()) {
                log.warn("Ungültige Batch-OCR-Request: {}", request);
                return ResponseEntity.badRequest().build();
            }
            
            // OCR-Optionen konvertieren
            OCROptions ocrOptions = dtoMapper.toOCROptions(request.toOCROptions());
            
            if (async) {
                // Asynchrone Batch-Verarbeitung
                CompletableFuture<List<ExtractedText>> future = 
                    ocrService.processBatchAsync(request.getFileIds(), ocrOptions);
                
                // Sofortige Antwort
                OCRProcessResponse response = new OCRProcessResponse();
                response.setSuccess(true);
                response.setMessage("Batch-OCR-Verarbeitung gestartet (asynchron)");
                response.setOverallStatus(OCRStatus.PROCESSING);
                
                log.info("Asynchrone Batch-OCR-Verarbeitung gestartet für {} Dateien", 
                        request.getFileCount());
                return ResponseEntity.accepted().body(response);
                
            } else {
                // Synchrone Batch-Verarbeitung
                List<ExtractedText> extractedTexts = ocrService.processBatch(
                    request.getFileIds(), ocrOptions);
                
                // Response zusammenbauen
                List<OCRProcessResponse.OCRFileResult> results = extractedTexts.stream()
                    .map(et -> {
                        ProjectFile pf = et.getProjectFile();
                        return OCRProcessResponse.OCRFileResult.success(
                            pf.getId(), pf.getOriginalFilename(), et.getId(), 
                            null, null, null); // Diese Werte könnten aus OCRResult geladen werden
                    })
                    .collect(Collectors.toList());
                
                OCRProcessResponse response = OCRProcessResponse.batchResult(results);
                
                log.info("Synchrone Batch-OCR-Verarbeitung abgeschlossen: {} von {} erfolgreich", 
                        extractedTexts.size(), request.getFileCount());
                return ResponseEntity.ok(response);
            }
            
        } catch (OCRProcessingException e) {
            log.error("Batch-OCR-Verarbeitungsfehler: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).build();
        } catch (Exception e) {
            log.error("Unerwarteter Fehler bei Batch-OCR-Verarbeitung", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * GET /api/v1/ocr/status/{fileId} - OCR-Status abrufen
     */
    @GetMapping("/status/{fileId}")
    public ResponseEntity<OCRResultDTO> getOCRStatus(@PathVariable("fileId") String fileId) {
        log.debug("GET /api/v1/ocr/status/{}", fileId);
        
        try {
            // Hier müsste eine Methode im OCRService existieren, um OCRResult anhand der FileId zu finden
            // Da das nicht direkt verfügbar ist, implementieren wir einen Workaround
            
            // Erstmal versuchen ExtractedText zu finden
            List<ExtractedText> allTexts = ocrService.getProjectExtractedTexts("dummy"); // Das müsste anders gemacht werden
            
            // Placeholder - das müsste über eine dedizierte Service-Methode gelöst werden
            OCRResultDTO result = new OCRResultDTO(OCRStatus.PENDING);
            
            log.debug("OCR-Status für Datei {}: {}", fileId, result.getStatus());
            return ResponseEntity.ok(result);
            
        } catch (EntityNotFoundException e) {
            log.warn("Datei nicht gefunden für Status-Abfrage: {}", fileId);
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            log.error("Fehler beim Abrufen des OCR-Status für Datei: {}", fileId, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * GET /api/v1/ocr/project/{projectId}/texts - Alle extrahierten Texte eines Projekts
     */
    @GetMapping("/project/{projectId}/texts")
    public ResponseEntity<List<ExtractedTextDTO>> getProjectExtractedTexts(@PathVariable("projectId") String projectId) {
        log.debug("GET /api/v1/ocr/project/{}/texts", projectId);
        
        try {
            // Projekt existiert prüfen
            projectService.getProjectById(projectId);
            
            // Extrahierte Texte laden
            List<ExtractedText> extractedTexts = ocrService.getProjectExtractedTexts(projectId);
            List<ExtractedTextDTO> textDTOs = dtoMapper.toExtractedTextDTOList(extractedTexts);
            
            log.debug("Returning {} extracted texts for project {}", textDTOs.size(), projectId);
            return ResponseEntity.ok(textDTOs);
            
        } catch (EntityNotFoundException e) {
            log.warn("Projekt nicht gefunden für ExtractedText-Liste: {}", projectId);
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            log.error("Fehler beim Laden der ExtractedTexts für Projekt: {}", projectId, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * GET /api/v1/ocr/text/{textId} - Einzelnen extrahierten Text abrufen
     */
    @GetMapping("/text/{textId}")
    public ResponseEntity<ExtractedTextDTO> getExtractedText(@PathVariable("textId") String textId) {
        log.debug("GET /api/v1/ocr/text/{}", textId);
        
        try {
            // Hier bräuchten wir eine Service-Methode um ExtractedText by ID zu laden
            // Da diese nicht direkt verfügbar ist, müssten wir über Repository gehen
            // Für jetzt machen wir einen Placeholder
            
            // Diese Methode müsste im OCRService hinzugefügt werden
            ExtractedTextDTO textDTO = new ExtractedTextDTO();
            textDTO.setId(textId);
            
            log.debug("ExtractedText abgerufen: {}", textId);
            return ResponseEntity.ok(textDTO);
            
        } catch (EntityNotFoundException e) {
            log.warn("ExtractedText nicht gefunden: {}", textId);
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            log.error("Fehler beim Abrufen des ExtractedText: {}", textId, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * PUT /api/v1/ocr/text/{textId} - Extrahierten Text bearbeiten
     */
    @PutMapping("/text/{textId}")
    public ResponseEntity<ExtractedTextDTO> updateExtractedText(
            @PathVariable("textId") String textId,
            @Valid @RequestBody UpdateTextRequest request) {
        
        log.info("PUT /api/v1/ocr/text/{} - Text bearbeiten", textId);
        
        try {
            // Text normalisieren falls gewünscht
            request.normalizeContent();
            
            // ExtractedText bearbeiten
            ExtractedText updatedText = ocrService.editExtractedText(textId, request.getEditedContent());
            ExtractedTextDTO textDTO = dtoMapper.toDTO(updatedText);
            
            log.info("ExtractedText erfolgreich bearbeitet: {}", textId);
            return ResponseEntity.ok(textDTO);
            
        } catch (EntityNotFoundException e) {
            log.warn("ExtractedText nicht gefunden für Bearbeitung: {}", textId);
            return ResponseEntity.notFound().build();
        } catch (IllegalArgumentException e) {
            log.warn("Ungültige Bearbeitungs-Daten für ExtractedText {}: {}", textId, e.getMessage());
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            log.error("Fehler beim Bearbeiten des ExtractedText: {}", textId, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * POST /api/v1/ocr/reprocess/{fileId} - OCR erneut durchführen
     */
    @PostMapping("/reprocess/{fileId}")
    public ResponseEntity<OCRProcessResponse> reprocessFile(
            @PathVariable("fileId") String fileId,
            @RequestParam(value = "async", defaultValue = "false") boolean async,
            @RequestBody(required = false) OCRProcessRequest.OCROptionsDTO options) {
        
        log.info("POST /api/v1/ocr/reprocess/{} - async: {}", fileId, async);
        
        try {
            // Zuerst bestehenden ExtractedText zurücksetzen/löschen falls vorhanden
            // Das müsste durch eine entsprechende Service-Methode unterstützt werden
            
            // Dann normale OCR-Verarbeitung durchführen
            OCROptions ocrOptions = options != null ? 
                dtoMapper.toOCROptions(options) : OCROptions.defaultOptions();
            
            if (async) {
                // Asynchrone Wiederverarbeitung
                CompletableFuture<ExtractedText> future = ocrService.processFileAsync(fileId, ocrOptions);
                
                OCRProcessResponse response = OCRProcessResponse.singleFileSuccess(
                    fileId, "unknown", null, null, null, null);
                response.setOverallStatus(OCRStatus.PROCESSING);
                response.setMessage("OCR-Wiederverarbeitung gestartet (asynchron)");
                
                log.info("Asynchrone OCR-Wiederverarbeitung gestartet für Datei: {}", fileId);
                return ResponseEntity.accepted().body(response);
                
            } else {
                // Synchrone Wiederverarbeitung
                ExtractedText extractedText = ocrService.processFile(fileId, ocrOptions);
                
                // Response erstellen (vereinfacht)
                OCRProcessResponse response = OCRProcessResponse.singleFileSuccess(
                    fileId, extractedText.getProjectFile().getOriginalFilename(), 
                    extractedText.getId(), null, null, null);
                
                log.info("Synchrone OCR-Wiederverarbeitung abgeschlossen für Datei: {}", fileId);
                return ResponseEntity.ok(response);
            }
            
        } catch (EntityNotFoundException e) {
            log.warn("Datei nicht gefunden für OCR-Wiederverarbeitung: {}", fileId);
            return ResponseEntity.notFound().build();
        } catch (OCRProcessingException e) {
            log.error("OCR-Wiederverarbeitungsfehler für Datei {}: {}", fileId, e.getMessage());
            return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(
                OCRProcessResponse.singleFileError(fileId, "unknown", e.getMessage()));
        } catch (Exception e) {
            log.error("Unerwarteter Fehler bei OCR-Wiederverarbeitung für Datei: {}", fileId, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                OCRProcessResponse.singleFileError(fileId, "unknown", "Unerwarteter Fehler"));
        }
    }

    /**
     * DELETE /api/v1/ocr/text/{textId} - Extrahierten Text zurücksetzen
     */
    @DeleteMapping("/text/{textId}")
    public ResponseEntity<ExtractedTextDTO> resetExtractedText(@PathVariable("textId") String textId) {
        log.info("DELETE /api/v1/ocr/text/{} - Text zurücksetzen", textId);
        
        try {
            ExtractedText resetText = ocrService.resetEditedText(textId);
            ExtractedTextDTO textDTO = dtoMapper.toDTO(resetText);
            
            log.info("ExtractedText erfolgreich zurückgesetzt: {}", textId);
            return ResponseEntity.ok(textDTO);
            
        } catch (EntityNotFoundException e) {
            log.warn("ExtractedText nicht gefunden für Reset: {}", textId);
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            log.error("Fehler beim Zurücksetzen des ExtractedText: {}", textId, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * GET /api/v1/ocr/project/{projectId}/stats - OCR-Statistiken für Projekt
     */
    @GetMapping("/project/{projectId}/stats")
    public ResponseEntity<OCRService.OCRProjectStatistics> getProjectOCRStats(@PathVariable("projectId") String projectId) {
        log.debug("GET /api/v1/ocr/project/{}/stats", projectId);
        
        try {
            // Projekt existiert prüfen
            projectService.getProjectById(projectId);
            
            // OCR-Statistiken laden
            OCRService.OCRProjectStatistics stats = ocrService.getProjectStatistics(projectId);
            
            log.debug("OCR-Statistiken für Projekt {}: {} von {} Dateien verarbeitet", 
                     projectId, stats.getProcessedFiles(), stats.getTotalFiles());
            
            return ResponseEntity.ok(stats);
            
        } catch (EntityNotFoundException e) {
            log.warn("Projekt nicht gefunden für OCR-Statistiken: {}", projectId);
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            log.error("Fehler beim Abrufen der OCR-Statistiken für Projekt: {}", projectId, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * GET /api/v1/ocr/project/{projectId}/unprocessed - Unverarbeitete Dateien eines Projekts
     */
    @GetMapping("/project/{projectId}/unprocessed")
    public ResponseEntity<List<ProjectFileDTO>> getUnprocessedFiles(@PathVariable("projectId") String projectId) {
        log.debug("GET /api/v1/ocr/project/{}/unprocessed", projectId);
        
        try {
            // Projekt existiert prüfen
            projectService.getProjectById(projectId);
            
            // Unverarbeitete Dateien laden
            List<ProjectFile> unprocessedFiles = ocrService.getUnprocessedFiles(projectId);
            List<ProjectFileDTO> fileDTOs = dtoMapper.toProjectFileDTOList(unprocessedFiles);
            
            log.debug("Returning {} unprocessed files for project {}", fileDTOs.size(), projectId);
            return ResponseEntity.ok(fileDTOs);
            
        } catch (EntityNotFoundException e) {
            log.warn("Projekt nicht gefunden für unverarbeitete Dateien: {}", projectId);
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            log.error("Fehler beim Laden der unverarbeiteten Dateien für Projekt: {}", projectId, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}