package com.fao.flashcards.adapter.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.fao.flashcards.application.model.ExtractedText;
import com.fao.flashcards.application.model.ExtractionSource;
import com.fao.flashcards.application.model.Project;
import com.fao.flashcards.application.model.ProjectFile;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Repository für extrahierte Texte.
 * Bietet CRUD-Operationen und spezielle Abfragen für Text-Management und Suche.
 */
@Repository
public interface ExtractedTextRepository extends JpaRepository<ExtractedText, String> {
    
    /**
     * Findet ExtractedText nach ProjectFile.
     */
    Optional<ExtractedText> findByProjectFile(ProjectFile projectFile);
    
    /**
     * Findet ExtractedText nach ProjectFile-ID.
     */
    Optional<ExtractedText> findByProjectFileId(String projectFileId);
    
    /**
     * Findet alle ExtractedTexts eines bestimmten Projekts.
     */
    @Query("SELECT et FROM ExtractedText et WHERE et.projectFile.project = :project")
    List<ExtractedText> findByProject(@Param("project") Project project);
    
    /**
     * Findet alle ExtractedTexts eines bestimmten Projekts nach Projekt-ID.
     */
    @Query("SELECT et FROM ExtractedText et WHERE et.projectFile.project.id = :projectId")
    List<ExtractedText> findByProjectId(@Param("projectId") String projectId);
    
    /**
     * Findet alle ExtractedTexts eines bestimmten Projekts mit Paginierung.
     */
    @Query("SELECT et FROM ExtractedText et WHERE et.projectFile.project.id = :projectId")
    Page<ExtractedText> findByProjectId(@Param("projectId") String projectId, Pageable pageable);
    
    /**
     * Findet ExtractedTexts nach Extraktionsquelle.
     */
    List<ExtractedText> findByExtractionSource(ExtractionSource extractionSource);
    
    /**
     * Findet ExtractedTexts eines Projekts nach Extraktionsquelle.
     */
    @Query("SELECT et FROM ExtractedText et WHERE et.projectFile.project.id = :projectId " +
           "AND et.extractionSource = :extractionSource")
    List<ExtractedText> findByProjectIdAndExtractionSource(@Param("projectId") String projectId,
                                                           @Param("extractionSource") ExtractionSource extractionSource);
    
    /**
     * Findet bearbeitete Texte.
     */
    @Query("SELECT et FROM ExtractedText et WHERE et.isEdited = true")
    List<ExtractedText> findEditedTexts();
    
    /**
     * Findet unbearbeitete Texte.
     */
    @Query("SELECT et FROM ExtractedText et WHERE et.isEdited = false")
    List<ExtractedText> findUnEditedTexts();
    
    /**
     * Findet bearbeitete Texte eines bestimmten Projekts.
     */
    @Query("SELECT et FROM ExtractedText et WHERE et.projectFile.project.id = :projectId AND et.isEdited = true")
    List<ExtractedText> findEditedTextsByProjectId(@Param("projectId") String projectId);
    
    /**
     * Findet unbearbeitete Texte eines bestimmten Projekts.
     */
    @Query("SELECT et FROM ExtractedText et WHERE et.projectFile.project.id = :projectId AND et.isEdited = false")
    List<ExtractedText> findUnEditedTextsByProjectId(@Param("projectId") String projectId);
    
    /**
     * Volltext-Suche im extrahierten Content.
     */
    @Query("SELECT et FROM ExtractedText et WHERE " +
           "LOWER(et.extractedContent) LIKE LOWER(CONCAT('%', :searchTerm, '%'))")
    List<ExtractedText> searchInExtractedContent(@Param("searchTerm") String searchTerm);
    
    /**
     * Volltext-Suche im bearbeiteten Content.
     */
    @Query("SELECT et FROM ExtractedText et WHERE " +
           "LOWER(et.editedContent) LIKE LOWER(CONCAT('%', :searchTerm, '%'))")
    List<ExtractedText> searchInEditedContent(@Param("searchTerm") String searchTerm);
    
    /**
     * Volltext-Suche in beiden Content-Feldern.
     */
    @Query("SELECT et FROM ExtractedText et WHERE " +
           "LOWER(et.extractedContent) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
           "LOWER(et.editedContent) LIKE LOWER(CONCAT('%', :searchTerm, '%'))")
    List<ExtractedText> searchInContent(@Param("searchTerm") String searchTerm);
    
    /**
     * Volltext-Suche in beiden Content-Feldern mit Paginierung.
     */
    @Query("SELECT et FROM ExtractedText et WHERE " +
           "LOWER(et.extractedContent) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
           "LOWER(et.editedContent) LIKE LOWER(CONCAT('%', :searchTerm, '%'))")
    Page<ExtractedText> searchInContent(@Param("searchTerm") String searchTerm, Pageable pageable);
    
    /**
     * Volltext-Suche in Content eines bestimmten Projekts.
     */
    @Query("SELECT et FROM ExtractedText et WHERE et.projectFile.project.id = :projectId AND " +
           "(LOWER(et.extractedContent) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
           "LOWER(et.editedContent) LIKE LOWER(CONCAT('%', :searchTerm, '%')))")
    List<ExtractedText> searchInContentByProjectId(@Param("projectId") String projectId,
                                                   @Param("searchTerm") String searchTerm);
    
    /**
     * Findet Texte die nach einem bestimmten Datum extrahiert wurden.
     */
    List<ExtractedText> findByExtractedAtAfter(LocalDateTime dateTime);
    
    /**
     * Findet Texte die nach einem bestimmten Datum bearbeitet wurden.
     */
    List<ExtractedText> findByLastEditedAtAfter(LocalDateTime dateTime);
    
    /**
     * Findet Texte sortiert nach Extraktionsdatum (neueste zuerst).
     */
    List<ExtractedText> findAllByOrderByExtractedAtDesc();
    
    /**
     * Findet Texte eines Projekts sortiert nach Extraktionsdatum.
     */
    @Query("SELECT et FROM ExtractedText et WHERE et.projectFile.project.id = :projectId " +
           "ORDER BY et.extractedAt DESC")
    List<ExtractedText> findByProjectIdOrderByExtractedAtDesc(@Param("projectId") String projectId);
    
    /**
     * Findet leere Texte (ohne extrahierten Content).
     */
    @Query("SELECT et FROM ExtractedText et WHERE et.extractedContent IS NULL OR et.extractedContent = ''")
    List<ExtractedText> findEmptyTexts();
    
    /**
     * Findet nicht-leere Texte.
     */
    @Query("SELECT et FROM ExtractedText et WHERE et.extractedContent IS NOT NULL AND et.extractedContent != ''")
    List<ExtractedText> findNonEmptyTexts();
    
    /**
     * Zählt ExtractedTexts eines bestimmten Projekts.
     */
    @Query("SELECT COUNT(et) FROM ExtractedText et WHERE et.projectFile.project.id = :projectId")
    long countByProjectId(@Param("projectId") String projectId);
    
    /**
     * Zählt bearbeitete Texte eines bestimmten Projekts.
     */
    @Query("SELECT COUNT(et) FROM ExtractedText et WHERE et.projectFile.project.id = :projectId AND et.isEdited = true")
    long countEditedTextsByProjectId(@Param("projectId") String projectId);
    
    /**
     * Zählt Texte nach Extraktionsquelle.
     */
    long countByExtractionSource(ExtractionSource extractionSource);
    
    /**
     * Findet Texte nach mehreren Extraktionsquellen.
     */
    @Query("SELECT et FROM ExtractedText et WHERE et.extractionSource IN :extractionSources")
    List<ExtractedText> findByExtractionSourceIn(@Param("extractionSources") List<ExtractionSource> extractionSources);
    
    /**
     * Findet alle Texte die Metadata enthalten.
     */
    @Query("SELECT et FROM ExtractedText et WHERE et.metadata IS NOT NULL AND et.metadata != ''")
    List<ExtractedText> findTextsWithMetadata();
    
    /**
     * Sucht in Metadata-Feldern.
     */
    @Query("SELECT et FROM ExtractedText et WHERE " +
           "LOWER(et.metadata) LIKE LOWER(CONCAT('%', :searchTerm, '%'))")
    List<ExtractedText> searchInMetadata(@Param("searchTerm") String searchTerm);
    
    /**
     * Prüft ob ExtractedText für eine bestimmte ProjectFile existiert.
     */
    boolean existsByProjectFileId(String projectFileId);
    
    /**
     * Findet den neuesten ExtractedText eines Projekts.
     */
    @Query("SELECT et FROM ExtractedText et WHERE et.projectFile.project.id = :projectId " +
           "ORDER BY et.extractedAt DESC LIMIT 1")
    Optional<ExtractedText> findLatestByProjectId(@Param("projectId") String projectId);
    
    /**
     * Findet ExtractedTexts in einem bestimmten Zeitbereich.
     */
    @Query("SELECT et FROM ExtractedText et WHERE et.extractedAt BETWEEN :startDate AND :endDate")
    List<ExtractedText> findByExtractedAtBetween(@Param("startDate") LocalDateTime startDate,
                                                 @Param("endDate") LocalDateTime endDate);
}