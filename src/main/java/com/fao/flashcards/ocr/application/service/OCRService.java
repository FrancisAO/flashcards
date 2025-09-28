package com.fao.flashcards.ocr.application.service;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import com.fao.flashcards.cards.model.FileType;
import com.fao.flashcards.ocr.application.OCRProcessingException;
import com.fao.flashcards.ocr.application.port.out.ExtractedTextOutputPort;
import com.fao.flashcards.ocr.application.port.out.OCRProcessingPort;
import com.fao.flashcards.ocr.application.port.out.OCRResultOutputPort;
import com.fao.flashcards.ocr.application.port.out.ProjectFileOutputPort;
import com.fao.flashcards.ocr.model.ExtractedText;
import com.fao.flashcards.ocr.model.ExtractionSource;
import com.fao.flashcards.ocr.model.OCROptions;
import com.fao.flashcards.ocr.model.OCRResult;
import com.fao.flashcards.ocr.model.OCRStatus;
import com.fao.flashcards.ocr.model.ProjectFile;

import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.extern.slf4j.Slf4j;

/**
 * Service für OCR-Verarbeitung.
 * Orchestriert OCR-Verarbeitung, Status-Management und Text-Bearbeitung.
 */
@Service
@Validated
@Slf4j
public class OCRService {

    private final OCRProcessingPort ocrProcessingPort;
    private final ExtractedTextOutputPort extractedTextRepository;
    private final OCRResultOutputPort ocrResultRepository;
    private final ProjectFileOutputPort projectFileRepository;
    private final ProjectService projectService;

    @Autowired
    public OCRService(OCRProcessingPort ocrProcessingPort,
            ExtractedTextOutputPort extractedTextRepository,
            OCRResultOutputPort ocrResultRepository,
            ProjectFileOutputPort projectFileRepository,
            ProjectService projectService) {
        this.ocrProcessingPort = ocrProcessingPort;
        this.extractedTextRepository = extractedTextRepository;
        this.ocrResultRepository = ocrResultRepository;
        this.projectFileRepository = projectFileRepository;
        this.projectService = projectService;
    }

    /**
     * Verarbeitet eine einzelne Datei per OCR.
     */
    @Transactional
    public ExtractedText processFile(@NotBlank String fileId, @Valid OCROptions options) throws OCRProcessingException {
        log.info("Starte OCR-Verarbeitung für Datei: {}", fileId);

        ProjectFile projectFile = getProjectFile(fileId);

        // Prüfen ob bereits ExtractedText existiert
        if (extractedTextRepository.existsByProjectFileId(fileId)) {
            throw new IllegalStateException(
                    "Datei wurde bereits verarbeitet. ExtractedText existiert bereits für Datei: " + fileId);
        }

        // ExtractedText mit PENDING Status erstellen
        ExtractedText extractedText = createInitialExtractedText(projectFile);
        OCRResult ocrResult = createInitialOCRResult(extractedText);

        try {
            // Status auf PROCESSING setzen
            ocrResult.markAsProcessing();
            ocrResultRepository.save(ocrResult);

            // Datei lesen und OCR durchführen
            try (InputStream fileStream = Files.newInputStream(Paths.get(projectFile.getStoragePath()))) {
                OCRResult processedResult = ocrProcessingPort.processFile(projectFile, fileStream, options);

                // Ergebnis in ExtractedText übertragen
                extractedText.setExtractedContent(processedResult.getApiResponse());
                extractedText.setExtractionSource(determineExtractionSource(projectFile));
                extractedText.setExtractedAt(LocalDateTime.now());


                // OCRResult aktualisieren
                ocrResult.markAsSuccess(
                        processedResult.getPagesProcessed(),
                        processedResult.getProcessingTimeMs(),
                        processedResult.getApiResponse(),
                        processedResult.getModelUsed());
                ocrResult.setConfidenceScore(processedResult.getConfidenceScore());

                extractedTextRepository.save(extractedText);
                ocrResultRepository.save(ocrResult);

                // Projekt-Statistiken aktualisieren
                projectService.updateExtractedTextCount(projectFile.getProject().getId());

                log.info("OCR-Verarbeitung erfolgreich abgeschlossen für Datei: {}", fileId);
                return extractedText;

            } catch (IOException e) {
                log.error("Fehler beim Lesen der Datei: {}", projectFile.getStoragePath(), e);
                throw new OCRProcessingException("Datei konnte nicht gelesen werden: " + e.getMessage(), e);
            }

        } catch (Exception e) {
            log.error("OCR-Verarbeitung fehlgeschlagen für Datei: {}", fileId, e);

            // Status auf FAILED setzen
            ocrResult.markAsFailed(e.getMessage());
            ocrResultRepository.save(ocrResult);

            throw new OCRProcessingException("OCR-Verarbeitung fehlgeschlagen: " + e.getMessage(), e);
        }
    }

    /**
     * Verarbeitet eine einzelne Datei per OCR mit Standard-Optionen.
     */
    @Transactional
    public ExtractedText processFile(@NotBlank String fileId) throws OCRProcessingException {
        return processFile(fileId, OCROptions.defaultOptions());
    }

    /**
     * Verarbeitet mehrere Dateien als Batch per OCR.
     */
    @Transactional
    public List<ExtractedText> processBatch(@NotNull List<String> fileIds, @Valid OCROptions options)
            throws OCRProcessingException {
        log.info("Starte Batch-OCR-Verarbeitung für {} Dateien", fileIds.size());

        List<ExtractedText> results = new ArrayList<>();
        List<OCRProcessingPort.FileProcessingData> fileDataList = new ArrayList<>();
        List<ExtractedText> extractedTexts = new ArrayList<>();
        List<OCRResult> ocrResults = new ArrayList<>();

        try {
            // Vorbereitung: ProjectFiles laden und ExtractedTexts erstellen
            for (String fileId : fileIds) {
                ProjectFile projectFile = getProjectFile(fileId);

                if (extractedTextRepository.existsByProjectFileId(fileId)) {
                    log.warn("Datei {} wurde bereits verarbeitet, überspringe", fileId);
                    continue;
                }

                ExtractedText extractedText = createInitialExtractedText(projectFile);
                OCRResult ocrResult = createInitialOCRResult(extractedText);

                extractedTexts.add(extractedText);
                ocrResults.add(ocrResult);

                // FileProcessingData für Batch erstellen
                try {
                    InputStream fileStream = Files.newInputStream(Paths.get(projectFile.getStoragePath()));
                    fileDataList.add(new OCRProcessingPort.FileProcessingData(
                            projectFile, fileStream, projectFile.getFileSize()));
                } catch (IOException e) {
                    log.error("Fehler beim Öffnen der Datei: {}", projectFile.getStoragePath(), e);
                    ocrResult.markAsFailed("Datei konnte nicht gelesen werden: " + e.getMessage());
                    ocrResultRepository.save(ocrResult);
                }
            }

            if (fileDataList.isEmpty()) {
                log.info("Keine Dateien für Batch-Verarbeitung verfügbar");
                return results;
            }

            // Status aller OCRResults auf PROCESSING setzen
            for (OCRResult ocrResult : ocrResults) {
                ocrResult.markAsProcessing();
                ocrResultRepository.save(ocrResult);
            }

            // Batch-OCR durchführen
            List<OCRResult> processedResults = ocrProcessingPort.processBatch(fileDataList, options);

            // Ergebnisse verarbeiten
            for (int i = 0; i < processedResults.size() && i < extractedTexts.size(); i++) {
                OCRResult processedResult = processedResults.get(i);
                ExtractedText extractedText = extractedTexts.get(i);
                OCRResult ocrResult = ocrResults.get(i);

                if (processedResult.getStatus() == OCRStatus.SUCCESS) {
                    // Erfolgreiche Verarbeitung
                    extractedText.setExtractedContent(processedResult.getApiResponse());
                    extractedText.setExtractionSource(determineExtractionSource(fileDataList.get(i).getProjectFile()));

                    ocrResult.markAsSuccess(
                            processedResult.getPagesProcessed(),
                            processedResult.getProcessingTimeMs(),
                            processedResult.getApiResponse(),
                            processedResult.getModelUsed());
                    ocrResult.setConfidenceScore(processedResult.getConfidenceScore());

                    results.add(extractedText);
                } else {
                    // Fehlerhafte Verarbeitung
                    ocrResult.markAsFailed(processedResult.getErrorDetails());
                }

                extractedTextRepository.save(extractedText);
                ocrResultRepository.save(ocrResult);
            }

            // Projekt-Statistiken für alle beteiligten Projekte aktualisieren
            fileIds.stream()
                    .map(this::getProjectFile)
                    .map(pf -> pf.getProject().getId())
                    .distinct()
                    .forEach(projectService::updateExtractedTextCount);

            log.info("Batch-OCR-Verarbeitung abgeschlossen. {} von {} Dateien erfolgreich verarbeitet",
                    results.size(), fileIds.size());
            return results;

        } finally {
            // FileStreams schließen
            for (OCRProcessingPort.FileProcessingData fileData : fileDataList) {
                try {
                    fileData.getFileStream().close();
                } catch (IOException e) {
                    log.warn("Fehler beim Schließen des FileStreams", e);
                }
            }
        }
    }

    /**
     * Verarbeitet mehrere Dateien als Batch per OCR mit Standard-Optionen.
     */
    @Transactional
    public List<ExtractedText> processBatch(@NotNull List<String> fileIds) throws OCRProcessingException {
        return processBatch(fileIds, OCROptions.defaultOptions());
    }

    /**
     * Asynchrone OCR-Verarbeitung einer einzelnen Datei.
     */
    @Async
    public CompletableFuture<ExtractedText> processFileAsync(@NotBlank String fileId, @Valid OCROptions options) {
        try {
            ExtractedText result = processFile(fileId, options);
            return CompletableFuture.completedFuture(result);
        } catch (Exception e) {
            log.error("Asynchrone OCR-Verarbeitung fehlgeschlagen für Datei: {}", fileId, e);
            CompletableFuture<ExtractedText> future = new CompletableFuture<>();
            future.completeExceptionally(e);
            return future;
        }
    }

    /**
     * Asynchrone Batch-OCR-Verarbeitung.
     */
    @Async
    public CompletableFuture<List<ExtractedText>> processBatchAsync(@NotNull List<String> fileIds,
            @Valid OCROptions options) {
        try {
            List<ExtractedText> results = processBatch(fileIds, options);
            return CompletableFuture.completedFuture(results);
        } catch (Exception e) {
            log.error("Asynchrone Batch-OCR-Verarbeitung fehlgeschlagen", e);
            CompletableFuture<List<ExtractedText>> future = new CompletableFuture<>();
            future.completeExceptionally(e);
            return future;
        }
    }

    /**
     * Bearbeitet den extrahierten Text.
     */
    @Transactional
    public ExtractedText editExtractedText(@NotBlank String extractedTextId, @NotBlank String editedContent) {
        log.info("Bearbeite ExtractedText: {}", extractedTextId);

        ExtractedText extractedText = extractedTextRepository.findById(extractedTextId)
                .orElseThrow(() -> new EntityNotFoundException(
                        "ExtractedText mit ID " + extractedTextId + " nicht gefunden"));

        extractedText.updateEditedContent(editedContent);

        ExtractedText savedText = extractedTextRepository.save(extractedText);
        log.info("ExtractedText erfolgreich bearbeitet: {}", extractedTextId);
        return savedText;
    }

    /**
     * Setzt den bearbeiteten Text zurück auf den ursprünglich extrahierten Text.
     */
    @Transactional
    public ExtractedText resetEditedText(@NotBlank String extractedTextId) {
        log.info("Setze bearbeiteten Text zurück: {}", extractedTextId);

        ExtractedText extractedText = extractedTextRepository.findById(extractedTextId)
                .orElseThrow(() -> new EntityNotFoundException(
                        "ExtractedText mit ID " + extractedTextId + " nicht gefunden"));

        extractedText.setEditedContent(null);
        extractedText.setIsEdited(false);

        ExtractedText savedText = extractedTextRepository.save(extractedText);
        log.info("Bearbeiteter Text erfolgreich zurückgesetzt: {}", extractedTextId);
        return savedText;
    }

    /**
     * Findet alle Dateien eines Projekts die noch nicht per OCR verarbeitet wurden.
     */
    @Transactional(readOnly = true)
    public List<ProjectFile> getUnprocessedFiles(@NotBlank String projectId) {
        log.debug("Lade unverarbeitete Dateien für Projekt: {}", projectId);
        return projectFileRepository.findFilesWithoutExtractedTextByProjectId(projectId);
    }

    /**
     * Findet alle ExtractedTexts eines Projekts.
     */
    @Transactional(readOnly = true)
    public List<ExtractedText> getProjectExtractedTexts(@NotBlank String projectId) {
        log.debug("Lade ExtractedTexts für Projekt: {}", projectId);
        return extractedTextRepository.findByProjectId(projectId);
    }

    /**
     * Findet OCR-Ergebnisse eines Projekts.
     */
    @Transactional(readOnly = true)
    public List<OCRResult> getProjectOCRResults(@NotBlank String projectId) {
        log.debug("Lade OCR-Ergebnisse für Projekt: {}", projectId);
        return ocrResultRepository.findByProjectId(projectId);
    }

    /**
     * Gibt OCR-Status-Statistiken für ein Projekt zurück.
     */
    @Transactional(readOnly = true)
    public OCRProjectStatistics getProjectStatistics(@NotBlank String projectId) {
        log.debug("Lade OCR-Statistiken für Projekt: {}", projectId);

        long totalFiles = projectFileRepository.countByProjectId(projectId);
        long processedFiles = extractedTextRepository.countByProjectId(projectId);
        long successfulOCR = ocrResultRepository.countSuccessfulByProjectId(projectId);
        long failedOCR = ocrResultRepository.countFailedByProjectId(projectId);
        Double avgProcessingTime = ocrResultRepository.calculateAverageProcessingTimeByProjectId(projectId);
        Double successRate = ocrResultRepository.calculateSuccessRateByProjectId(projectId);

        return new OCRProjectStatistics(
                projectId, totalFiles, processedFiles, successfulOCR, failedOCR,
                avgProcessingTime != null ? avgProcessingTime : 0.0,
                successRate != null ? successRate : 0.0);
    }

    /**
     * Hilfsmethode: ProjectFile laden.
     */
    private ProjectFile getProjectFile(String fileId) {
        return projectFileRepository.findById(fileId)
                .orElseThrow(() -> new EntityNotFoundException("ProjectFile mit ID " + fileId + " nicht gefunden"));
    }

    /**
     * Hilfsmethode: Initialen ExtractedText erstellen.
     */
    private ExtractedText createInitialExtractedText(ProjectFile projectFile) {
        ExtractedText extractedText = new ExtractedText(projectFile, null, determineExtractionSource(projectFile));
        return extractedTextRepository.save(extractedText);
    }

    /**
     * Hilfsmethode: ExtractionSource basierend auf FileType bestimmen.
     */
    private ExtractionSource determineExtractionSource(ProjectFile projectFile) {
        return projectFile.getFileType() == FileType.IMAGE ? ExtractionSource.IMAGE : ExtractionSource.DOCUMENT;
    }

    /**
     * Hilfsmethode: Initialen OCRResult erstellen.
     */
    private OCRResult createInitialOCRResult(ExtractedText extractedText) {
        OCRResult result = new OCRResult(extractedText, OCRStatus.PENDING);
        result.setCreatedAt(LocalDateTime.now());
        return ocrResultRepository.save(result);
    }

    /**
     * Datenklasse für OCR-Projekt-Statistiken.
     */
    public static class OCRProjectStatistics {
        private final String projectId;
        private final long totalFiles;
        private final long processedFiles;
        private final long successfulOCR;
        private final long failedOCR;
        private final double averageProcessingTime;
        private final double successRate;

        public OCRProjectStatistics(String projectId, long totalFiles, long processedFiles,
                long successfulOCR, long failedOCR, double averageProcessingTime,
                double successRate) {
            this.projectId = projectId;
            this.totalFiles = totalFiles;
            this.processedFiles = processedFiles;
            this.successfulOCR = successfulOCR;
            this.failedOCR = failedOCR;
            this.averageProcessingTime = averageProcessingTime;
            this.successRate = successRate;
        }

        // Getters
        public String getProjectId() {
            return projectId;
        }

        public long getTotalFiles() {
            return totalFiles;
        }

        public long getProcessedFiles() {
            return processedFiles;
        }

        public long getUnprocessedFiles() {
            return totalFiles - processedFiles;
        }

        public long getSuccessfulOCR() {
            return successfulOCR;
        }

        public long getFailedOCR() {
            return failedOCR;
        }

        public double getAverageProcessingTime() {
            return averageProcessingTime;
        }

        public double getSuccessRate() {
            return successRate;
        }
    }
}