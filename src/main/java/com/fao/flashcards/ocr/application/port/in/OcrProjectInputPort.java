package com.fao.flashcards.ocr.application.port.in;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.fao.flashcards.ocr.model.Project;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public interface OcrProjectInputPort {

    /**
     * Erstellt ein neues Projekt.
     */
    Project createProject(@Valid @NotNull Project project);

    /**
     * Findet ein Projekt nach ID.
     */
    Project getProjectById(@NotBlank String projectId);

    /**
     * Findet alle Projekte mit Paginierung.
     */
    Page<Project> getAllProjects(Pageable pageable);

    /**
     * Sucht Projekte nach Namen oder Beschreibung.
     */
    Page<Project> searchProjects(@NotBlank String searchTerm, Pageable pageable);

    /**
     * Findet Projekte nach Tags.
     */
    List<Project> getProjectsByTags(@NotNull Set<String> tags);

    /**
     * Findet Projekte die alle angegebenen Tags enthalten.
     */
    List<Project> getProjectsByAllTags(@NotNull Set<String> tags);

    /**
     * Aktualisiert ein bestehendes Projekt.
     */
    Project updateProject(@NotBlank String projectId, @Valid @NotNull Project projectUpdate);

    /**
     * Fügt einen Tag zu einem Projekt hinzu.
     */
    Project addTag(@NotBlank String projectId, @NotBlank String tag);

    /**
     * Entfernt einen Tag von einem Projekt.
     */
    Project removeTag(@NotBlank String projectId, @NotBlank String tag);

    /**
     * Setzt alle Tags eines Projekts.
     */
    Project setTags(@NotBlank String projectId, @NotNull Set<String> tags);

    /**
     * Aktualisiert die Datei-Anzahl eines Projekts.
     */
    void updateFileCount(@NotBlank String projectId);

    /**
     * Aktualisiert die ExtractedText-Anzahl eines Projekts.
     */
    void updateExtractedTextCount(@NotBlank String projectId);

    /**
     * Aktualisiert beide Statistiken eines Projekts.
     */
    void updateProjectStatistics(@NotBlank String projectId);

    /**
     * Löscht ein Projekt.
     */
    void deleteProject(@NotBlank String projectId);

    /**
     * Findet alle verwendeten Tags.
     */
    List<String> getAllUsedTags();

    /**
     * Findet Projekte ohne extrahierte Texte.
     */
    List<Project> getProjectsWithoutExtractedTexts();

    /**
     * Findet Projekte mit extrahierten Texten.
     */
    List<Project> getProjectsWithExtractedTexts();

    /**
     * Findet Projekte die nach einem bestimmten Datum erstellt wurden.
     */
    List<Project> getProjectsCreatedAfter(@NotNull LocalDateTime dateTime);

    /**
     * Zählt Projekte mit einem bestimmten Tag.
     */
    long countProjectsByTag(@NotBlank String tag);

    /**
     * Prüft ob ein Projektname bereits existiert.
     */
    boolean existsByName(@NotBlank String name);

}