package com.fao.flashcards.ocr.adapter.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.fao.flashcards.ocr.application.port.out.ExtractedTextRepository;
import com.fao.flashcards.ocr.model.ExtractedText;
import com.fao.flashcards.ocr.model.ExtractionSource;
import com.fao.flashcards.ocr.model.Project;
import com.fao.flashcards.ocr.model.ProjectFile;

/**
 * Adapter der das ExtractedTextOutputPort Interface implementiert und
 * die Spring Boot Repository Funktionalität bereitstellt.
 */
@Component
public class JpaExtractedTextRepository implements ExtractedTextRepository {

    private final JpaExtractedTextSpringDataRepository extractedTextRepository;

    public JpaExtractedTextRepository(JpaExtractedTextSpringDataRepository extractedTextRepository) {
        this.extractedTextRepository = extractedTextRepository;
    }

    @Transactional
    @Override
    public ExtractedText save(ExtractedText extractedText) {
        return extractedTextRepository.save(extractedText);
    }

    @Transactional
    @Override
    public List<ExtractedText> saveAll(Iterable<ExtractedText> extractedTexts) {
        return extractedTextRepository.saveAll(extractedTexts);
    }

    @Transactional(readOnly = true)
    @Override
    public Optional<ExtractedText> findById(String id) {
        return extractedTextRepository.findById(id);
    }

    @Transactional(readOnly = true)
    @Override
    public List<ExtractedText> findAll() {
        return extractedTextRepository.findAll();
    }

    @Transactional(readOnly = true)
    @Override
    public boolean existsById(String id) {
        return extractedTextRepository.existsById(id);
    }

    @Transactional(readOnly = true)
    @Override
    public long count() {
        return extractedTextRepository.count();
    }

    @Transactional
    @Override
    public void deleteById(String id) {
        extractedTextRepository.deleteById(id);
    }

    @Transactional
    @Override
    public void delete(ExtractedText extractedText) {
        extractedTextRepository.delete(extractedText);
    }

    @Transactional
    @Override
    public void deleteAll() {
        extractedTextRepository.deleteAll();
    }

    @Transactional(readOnly = true)
    @Override
    public Optional<ExtractedText> findByProjectFile(ProjectFile projectFile) {
        return extractedTextRepository.findByProjectFile(projectFile);
    }

    @Transactional(readOnly = true)
    @Override
    public Optional<ExtractedText> findByProjectFileId(String projectFileId) {
        return extractedTextRepository.findByProjectFileId(projectFileId);
    }

    @Transactional(readOnly = true)
    @Override
    public List<ExtractedText> findByProject(Project project) {
        return extractedTextRepository.findByProject(project);
    }

    @Transactional(readOnly = true)
    @Override
    public List<ExtractedText> findByProjectId(String projectId) {
        return extractedTextRepository.findByProjectId(projectId);
    }

    @Transactional(readOnly = true)
    @Override
    public List<ExtractedText> findByExtractionSource(ExtractionSource extractionSource) {
        return extractedTextRepository.findByExtractionSource(extractionSource);
    }

    @Transactional(readOnly = true)
    @Override
    public List<ExtractedText> findByProjectIdAndExtractionSource(String projectId, ExtractionSource extractionSource) {
        return extractedTextRepository.findByProjectIdAndExtractionSource(projectId, extractionSource);
    }

    @Transactional(readOnly = true)
    @Override
    public List<ExtractedText> findEditedTexts() {
        return extractedTextRepository.findEditedTexts();
    }

    @Transactional(readOnly = true)
    @Override
    public List<ExtractedText> findUnEditedTexts() {
        return extractedTextRepository.findUnEditedTexts();
    }

    @Transactional(readOnly = true)
    @Override
    public List<ExtractedText> findEditedTextsByProjectId(String projectId) {
        return extractedTextRepository.findEditedTextsByProjectId(projectId);
    }

    @Transactional(readOnly = true)
    @Override
    public List<ExtractedText> findUnEditedTextsByProjectId(String projectId) {
        return extractedTextRepository.findUnEditedTextsByProjectId(projectId);
    }

    @Transactional(readOnly = true)
    @Override
    public List<ExtractedText> searchInExtractedContent(String searchTerm) {
        return extractedTextRepository.searchInExtractedContent(searchTerm);
    }

    @Transactional(readOnly = true)
    @Override
    public List<ExtractedText> searchInEditedContent(String searchTerm) {
        return extractedTextRepository.searchInEditedContent(searchTerm);
    }

    @Transactional(readOnly = true)
    @Override
    public List<ExtractedText> searchInContent(String searchTerm) {
        return extractedTextRepository.searchInContent(searchTerm);
    }

    @Transactional(readOnly = true)
    @Override
    public List<ExtractedText> searchInContentByProjectId(String projectId, String searchTerm) {
        return extractedTextRepository.searchInContentByProjectId(projectId, searchTerm);
    }

    @Transactional(readOnly = true)
    @Override
    public List<ExtractedText> findByExtractedAtAfter(LocalDateTime dateTime) {
        return extractedTextRepository.findByExtractedAtAfter(dateTime);
    }

    @Transactional(readOnly = true)
    @Override
    public List<ExtractedText> findByLastEditedAtAfter(LocalDateTime dateTime) {
        return extractedTextRepository.findByLastEditedAtAfter(dateTime);
    }

    @Transactional(readOnly = true)
    @Override
    public List<ExtractedText> findAllByOrderByExtractedAtDesc() {
        return extractedTextRepository.findAllByOrderByExtractedAtDesc();
    }

    @Transactional(readOnly = true)
    @Override
    public List<ExtractedText> findByProjectIdOrderByExtractedAtDesc(String projectId) {
        return extractedTextRepository.findByProjectIdOrderByExtractedAtDesc(projectId);
    }

    @Transactional(readOnly = true)
    @Override
    public List<ExtractedText> findEmptyTexts() {
        return extractedTextRepository.findEmptyTexts();
    }

    @Transactional(readOnly = true)
    @Override
    public List<ExtractedText> findNonEmptyTexts() {
        return extractedTextRepository.findNonEmptyTexts();
    }

    @Transactional(readOnly = true)
    @Override
    public long countByProjectId(String projectId) {
        return extractedTextRepository.countByProjectId(projectId);
    }

    @Transactional(readOnly = true)
    @Override
    public long countEditedTextsByProjectId(String projectId) {
        return extractedTextRepository.countEditedTextsByProjectId(projectId);
    }

    @Transactional(readOnly = true)
    @Override
    public long countByExtractionSource(ExtractionSource extractionSource) {
        return extractedTextRepository.countByExtractionSource(extractionSource);
    }

    @Transactional(readOnly = true)
    @Override
    public List<ExtractedText> findByExtractionSourceIn(List<ExtractionSource> extractionSources) {
        return extractedTextRepository.findByExtractionSourceIn(extractionSources);
    }

    @Transactional(readOnly = true)
    @Override
    public List<ExtractedText> findTextsWithMetadata() {
        return extractedTextRepository.findTextsWithMetadata();
    }

    @Transactional(readOnly = true)
    @Override
    public List<ExtractedText> searchInMetadata(String searchTerm) {
        return extractedTextRepository.searchInMetadata(searchTerm);
    }

    @Transactional(readOnly = true)
    @Override
    public boolean existsByProjectFileId(String projectFileId) {
        return extractedTextRepository.existsByProjectFileId(projectFileId);
    }

    @Transactional(readOnly = true)
    @Override
    public Optional<ExtractedText> findLatestByProjectId(String projectId) {
        return extractedTextRepository.findLatestByProjectId(projectId);
    }

    @Transactional(readOnly = true)
    @Override
    public List<ExtractedText> findByExtractedAtBetween(LocalDateTime startDate, LocalDateTime endDate) {
        return extractedTextRepository.findByExtractedAtBetween(startDate, endDate);
    }
}