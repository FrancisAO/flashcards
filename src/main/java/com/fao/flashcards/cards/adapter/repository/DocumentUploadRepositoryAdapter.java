package com.fao.flashcards.cards.adapter.repository;

import com.fao.flashcards.cards.application.port.out.repository.DocumentUploadOutputPort;
import com.fao.flashcards.cards.model.DocumentUpload;

import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

/**
 * Adapter der das DocumentUploadOutputPort Interface implementiert und 
 * die Spring Boot Repository Funktionalität bereitstellt.
 */
@Component
public class DocumentUploadRepositoryAdapter implements DocumentUploadOutputPort {
    
    private final DocumentUploadRepository documentUploadRepository;
    
    public DocumentUploadRepositoryAdapter(DocumentUploadRepository documentUploadRepository) {
        this.documentUploadRepository = documentUploadRepository;
    }
    
    @Override
    public DocumentUpload save(DocumentUpload documentUpload) {
        return documentUploadRepository.save(documentUpload);
    }
    
    @Override
    public List<DocumentUpload> saveAll(Iterable<DocumentUpload> documentUploads) {
        return documentUploadRepository.saveAll(documentUploads);
    }
    
    @Override
    public Optional<DocumentUpload> findById(String id) {
        return documentUploadRepository.findById(id);
    }
    
    @Override
    public List<DocumentUpload> findAll() {
        return documentUploadRepository.findAll();
    }
    
    @Override
    public boolean existsById(String id) {
        return documentUploadRepository.existsById(id);
    }
    
    @Override
    public long count() {
        return documentUploadRepository.count();
    }
    
    @Override
    public void deleteById(String id) {
        documentUploadRepository.deleteById(id);
    }
    
    @Override
    public void delete(DocumentUpload documentUpload) {
        documentUploadRepository.delete(documentUpload);
    }
    
    @Override
    public void deleteAll() {
        documentUploadRepository.deleteAll();
    }
    
    @Override
    public List<DocumentUpload> findByGenerationRequestId(String generationRequestId) {
        return documentUploadRepository.findByGenerationRequestId(generationRequestId);
    }
}