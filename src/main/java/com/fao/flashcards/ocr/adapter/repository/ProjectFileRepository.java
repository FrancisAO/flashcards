package com.fao.flashcards.ocr.adapter.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.fao.flashcards.cards.model.FileType;
import com.fao.flashcards.ocr.model.Project;
import com.fao.flashcards.ocr.model.ProjectFile;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Repository für Projektdateien.
 * Bietet CRUD-Operationen und spezielle Abfragen für Datei-Management.
 */
@Repository
public interface ProjectFileRepository extends JpaRepository<ProjectFile, String> {
    
    /**
     * Findet alle Dateien eines bestimmten Projekts.
     */
    List<ProjectFile> findByProject(Project project);
    
    /**
     * Findet alle Dateien eines bestimmten Projekts mit Paginierung.
     */
    Page<ProjectFile> findByProject(Project project, Pageable pageable);
    
    /**
     * Findet alle Dateien eines bestimmten Projekts nach Projekt-ID.
     */
    List<ProjectFile> findByProjectId(String projectId);
    
    /**
     * Findet alle Dateien eines bestimmten Projekts nach Projekt-ID mit Paginierung.
     */
    Page<ProjectFile> findByProjectId(String projectId, Pageable pageable);
    
    /**
     * Findet Dateien nach Projekt und Dateityp.
     */
    List<ProjectFile> findByProjectAndFileType(Project project, FileType fileType);
    
    /**
     * Findet Dateien nach Projekt-ID und Dateityp.
     */
    List<ProjectFile> findByProjectIdAndFileType(String projectId, FileType fileType);
    
    /**
     * Findet Dateien nach Dateityp.
     */
    List<ProjectFile> findByFileType(FileType fileType);
    
    /**
     * Findet Dateien ohne ExtractedText (für OCR-Verarbeitung).
     */
    @Query("SELECT pf FROM ProjectFile pf WHERE pf.id NOT IN " +
           "(SELECT DISTINCT et.projectFile.id FROM ExtractedText et WHERE et.projectFile.id = pf.id)")
    List<ProjectFile> findFilesWithoutExtractedText();
    
    /**
     * Findet Dateien eines Projekts ohne ExtractedText.
     */
    @Query("SELECT pf FROM ProjectFile pf WHERE pf.project.id = :projectId AND pf.id NOT IN " +
           "(SELECT DISTINCT et.projectFile.id FROM ExtractedText et WHERE et.projectFile.id = pf.id)")
    List<ProjectFile> findFilesWithoutExtractedTextByProjectId(@Param("projectId") String projectId);
    
    /**
     * Findet Dateien mit ExtractedText.
     */
    @Query("SELECT DISTINCT pf FROM ProjectFile pf JOIN ExtractedText et ON et.projectFile.id = pf.id")
    List<ProjectFile> findFilesWithExtractedText();
    
    /**
     * Findet Dateien eines Projekts mit ExtractedText.
     */
    @Query("SELECT DISTINCT pf FROM ProjectFile pf JOIN ExtractedText et ON et.projectFile.id = pf.id " +
           "WHERE pf.project.id = :projectId")
    List<ProjectFile> findFilesWithExtractedTextByProjectId(@Param("projectId") String projectId);
    
    /**
     * Findet Dateien nach Original-Dateiname (case-insensitive).
     */
    @Query("SELECT pf FROM ProjectFile pf WHERE LOWER(pf.originalFilename) LIKE LOWER(CONCAT('%', :filename, '%'))")
    List<ProjectFile> findByOriginalFilenameContainingIgnoreCase(@Param("filename") String filename);
    
    /**
     * Findet Dateien nach Content-Type.
     */
    List<ProjectFile> findByContentType(String contentType);
    
    /**
     * Findet Dateien nach Content-Type-Pattern.
     */
    @Query("SELECT pf FROM ProjectFile pf WHERE pf.contentType LIKE :contentTypePattern")
    List<ProjectFile> findByContentTypePattern(@Param("contentTypePattern") String contentTypePattern);
    
    /**
     * Findet Dateien die größer als eine bestimmte Größe sind.
     */
    @Query("SELECT pf FROM ProjectFile pf WHERE pf.fileSize > :minSize")
    List<ProjectFile> findByFileSizeGreaterThan(@Param("minSize") Long minSize);
    
    /**
     * Findet Dateien die kleiner als eine bestimmte Größe sind.
     */
    @Query("SELECT pf FROM ProjectFile pf WHERE pf.fileSize < :maxSize")
    List<ProjectFile> findByFileSizeLessThan(@Param("maxSize") Long maxSize);
    
    /**
     * Findet Dateien in einem bestimmten Größenbereich.
     */
    @Query("SELECT pf FROM ProjectFile pf WHERE pf.fileSize BETWEEN :minSize AND :maxSize")
    List<ProjectFile> findByFileSizeBetween(@Param("minSize") Long minSize, @Param("maxSize") Long maxSize);
    
    /**
     * Findet Dateien die nach einem bestimmten Datum hochgeladen wurden.
     */
    List<ProjectFile> findByUploadedAtAfter(LocalDateTime dateTime);
    
    /**
     * Findet Dateien die vor einem bestimmten Datum hochgeladen wurden.
     */
    List<ProjectFile> findByUploadedAtBefore(LocalDateTime dateTime);
    
    /**
     * Findet Dateien sortiert nach Upload-Datum (neueste zuerst).
     */
    List<ProjectFile> findAllByOrderByUploadedAtDesc();
    
    /**
     * Findet Dateien eines Projekts sortiert nach Upload-Datum.
     */
    List<ProjectFile> findByProjectIdOrderByUploadedAtDesc(String projectId);
    
    /**
     * Zählt Dateien eines bestimmten Projekts.
     */
    long countByProjectId(String projectId);
    
    /**
     * Zählt Dateien eines bestimmten Projekts nach Dateityp.
     */
    long countByProjectIdAndFileType(String projectId, FileType fileType);
    
    /**
     * Berechnet die Gesamtgröße aller Dateien eines Projekts.
     */
    @Query("SELECT COALESCE(SUM(pf.fileSize), 0) FROM ProjectFile pf WHERE pf.project.id = :projectId")
    Long calculateTotalFileSizeByProjectId(@Param("projectId") String projectId);
    
    /**
     * Findet eine Datei nach Storage-Path.
     */
    Optional<ProjectFile> findByStoragePath(String storagePath);
    
    /**
     * Prüft ob eine Datei mit dem gegebenen Storage-Path existiert.
     */
    boolean existsByStoragePath(String storagePath);
    
    /**
     * Findet Dateien nach mehreren Dateitypen.
     */
    @Query("SELECT pf FROM ProjectFile pf WHERE pf.fileType IN :fileTypes")
    List<ProjectFile> findByFileTypeIn(@Param("fileTypes") List<FileType> fileTypes);
    
    /**
     * Findet Dateien eines Projekts nach mehreren Dateitypen.
     */
    @Query("SELECT pf FROM ProjectFile pf WHERE pf.project.id = :projectId AND pf.fileType IN :fileTypes")
    List<ProjectFile> findByProjectIdAndFileTypeIn(@Param("projectId") String projectId, 
                                                  @Param("fileTypes") List<FileType> fileTypes);
}