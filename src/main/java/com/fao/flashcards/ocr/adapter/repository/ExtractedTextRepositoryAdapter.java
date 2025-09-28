package com.fao.flashcards.ocr.adapter.repository;

import com.fao.flashcards.ocr.application.port.out.ExtractedTextOutputPort;
import com.fao.flashcards.ocr.model.ExtractedText;
import com.fao.flashcards.ocr.model.ExtractionSource;
import com.fao.flashcards.ocr.model.Project;
import com.fao.flashcards.ocr.model.ProjectFile;

import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Adapter der das ExtractedTextOutputPort Interface implementiert und 
 * die Spring Boot Repository Funktionalität bereitstellt.
 */
@Component
public class ExtractedTextRepositoryAdapter implements ExtractedTextOutputPort {
    
    private final ExtractedTextRepository extractedTextRepository;
    
    public ExtractedTextRepositoryAdapter(ExtractedTextRepository extractedTextRepository) {
        this.extractedTextRepository = extractedTextRepository;
    }
    
    @Override
    public ExtractedText save(ExtractedText extractedText) {
        return extractedTextRepository.save(extractedText);
    }
    
    @Override
    public List<ExtractedText> saveAll(Iterable<ExtractedText> extractedTexts) {
        return extractedTextRepository.saveAll(extractedTexts);
    }
    
    @Override
    public Optional<ExtractedText> findById(String id) {
        return extractedTextRepository.findById(id);
    }
    
    @Override
    public List<ExtractedText> findAll() {
        return extractedTextRepository.findAll();
    }
    
    @Override
    public boolean existsById(String id) {
        return extractedTextRepository.existsById(id);
    }
    
    @Override
    public long count() {
        return extractedTextRepository.count();
    }
    
    @Override
    public void deleteById(String id) {
        extractedTextRepository.deleteById(id);
    }
    
    @Override
    public void delete(ExtractedText extractedText) {
        extractedTextRepository.delete(extractedText);
    }
    
    @Override
    public void deleteAll() {
        extractedTextRepository.deleteAll();
    }
    
    @Override
    public Optional<ExtractedText> findByProjectFile(ProjectFile projectFile) {
        return extractedTextRepository.findByProjectFile(projectFile);
    }
    
    @Override
    public Optional<ExtractedText> findByProjectFileId(String projectFileId) {
        return extractedTextRepository.findByProjectFileId(projectFileId);
    }
    
    @Override
    public List<ExtractedText> findByProject(Project project) {
        return extractedTextRepository.findByProject(project);
    }
    
    @Override
    public List<ExtractedText> findByProjectId(String projectId) {
        return extractedTextRepository.findByProjectId(projectId);
    }
    
    @Override
    public List<ExtractedText> findByExtractionSource(ExtractionSource extractionSource) {
        return extractedTextRepository.findByExtractionSource(extractionSource);
    }
    
    @Override
    public List<ExtractedText> findByProjectIdAndExtractionSource(String projectId, ExtractionSource extractionSource) {
        return extractedTextRepository.findByProjectIdAndExtractionSource(projectId, extractionSource);
    }
    
    @Override
    public List<ExtractedText> findEditedTexts() {
        return extractedTextRepository.findEditedTexts();
    }
    
    @Override
    public List<ExtractedText> findUnEditedTexts() {
        return extractedTextRepository.findUnEditedTexts();
    }
    
    @Override
    public List<ExtractedText> findEditedTextsByProjectId(String projectId) {
        return extractedTextRepository.findEditedTextsByProjectId(projectId);
    }
    
    @Override
    public List<ExtractedText> findUnEditedTextsByProjectId(String projectId) {
        return extractedTextRepository.findUnEditedTextsByProjectId(projectId);
    }
    
    @Override
    public List<ExtractedText> searchInExtractedContent(String searchTerm) {
        return extractedTextRepository.searchInExtractedContent(searchTerm);
    }
    
    @Override
    public List<ExtractedText> searchInEditedContent(String searchTerm) {
        return extractedTextRepository.searchInEditedContent(searchTerm);
    }
    
    @Override
    public List<ExtractedText> searchInContent(String searchTerm) {
        return extractedTextRepository.searchInContent(searchTerm);
    }
    
    @Override
    public List<ExtractedText> searchInContentByProjectId(String projectId, String searchTerm) {
        return extractedTextRepository.searchInContentByProjectId(projectId, searchTerm);
    }
    
    @Override
    public List<ExtractedText> findByExtractedAtAfter(LocalDateTime dateTime) {
        return extractedTextRepository.findByExtractedAtAfter(dateTime);
    }
    
    @Override
    public List<ExtractedText> findByLastEditedAtAfter(LocalDateTime dateTime) {
        return extractedTextRepository.findByLastEditedAtAfter(dateTime);
    }
    
    @Override
    public List<ExtractedText> findAllByOrderByExtractedAtDesc() {
        return extractedTextRepository.findAllByOrderByExtractedAtDesc();
    }
    
    @Override
    public List<ExtractedText> findByProjectIdOrderByExtractedAtDesc(String projectId) {
        return extractedTextRepository.findByProjectIdOrderByExtractedAtDesc(projectId);
    }
    
    @Override
    public List<ExtractedText> findEmptyTexts() {
        return extractedTextRepository.findEmptyTexts();
    }
    
    @Override
    public List<ExtractedText> findNonEmptyTexts() {
        return extractedTextRepository.findNonEmptyTexts();
    }
    
    @Override
    public long countByProjectId(String projectId) {
        return extractedTextRepository.countByProjectId(projectId);
    }
    
    @Override
    public long countEditedTextsByProjectId(String projectId) {
        return extractedTextRepository.countEditedTextsByProjectId(projectId);
    }
    
    @Override
    public long countByExtractionSource(ExtractionSource extractionSource) {
        return extractedTextRepository.countByExtractionSource(extractionSource);
    }
    
    @Override
    public List<ExtractedText> findByExtractionSourceIn(List<ExtractionSource> extractionSources) {
        return extractedTextRepository.findByExtractionSourceIn(extractionSources);
    }
    
    @Override
    public List<ExtractedText> findTextsWithMetadata() {
        return extractedTextRepository.findTextsWithMetadata();
    }
    
    @Override
    public List<ExtractedText> searchInMetadata(String searchTerm) {
        return extractedTextRepository.searchInMetadata(searchTerm);
    }
    
    @Override
    public boolean existsByProjectFileId(String projectFileId) {
        return extractedTextRepository.existsByProjectFileId(projectFileId);
    }
    
    @Override
    public Optional<ExtractedText> findLatestByProjectId(String projectId) {
        return extractedTextRepository.findLatestByProjectId(projectId);
    }
    
    @Override
    public List<ExtractedText> findByExtractedAtBetween(LocalDateTime startDate, LocalDateTime endDate) {
        return extractedTextRepository.findByExtractedAtBetween(startDate, endDate);
    }
}