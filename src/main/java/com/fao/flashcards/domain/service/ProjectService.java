package com.fao.flashcards.domain.service;

import com.fao.flashcards.domain.model.Project;
import com.fao.flashcards.domain.repository.ExtractedTextRepository;
import com.fao.flashcards.domain.repository.ProjectFileRepository;
import com.fao.flashcards.domain.repository.ProjectRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

/**
 * Service für OCR-Projekt-Management.
 * Verwaltet CRUD-Operationen, Tag-Management und Statistik-Updates für Projekte.
 */
@Service
@Validated
@Slf4j
public class ProjectService {

    private final ProjectRepository projectRepository;
    private final ProjectFileRepository projectFileRepository;
    private final ExtractedTextRepository extractedTextRepository;

    @Autowired
    public ProjectService(ProjectRepository projectRepository,
                         ProjectFileRepository projectFileRepository,
                         ExtractedTextRepository extractedTextRepository) {
        this.projectRepository = projectRepository;
        this.projectFileRepository = projectFileRepository;
        this.extractedTextRepository = extractedTextRepository;
    }

    /**
     * Erstellt ein neues Projekt.
     */
    @Transactional
    public Project createProject(@Valid @NotNull Project project) {
        log.info("Erstelle neues Projekt: {}", project.getName());
        
        if (projectRepository.existsByName(project.getName())) {
            throw new IllegalArgumentException("Ein Projekt mit dem Namen '" + project.getName() + "' existiert bereits");
        }
        
        Project savedProject = projectRepository.save(project);
        log.info("Projekt erfolgreich erstellt mit ID: {}", savedProject.getId());
        return savedProject;
    }

    /**
     * Findet ein Projekt nach ID.
     */
    @Transactional(readOnly = true)
    public Project getProjectById(@NotBlank String projectId) {
        log.debug("Suche Projekt mit ID: {}", projectId);
        return projectRepository.findById(projectId)
                .orElseThrow(() -> new EntityNotFoundException("Projekt mit ID " + projectId + " nicht gefunden"));
    }

    /**
     * Findet alle Projekte mit Paginierung.
     */
    @Transactional(readOnly = true)
    public Page<Project> getAllProjects(Pageable pageable) {
        log.debug("Lade alle Projekte mit Paginierung: {}", pageable);
        return projectRepository.findAllByOrderByCreatedAtDesc(pageable);
    }

    /**
     * Sucht Projekte nach Namen oder Beschreibung.
     */
    @Transactional(readOnly = true)
    public Page<Project> searchProjects(@NotBlank String searchTerm, Pageable pageable) {
        log.debug("Suche Projekte mit Begriff: {}", searchTerm);
        return projectRepository.searchByNameOrDescription(searchTerm, pageable);
    }

    /**
     * Findet Projekte nach Tags.
     */
    @Transactional(readOnly = true)
    public List<Project> getProjectsByTags(@NotNull Set<String> tags) {
        log.debug("Suche Projekte mit Tags: {}", tags);
        if (tags.isEmpty()) {
            return List.of();
        }
        return projectRepository.findByTagsIn(tags);
    }

    /**
     * Findet Projekte die alle angegebenen Tags enthalten.
     */
    @Transactional(readOnly = true)
    public List<Project> getProjectsByAllTags(@NotNull Set<String> tags) {
        log.debug("Suche Projekte mit allen Tags: {}", tags);
        if (tags.isEmpty()) {
            return List.of();
        }
        return projectRepository.findByAllTags(tags, tags.size());
    }

    /**
     * Aktualisiert ein bestehendes Projekt.
     */
    @Transactional
    public Project updateProject(@NotBlank String projectId, @Valid @NotNull Project projectUpdate) {
        log.info("Aktualisiere Projekt mit ID: {}", projectId);
        
        Project existingProject = getProjectById(projectId);
        
        // Prüfe Name-Eindeutigkeit nur wenn sich der Name geändert hat
        if (!existingProject.getName().equals(projectUpdate.getName()) && 
            projectRepository.existsByName(projectUpdate.getName())) {
            throw new IllegalArgumentException("Ein Projekt mit dem Namen '" + projectUpdate.getName() + "' existiert bereits");
        }
        
        existingProject.setName(projectUpdate.getName());
        existingProject.setDescription(projectUpdate.getDescription());
        
        Project savedProject = projectRepository.save(existingProject);
        log.info("Projekt erfolgreich aktualisiert: {}", savedProject.getId());
        return savedProject;
    }

    /**
     * Fügt einen Tag zu einem Projekt hinzu.
     */
    @Transactional
    public Project addTag(@NotBlank String projectId, @NotBlank String tag) {
        log.info("Füge Tag '{}' zu Projekt {} hinzu", tag, projectId);
        
        Project project = getProjectById(projectId);
        project.getTags().add(tag.trim().toLowerCase());
        
        Project savedProject = projectRepository.save(project);
        log.debug("Tag erfolgreich hinzugefügt zu Projekt: {}", projectId);
        return savedProject;
    }

    /**
     * Entfernt einen Tag von einem Projekt.
     */
    @Transactional
    public Project removeTag(@NotBlank String projectId, @NotBlank String tag) {
        log.info("Entferne Tag '{}' von Projekt {}", tag, projectId);
        
        Project project = getProjectById(projectId);
        project.getTags().remove(tag.trim().toLowerCase());
        
        Project savedProject = projectRepository.save(project);
        log.debug("Tag erfolgreich entfernt von Projekt: {}", projectId);
        return savedProject;
    }

    /**
     * Setzt alle Tags eines Projekts.
     */
    @Transactional
    public Project setTags(@NotBlank String projectId, @NotNull Set<String> tags) {
        log.info("Setze Tags für Projekt {}: {}", projectId, tags);
        
        Project project = getProjectById(projectId);
        project.getTags().clear();
        tags.stream()
            .map(tag -> tag.trim().toLowerCase())
            .forEach(project.getTags()::add);
        
        Project savedProject = projectRepository.save(project);
        log.debug("Tags erfolgreich gesetzt für Projekt: {}", projectId);
        return savedProject;
    }

    /**
     * Aktualisiert die Datei-Anzahl eines Projekts.
     */
    @Transactional
    public void updateFileCount(@NotBlank String projectId) {
        log.debug("Aktualisiere Datei-Anzahl für Projekt: {}", projectId);
        
        Project project = getProjectById(projectId);
        long fileCount = projectFileRepository.countByProjectId(projectId);
        
        project.setFileCount((int) fileCount);
        projectRepository.save(project);
        
        log.debug("Datei-Anzahl für Projekt {} aktualisiert: {}", projectId, fileCount);
    }

    /**
     * Aktualisiert die ExtractedText-Anzahl eines Projekts.
     */
    @Transactional
    public void updateExtractedTextCount(@NotBlank String projectId) {
        log.debug("Aktualisiere ExtractedText-Anzahl für Projekt: {}", projectId);
        
        Project project = getProjectById(projectId);
        long extractedTextCount = extractedTextRepository.countByProjectId(projectId);
        
        project.setExtractedTextCount((int) extractedTextCount);
        projectRepository.save(project);
        
        log.debug("ExtractedText-Anzahl für Projekt {} aktualisiert: {}", projectId, extractedTextCount);
    }

    /**
     * Aktualisiert beide Statistiken eines Projekts.
     */
    @Transactional
    public void updateProjectStatistics(@NotBlank String projectId) {
        log.debug("Aktualisiere Statistiken für Projekt: {}", projectId);
        
        Project project = getProjectById(projectId);
        long fileCount = projectFileRepository.countByProjectId(projectId);
        long extractedTextCount = extractedTextRepository.countByProjectId(projectId);
        
        project.setFileCount((int) fileCount);
        project.setExtractedTextCount((int) extractedTextCount);
        projectRepository.save(project);
        
        log.debug("Statistiken für Projekt {} aktualisiert - Dateien: {}, ExtractedTexts: {}", 
                 projectId, fileCount, extractedTextCount);
    }

    /**
     * Löscht ein Projekt.
     */
    @Transactional
    public void deleteProject(@NotBlank String projectId) {
        log.info("Lösche Projekt mit ID: {}", projectId);
        
        Project project = getProjectById(projectId);
        
        // Prüfe ob das Projekt Dateien oder extrahierte Texte enthält
        long fileCount = projectFileRepository.countByProjectId(projectId);
        if (fileCount > 0) {
            throw new IllegalStateException("Projekt kann nicht gelöscht werden, da es noch " + fileCount + " Datei(en) enthält");
        }
        
        projectRepository.delete(project);
        log.info("Projekt erfolgreich gelöscht: {}", projectId);
    }

    /**
     * Findet alle verwendeten Tags.
     */
    @Transactional(readOnly = true)
    public List<String> getAllUsedTags() {
        log.debug("Lade alle verwendeten Tags");
        return projectRepository.findAllUsedTags();
    }

    /**
     * Findet Projekte ohne extrahierte Texte.
     */
    @Transactional(readOnly = true)
    public List<Project> getProjectsWithoutExtractedTexts() {
        log.debug("Lade Projekte ohne extrahierte Texte");
        return projectRepository.findProjectsWithoutExtractedTexts();
    }

    /**
     * Findet Projekte mit extrahierten Texten.
     */
    @Transactional(readOnly = true)
    public List<Project> getProjectsWithExtractedTexts() {
        log.debug("Lade Projekte mit extrahierten Texten");
        return projectRepository.findProjectsWithExtractedTexts();
    }

    /**
     * Findet Projekte die nach einem bestimmten Datum erstellt wurden.
     */
    @Transactional(readOnly = true)
    public List<Project> getProjectsCreatedAfter(@NotNull LocalDateTime dateTime) {
        log.debug("Lade Projekte erstellt nach: {}", dateTime);
        return projectRepository.findByCreatedAtAfter(dateTime);
    }

    /**
     * Zählt Projekte mit einem bestimmten Tag.
     */
    @Transactional(readOnly = true)
    public long countProjectsByTag(@NotBlank String tag) {
        log.debug("Zähle Projekte mit Tag: {}", tag);
        return projectRepository.countByTag(tag);
    }

    /**
     * Prüft ob ein Projektname bereits existiert.
     */
    @Transactional(readOnly = true)
    public boolean existsByName(@NotBlank String name) {
        return projectRepository.existsByName(name);
    }
}