package com.fao.flashcards.ocr.adapter.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.fao.flashcards.ocr.application.port.out.OCRResultRepository;
import com.fao.flashcards.ocr.model.ExtractedText;
import com.fao.flashcards.ocr.model.OCRResult;
import com.fao.flashcards.ocr.model.OCRStatus;

/**
 * Adapter der das OCRResultOutputPort Interface implementiert und
 * die Spring Boot Repository Funktionalität bereitstellt.
 */
@Component
public class JpaOCRResultRepository implements OCRResultRepository {

    private final JpaOCRResultSpringDataRepository ocrResultRepository;

    public JpaOCRResultRepository(JpaOCRResultSpringDataRepository ocrResultRepository) {
        this.ocrResultRepository = ocrResultRepository;
    }

    @Transactional
    @Override
    public OCRResult save(OCRResult ocrResult) {
        return ocrResultRepository.save(ocrResult);
    }

    @Transactional
    @Override
    public List<OCRResult> saveAll(Iterable<OCRResult> ocrResults) {
        return ocrResultRepository.saveAll(ocrResults);
    }

    @Transactional(readOnly = true)
    @Override
    public Optional<OCRResult> findById(String id) {
        return ocrResultRepository.findById(id);
    }

    @Transactional(readOnly = true)
    @Override
    public List<OCRResult> findAll() {
        return ocrResultRepository.findAll();
    }

    @Transactional(readOnly = true)
    @Override
    public boolean existsById(String id) {
        return ocrResultRepository.existsById(id);
    }

    @Transactional(readOnly = true)
    @Override
    public long count() {
        return ocrResultRepository.count();
    }

    @Transactional
    @Override
    public void deleteById(String id) {
        ocrResultRepository.deleteById(id);
    }

    @Transactional
    @Override
    public void delete(OCRResult ocrResult) {
        ocrResultRepository.delete(ocrResult);
    }

    @Transactional
    @Override
    public void deleteAll() {
        ocrResultRepository.deleteAll();
    }

    @Transactional(readOnly = true)
    @Override
    public Optional<OCRResult> findByExtractedText(ExtractedText extractedText) {
        return ocrResultRepository.findByExtractedText(extractedText);
    }

    @Transactional(readOnly = true)
    @Override
    public Optional<OCRResult> findByExtractedTextId(String extractedTextId) {
        return ocrResultRepository.findByExtractedTextId(extractedTextId);
    }

    @Transactional(readOnly = true)
    @Override
    public List<OCRResult> findByStatus(OCRStatus status) {
        return ocrResultRepository.findByStatus(status);
    }

    @Transactional(readOnly = true)
    @Override
    public List<OCRResult> findByProjectId(String projectId) {
        return ocrResultRepository.findByProjectId(projectId);
    }

    @Transactional(readOnly = true)
    @Override
    public List<OCRResult> findByProjectIdAndStatus(String projectId, OCRStatus status) {
        return ocrResultRepository.findByProjectIdAndStatus(projectId, status);
    }

    @Transactional(readOnly = true)
    @Override
    public List<OCRResult> findSuccessfulResults() {
        return ocrResultRepository.findSuccessfulResults();
    }

    @Transactional(readOnly = true)
    @Override
    public List<OCRResult> findFailedResults() {
        return ocrResultRepository.findFailedResults();
    }

    @Transactional(readOnly = true)
    @Override
    public List<OCRResult> findProcessingResults() {
        return ocrResultRepository.findProcessingResults();
    }

    @Transactional(readOnly = true)
    @Override
    public List<OCRResult> findPendingResults() {
        return ocrResultRepository.findPendingResults();
    }

    @Transactional(readOnly = true)
    @Override
    public List<OCRResult> findByModelUsed(String modelUsed) {
        return ocrResultRepository.findByModelUsed(modelUsed);
    }

    @Transactional(readOnly = true)
    @Override
    public List<OCRResult> findByConfidenceScoreGreaterThanEqual(Double minConfidence) {
        return ocrResultRepository.findByConfidenceScoreGreaterThanEqual(minConfidence);
    }

    @Transactional(readOnly = true)
    @Override
    public List<OCRResult> findByConfidenceScoreBetween(Double minConfidence, Double maxConfidence) {
        return ocrResultRepository.findByConfidenceScoreBetween(minConfidence, maxConfidence);
    }

    @Transactional(readOnly = true)
    @Override
    public List<OCRResult> findSlowProcessingResults(Long thresholdMs) {
        return ocrResultRepository.findSlowProcessingResults(thresholdMs);
    }

    @Transactional(readOnly = true)
    @Override
    public List<OCRResult> findFastProcessingResults(Long thresholdMs) {
        return ocrResultRepository.findFastProcessingResults(thresholdMs);
    }

    @Transactional(readOnly = true)
    @Override
    public List<OCRResult> findByCreatedAtAfter(LocalDateTime dateTime) {
        return ocrResultRepository.findByCreatedAtAfter(dateTime);
    }

    @Transactional(readOnly = true)
    @Override
    public List<OCRResult> findAllByOrderByCreatedAtDesc() {
        return ocrResultRepository.findAllByOrderByCreatedAtDesc();
    }

    @Transactional(readOnly = true)
    @Override
    public List<OCRResult> findByProjectIdOrderByCreatedAtDesc(String projectId) {
        return ocrResultRepository.findByProjectIdOrderByCreatedAtDesc(projectId);
    }

    @Transactional(readOnly = true)
    @Override
    public long countByStatus(OCRStatus status) {
        return ocrResultRepository.countByStatus(status);
    }

    @Transactional(readOnly = true)
    @Override
    public long countByProjectId(String projectId) {
        return ocrResultRepository.countByProjectId(projectId);
    }

    @Transactional(readOnly = true)
    @Override
    public long countSuccessfulByProjectId(String projectId) {
        return ocrResultRepository.countSuccessfulByProjectId(projectId);
    }

    @Transactional(readOnly = true)
    @Override
    public long countFailedByProjectId(String projectId) {
        return ocrResultRepository.countFailedByProjectId(projectId);
    }

    @Transactional(readOnly = true)
    @Override
    public Double calculateAverageProcessingTime() {
        return ocrResultRepository.calculateAverageProcessingTime();
    }

    @Transactional(readOnly = true)
    @Override
    public Double calculateAverageProcessingTimeByProjectId(String projectId) {
        return ocrResultRepository.calculateAverageProcessingTimeByProjectId(projectId);
    }

    @Transactional(readOnly = true)
    @Override
    public Double calculateSuccessRate() {
        return ocrResultRepository.calculateSuccessRate();
    }

    @Transactional(readOnly = true)
    @Override
    public Double calculateSuccessRateByProjectId(String projectId) {
        return ocrResultRepository.calculateSuccessRateByProjectId(projectId);
    }

    @Transactional(readOnly = true)
    @Override
    public Long findMinProcessingTime() {
        return ocrResultRepository.findMinProcessingTime();
    }

    @Transactional(readOnly = true)
    @Override
    public Long findMaxProcessingTime() {
        return ocrResultRepository.findMaxProcessingTime();
    }

    @Transactional(readOnly = true)
    @Override
    public Double calculateAverageConfidenceScore() {
        return ocrResultRepository.calculateAverageConfidenceScore();
    }

    @Transactional(readOnly = true)
    @Override
    public List<OCRResult> findResultsWithErrors() {
        return ocrResultRepository.findResultsWithErrors();
    }

    @Transactional(readOnly = true)
    @Override
    public List<OCRResult> searchInErrorDetails(String searchTerm) {
        return ocrResultRepository.searchInErrorDetails(searchTerm);
    }

    @Transactional(readOnly = true)
    @Override
    public List<OCRResult> findByPagesProcessedGreaterThanEqual(Integer minPages) {
        return ocrResultRepository.findByPagesProcessedGreaterThanEqual(minPages);
    }

    @Transactional(readOnly = true)
    @Override
    public Long calculateTotalPagesProcessed() {
        return ocrResultRepository.calculateTotalPagesProcessed();
    }

    @Transactional(readOnly = true)
    @Override
    public List<OCRResult> findByCreatedAtBetween(LocalDateTime startDate, LocalDateTime endDate) {
        return ocrResultRepository.findByCreatedAtBetween(startDate, endDate);
    }

    @Transactional(readOnly = true)
    @Override
    public List<OCRResult> findByStatusIn(List<OCRStatus> statuses) {
        return ocrResultRepository.findByStatusIn(statuses);
    }

    @Transactional(readOnly = true)
    @Override
    public boolean existsByExtractedTextId(String extractedTextId) {
        return ocrResultRepository.existsByExtractedTextId(extractedTextId);
    }

    @Transactional(readOnly = true)
    @Override
    public Optional<OCRResult> findLatestByProjectId(String projectId) {
        return ocrResultRepository.findLatestByProjectId(projectId);
    }

    @Transactional(readOnly = true)
    @Override
    public List<Object[]> getStatusStatistics() {
        return ocrResultRepository.getStatusStatistics();
    }

    @Transactional(readOnly = true)
    @Override
    public List<Object[]> getModelUsageStatistics() {
        return ocrResultRepository.getModelUsageStatistics();
    }
}