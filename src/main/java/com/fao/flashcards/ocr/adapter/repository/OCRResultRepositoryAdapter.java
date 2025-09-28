package com.fao.flashcards.ocr.adapter.repository;

import com.fao.flashcards.ocr.application.port.out.OCRResultOutputPort;
import com.fao.flashcards.ocr.model.ExtractedText;
import com.fao.flashcards.ocr.model.OCRResult;
import com.fao.flashcards.ocr.model.OCRStatus;

import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Adapter der das OCRResultOutputPort Interface implementiert und 
 * die Spring Boot Repository Funktionalität bereitstellt.
 */
@Component
public class OCRResultRepositoryAdapter implements OCRResultOutputPort {
    
    private final OCRResultRepository ocrResultRepository;
    
    public OCRResultRepositoryAdapter(OCRResultRepository ocrResultRepository) {
        this.ocrResultRepository = ocrResultRepository;
    }
    
    @Override
    public OCRResult save(OCRResult ocrResult) {
        return ocrResultRepository.save(ocrResult);
    }
    
    @Override
    public List<OCRResult> saveAll(Iterable<OCRResult> ocrResults) {
        return ocrResultRepository.saveAll(ocrResults);
    }
    
    @Override
    public Optional<OCRResult> findById(String id) {
        return ocrResultRepository.findById(id);
    }
    
    @Override
    public List<OCRResult> findAll() {
        return ocrResultRepository.findAll();
    }
    
    @Override
    public boolean existsById(String id) {
        return ocrResultRepository.existsById(id);
    }
    
    @Override
    public long count() {
        return ocrResultRepository.count();
    }
    
    @Override
    public void deleteById(String id) {
        ocrResultRepository.deleteById(id);
    }
    
    @Override
    public void delete(OCRResult ocrResult) {
        ocrResultRepository.delete(ocrResult);
    }
    
    @Override
    public void deleteAll() {
        ocrResultRepository.deleteAll();
    }
    
    @Override
    public Optional<OCRResult> findByExtractedText(ExtractedText extractedText) {
        return ocrResultRepository.findByExtractedText(extractedText);
    }
    
    @Override
    public Optional<OCRResult> findByExtractedTextId(String extractedTextId) {
        return ocrResultRepository.findByExtractedTextId(extractedTextId);
    }
    
    @Override
    public List<OCRResult> findByStatus(OCRStatus status) {
        return ocrResultRepository.findByStatus(status);
    }
    
    @Override
    public List<OCRResult> findByProjectId(String projectId) {
        return ocrResultRepository.findByProjectId(projectId);
    }
    
    @Override
    public List<OCRResult> findByProjectIdAndStatus(String projectId, OCRStatus status) {
        return ocrResultRepository.findByProjectIdAndStatus(projectId, status);
    }
    
    @Override
    public List<OCRResult> findSuccessfulResults() {
        return ocrResultRepository.findSuccessfulResults();
    }
    
    @Override
    public List<OCRResult> findFailedResults() {
        return ocrResultRepository.findFailedResults();
    }
    
    @Override
    public List<OCRResult> findProcessingResults() {
        return ocrResultRepository.findProcessingResults();
    }
    
    @Override
    public List<OCRResult> findPendingResults() {
        return ocrResultRepository.findPendingResults();
    }
    
    @Override
    public List<OCRResult> findByModelUsed(String modelUsed) {
        return ocrResultRepository.findByModelUsed(modelUsed);
    }
    
    @Override
    public List<OCRResult> findByConfidenceScoreGreaterThanEqual(Double minConfidence) {
        return ocrResultRepository.findByConfidenceScoreGreaterThanEqual(minConfidence);
    }
    
    @Override
    public List<OCRResult> findByConfidenceScoreBetween(Double minConfidence, Double maxConfidence) {
        return ocrResultRepository.findByConfidenceScoreBetween(minConfidence, maxConfidence);
    }
    
    @Override
    public List<OCRResult> findSlowProcessingResults(Long thresholdMs) {
        return ocrResultRepository.findSlowProcessingResults(thresholdMs);
    }
    
    @Override
    public List<OCRResult> findFastProcessingResults(Long thresholdMs) {
        return ocrResultRepository.findFastProcessingResults(thresholdMs);
    }
    
    @Override
    public List<OCRResult> findByCreatedAtAfter(LocalDateTime dateTime) {
        return ocrResultRepository.findByCreatedAtAfter(dateTime);
    }
    
    @Override
    public List<OCRResult> findAllByOrderByCreatedAtDesc() {
        return ocrResultRepository.findAllByOrderByCreatedAtDesc();
    }
    
    @Override
    public List<OCRResult> findByProjectIdOrderByCreatedAtDesc(String projectId) {
        return ocrResultRepository.findByProjectIdOrderByCreatedAtDesc(projectId);
    }
    
    @Override
    public long countByStatus(OCRStatus status) {
        return ocrResultRepository.countByStatus(status);
    }
    
    @Override
    public long countByProjectId(String projectId) {
        return ocrResultRepository.countByProjectId(projectId);
    }
    
    @Override
    public long countSuccessfulByProjectId(String projectId) {
        return ocrResultRepository.countSuccessfulByProjectId(projectId);
    }
    
    @Override
    public long countFailedByProjectId(String projectId) {
        return ocrResultRepository.countFailedByProjectId(projectId);
    }
    
    @Override
    public Double calculateAverageProcessingTime() {
        return ocrResultRepository.calculateAverageProcessingTime();
    }
    
    @Override
    public Double calculateAverageProcessingTimeByProjectId(String projectId) {
        return ocrResultRepository.calculateAverageProcessingTimeByProjectId(projectId);
    }
    
    @Override
    public Double calculateSuccessRate() {
        return ocrResultRepository.calculateSuccessRate();
    }
    
    @Override
    public Double calculateSuccessRateByProjectId(String projectId) {
        return ocrResultRepository.calculateSuccessRateByProjectId(projectId);
    }
    
    @Override
    public Long findMinProcessingTime() {
        return ocrResultRepository.findMinProcessingTime();
    }
    
    @Override
    public Long findMaxProcessingTime() {
        return ocrResultRepository.findMaxProcessingTime();
    }
    
    @Override
    public Double calculateAverageConfidenceScore() {
        return ocrResultRepository.calculateAverageConfidenceScore();
    }
    
    @Override
    public List<OCRResult> findResultsWithErrors() {
        return ocrResultRepository.findResultsWithErrors();
    }
    
    @Override
    public List<OCRResult> searchInErrorDetails(String searchTerm) {
        return ocrResultRepository.searchInErrorDetails(searchTerm);
    }
    
    @Override
    public List<OCRResult> findByPagesProcessedGreaterThanEqual(Integer minPages) {
        return ocrResultRepository.findByPagesProcessedGreaterThanEqual(minPages);
    }
    
    @Override
    public Long calculateTotalPagesProcessed() {
        return ocrResultRepository.calculateTotalPagesProcessed();
    }
    
    @Override
    public List<OCRResult> findByCreatedAtBetween(LocalDateTime startDate, LocalDateTime endDate) {
        return ocrResultRepository.findByCreatedAtBetween(startDate, endDate);
    }
    
    @Override
    public List<OCRResult> findByStatusIn(List<OCRStatus> statuses) {
        return ocrResultRepository.findByStatusIn(statuses);
    }
    
    @Override
    public boolean existsByExtractedTextId(String extractedTextId) {
        return ocrResultRepository.existsByExtractedTextId(extractedTextId);
    }
    
    @Override
    public Optional<OCRResult> findLatestByProjectId(String projectId) {
        return ocrResultRepository.findLatestByProjectId(projectId);
    }
    
    @Override
    public List<Object[]> getStatusStatistics() {
        return ocrResultRepository.getStatusStatistics();
    }
    
    @Override
    public List<Object[]> getModelUsageStatistics() {
        return ocrResultRepository.getModelUsageStatistics();
    }
}