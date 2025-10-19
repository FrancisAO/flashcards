package com.fao.flashcards.ocr.adapter.rest;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.fao.flashcards.ocr.application.port.dto.DTOMapper;
import com.fao.flashcards.ocr.application.port.dto.ProjectFileDTO;
import com.fao.flashcards.ocr.application.port.in.FileUploadInputPort;
import com.fao.flashcards.ocr.application.port.in.OCRInputPort;
import com.fao.flashcards.ocr.model.ExtractedText;
import com.fao.flashcards.ocr.model.FileUploadResponse;
import com.fao.flashcards.ocr.model.ProjectFile;
import com.fao.flashcards.shared.model.pagination.Page;
import com.fao.flashcards.shared.model.pagination.PageRequest;

import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;

/**
 * REST Controller für Datei-Management.
 * Stellt Endpoints für Upload, Download und Verwaltung von Dateien bereit.
 */
@RestController
@RequestMapping("/api/v1/files")
@Validated
@Slf4j
@CrossOrigin(origins = "*", maxAge = 3600)
public class FileController {

    private final FileUploadInputPort fileUploadService;
    private final DTOMapper dtoMapper;
    private final OCRInputPort ocrService;

    @Autowired
    public FileController(FileUploadInputPort fileUploadService, DTOMapper dtoMapper, OCRInputPort ocrService) {
        this.fileUploadService = fileUploadService;
        this.dtoMapper = dtoMapper;
        this.ocrService = ocrService;
    }

    

    /**
     * POST /api/v1/files/upload/{projectId} - Einzelne Datei hochladen
     */
    @PostMapping("/upload/{projectId}")
    public ResponseEntity<FileUploadResponse> uploadFile(
            @PathVariable("projectId") String projectId,
            @RequestParam("file") MultipartFile file) {
        
        log.info("POST /api/v1/files/upload/{} - Datei: {}", projectId, file.getOriginalFilename());
        
        try {
            // Grundlegende Validierung
            if (file.isEmpty()) {
                log.warn("Leere Datei hochgeladen für Projekt: {}", projectId);
                return ResponseEntity.badRequest()
                    .body(FileUploadResponse.error("Datei ist leer"));
            }
            
            // Datei hochladen

            ProjectFile projectFile = fileUploadService.uploadFile(projectId, new MultipartFileAdapter(file));
            FileUploadResponse response = dtoMapper.toFileUploadResponse(projectFile);
            
            log.info("Datei erfolgreich hochgeladen: {} -> {} (ID: {})", 
                    file.getOriginalFilename(), projectId, projectFile.getId());
            
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
            
        } catch (EntityNotFoundException e) {
            log.warn("Projekt nicht gefunden für Upload: {}", projectId);
            return ResponseEntity.notFound().build();
        } catch (IllegalArgumentException e) {
            log.warn("Ungültige Datei für Upload: {}", e.getMessage());
            return ResponseEntity.badRequest()
                .body(FileUploadResponse.error(e.getMessage()));
        } catch (IOException e) {
            log.error("IO-Fehler beim Datei-Upload für Projekt: {}", projectId, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(FileUploadResponse.error("Fehler beim Speichern der Datei: " + e.getMessage()));
        } catch (Exception e) {
            log.error("Unerwarteter Fehler beim Datei-Upload für Projekt: {}", projectId, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(FileUploadResponse.error("Unerwarteter Fehler beim Upload"));
        }
    }

    /**
     * POST /api/v1/files/upload/batch/{projectId} - Mehrere Dateien hochladen
     */
    @PostMapping("/upload/batch/{projectId}")
    public ResponseEntity<List<FileUploadResponse>> uploadBatchFiles(
            @PathVariable("projectId") String projectId,
            @RequestParam("files") MultipartFile[] files) {
        
        log.info("POST /api/v1/files/upload/batch/{} - {} Dateien", projectId, files.length);
        
        if (files.length == 0) {
            log.warn("Keine Dateien für Batch-Upload empfangen");
            return ResponseEntity.badRequest().body(List.of());
        }
        
        List<FileUploadResponse> responses = new ArrayList<>();
        int successCount = 0;
        int errorCount = 0;
        
        for (MultipartFile file : files) {
            try {
                if (file.isEmpty()) {
                    log.warn("Leere Datei im Batch übersprungen: {}", file.getOriginalFilename());
                    responses.add(FileUploadResponse.error(
                        "Datei '" + file.getOriginalFilename() + "' ist leer"));
                    errorCount++;
                    continue;
                }
                
                ProjectFile projectFile = fileUploadService.uploadFile(projectId, new MultipartFileAdapter(file));
                FileUploadResponse response = dtoMapper.toFileUploadResponse(projectFile);
                responses.add(response);
                successCount++;
                
                log.debug("Datei erfolgreich im Batch hochgeladen: {}", file.getOriginalFilename());
                
            } catch (Exception e) {
                log.error("Fehler beim Upload von Datei '{}' im Batch: {}", 
                         file.getOriginalFilename(), e.getMessage());
                responses.add(FileUploadResponse.error(
                    "Fehler bei Datei '" + file.getOriginalFilename() + "': " + e.getMessage()));
                errorCount++;
            }
        }
        
        log.info("Batch-Upload abgeschlossen für Projekt {}: {} erfolgreich, {} Fehler", 
                projectId, successCount, errorCount);
        
        // Status basierend auf Ergebnis
        if (successCount == 0) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responses);
        } else if (errorCount == 0) {
            return ResponseEntity.status(HttpStatus.CREATED).body(responses);
        } else {
            return ResponseEntity.status(HttpStatus.MULTI_STATUS).body(responses);
        }
    }

    /**
     * GET /api/v1/files/download/{fileId} - Datei herunterladen
     */
    @GetMapping("/download/{fileId}")
    public ResponseEntity<Resource> downloadFile(@PathVariable("fileId") String fileId) {
        log.debug("GET /api/v1/files/download/{}", fileId);
        
        try {
            // Datei-Info laden
            ProjectFile projectFile = fileUploadService.getFileInfo(fileId);
            
            // Prüfen ob physische Datei existiert
            Path filePath = Paths.get(projectFile.getStoragePath());
            if (!Files.exists(filePath)) {
                log.warn("Physische Datei nicht gefunden: {}", filePath);
                return ResponseEntity.notFound().build();
            }
            
            // Resource erstellen
            Resource resource = new FileSystemResource(filePath);
            
            // Content-Type bestimmen
            String contentType = projectFile.getContentType();
            if (contentType == null) {
                contentType = "application/octet-stream";
            }
            
            // Response Headers setzen
            HttpHeaders headers = new HttpHeaders();
            headers.add(HttpHeaders.CONTENT_DISPOSITION, 
                       "attachment; filename=\"" + projectFile.getOriginalFilename() + "\"");
            headers.add(HttpHeaders.CONTENT_TYPE, contentType);
            headers.add(HttpHeaders.CONTENT_LENGTH, String.valueOf(projectFile.getFileSize()));
            
            log.debug("Datei zum Download bereitgestellt: {} ({})", 
                     projectFile.getOriginalFilename(), fileId);
            
            return ResponseEntity.ok()
                .headers(headers)
                .contentType(MediaType.parseMediaType(contentType))
                .body(resource);
            
        } catch (EntityNotFoundException e) {
            log.warn("Datei nicht gefunden für Download: {}", fileId);
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            log.error("Fehler beim Download der Datei: {}", fileId, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * GET /api/v1/files/{fileId} - Datei-Metadaten abrufen
     */
    @GetMapping("/{fileId}")
    public ResponseEntity<ProjectFileDTO> getFileInfo(@PathVariable("fileId") String fileId) {
        log.debug("GET /api/v1/files/{}", fileId);
        
        try {
            ProjectFile projectFile = fileUploadService.getFileInfo(fileId);
            ProjectFileDTO fileDTO = dtoMapper.toDTO(projectFile);
            
            // Prüfen ob physische Datei existiert
            boolean physicalFileExists = fileUploadService.physicalFileExists(fileId);
            if (!physicalFileExists) {
                log.warn("Physische Datei fehlt für ID: {}", fileId);
                // DTO trotzdem zurückgeben, aber Status könnte erweitert werden
            }
            
            log.debug("Datei-Info abgerufen: {}", fileId);
            return ResponseEntity.ok(fileDTO);
            
        } catch (EntityNotFoundException e) {
            log.warn("Datei nicht gefunden: {}", fileId);
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            log.error("Fehler beim Abrufen der Datei-Info: {}", fileId, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * DELETE /api/v1/files/{fileId} - Datei löschen
     */
    @DeleteMapping("/{fileId}")
    public ResponseEntity<Void> deleteFile(@PathVariable("fileId") String fileId) {
        log.info("DELETE /api/v1/files/{}", fileId);
        
        try {
            fileUploadService.deleteFile(fileId);
            
            log.info("Datei erfolgreich gelöscht: {}", fileId);
            return ResponseEntity.noContent().build();
            
        } catch (EntityNotFoundException e) {
            log.warn("Datei nicht gefunden für Löschung: {}", fileId);
            return ResponseEntity.notFound().build();
        } catch (IOException e) {
            log.error("IO-Fehler beim Löschen der Datei: {}", fileId, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        } catch (Exception e) {
            log.error("Fehler beim Löschen der Datei: {}", fileId, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * GET /api/v1/files/{fileId}/exists - Prüft ob Datei existiert
     */
    @GetMapping("/{fileId}/exists")
    public ResponseEntity<Boolean> fileExists(@PathVariable("fileId") String fileId) {
        log.debug("GET /api/v1/files/{}/exists", fileId);
        
        try {
            boolean exists = fileUploadService.fileExists(fileId);
            boolean physicalExists = fileUploadService.physicalFileExists(fileId);
            
            // Beide müssen existieren
            boolean bothExist = exists && physicalExists;
            
            log.debug("Datei-Existenz geprüft für {}: DB={}, Physical={}", fileId, exists, physicalExists);
            return ResponseEntity.ok(bothExist);
            
        } catch (Exception e) {
            log.error("Fehler beim Prüfen der Datei-Existenz: {}", fileId, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(false);
        }
    }

    /**
     * GET /api/v1/files/project/{projectId}/stats - Datei-Statistiken für Projekt
     */
    @GetMapping("/project/{projectId}/stats")
    public ResponseEntity<FileProjectStats> getProjectFileStats(@PathVariable("projectId") String projectId) {
        log.debug("GET /api/v1/files/project/{}/stats", projectId);
        
        try {
            List<ProjectFile> projectFiles = fileUploadService.getProjectFiles(projectId);
            long totalSize = fileUploadService.getTotalProjectFileSize(projectId);
            
            FileProjectStats stats = new FileProjectStats(
                projectFiles.size(),
                totalSize,
                projectFiles.stream().mapToLong(ProjectFile::getFileSize).max().orElse(0L),
                projectFiles.stream().mapToLong(ProjectFile::getFileSize).min().orElse(0L)
            );
            
            log.debug("Datei-Statistiken für Projekt {}: {} Dateien, {} bytes total", 
                     projectId, stats.getFileCount(), stats.getTotalSize());
            
            return ResponseEntity.ok(stats);
            
        } catch (Exception e) {
            log.error("Fehler beim Abrufen der Datei-Statistiken für Projekt: {}", projectId, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * GET /api/v1/files - Dateien eines Projekts abrufen (mit Pagination und Suche)
     */
    @GetMapping
    public ResponseEntity<PaginatedResponse<ProjectFileDTO>> getFilesByProject(
            @RequestParam("projectId") String projectId,
            @RequestParam(value = "filename", required = false) String filename,
            @RequestParam(value = "fileType", required = false) String fileType,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "20") int size) {
        
        log.debug("GET /api/v1/files - Projekt: {}, Filename: {}, FileType: {}, Page: {}, Size: {}", 
                 projectId, filename, fileType, page, size);
        
        try {
            // Pagination Parameter validieren
            if (page < 0) page = 0;
            if (size <= 0 || size > 100) size = 20; // Max 100 Dateien pro Seite

            PageRequest pageable = new PageRequest(page, size);

            // Dateien mit optionalen Filtern abrufen
            Page<ProjectFile> projectFilePage;
            if (filename != null || fileType != null) {
                projectFilePage = fileUploadService.searchProjectFiles(projectId, filename, fileType, pageable);
            } else {
                projectFilePage = fileUploadService.getProjectFiles(projectId, pageable);
            }
            
            // In DTOs konvertieren
            List<ProjectFileDTO> fileDTOs = projectFilePage.getContent().stream()
                .map(dtoMapper::toDTO)
                .collect(Collectors.toList());

            // Extrahierte Texte ermitteln
            List<ExtractedText> extractedTexts = ocrService.getProjectExtractedTexts(projectId);
            for(ExtractedText text : extractedTexts) {
                for(ProjectFileDTO fileDTO : fileDTOs) {
                    if(text.getProjectFile().getId().equals(fileDTO.getId())) {
                        fileDTO.setHasExtractedText(true);
                        break;
                    }
                }
            }

            PaginatedResponse<ProjectFileDTO> response = new PaginatedResponse<>(
                fileDTOs,
                projectFilePage.getNumber(),
                projectFilePage.getSize(),
                projectFilePage.getTotalElements(),
                projectFilePage.getTotalPages(),
                projectFilePage.isFirst(),
                projectFilePage.isLast()
            );
            
            log.debug("Dateien für Projekt {} abgerufen: {} von {} Dateien (Seite {} von {})", 
                     projectId, fileDTOs.size(), projectFilePage.getTotalElements(), 
                     page + 1, projectFilePage.getTotalPages());
            
            return ResponseEntity.ok(response);
            
        } catch (EntityNotFoundException e) {
            log.warn("Projekt nicht gefunden: {}", projectId);
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            log.error("Fehler beim Abrufen der Dateien für Projekt: {}", projectId, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Statistik-DTO für Projekt-Dateien.
     */
    public static class FileProjectStats {
        private final int fileCount;
        private final long totalSize;
        private final long maxFileSize;
        private final long minFileSize;

        public FileProjectStats(int fileCount, long totalSize, long maxFileSize, long minFileSize) {
            this.fileCount = fileCount;
            this.totalSize = totalSize;
            this.maxFileSize = maxFileSize;
            this.minFileSize = minFileSize;
        }

        public int getFileCount() { return fileCount; }
        public long getTotalSize() { return totalSize; }
        public long getMaxFileSize() { return maxFileSize; }
        public long getMinFileSize() { return minFileSize; }
        
        public double getAverageFileSize() {
            return fileCount > 0 ? (double) totalSize / fileCount : 0.0;
        }
        
        public String getFormattedTotalSize() {
            return formatFileSize(totalSize);
        }
        
        public String getFormattedAverageSize() {
            return formatFileSize((long) getAverageFileSize());
        }
        
        private String formatFileSize(long bytes) {
            if (bytes < 1024) return bytes + " B";
            int exp = (int) (Math.log(bytes) / Math.log(1024));
            String pre = "KMGTPE".charAt(exp - 1) + "";
            return String.format("%.1f %sB", bytes / Math.pow(1024, exp), pre);
        }
    }

    /**
     * Paginierte Antwort für Listen von Objekten.
     */
    public static class PaginatedResponse<T> {
        private final List<T> content;
        private final int page;
        private final int size;
        private final long totalElements;
        private final int totalPages;
        private final boolean first;
        private final boolean last;

        public PaginatedResponse(List<T> content, int page, int size, long totalElements, 
                               int totalPages, boolean first, boolean last) {
            this.content = content;
            this.page = page;
            this.size = size;
            this.totalElements = totalElements;
            this.totalPages = totalPages;
            this.first = first;
            this.last = last;
        }

        public List<T> getContent() { return content; }
        public int getPage() { return page; }
        public int getSize() { return size; }
        public long getTotalElements() { return totalElements; }
        public int getTotalPages() { return totalPages; }
        public boolean isFirst() { return first; }
        public boolean isLast() { return last; }
        public boolean hasNext() { return !last; }
        public boolean hasPrevious() { return !first; }
    }
}