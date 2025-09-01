package com.fao.flashcards.domain.repository;

import com.fao.flashcards.domain.model.ExtractedText;
import com.fao.flashcards.domain.model.OCRResult;
import com.fao.flashcards.domain.model.OCRStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Repository für OCR-Verarbeitungsergebnisse.
 * Bietet CRUD-Operationen und spezielle Abfragen für Performance-Metriken und Status-Tracking.
 */
@Repository
public interface OCRResultRepository extends JpaRepository<OCRResult, String> {
    
    /**
     * Findet OCRResult nach ExtractedText.
     */
    Optional<OCRResult> findByExtractedText(ExtractedText extractedText);
    
    /**
     * Findet OCRResult nach ExtractedText-ID.
     */
    Optional<OCRResult> findByExtractedTextId(String extractedTextId);
    
    /**
     * Findet OCR-Ergebnisse nach Status.
     */
    List<OCRResult> findByStatus(OCRStatus status);
    
    /**
     * Findet OCR-Ergebnisse nach Status mit Paginierung.
     */
    Page<OCRResult> findByStatus(OCRStatus status, Pageable pageable);
    
    /**
     * Findet alle OCR-Ergebnisse eines bestimmten Projekts.
     */
    @Query("SELECT ocr FROM OCRResult ocr WHERE ocr.extractedText.projectFile.project.id = :projectId")
    List<OCRResult> findByProjectId(@Param("projectId") String projectId);
    
    /**
     * Findet OCR-Ergebnisse eines Projekts nach Status.
     */
    @Query("SELECT ocr FROM OCRResult ocr WHERE ocr.extractedText.projectFile.project.id = :projectId " +
           "AND ocr.status = :status")
    List<OCRResult> findByProjectIdAndStatus(@Param("projectId") String projectId, 
                                           @Param("status") OCRStatus status);
    
    /**
     * Findet erfolgreiche OCR-Ergebnisse.
     */
    @Query("SELECT ocr FROM OCRResult ocr WHERE ocr.status = 'SUCCESS'")
    List<OCRResult> findSuccessfulResults();
    
    /**
     * Findet fehlgeschlagene OCR-Ergebnisse.
     */
    @Query("SELECT ocr FROM OCRResult ocr WHERE ocr.status = 'FAILED'")
    List<OCRResult> findFailedResults();
    
    /**
     * Findet laufende OCR-Verarbeitungen.
     */
    @Query("SELECT ocr FROM OCRResult ocr WHERE ocr.status = 'PROCESSING'")
    List<OCRResult> findProcessingResults();
    
    /**
     * Findet ausstehende OCR-Verarbeitungen.
     */
    @Query("SELECT ocr FROM OCRResult ocr WHERE ocr.status = 'PENDING'")
    List<OCRResult> findPendingResults();
    
    /**
     * Findet OCR-Ergebnisse nach verwendetem Model.
     */
    List<OCRResult> findByModelUsed(String modelUsed);
    
    /**
     * Findet OCR-Ergebnisse mit einer bestimmten Mindest-Confidence.
     */
    @Query("SELECT ocr FROM OCRResult ocr WHERE ocr.confidenceScore >= :minConfidence")
    List<OCRResult> findByConfidenceScoreGreaterThanEqual(@Param("minConfidence") Double minConfidence);
    
    /**
     * Findet OCR-Ergebnisse mit Confidence-Score im angegebenen Bereich.
     */
    @Query("SELECT ocr FROM OCRResult ocr WHERE ocr.confidenceScore BETWEEN :minConfidence AND :maxConfidence")
    List<OCRResult> findByConfidenceScoreBetween(@Param("minConfidence") Double minConfidence,
                                               @Param("maxConfidence") Double maxConfidence);
    
    /**
     * Findet OCR-Ergebnisse nach Verarbeitungszeit (langsamer als Threshold).
     */
    @Query("SELECT ocr FROM OCRResult ocr WHERE ocr.processingTimeMs > :thresholdMs")
    List<OCRResult> findSlowProcessingResults(@Param("thresholdMs") Long thresholdMs);
    
    /**
     * Findet OCR-Ergebnisse nach Verarbeitungszeit (schneller als Threshold).
     */
    @Query("SELECT ocr FROM OCRResult ocr WHERE ocr.processingTimeMs <= :thresholdMs")
    List<OCRResult> findFastProcessingResults(@Param("thresholdMs") Long thresholdMs);
    
    /**
     * Findet OCR-Ergebnisse die nach einem bestimmten Datum erstellt wurden.
     */
    List<OCRResult> findByCreatedAtAfter(LocalDateTime dateTime);
    
    /**
     * Findet OCR-Ergebnisse sortiert nach Erstellungsdatum (neueste zuerst).
     */
    List<OCRResult> findAllByOrderByCreatedAtDesc();
    
    /**
     * Findet OCR-Ergebnisse eines Projekts sortiert nach Erstellungsdatum.
     */
    @Query("SELECT ocr FROM OCRResult ocr WHERE ocr.extractedText.projectFile.project.id = :projectId " +
           "ORDER BY ocr.createdAt DESC")
    List<OCRResult> findByProjectIdOrderByCreatedAtDesc(@Param("projectId") String projectId);
    
    /**
     * Zählt OCR-Ergebnisse nach Status.
     */
    long countByStatus(OCRStatus status);
    
    /**
     * Zählt OCR-Ergebnisse eines bestimmten Projekts.
     */
    @Query("SELECT COUNT(ocr) FROM OCRResult ocr WHERE ocr.extractedText.projectFile.project.id = :projectId")
    long countByProjectId(@Param("projectId") String projectId);
    
    /**
     * Zählt erfolgreiche OCR-Verarbeitungen eines Projekts.
     */
    @Query("SELECT COUNT(ocr) FROM OCRResult ocr WHERE ocr.extractedText.projectFile.project.id = :projectId " +
           "AND ocr.status = 'SUCCESS'")
    long countSuccessfulByProjectId(@Param("projectId") String projectId);
    
    /**
     * Zählt fehlgeschlagene OCR-Verarbeitungen eines Projekts.
     */
    @Query("SELECT COUNT(ocr) FROM OCRResult ocr WHERE ocr.extractedText.projectFile.project.id = :projectId " +
           "AND ocr.status = 'FAILED'")
    long countFailedByProjectId(@Param("projectId") String projectId);
    
    /**
     * Berechnet die durchschnittliche Verarbeitungszeit.
     */
    @Query("SELECT AVG(ocr.processingTimeMs) FROM OCRResult ocr WHERE ocr.status = 'SUCCESS'")
    Double calculateAverageProcessingTime();
    
    /**
     * Berechnet die durchschnittliche Verarbeitungszeit eines Projekts.
     */
    @Query("SELECT AVG(ocr.processingTimeMs) FROM OCRResult ocr " +
           "WHERE ocr.extractedText.projectFile.project.id = :projectId AND ocr.status = 'SUCCESS'")
    Double calculateAverageProcessingTimeByProjectId(@Param("projectId") String projectId);
    
    /**
     * Berechnet die Erfolgsrate als Prozentsatz.
     */
    @Query("SELECT (CAST(COUNT(CASE WHEN ocr.status = 'SUCCESS' THEN 1 END) AS DOUBLE) / COUNT(*)) * 100 " +
           "FROM OCRResult ocr")
    Double calculateSuccessRate();
    
    /**
     * Berechnet die Erfolgsrate eines Projekts als Prozentsatz.
     */
    @Query("SELECT (CAST(COUNT(CASE WHEN ocr.status = 'SUCCESS' THEN 1 END) AS DOUBLE) / COUNT(*)) * 100 " +
           "FROM OCRResult ocr WHERE ocr.extractedText.projectFile.project.id = :projectId")
    Double calculateSuccessRateByProjectId(@Param("projectId") String projectId);
    
    /**
     * Findet die schnellste Verarbeitungszeit.
     */
    @Query("SELECT MIN(ocr.processingTimeMs) FROM OCRResult ocr WHERE ocr.status = 'SUCCESS'")
    Long findMinProcessingTime();
    
    /**
     * Findet die langsamste Verarbeitungszeit.
     */
    @Query("SELECT MAX(ocr.processingTimeMs) FROM OCRResult ocr WHERE ocr.status = 'SUCCESS'")
    Long findMaxProcessingTime();
    
    /**
     * Berechnet die durchschnittliche Confidence-Score.
     */
    @Query("SELECT AVG(ocr.confidenceScore) FROM OCRResult ocr WHERE ocr.confidenceScore IS NOT NULL")
    Double calculateAverageConfidenceScore();
    
    /**
     * Findet OCR-Ergebnisse mit Error-Details.
     */
    @Query("SELECT ocr FROM OCRResult ocr WHERE ocr.errorDetails IS NOT NULL AND ocr.errorDetails != ''")
    List<OCRResult> findResultsWithErrors();
    
    /**
     * Sucht in Error-Details.
     */
    @Query("SELECT ocr FROM OCRResult ocr WHERE " +
           "LOWER(ocr.errorDetails) LIKE LOWER(CONCAT('%', :searchTerm, '%'))")
    List<OCRResult> searchInErrorDetails(@Param("searchTerm") String searchTerm);
    
    /**
     * Findet OCR-Ergebnisse nach Anzahl verarbeiteter Seiten.
     */
    @Query("SELECT ocr FROM OCRResult ocr WHERE ocr.pagesProcessed >= :minPages")
    List<OCRResult> findByPagesProcessedGreaterThanEqual(@Param("minPages") Integer minPages);
    
    /**
     * Berechnet die Gesamtanzahl verarbeiteter Seiten.
     */
    @Query("SELECT COALESCE(SUM(ocr.pagesProcessed), 0) FROM OCRResult ocr WHERE ocr.status = 'SUCCESS'")
    Long calculateTotalPagesProcessed();
    
    /**
     * Findet OCR-Ergebnisse in einem bestimmten Zeitbereich.
     */
    @Query("SELECT ocr FROM OCRResult ocr WHERE ocr.createdAt BETWEEN :startDate AND :endDate")
    List<OCRResult> findByCreatedAtBetween(@Param("startDate") LocalDateTime startDate,
                                         @Param("endDate") LocalDateTime endDate);
    
    /**
     * Findet OCR-Ergebnisse nach mehreren Status-Werten.
     */
    @Query("SELECT ocr FROM OCRResult ocr WHERE ocr.status IN :statuses")
    List<OCRResult> findByStatusIn(@Param("statuses") List<OCRStatus> statuses);
    
    /**
     * Prüft ob OCRResult für eine bestimmte ExtractedText existiert.
     */
    boolean existsByExtractedTextId(String extractedTextId);
    
    /**
     * Findet das neueste OCR-Ergebnis eines Projekts.
     */
    @Query("SELECT ocr FROM OCRResult ocr WHERE ocr.extractedText.projectFile.project.id = :projectId " +
           "ORDER BY ocr.createdAt DESC LIMIT 1")
    Optional<OCRResult> findLatestByProjectId(@Param("projectId") String projectId);
    
    /**
     * Statistik: Anzahl OCR-Ergebnisse pro Status.
     */
    @Query("SELECT ocr.status, COUNT(ocr) FROM OCRResult ocr GROUP BY ocr.status")
    List<Object[]> getStatusStatistics();
    
    /**
     * Statistik: Anzahl OCR-Ergebnisse pro Model.
     */
    @Query("SELECT ocr.modelUsed, COUNT(ocr) FROM OCRResult ocr WHERE ocr.modelUsed IS NOT NULL " +
           "GROUP BY ocr.modelUsed")
    List<Object[]> getModelUsageStatistics();
}