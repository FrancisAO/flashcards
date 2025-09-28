package com.fao.flashcards.ocr.adapter.repository;

import com.fao.flashcards.cards.model.FileType;
import com.fao.flashcards.ocr.application.port.out.ProjectFileOutputPort;
import com.fao.flashcards.ocr.model.Project;
import com.fao.flashcards.ocr.model.ProjectFile;

import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Adapter der das ProjectFileOutputPort Interface implementiert und 
 * die Spring Boot Repository Funktionalität bereitstellt.
 */
@Component
public class ProjectFileRepositoryAdapter implements ProjectFileOutputPort {
    
    private final ProjectFileRepository projectFileRepository;
    
    public ProjectFileRepositoryAdapter(ProjectFileRepository projectFileRepository) {
        this.projectFileRepository = projectFileRepository;
    }
    
    @Override
    public ProjectFile save(ProjectFile projectFile) {
        return projectFileRepository.save(projectFile);
    }
    
    @Override
    public List<ProjectFile> saveAll(Iterable<ProjectFile> projectFiles) {
        return projectFileRepository.saveAll(projectFiles);
    }
    
    @Override
    public Optional<ProjectFile> findById(String id) {
        return projectFileRepository.findById(id);
    }
    
    @Override
    public List<ProjectFile> findAll() {
        return projectFileRepository.findAll();
    }
    
    @Override
    public boolean existsById(String id) {
        return projectFileRepository.existsById(id);
    }
    
    @Override
    public long count() {
        return projectFileRepository.count();
    }
    
    @Override
    public void deleteById(String id) {
        projectFileRepository.deleteById(id);
    }
    
    @Override
    public void delete(ProjectFile projectFile) {
        projectFileRepository.delete(projectFile);
    }
    
    @Override
    public void deleteAll() {
        projectFileRepository.deleteAll();
    }
    
    @Override
    public List<ProjectFile> findByProject(Project project) {
        return projectFileRepository.findByProject(project);
    }
    
    @Override
    public List<ProjectFile> findByProjectId(String projectId) {
        return projectFileRepository.findByProjectId(projectId);
    }
    
    @Override
    public List<ProjectFile> findByProjectAndFileType(Project project, FileType fileType) {
        return projectFileRepository.findByProjectAndFileType(project, fileType);
    }
    
    @Override
    public List<ProjectFile> findByProjectIdAndFileType(String projectId, FileType fileType) {
        return projectFileRepository.findByProjectIdAndFileType(projectId, fileType);
    }
    
    @Override
    public List<ProjectFile> findByFileType(FileType fileType) {
        return projectFileRepository.findByFileType(fileType);
    }
    
    @Override
    public List<ProjectFile> findFilesWithoutExtractedText() {
        return projectFileRepository.findFilesWithoutExtractedText();
    }
    
    @Override
    public List<ProjectFile> findFilesWithoutExtractedTextByProjectId(String projectId) {
        return projectFileRepository.findFilesWithoutExtractedTextByProjectId(projectId);
    }
    
    @Override
    public List<ProjectFile> findFilesWithExtractedText() {
        return projectFileRepository.findFilesWithExtractedText();
    }
    
    @Override
    public List<ProjectFile> findFilesWithExtractedTextByProjectId(String projectId) {
        return projectFileRepository.findFilesWithExtractedTextByProjectId(projectId);
    }
    
    @Override
    public List<ProjectFile> findByOriginalFilenameContainingIgnoreCase(String filename) {
        return projectFileRepository.findByOriginalFilenameContainingIgnoreCase(filename);
    }
    
    @Override
    public List<ProjectFile> findByContentType(String contentType) {
        return projectFileRepository.findByContentType(contentType);
    }
    
    @Override
    public List<ProjectFile> findByContentTypePattern(String contentTypePattern) {
        return projectFileRepository.findByContentTypePattern(contentTypePattern);
    }
    
    @Override
    public List<ProjectFile> findByFileSizeGreaterThan(Long minSize) {
        return projectFileRepository.findByFileSizeGreaterThan(minSize);
    }
    
    @Override
    public List<ProjectFile> findByFileSizeLessThan(Long maxSize) {
        return projectFileRepository.findByFileSizeLessThan(maxSize);
    }
    
    @Override
    public List<ProjectFile> findByFileSizeBetween(Long minSize, Long maxSize) {
        return projectFileRepository.findByFileSizeBetween(minSize, maxSize);
    }
    
    @Override
    public List<ProjectFile> findByUploadedAtAfter(LocalDateTime dateTime) {
        return projectFileRepository.findByUploadedAtAfter(dateTime);
    }
    
    @Override
    public List<ProjectFile> findByUploadedAtBefore(LocalDateTime dateTime) {
        return projectFileRepository.findByUploadedAtBefore(dateTime);
    }
    
    @Override
    public List<ProjectFile> findAllByOrderByUploadedAtDesc() {
        return projectFileRepository.findAllByOrderByUploadedAtDesc();
    }
    
    @Override
    public List<ProjectFile> findByProjectIdOrderByUploadedAtDesc(String projectId) {
        return projectFileRepository.findByProjectIdOrderByUploadedAtDesc(projectId);
    }
    
    @Override
    public long countByProjectId(String projectId) {
        return projectFileRepository.countByProjectId(projectId);
    }
    
    @Override
    public long countByProjectIdAndFileType(String projectId, FileType fileType) {
        return projectFileRepository.countByProjectIdAndFileType(projectId, fileType);
    }
    
    @Override
    public Long calculateTotalFileSizeByProjectId(String projectId) {
        return projectFileRepository.calculateTotalFileSizeByProjectId(projectId);
    }
    
    @Override
    public Optional<ProjectFile> findByStoragePath(String storagePath) {
        return projectFileRepository.findByStoragePath(storagePath);
    }
    
    @Override
    public boolean existsByStoragePath(String storagePath) {
        return projectFileRepository.existsByStoragePath(storagePath);
    }
    
    @Override
    public List<ProjectFile> findByFileTypeIn(List<FileType> fileTypes) {
        return projectFileRepository.findByFileTypeIn(fileTypes);
    }
    
    @Override
    public List<ProjectFile> findByProjectIdAndFileTypeIn(String projectId, List<FileType> fileTypes) {
        return projectFileRepository.findByProjectIdAndFileTypeIn(projectId, fileTypes);
    }
}