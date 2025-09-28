package com.fao.flashcards.ocr.adapter.repository;

import com.fao.flashcards.ocr.application.port.out.ProjectOutputPort;
import com.fao.flashcards.ocr.model.Project;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;

/**
 * Adapter der das ProjectOutputPort Interface implementiert und 
 * die Spring Boot Repository Funktionalität bereitstellt.
 */
@Component
public class ProjectRepositoryAdapter implements ProjectOutputPort {
    
    private final ProjectRepository projectRepository;
    
    public ProjectRepositoryAdapter(ProjectRepository projectRepository) {
        this.projectRepository = projectRepository;
    }
    
    @Override
    public Project save(Project project) {
        return projectRepository.save(project);
    }
    
    @Override
    public List<Project> saveAll(Iterable<Project> projects) {
        return projectRepository.saveAll(projects);
    }
    
    @Override
    public Optional<Project> findById(String id) {
        return projectRepository.findById(id);
    }
    
    @Override
    public List<Project> findAll() {
        return projectRepository.findAll();
    }
    
    @Override
    public boolean existsById(String id) {
        return projectRepository.existsById(id);
    }
    
    @Override
    public long count() {
        return projectRepository.count();
    }
    
    @Override
    public void deleteById(String id) {
        projectRepository.deleteById(id);
    }
    
    @Override
    public void delete(Project project) {
        projectRepository.delete(project);
    }
    
    @Override
    public void deleteAll() {
        projectRepository.deleteAll();
    }
    
    @Override
    public List<Project> findByNameContainingIgnoreCase(String name) {
        return projectRepository.findByNameContainingIgnoreCase(name);
    }
    
    @Override
    public List<Project> findByTagsIn(Set<String> tags) {
        return projectRepository.findByTagsIn(tags);
    }
    
    @Override
    public List<Project> findByAllTags(Set<String> tags, long tagCount) {
        return projectRepository.findByAllTags(tags, tagCount);
    }
    
    @Override
    public List<Project> findAllByOrderByCreatedAtDesc() {
        return projectRepository.findAllByOrderByCreatedAtDesc();
    }
    
    @Override
    public Page<Project> findAllByOrderByCreatedAtDesc(Pageable pageable) {
        return projectRepository.findAllByOrderByCreatedAtDesc(pageable);
    }
    
    @Override
    public List<Project> findByCreatedAtAfter(LocalDateTime dateTime) {
        return projectRepository.findByCreatedAtAfter(dateTime);
    }
    
    @Override
    public List<Project> findByFileCountGreaterThanEqual(int minFileCount) {
        return projectRepository.findByFileCountGreaterThanEqual(minFileCount);
    }
    
    @Override
    public List<Project> findProjectsWithExtractedTexts() {
        return projectRepository.findProjectsWithExtractedTexts();
    }
    
    @Override
    public List<Project> findProjectsWithoutExtractedTexts() {
        return projectRepository.findProjectsWithoutExtractedTexts();
    }
    
    @Override
    public List<Project> searchByNameOrDescription(String searchTerm) {
        return projectRepository.searchByNameOrDescription(searchTerm);
    }
    
    @Override
    public Page<Project> searchByNameOrDescription(String searchTerm, Pageable pageable) {
        return projectRepository.searchByNameOrDescription(searchTerm, pageable);
    }
    
    @Override
    public long countByTag(String tag) {
        return projectRepository.countByTag(tag);
    }
    
    @Override
    public List<String> findAllUsedTags() {
        return projectRepository.findAllUsedTags();
    }
    
    @Override
    public boolean existsByName(String name) {
        return projectRepository.existsByName(name);
    }
    
    @Override
    public Optional<Project> findByName(String name) {
        return projectRepository.findByName(name);
    }
}