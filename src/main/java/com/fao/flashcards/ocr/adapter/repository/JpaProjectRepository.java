package com.fao.flashcards.ocr.adapter.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.fao.flashcards.ocr.application.port.out.ProjectRepository;
import com.fao.flashcards.ocr.model.Project;
import com.fao.flashcards.shared.model.pagination.Page;
import com.fao.flashcards.shared.model.pagination.PageRequest;

/**
 * Adapter der das ProjectOutputPort Interface implementiert und
 * die Spring Boot Repository Funktionalität bereitstellt.
 */
@Component
public class JpaProjectRepository implements ProjectRepository {

    private final JpaProjectSpringDataRepository projectRepository;

    public JpaProjectRepository(JpaProjectSpringDataRepository projectRepository) {
        this.projectRepository = projectRepository;
    }

    @Transactional
    @Override
    public Project save(Project project) {
        return projectRepository.save(project);
    }

    @Transactional
    @Override
    public List<Project> saveAll(Iterable<Project> projects) {
        return projectRepository.saveAll(projects);
    }

    @Transactional(readOnly = true)
    @Override
    public Optional<Project> findById(String id) {
        return projectRepository.findById(id);
    }

    @Transactional(readOnly = true)
    @Override
    public List<Project> findAll() {
        return projectRepository.findAll();
    }

    @Transactional(readOnly = true)
    @Override
    public boolean existsById(String id) {
        return projectRepository.existsById(id);
    }

    @Transactional(readOnly = true)
    @Override
    public long count() {
        return projectRepository.count();
    }

    @Transactional
    @Override
    public void deleteById(String id) {
        projectRepository.deleteById(id);
    }

    @Transactional
    @Override
    public void delete(Project project) {
        projectRepository.delete(project);
    }

    @Transactional
    @Override
    public void deleteAll() {
        projectRepository.deleteAll();
    }

    @Transactional(readOnly = true)
    @Override
    public List<Project> findByNameContainingIgnoreCase(String name) {
        return projectRepository.findByNameContainingIgnoreCase(name);
    }

    @Transactional(readOnly = true)
    @Override
    public List<Project> findByTagsIn(Set<String> tags) {
        return projectRepository.findByTagsIn(tags);
    }

    @Transactional(readOnly = true)
    @Override
    public List<Project> findByAllTags(Set<String> tags, long tagCount) {
        return projectRepository.findByAllTags(tags, tagCount);
    }

    @Transactional(readOnly = true)
    @Override
    public List<Project> findAllByOrderByCreatedAtDesc() {
        return projectRepository.findAllByOrderByCreatedAtDesc();
    }

    @Transactional(readOnly = true)
    @Override
    public Page<Project> findAllByOrderByCreatedAtDesc(PageRequest pageRequest) {
        org.springframework.data.domain.Pageable pageable = org.springframework.data.domain.PageRequest.of(
                pageRequest.getPage(),
                pageRequest.getSize());
        org.springframework.data.domain.Page<Project> springPage = projectRepository
                .findAllByOrderByCreatedAtDesc(pageable);
        return new Page<>(springPage.getContent(), pageRequest, springPage.getTotalElements());
    }

    @Transactional(readOnly = true)
    @Override
    public List<Project> findByCreatedAtAfter(LocalDateTime dateTime) {
        return projectRepository.findByCreatedAtAfter(dateTime);
    }

    @Transactional(readOnly = true)
    @Override
    public List<Project> findByFileCountGreaterThanEqual(int minFileCount) {
        return projectRepository.findByFileCountGreaterThanEqual(minFileCount);
    }

    @Transactional(readOnly = true)
    @Override
    public List<Project> findProjectsWithExtractedTexts() {
        return projectRepository.findProjectsWithExtractedTexts();
    }

    @Transactional(readOnly = true)
    @Override
    public List<Project> findProjectsWithoutExtractedTexts() {
        return projectRepository.findProjectsWithoutExtractedTexts();
    }

    @Transactional(readOnly = true)
    @Override
    public List<Project> searchByNameOrDescription(String searchTerm) {
        return projectRepository.searchByNameOrDescription(searchTerm);
    }

    @Transactional(readOnly = true)
    @Override
    public Page<Project> searchByNameOrDescription(String searchTerm, PageRequest pageRequest) {
        org.springframework.data.domain.Pageable pageable = org.springframework.data.domain.PageRequest.of(
                pageRequest.getPage(),
                pageRequest.getSize());
        org.springframework.data.domain.Page<Project> springPage = projectRepository
                .searchByNameOrDescription(searchTerm, pageable);
        return new Page<>(springPage.getContent(), pageRequest, springPage.getTotalElements());
    }

    @Transactional(readOnly = true)
    @Override
    public long countByTag(String tag) {
        return projectRepository.countByTag(tag);
    }

    @Transactional(readOnly = true)
    @Override
    public List<String> findAllUsedTags() {
        return projectRepository.findAllUsedTags();
    }

    @Transactional(readOnly = true)
    @Override
    public boolean existsByName(String name) {
        return projectRepository.existsByName(name);
    }

    @Transactional(readOnly = true)
    @Override
    public Optional<Project> findByName(String name) {
        return projectRepository.findByName(name);
    }
}