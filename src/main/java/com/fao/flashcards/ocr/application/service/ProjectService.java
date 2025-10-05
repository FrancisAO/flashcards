package com.fao.flashcards.ocr.application.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.validation.annotation.Validated;

import com.fao.flashcards.ocr.application.port.out.ExtractedTextRepository;
import com.fao.flashcards.ocr.application.port.out.ProjectFileRepository;
import com.fao.flashcards.ocr.application.port.out.ProjectRepository;
import com.fao.flashcards.ocr.model.Project;

import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.extern.slf4j.Slf4j;

/**
 * Service für OCR-Projekt-Management.
 * Verwaltet CRUD-Operationen, Tag-Management und Statistik-Updates für
 * Projekte.
 */
@Validated
@Slf4j
public class ProjectService {

    private final ProjectRepository projectOutputPort;
    private final ProjectFileRepository projectFileOutputPort;
    private final ExtractedTextRepository projectTextOutputPort;

    public ProjectService(ProjectRepository projectRepository,
            ProjectFileRepository projectFileRepository,
            ExtractedTextRepository extractedTextRepository) {
        this.projectOutputPort = projectRepository;
        this.projectFileOutputPort = projectFileRepository;
        this.projectTextOutputPort = extractedTextRepository;
    }

    /**
     * Erstellt ein neues Projekt.
     */
    public Project createProject(@Valid @NotNull Project project) {
        log.info("Erstelle neues Projekt: {}", project.getName());

        if (projectOutputPort.existsByName(project.getName())) {
            throw new IllegalArgumentException(
                    "Ein Projekt mit dem Namen '" + project.getName() + "' existiert bereits");
        }

        Project savedProject = projectOutputPort.save(project);
        log.info("Projekt erfolgreich erstellt mit ID: {}", savedProject.getId());
        return savedProject;
    }

    /**
     * Findet ein Projekt nach ID.
     */
    public Project getProjectById(@NotBlank String projectId) {
        log.debug("Suche Projekt mit ID: {}", projectId);
        return projectOutputPort.findById(projectId)
                .orElseThrow(() -> new EntityNotFoundException("Projekt mit ID " + projectId + " nicht gefunden"));
    }

    /**
     * Findet alle Projekte mit Paginierung.
     */
    public Page<Project> getAllProjects(Pageable pageable) {
        log.debug("Lade alle Projekte mit Paginierung: {}", pageable);
        return projectOutputPort.findAllByOrderByCreatedAtDesc(pageable);
    }

    /**
     * Sucht Projekte nach Namen oder Beschreibung.
     */
    public Page<Project> searchProjects(@NotBlank String searchTerm, Pageable pageable) {
        log.debug("Suche Projekte mit Begriff: {}", searchTerm);
        return projectOutputPort.searchByNameOrDescription(searchTerm, pageable);
    }

    /**
     * Findet Projekte nach Tags.
     */
    public List<Project> getProjectsByTags(@NotNull Set<String> tags) {
        log.debug("Suche Projekte mit Tags: {}", tags);
        if (tags.isEmpty()) {
            return List.of();
        }
        return projectOutputPort.findByTagsIn(tags);
    }

    /**
     * Findet Projekte die alle angegebenen Tags enthalten.
     */
    public List<Project> getProjectsByAllTags(@NotNull Set<String> tags) {
        log.debug("Suche Projekte mit allen Tags: {}", tags);
        if (tags.isEmpty()) {
            return List.of();
        }
        return projectOutputPort.findByAllTags(tags, tags.size());
    }

    /**
     * Aktualisiert ein bestehendes Projekt.
     */
    public Project updateProject(@NotBlank String projectId, @Valid @NotNull Project projectUpdate) {
        log.info("Aktualisiere Projekt mit ID: {}", projectId);

        Project existingProject = getProjectById(projectId);

        // Prüfe Name-Eindeutigkeit nur wenn sich der Name geändert hat
        if (!existingProject.getName().equals(projectUpdate.getName()) &&
                projectOutputPort.existsByName(projectUpdate.getName())) {
            throw new IllegalArgumentException(
                    "Ein Projekt mit dem Namen '" + projectUpdate.getName() + "' existiert bereits");
        }

        existingProject.setName(projectUpdate.getName());
        existingProject.setDescription(projectUpdate.getDescription());

        Project savedProject = projectOutputPort.save(existingProject);
        log.info("Projekt erfolgreich aktualisiert: {}", savedProject.getId());
        return savedProject;
    }

    /**
     * Fügt einen Tag zu einem Projekt hinzu.
     */
    public Project addTag(@NotBlank String projectId, @NotBlank String tag) {
        log.info("Füge Tag '{}' zu Projekt {} hinzu", tag, projectId);

        Project project = getProjectById(projectId);
        project.getTags().add(tag.trim().toLowerCase());

        Project savedProject = projectOutputPort.save(project);
        log.debug("Tag erfolgreich hinzugefügt zu Projekt: {}", projectId);
        return savedProject;
    }

    /**
     * Entfernt einen Tag von einem Projekt.
     */
    public Project removeTag(@NotBlank String projectId, @NotBlank String tag) {
        log.info("Entferne Tag '{}' von Projekt {}", tag, projectId);

        Project project = getProjectById(projectId);
        project.getTags().remove(tag.trim().toLowerCase());

        Project savedProject = projectOutputPort.save(project);
        log.debug("Tag erfolgreich entfernt von Projekt: {}", projectId);
        return savedProject;
    }

    /**
     * Setzt alle Tags eines Projekts.
     */
    public Project setTags(@NotBlank String projectId, @NotNull Set<String> tags) {
        log.info("Setze Tags für Projekt {}: {}", projectId, tags);

        Project project = getProjectById(projectId);
        project.getTags().clear();
        tags.stream()
                .map(tag -> tag.trim().toLowerCase())
                .forEach(project.getTags()::add);

        Project savedProject = projectOutputPort.save(project);
        log.debug("Tags erfolgreich gesetzt für Projekt: {}", projectId);
        return savedProject;
    }

    /**
     * Aktualisiert die Datei-Anzahl eines Projekts.
     */
    public void updateFileCount(@NotBlank String projectId) {
        log.debug("Aktualisiere Datei-Anzahl für Projekt: {}", projectId);

        Project project = getProjectById(projectId);
        long fileCount = projectFileOutputPort.countByProjectId(projectId);

        project.setFileCount((int) fileCount);
        projectOutputPort.save(project);

        log.debug("Datei-Anzahl für Projekt {} aktualisiert: {}", projectId, fileCount);
    }

    /**
     * Aktualisiert die ExtractedText-Anzahl eines Projekts.
     */
    public void updateExtractedTextCount(@NotBlank String projectId) {
        log.debug("Aktualisiere ExtractedText-Anzahl für Projekt: {}", projectId);

        Project project = getProjectById(projectId);
        long extractedTextCount = projectTextOutputPort.countByProjectId(projectId);

        project.setExtractedTextCount((int) extractedTextCount);
        projectOutputPort.save(project);

        log.debug("ExtractedText-Anzahl für Projekt {} aktualisiert: {}", projectId, extractedTextCount);
    }

    /**
     * Aktualisiert beide Statistiken eines Projekts.
     */
    public void updateProjectStatistics(@NotBlank String projectId) {
        log.debug("Aktualisiere Statistiken für Projekt: {}", projectId);

        Project project = getProjectById(projectId);
        long fileCount = projectFileOutputPort.countByProjectId(projectId);
        long extractedTextCount = projectTextOutputPort.countByProjectId(projectId);

        project.setFileCount((int) fileCount);
        project.setExtractedTextCount((int) extractedTextCount);
        projectOutputPort.save(project);

        log.debug("Statistiken für Projekt {} aktualisiert - Dateien: {}, ExtractedTexts: {}",
                projectId, fileCount, extractedTextCount);
    }

    /**
     * Löscht ein Projekt.
     */
    public void deleteProject(@NotBlank String projectId) {
        log.info("Lösche Projekt mit ID: {}", projectId);

        Project project = getProjectById(projectId);

        // Prüfe ob das Projekt Dateien oder extrahierte Texte enthält
        long fileCount = projectFileOutputPort.countByProjectId(projectId);
        if (fileCount > 0) {
            throw new IllegalStateException(
                    "Projekt kann nicht gelöscht werden, da es noch " + fileCount + " Datei(en) enthält");
        }

        projectOutputPort.delete(project);
        log.info("Projekt erfolgreich gelöscht: {}", projectId);
    }

    /**
     * Findet alle verwendeten Tags.
     */
    public List<String> getAllUsedTags() {
        log.debug("Lade alle verwendeten Tags");
        return projectOutputPort.findAllUsedTags();
    }

    /**
     * Findet Projekte ohne extrahierte Texte.
     */
    public List<Project> getProjectsWithoutExtractedTexts() {
        log.debug("Lade Projekte ohne extrahierte Texte");
        return projectOutputPort.findProjectsWithoutExtractedTexts();
    }

    /**
     * Findet Projekte mit extrahierten Texten.
     */
    public List<Project> getProjectsWithExtractedTexts() {
        log.debug("Lade Projekte mit extrahierten Texten");
        return projectOutputPort.findProjectsWithExtractedTexts();
    }

    /**
     * Findet Projekte die nach einem bestimmten Datum erstellt wurden.
     */
    public List<Project> getProjectsCreatedAfter(@NotNull LocalDateTime dateTime) {
        log.debug("Lade Projekte erstellt nach: {}", dateTime);
        return projectOutputPort.findByCreatedAtAfter(dateTime);
    }

    /**
     * Zählt Projekte mit einem bestimmten Tag.
     */
    public long countProjectsByTag(@NotBlank String tag) {
        log.debug("Zähle Projekte mit Tag: {}", tag);
        return projectOutputPort.countByTag(tag);
    }

    /**
     * Prüft ob ein Projektname bereits existiert.
     */
    public boolean existsByName(@NotBlank String name) {
        return projectOutputPort.existsByName(name);
    }
}