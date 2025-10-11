package com.fao.flashcards.ocr.application.port.in;

import java.util.List;
import java.util.concurrent.CompletableFuture;

import com.fao.flashcards.ocr.application.OCRProcessingException;
import com.fao.flashcards.ocr.application.service.OCRService.OCRProjectStatistics;
import com.fao.flashcards.ocr.model.ExtractedText;
import com.fao.flashcards.ocr.model.OCROptions;
import com.fao.flashcards.ocr.model.OCRResult;
import com.fao.flashcards.ocr.model.ProjectFile;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public interface OCRInputPort {

        /**
         * Verarbeitet eine einzelne Datei per OCR.
         */
        ExtractedText processFile(@NotBlank String fileId, @Valid OCROptions options) throws OCRProcessingException;

        /**
         * Verarbeitet eine einzelne Datei per OCR mit Standard-Optionen.
         */
        ExtractedText processFile(@NotBlank String fileId) throws OCRProcessingException;

        /**
         * Verarbeitet mehrere Dateien als Batch per OCR.
         */
        List<ExtractedText> processBatch(@NotNull List<@NotBlank String> fileIds, @Valid OCROptions options)
                        throws OCRProcessingException;

        /**
         * Verarbeitet mehrere Dateien als Batch per OCR mit Standard-Optionen.
         */
        List<ExtractedText> processBatch(@NotNull List<@NotBlank String> fileIds) throws OCRProcessingException;

        /**
         * Asynchrone OCR-Verarbeitung einer einzelnen Datei.
         */
        CompletableFuture<ExtractedText> processFileAsync(@NotBlank String fileId, @Valid OCROptions options);

        /**
         * Asynchrone Batch-OCR-Verarbeitung.
         */
        CompletableFuture<List<ExtractedText>> processBatchAsync(@NotNull List<@NotBlank String> fileIds,
                        @Valid OCROptions options);

        /**
         * Bearbeitet den extrahierten Text.
         */
        ExtractedText editExtractedText(@NotBlank String extractedTextId, @NotBlank String editedContent);

        /**
         * Setzt den bearbeiteten Text zurück auf den ursprünglich extrahierten Text.
         */
        ExtractedText resetEditedText(@NotBlank String extractedTextId);

        /**
         * Findet alle Dateien eines Projekts die noch nicht per OCR verarbeitet wurden.
         */
        List<ProjectFile> getUnprocessedFiles(@NotBlank String projectId);

        /**
         * Findet alle ExtractedTexts eines Projekts.
         */
        List<ExtractedText> getProjectExtractedTexts(@NotBlank String projectId);

        /**
         * Findet OCR-Ergebnisse eines Projekts.
         */
        List<OCRResult> getProjectOCRResults(@NotBlank String projectId);

        /**
         * Gibt OCR-Status-Statistiken für ein Projekt zurück.
         */
        OCRProjectStatistics getProjectStatistics(@NotBlank String projectId);

}