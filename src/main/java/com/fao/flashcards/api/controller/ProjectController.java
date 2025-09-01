package com.fao.flashcards.api.controller;

import com.fao.flashcards.api.dto.*;
import com.fao.flashcards.api.dto.ProjectStatisticsDTO.RecentActivityDTO;
import com.fao.flashcards.domain.model.Project;
import com.fao.flashcards.domain.model.ProjectFile;
import com.fao.flashcards.domain.service.FileUploadService;
import com.fao.flashcards.domain.service.ProjectService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

/**
 * REST Controller für Projekt-Management.
 * Stellt Endpoints für CRUD-Operationen an Projekten bereit.
 */
@RestController
@RequestMapping("/api/v1/projects")
@Validated
@Slf4j
@CrossOrigin(origins = "*", maxAge = 3600)
public class ProjectController {

    private final ProjectService projectService;
    private final FileUploadService fileUploadService;
    private final DTOMapper dtoMapper;

    @Autowired
    public ProjectController(ProjectService projectService,
                           FileUploadService fileUploadService,
                           DTOMapper dtoMapper) {
        this.projectService = projectService;
        this.fileUploadService = fileUploadService;
        this.dtoMapper = dtoMapper;
    }

    /**
     * GET /api/v1/projects - Liste aller Projekte mit Paginierung
     */
    @GetMapping
    public ResponseEntity<Page<ProjectDTO>> getAllProjects(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDir,
            @RequestParam(required = false) String search) {
        
        log.debug("GET /api/v1/projects - page: {}, size: {}, sortBy: {}, sortDir: {}, search: {}", 
                 page, size, sortBy, sortDir, search);
        
        try {
            // Sorting erstellen
            Sort.Direction direction = sortDir.equalsIgnoreCase("desc") ? 
                Sort.Direction.DESC : Sort.Direction.ASC;
            Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortBy));
            
            Page<Project> projectsPage;
            if (search != null && !search.trim().isEmpty()) {
                projectsPage = projectService.searchProjects(search.trim(), pageable);
            } else {
                projectsPage = projectService.getAllProjects(pageable);
            }
            
            // Convert to DTOs
            Page<ProjectDTO> projectDTOs = projectsPage.map(dtoMapper::toDTO);
            
            log.debug("Returning {} projects (page {} of {})", 
                     projectDTOs.getNumberOfElements(), 
                     projectDTOs.getNumber() + 1, 
                     projectDTOs.getTotalPages());
            
            return ResponseEntity.ok(projectDTOs);
            
        } catch (Exception e) {
            log.error("Fehler beim Laden der Projekte", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * GET /api/v1/projects/statistics - Projekt-Statistiken abrufen
     */
    @GetMapping("/statistics")
    public ResponseEntity<ProjectStatisticsDTO> getProjectStatistics() {
        log.debug("GET /api/v1/projects/statistics");
        
        try {
            // Statistiken vom Service laden
            // Nutze Paginierung um Gesamtanzahl zu ermitteln
            Pageable pageable = PageRequest.of(0, 1);
            Page<Project> projectPage = projectService.getAllProjects(pageable);
            long totalProjects = projectPage.getTotalElements();
            
            List<String> allTags = projectService.getAllUsedTags();
            
            // Für Demo-Zwecke verwenden wir vereinfachte Statistiken
            // In einer vollständigen Implementierung würden diese vom Service kommen
            ProjectStatisticsDTO statistics = new ProjectStatisticsDTO();
            statistics.setTotalProjects(totalProjects);

            statistics.setMostUsedTags(allTags.stream().limit(5).toList());
            
            // Kürzliche Aktivitäten (Demo-Daten)
            RecentActivityDTO recentActivity = new RecentActivityDTO();
            recentActivity.setProjectsCreated(Math.min(totalProjects, 5L));
            recentActivity.setFilesUploaded(Math.min(totalProjects * 2L, 15L));
            recentActivity.setTextsExtracted(Math.min(totalProjects * 5L, 25L));
            recentActivity.setPeriod("last_7_days");
            statistics.setRecentActivity(recentActivity);
            
            log.debug("Statistiken erfolgreich geladen: {} Projekte", totalProjects);
            return ResponseEntity.ok(statistics);
            
        } catch (Exception e) {
            log.error("Fehler beim Laden der Projekt-Statistiken", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * GET /api/v1/projects/{projectId} - Einzelnes Projekt abrufen
     */
    @GetMapping("/{projectId}")
    public ResponseEntity<ProjectDTO> getProject(@PathVariable String projectId) {
        log.debug("GET /api/v1/projects/{}", projectId);
        
        try {
            Project project = projectService.getProjectById(projectId);
            ProjectDTO projectDTO = dtoMapper.toDTO(project);
            
            log.debug("Projekt {} erfolgreich geladen", projectId);
            return ResponseEntity.ok(projectDTO);
            
        } catch (EntityNotFoundException e) {
            log.warn("Projekt nicht gefunden: {} {}", projectId, e);
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            log.error("Fehler beim Laden des Projekts: {}", projectId, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/{projectId}/statistics")
    public ResponseEntity<ProjectStatisticsDTO> getProjectStatistics(@PathVariable String projectId) {
        log.debug("GET /api/v1/projects/{}/statistics", projectId);

        try {
            Project project = projectService.getProjectById(projectId);
            ProjectStatisticsDTO statisticsDTO = new ProjectStatisticsDTO();
            statisticsDTO.setTotalFiles((long)project.getFileCount());
            statisticsDTO.setTotalExtractedTexts((long)project.getExtractedTextCount());
            statisticsDTO.setTotalFileSize(0L);
    

            log.debug("Projekt {} erfolgreich geladen", projectId);
            return ResponseEntity.ok(statisticsDTO);

        } catch (EntityNotFoundException e) {
            log.warn("Projekt nicht gefunden: {} {}", projectId, e);
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            log.error("Fehler beim Laden des Projekts: {}", projectId, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * POST /api/v1/projects - Neues Projekt erstellen
     */
    @PostMapping
    public ResponseEntity<ProjectDTO> createProject(@Valid @RequestBody CreateProjectRequest request) {
        log.info("POST /api/v1/projects - Erstelle Projekt: {}", request.getName());
        
        try {
            // Tags normalisieren
            request.normalizeTags();
            
            // Request zu Entity konvertieren
            Project project = dtoMapper.toEntity(request);
            
            // Projekt erstellen
            Project savedProject = projectService.createProject(project);
            ProjectDTO projectDTO = dtoMapper.toDTO(savedProject);
            
            log.info("Projekt erfolgreich erstellt: {} (ID: {})", 
                    savedProject.getName(), savedProject.getId());
            
            return ResponseEntity.status(HttpStatus.CREATED).body(projectDTO);
            
        } catch (IllegalArgumentException e) {
            log.warn("Ungültige Projekt-Daten: {}", e.getMessage());
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            log.error("Fehler beim Erstellen des Projekts: {}", request.getName(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * PUT /api/v1/projects/{projectId} - Projekt aktualisieren
     */
    @PutMapping("/{projectId}")
    public ResponseEntity<ProjectDTO> updateProject(@PathVariable String projectId, 
                                                   @Valid @RequestBody UpdateProjectRequest request) {
        log.info("PUT /api/v1/projects/{} - Aktualisiere Projekt: {}", projectId, request.getName());
        
        try {
            // Tags normalisieren
            request.normalizeTags();
            
            // Request zu Entity konvertieren und aktualisieren
            Project updatedProject = new Project(request.getName(), request.getDescription());
            if (request.getTags() != null) {
                updatedProject.getTags().addAll(request.getTags());
            }
            
            // Projekt aktualisieren
            Project savedProject = projectService.updateProject(projectId, updatedProject);
            ProjectDTO projectDTO = dtoMapper.toDTO(savedProject);
            
            log.info("Projekt erfolgreich aktualisiert: {} (ID: {})", 
                    savedProject.getName(), savedProject.getId());
            
            return ResponseEntity.ok(projectDTO);
            
        } catch (EntityNotFoundException e) {
            log.warn("Projekt nicht gefunden für Update: {}", projectId);
            return ResponseEntity.notFound().build();
        } catch (IllegalArgumentException e) {
            log.warn("Ungültige Update-Daten für Projekt {}: {}", projectId, e.getMessage());
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            log.error("Fehler beim Aktualisieren des Projekts: {}", projectId, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * DELETE /api/v1/projects/{projectId} - Projekt löschen
     */
    @DeleteMapping("/{projectId}")
    public ResponseEntity<Void> deleteProject(@PathVariable String projectId) {
        log.info("DELETE /api/v1/projects/{}", projectId);
        
        try {
            projectService.deleteProject(projectId);
            
            log.info("Projekt erfolgreich gelöscht: {}", projectId);
            return ResponseEntity.noContent().build();
            
        } catch (EntityNotFoundException e) {
            log.warn("Projekt nicht gefunden für Löschung: {}", projectId);
            return ResponseEntity.notFound().build();
        } catch (IllegalStateException e) {
            log.warn("Projekt kann nicht gelöscht werden: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        } catch (Exception e) {
            log.error("Fehler beim Löschen des Projekts: {}", projectId, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * GET /api/v1/projects/{projectId}/files - Dateien eines Projekts abrufen
     */
    @GetMapping("/{projectId}/files")
    public ResponseEntity<List<ProjectFileDTO>> getProjectFiles(@PathVariable String projectId) {
        log.debug("GET /api/v1/projects/{}/files", projectId);
        
        try {
            // Projekt existiert prüfen
            projectService.getProjectById(projectId);
            
            // Dateien laden
            List<ProjectFile> projectFiles = fileUploadService.getProjectFiles(projectId);
            List<ProjectFileDTO> fileDTOs = dtoMapper.toProjectFileDTOList(projectFiles);
            
            log.debug("Returning {} files for project {}", fileDTOs.size(), projectId);
            return ResponseEntity.ok(fileDTOs);
            
        } catch (EntityNotFoundException e) {
            log.warn("Projekt nicht gefunden für Datei-Liste: {}", projectId);
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            log.error("Fehler beim Laden der Dateien für Projekt: {}", projectId, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * GET /api/v1/projects/tags - Alle verwendeten Tags abrufen
     */
    @GetMapping("/tags")
    public ResponseEntity<List<String>> getAllUsedTags() {
        log.debug("GET /api/v1/projects/tags");
        
        try {
            List<String> tags = projectService.getAllUsedTags();
            
            log.debug("Returning {} unique tags", tags.size());
            return ResponseEntity.ok(tags);
            
        } catch (Exception e) {
            log.error("Fehler beim Laden der Tags", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * POST /api/v1/projects/{projectId}/tags - Tag zu Projekt hinzufügen
     */
    @PostMapping("/{projectId}/tags")
    public ResponseEntity<ProjectDTO> addTag(@PathVariable String projectId, 
                                           @RequestParam String tag) {
        log.debug("POST /api/v1/projects/{}/tags - Tag: {}", projectId, tag);
        
        try {
            if (tag == null || tag.trim().isEmpty()) {
                return ResponseEntity.badRequest().build();
            }
            
            Project project = projectService.addTag(projectId, tag.trim());
            ProjectDTO projectDTO = dtoMapper.toDTO(project);
            
            log.debug("Tag '{}' zu Projekt {} hinzugefügt", tag, projectId);
            return ResponseEntity.ok(projectDTO);
            
        } catch (EntityNotFoundException e) {
            log.warn("Projekt nicht gefunden für Tag-Hinzufügung: {}", projectId);
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            log.error("Fehler beim Hinzufügen des Tags '{}' zu Projekt: {}", tag, projectId, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * DELETE /api/v1/projects/{projectId}/tags - Tag von Projekt entfernen
     */
    @DeleteMapping("/{projectId}/tags")
    public ResponseEntity<ProjectDTO> removeTag(@PathVariable String projectId, 
                                              @RequestParam String tag) {
        log.debug("DELETE /api/v1/projects/{}/tags - Tag: {}", projectId, tag);
        
        try {
            if (tag == null || tag.trim().isEmpty()) {
                return ResponseEntity.badRequest().build();
            }
            
            Project project = projectService.removeTag(projectId, tag.trim());
            ProjectDTO projectDTO = dtoMapper.toDTO(project);
            
            log.debug("Tag '{}' von Projekt {} entfernt", tag, projectId);
            return ResponseEntity.ok(projectDTO);
            
        } catch (EntityNotFoundException e) {
            log.warn("Projekt nicht gefunden für Tag-Entfernung: {}", projectId);
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            log.error("Fehler beim Entfernen des Tags '{}' von Projekt: {}", tag, projectId, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * PUT /api/v1/projects/{projectId}/tags - Alle Tags eines Projekts setzen
     */
    @PutMapping("/{projectId}/tags")
    public ResponseEntity<ProjectDTO> setTags(@PathVariable String projectId, 
                                            @RequestBody Set<String> tags) {
        log.debug("PUT /api/v1/projects/{}/tags - Tags: {}", projectId, tags);
        
        try {
            Project project = projectService.setTags(projectId, tags);
            ProjectDTO projectDTO = dtoMapper.toDTO(project);
            
            log.debug("Tags für Projekt {} gesetzt: {}", projectId, tags);
            return ResponseEntity.ok(projectDTO);
            
        } catch (EntityNotFoundException e) {
            log.warn("Projekt nicht gefunden für Tag-Update: {}", projectId);
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            log.error("Fehler beim Setzen der Tags für Projekt: {}", projectId, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}