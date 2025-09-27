package com.fao.flashcards.application.service;

import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.multipart.MultipartFile;

import com.fao.flashcards.adapter.repository.ProjectFileRepository;
import com.fao.flashcards.adapter.repository.ProjectRepository;
import com.fao.flashcards.application.model.FileType;
import com.fao.flashcards.application.model.Project;
import com.fao.flashcards.application.model.ProjectFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * Service für Datei-Upload-Verarbeitung.
 * Verwaltet Datei-Uploads, Validierung, Speicherung und Storage-Path-Generierung.
 */
@Service
@Validated
@Slf4j
public class FileUploadService {

    private final ProjectFileRepository projectFileRepository;
    private final ProjectRepository projectRepository;
    private final ProjectService projectService;

    // Konfigurierbare Werte
    @Value("${app.upload.base-path:uploads}")
    private String uploadBasePath;

    @Value("${app.upload.max-file-size:52428800}") // 50MB default
    private long maxFileSize;

    @Value("${app.upload.allowed-extensions:pdf,png,jpg,jpeg,gif,bmp,tiff,txt,docx,doc}")
    private String allowedExtensions;

    // Unterstützte MIME-Types für verschiedene Dateitypen
    private static final Map<FileType, Set<String>> SUPPORTED_MIME_TYPES = Map.of(
        FileType.IMAGE, Set.of(
            "image/jpeg", "image/jpg", "image/png", "image/gif",
            "image/bmp", "image/tiff", "image/tif", "image/webp"
        ),
        FileType.DOCUMENT, Set.of(
            "application/pdf",
            "text/plain", "text/csv",
            "application/vnd.openxmlformats-officedocument.wordprocessingml.document",
            "application/msword",
            "application/vnd.oasis.opendocument.text"
        )
    );

    // Gefährliche Dateiendungen die nicht erlaubt sind
    private static final Set<String> DANGEROUS_EXTENSIONS = Set.of(
        "exe", "bat", "cmd", "com", "pif", "scr", "vbs", "js", "jar", "sh", "ps1"
    );

    // Pattern für sichere Dateinamen
    private static final Pattern SAFE_FILENAME_PATTERN = Pattern.compile("^[a-zA-Z0-9._-]+$");

    @Autowired
    public FileUploadService(ProjectFileRepository projectFileRepository,
                           ProjectRepository projectRepository,
                           ProjectService projectService) {
        this.projectFileRepository = projectFileRepository;
        this.projectRepository = projectRepository;
        this.projectService = projectService;
    }

    /**
     * Verarbeitet einen Datei-Upload für ein bestimmtes Projekt.
     */
    @Transactional
    public ProjectFile uploadFile(@NotBlank String projectId, @NotNull MultipartFile file) throws IOException {
        log.info("Starte Datei-Upload für Projekt {}: {}", projectId, file.getOriginalFilename());

        // Projekt validieren
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new EntityNotFoundException("Projekt mit ID " + projectId + " nicht gefunden"));

        // Datei validieren
        validateFile(file);

        // FileType bestimmen
        FileType fileType = determineFileType(file);

        // Storage-Path generieren
        String storagePath = generateStoragePath(projectId, file.getOriginalFilename());

        // Datei physisch speichern
        Path actualPath = saveFileToStorage(file, storagePath);

        // ProjectFile-Entity erstellen und speichern
        ProjectFile projectFile = new ProjectFile(
            project,
            file.getOriginalFilename(),
            file.getContentType(),
            file.getSize(),
            actualPath.toString(),
            fileType
        );

        ProjectFile savedProjectFile = projectFileRepository.save(projectFile);
        
        // Projekt-Statistiken aktualisieren
        projectService.updateFileCount(projectId);

        log.info("Datei erfolgreich hochgeladen: {} -> {}", file.getOriginalFilename(), actualPath);
        return savedProjectFile;
    }

    /**
     * Validiert eine hochgeladene Datei.
     */
    private void validateFile(MultipartFile file) {
        if (file.isEmpty()) {
            throw new IllegalArgumentException("Datei ist leer");
        }

        if (file.getSize() > maxFileSize) {
            throw new IllegalArgumentException(
                String.format("Datei ist zu groß. Maximum: %d MB, Aktuell: %.2f MB", 
                             maxFileSize / 1024 / 1024, 
                             file.getSize() / 1024.0 / 1024.0)
            );
        }

        String originalFilename = file.getOriginalFilename();
        if (originalFilename == null || originalFilename.trim().isEmpty()) {
            throw new IllegalArgumentException("Dateiname ist ungültig");
        }

        // Dateiendung prüfen
        String extension = getFileExtension(originalFilename).toLowerCase();
        if (DANGEROUS_EXTENSIONS.contains(extension)) {
            throw new IllegalArgumentException("Dateityp '" + extension + "' ist nicht erlaubt");
        }

        Set<String> allowedExtensionSet = Set.of(allowedExtensions.toLowerCase().split(","));
        if (!allowedExtensionSet.contains(extension)) {
            throw new IllegalArgumentException(
                "Dateierweiterung '" + extension + "' ist nicht erlaubt. Erlaubt: " + allowedExtensions
            );
        }

        // Dateiname-Sicherheit prüfen
        String sanitizedName = sanitizeFilename(originalFilename);
        if (sanitizedName.length() < 3) {
            throw new IllegalArgumentException("Dateiname ist zu kurz oder enthält nur ungültige Zeichen");
        }

        log.debug("Datei-Validierung erfolgreich: {}", originalFilename);
    }

    /**
     * Bestimmt den FileType basierend auf MIME-Type und Dateiendung.
     */
    private FileType determineFileType(MultipartFile file) {
        String contentType = file.getContentType();
        String extension = getFileExtension(file.getOriginalFilename()).toLowerCase();

        // Erst nach MIME-Type suchen
        for (Map.Entry<FileType, Set<String>> entry : SUPPORTED_MIME_TYPES.entrySet()) {
            if (entry.getValue().contains(contentType)) {
                log.debug("FileType {} durch MIME-Type {} bestimmt", entry.getKey(), contentType);
                return entry.getKey();
            }
        }

        // Fallback nach Dateiendung
        FileType typeByExtension = determineFileTypeByExtension(extension);
        log.debug("FileType {} durch Dateiendung {} bestimmt", typeByExtension, extension);
        return typeByExtension;
    }

    /**
     * Bestimmt FileType nach Dateiendung.
     */
    private FileType determineFileTypeByExtension(String extension) {
        return switch (extension) {
            case "png", "jpg", "jpeg", "gif", "bmp", "tiff", "tif", "webp" -> FileType.IMAGE;
            case "pdf", "txt", "csv", "docx", "doc", "odt" -> FileType.DOCUMENT;
            default -> FileType.DOCUMENT; // Default fallback
        };
    }

    /**
     * Generiert einen eindeutigen Storage-Path für eine Datei.
     */
    private String generateStoragePath(String projectId, String originalFilename) {
        // Zeitstempel für Eindeutigkeit
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
        
        // UUID für zusätzliche Eindeutigkeit
        String uuid = UUID.randomUUID().toString().substring(0, 8);
        
        // Dateiname bereinigen
        String sanitizedFilename = sanitizeFilename(originalFilename);
        String extension = getFileExtension(sanitizedFilename);
        String nameWithoutExtension = sanitizedFilename.substring(0, 
            sanitizedFilename.length() - extension.length() - 1);
        
        // Path zusammenbauen: uploads/projektId/jahr/monat/timestamp_uuid_name.ext
        LocalDateTime now = LocalDateTime.now();
        String year = String.valueOf(now.getYear());
        String month = String.format("%02d", now.getMonthValue());
        
        String filename = String.format("%s_%s_%s.%s", timestamp, uuid, nameWithoutExtension, extension);
        
        return Paths.get(uploadBasePath, projectId, year, month, filename).toString();
    }

    /**
     * Speichert eine Datei physisch im Dateisystem.
     */
    private Path saveFileToStorage(MultipartFile file, String storagePath) throws IOException {
        Path targetPath = Paths.get(storagePath);
        
        // Verzeichnis erstellen falls nicht vorhanden
        Files.createDirectories(targetPath.getParent());
        
        // Prüfen ob Datei bereits existiert
        if (Files.exists(targetPath)) {
            // Neuen eindeutigen Namen generieren
            String newName = generateUniqueFilename(targetPath);
            targetPath = targetPath.getParent().resolve(newName);
        }
        
        // Datei kopieren
        try {
            Files.copy(file.getInputStream(), targetPath, StandardCopyOption.REPLACE_EXISTING);
            log.debug("Datei gespeichert: {}", targetPath);
            return targetPath;
        } catch (IOException e) {
            log.error("Fehler beim Speichern der Datei: {}", targetPath, e);
            throw new IOException("Datei konnte nicht gespeichert werden: " + e.getMessage(), e);
        }
    }

    /**
     * Generiert einen eindeutigen Dateinamen falls eine Datei bereits existiert.
     */
    private String generateUniqueFilename(Path originalPath) {
        String filename = originalPath.getFileName().toString();
        String extension = getFileExtension(filename);
        String nameWithoutExtension = filename.substring(0, filename.length() - extension.length() - 1);
        
        int counter = 1;
        String newFilename;
        Path newPath;
        
        do {
            newFilename = String.format("%s_(%d).%s", nameWithoutExtension, counter, extension);
            newPath = originalPath.getParent().resolve(newFilename);
            counter++;
        } while (Files.exists(newPath) && counter < 1000); // Sicherheitsgrenze
        
        if (counter >= 1000) {
            // Fallback mit UUID
            String uuid = UUID.randomUUID().toString().substring(0, 8);
            newFilename = String.format("%s_%s.%s", nameWithoutExtension, uuid, extension);
        }
        
        return newFilename;
    }

    /**
     * Bereinigt einen Dateinamen von gefährlichen Zeichen.
     */
    private String sanitizeFilename(String filename) {
        if (filename == null) return "unnamed";
        
        // Gefährliche Zeichen entfernen/ersetzen
        String sanitized = filename
            .replaceAll("[<>:\"/\\\\|?*]", "_") // Windows-problematische Zeichen
            .replaceAll("[\\x00-\\x1f\\x7f]", "") // Steuerzeichen
            .replaceAll("\\s+", "_") // Leerzeichen durch Unterstriche
            .replaceAll("_{2,}", "_") // Mehrfache Unterstriche reduzieren
            .trim();
        
        // Führende/nachfolgende Punkte entfernen (Windows-Problem)
        sanitized = sanitized.replaceAll("^[._]+|[._]+$", "");
        
        return sanitized.isEmpty() ? "unnamed" : sanitized;
    }

    /**
     * Extrahiert die Dateiendung aus einem Dateinamen.
     */
    private String getFileExtension(String filename) {
        if (filename == null || filename.isEmpty()) {
            return "";
        }
        
        int lastDotIndex = filename.lastIndexOf('.');
        if (lastDotIndex == -1 || lastDotIndex == filename.length() - 1) {
            return "";
        }
        
        return filename.substring(lastDotIndex + 1);
    }

    /**
     * Löscht eine ProjectFile und die zugehörige physische Datei.
     */
    @Transactional
    public void deleteFile(@NotBlank String fileId) throws IOException {
        log.info("Lösche Datei mit ID: {}", fileId);
        
        ProjectFile projectFile = projectFileRepository.findById(fileId)
                .orElseThrow(() -> new EntityNotFoundException("Datei mit ID " + fileId + " nicht gefunden"));
        
        String projectId = projectFile.getProject().getId();
        
        // Physische Datei löschen
        try {
            Path filePath = Paths.get(projectFile.getStoragePath());
            if (Files.exists(filePath)) {
                Files.delete(filePath);
                log.debug("Physische Datei gelöscht: {}", filePath);
            } else {
                log.warn("Physische Datei nicht gefunden: {}", filePath);
            }
        } catch (IOException e) {
            log.error("Fehler beim Löschen der physischen Datei: {}", projectFile.getStoragePath(), e);
            throw new IOException("Physische Datei konnte nicht gelöscht werden: " + e.getMessage(), e);
        }
        
        // Datenbank-Eintrag löschen
        projectFileRepository.delete(projectFile);
        
        // Projekt-Statistiken aktualisieren
        projectService.updateFileCount(projectId);
        
        log.info("Datei erfolgreich gelöscht: {}", fileId);
    }

    /**
     * Prüft ob eine Datei existiert.
     */
    @Transactional(readOnly = true)
    public boolean fileExists(@NotBlank String fileId) {
        return projectFileRepository.existsById(fileId);
    }

    /**
     * Gibt Informationen über eine Datei zurück.
     */
    @Transactional(readOnly = true)
    public ProjectFile getFileInfo(@NotBlank String fileId) {
        return projectFileRepository.findById(fileId)
                .orElseThrow(() -> new EntityNotFoundException("Datei mit ID " + fileId + " nicht gefunden"));
    }

    /**
     * Findet alle Dateien eines Projekts.
     */
    @Transactional(readOnly = true)
    public List<ProjectFile> getProjectFiles(@NotBlank String projectId) {
        return projectFileRepository.findByProjectIdOrderByUploadedAtDesc(projectId);
    }

    /**
     * Prüft ob eine physische Datei im Dateisystem existiert.
     */
    public boolean physicalFileExists(@NotBlank String fileId) {
        Optional<ProjectFile> projectFile = projectFileRepository.findById(fileId);
        if (projectFile.isEmpty()) {
            return false;
        }
        
        Path filePath = Paths.get(projectFile.get().getStoragePath());
        return Files.exists(filePath);
    }

    /**
     * Berechnet die Gesamtgröße aller Dateien eines Projekts.
     */
    @Transactional(readOnly = true)
    public long getTotalProjectFileSize(@NotBlank String projectId) {
        Long totalSize = projectFileRepository.calculateTotalFileSizeByProjectId(projectId);
        return totalSize != null ? totalSize : 0L;
    }

    @Transactional(readOnly = true)
    public Page<ProjectFile> searchProjectFiles(String projectId, String filename, String fileType, Pageable pageable) {
        List<ProjectFile> projectFiles = getProjectFiles(projectId);
        
        // Filter anwenden wenn Parameter gesetzt sind
        if (filename != null && !filename.trim().isEmpty()) {
            projectFiles = projectFiles.stream()
                    .filter(f -> f.getOriginalFilename().toLowerCase().contains(filename.toLowerCase()))
                    .collect(Collectors.toList());
        }
        
        if (fileType != null && !fileType.trim().isEmpty()) {
            projectFiles = projectFiles.stream()
                    .filter(f -> f.getFileType().name().equalsIgnoreCase(fileType))
                    .collect(Collectors.toList());
        }

        return createPageFromList(projectFiles, pageable);
    }

    @Transactional(readOnly = true)
    public Page<ProjectFile> getProjectFiles(String projectId, Pageable pageable) {
        List<ProjectFile> projectFiles = getProjectFiles(projectId);
        return createPageFromList(projectFiles, pageable);
    }
    
    /**
     * Hilfsmethode um aus einer Liste eine Page zu erstellen.
     */
    private Page<ProjectFile> createPageFromList(List<ProjectFile> projectFiles, Pageable pageable) {
        int total = projectFiles.size();
        int start = Math.min((int) pageable.getOffset(), total);
        int end = Math.min(start + pageable.getPageSize(), total);
        
        List<ProjectFile> content = start >= total ? 
            Collections.emptyList() : 
            projectFiles.subList(start, end);
        
        return new PageImpl<>(content, pageable, total);
    }
    }