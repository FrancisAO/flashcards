package com.fao.flashcards.ocr.application.port.in;

import java.io.IOException;
import java.util.List;

import com.fao.flashcards.ocr.model.ProjectFile;
import com.fao.flashcards.ocr.model.UploadableFile;
import com.fao.flashcards.shared.model.pagination.Page;
import com.fao.flashcards.shared.model.pagination.PageRequest;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public interface FileUploadInputPort {

    /**
     * Verarbeitet einen Datei-Upload für ein bestimmtes Projekt.
     */
    ProjectFile uploadFile(@NotBlank String projectId, @NotNull UploadableFile fileAdapter) throws IOException;

    /**
     * Löscht eine ProjectFile und die zugehörige physische Datei.
     */
    void deleteFile(@NotBlank String fileId) throws IOException;

    /**
     * Prüft ob eine Datei existiert.
     */
    boolean fileExists(@NotBlank String fileId);

    /**
     * Gibt Informationen über eine Datei zurück.
     */
    ProjectFile getFileInfo(@NotBlank String fileId);

    /**
     * Findet alle Dateien eines Projekts.
     */
    List<ProjectFile> getProjectFiles(@NotBlank String projectId);

    /**
     * Prüft ob eine physische Datei im Dateisystem existiert.
     */
    boolean physicalFileExists(@NotBlank String fileId);

    /**
     * Berechnet die Gesamtgröße aller Dateien eines Projekts.
     */
    long getTotalProjectFileSize(@NotBlank String projectId);

    Page<ProjectFile> searchProjectFiles(String projectId, String filename, String fileType, PageRequest pageable);

    Page<ProjectFile> getProjectFiles(String projectId, PageRequest pageable);

}