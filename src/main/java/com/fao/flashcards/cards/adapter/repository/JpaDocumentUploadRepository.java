package com.fao.flashcards.cards.adapter.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.fao.flashcards.cards.application.port.out.repository.DocumentUploadRepository;
import com.fao.flashcards.cards.model.DocumentUpload;

/**
 * Adapter der das DocumentUploadOutputPort Interface implementiert und
 * die Spring Boot Repository Funktionalität bereitstellt.
 */
@Component
public class JpaDocumentUploadRepository implements DocumentUploadRepository {

    private final JpaDocumentUploadSpringDataRepository documentUploadRepository;

    public JpaDocumentUploadRepository(JpaDocumentUploadSpringDataRepository documentUploadRepository) {
        this.documentUploadRepository = documentUploadRepository;
    }

    @Override
    @Transactional
    public DocumentUpload save(DocumentUpload documentUpload) {
        return documentUploadRepository.save(documentUpload);
    }

    @Override
    @Transactional
    public List<DocumentUpload> saveAll(Iterable<DocumentUpload> documentUploads) {
        return documentUploadRepository.saveAll(documentUploads);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<DocumentUpload> findById(String id) {
        return documentUploadRepository.findById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<DocumentUpload> findAll() {
        return documentUploadRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existsById(String id) {
        return documentUploadRepository.existsById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public long count() {
        return documentUploadRepository.count();
    }

    @Override
    @Transactional
    public void deleteById(String id) {
        documentUploadRepository.deleteById(id);
    }

    @Override
    @Transactional
    public void delete(DocumentUpload documentUpload) {
        documentUploadRepository.delete(documentUpload);
    }

    @Override
    @Transactional
    public void deleteAll() {
        documentUploadRepository.deleteAll();
    }

    @Override
    @Transactional(readOnly = true)
    public List<DocumentUpload> findByGenerationRequestId(String generationRequestId) {
        return documentUploadRepository.findByGenerationRequestId(generationRequestId);
    }
}