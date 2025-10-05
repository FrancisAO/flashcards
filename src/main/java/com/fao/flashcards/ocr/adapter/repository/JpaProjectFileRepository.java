package com.fao.flashcards.ocr.adapter.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.fao.flashcards.cards.model.FileType;
import com.fao.flashcards.ocr.application.port.out.ProjectFileRepository;
import com.fao.flashcards.ocr.model.Project;
import com.fao.flashcards.ocr.model.ProjectFile;

/**
 * Adapter der das ProjectFileOutputPort Interface implementiert und
 * die Spring Boot Repository Funktionalität bereitstellt.
 */
@Component
public class JpaProjectFileRepository implements ProjectFileRepository {

    private final JpaProjectFileSpringDataRepository projectFileRepository;

    public JpaProjectFileRepository(JpaProjectFileSpringDataRepository projectFileRepository) {
        this.projectFileRepository = projectFileRepository;
    }

    @Transactional
    @Override
    public ProjectFile save(ProjectFile projectFile) {
        return projectFileRepository.save(projectFile);
    }

    @Transactional
    @Override
    public List<ProjectFile> saveAll(Iterable<ProjectFile> projectFiles) {
        return projectFileRepository.saveAll(projectFiles);
    }

    @Transactional(readOnly = true)
    @Override
    public Optional<ProjectFile> findById(String id) {
        return projectFileRepository.findById(id);
    }

    @Transactional(readOnly = true)
    @Override
    public List<ProjectFile> findAll() {
        return projectFileRepository.findAll();
    }

    @Transactional(readOnly = true)
    @Override
    public boolean existsById(String id) {
        return projectFileRepository.existsById(id);
    }

    @Transactional(readOnly = true)
    @Override
    public long count() {
        return projectFileRepository.count();
    }

    @Transactional
    @Override
    public void deleteById(String id) {
        projectFileRepository.deleteById(id);
    }

    @Transactional
    @Override
    public void delete(ProjectFile projectFile) {
        projectFileRepository.delete(projectFile);
    }

    @Transactional
    @Override
    public void deleteAll() {
        projectFileRepository.deleteAll();
    }

    @Transactional(readOnly = true)
    @Override
    public List<ProjectFile> findByProject(Project project) {
        return projectFileRepository.findByProject(project);
    }

    @Transactional(readOnly = true)
    @Override
    public List<ProjectFile> findByProjectId(String projectId) {
        return projectFileRepository.findByProjectId(projectId);
    }

    @Transactional(readOnly = true)
    @Override
    public List<ProjectFile> findByProjectAndFileType(Project project, FileType fileType) {
        return projectFileRepository.findByProjectAndFileType(project, fileType);
    }

    @Transactional(readOnly = true)
    @Override
    public List<ProjectFile> findByProjectIdAndFileType(String projectId, FileType fileType) {
        return projectFileRepository.findByProjectIdAndFileType(projectId, fileType);
    }

    @Transactional(readOnly = true)
    @Override
    public List<ProjectFile> findByFileType(FileType fileType) {
        return projectFileRepository.findByFileType(fileType);
    }

    @Transactional(readOnly = true)
    @Override
    public List<ProjectFile> findFilesWithoutExtractedText() {
        return projectFileRepository.findFilesWithoutExtractedText();
    }

    @Transactional(readOnly = true)
    @Override
    public List<ProjectFile> findFilesWithoutExtractedTextByProjectId(String projectId) {
        return projectFileRepository.findFilesWithoutExtractedTextByProjectId(projectId);
    }

    @Transactional(readOnly = true)
    @Override
    public List<ProjectFile> findFilesWithExtractedText() {
        return projectFileRepository.findFilesWithExtractedText();
    }

    @Transactional(readOnly = true)
    @Override
    public List<ProjectFile> findFilesWithExtractedTextByProjectId(String projectId) {
        return projectFileRepository.findFilesWithExtractedTextByProjectId(projectId);
    }

    @Transactional(readOnly = true)
    @Override
    public List<ProjectFile> findByOriginalFilenameContainingIgnoreCase(String filename) {
        return projectFileRepository.findByOriginalFilenameContainingIgnoreCase(filename);
    }

    @Transactional(readOnly = true)
    @Override
    public List<ProjectFile> findByContentType(String contentType) {
        return projectFileRepository.findByContentType(contentType);
    }

    @Transactional(readOnly = true)
    @Override
    public List<ProjectFile> findByContentTypePattern(String contentTypePattern) {
        return projectFileRepository.findByContentTypePattern(contentTypePattern);
    }

    @Transactional(readOnly = true)
    @Override
    public List<ProjectFile> findByFileSizeGreaterThan(Long minSize) {
        return projectFileRepository.findByFileSizeGreaterThan(minSize);
    }

    @Transactional(readOnly = true)
    @Override
    public List<ProjectFile> findByFileSizeLessThan(Long maxSize) {
        return projectFileRepository.findByFileSizeLessThan(maxSize);
    }

    @Transactional(readOnly = true)
    @Override
    public List<ProjectFile> findByFileSizeBetween(Long minSize, Long maxSize) {
        return projectFileRepository.findByFileSizeBetween(minSize, maxSize);
    }

    @Transactional(readOnly = true)
    @Override
    public List<ProjectFile> findByUploadedAtAfter(LocalDateTime dateTime) {
        return projectFileRepository.findByUploadedAtAfter(dateTime);
    }

    @Transactional(readOnly = true)
    @Override
    public List<ProjectFile> findByUploadedAtBefore(LocalDateTime dateTime) {
        return projectFileRepository.findByUploadedAtBefore(dateTime);
    }

    @Transactional(readOnly = true)
    @Override
    public List<ProjectFile> findAllByOrderByUploadedAtDesc() {
        return projectFileRepository.findAllByOrderByUploadedAtDesc();
    }

    @Transactional(readOnly = true)
    @Override
    public List<ProjectFile> findByProjectIdOrderByUploadedAtDesc(String projectId) {
        return projectFileRepository.findByProjectIdOrderByUploadedAtDesc(projectId);
    }

    @Transactional(readOnly = true)
    @Override
    public long countByProjectId(String projectId) {
        return projectFileRepository.countByProjectId(projectId);
    }

    @Transactional(readOnly = true)
    @Override
    public long countByProjectIdAndFileType(String projectId, FileType fileType) {
        return projectFileRepository.countByProjectIdAndFileType(projectId, fileType);
    }

    @Transactional(readOnly = true)
    @Override
    public Long calculateTotalFileSizeByProjectId(String projectId) {
        return projectFileRepository.calculateTotalFileSizeByProjectId(projectId);
    }

    @Transactional(readOnly = true)
    @Override
    public Optional<ProjectFile> findByStoragePath(String storagePath) {
        return projectFileRepository.findByStoragePath(storagePath);
    }

    @Transactional(readOnly = true)
    @Override
    public boolean existsByStoragePath(String storagePath) {
        return projectFileRepository.existsByStoragePath(storagePath);
    }

    @Transactional(readOnly = true)
    @Override
    public List<ProjectFile> findByFileTypeIn(List<FileType> fileTypes) {
        return projectFileRepository.findByFileTypeIn(fileTypes);
    }

    @Transactional(readOnly = true)
    @Override
    public List<ProjectFile> findByProjectIdAndFileTypeIn(String projectId, List<FileType> fileTypes) {
        return projectFileRepository.findByProjectIdAndFileTypeIn(projectId, fileTypes);
    }
}