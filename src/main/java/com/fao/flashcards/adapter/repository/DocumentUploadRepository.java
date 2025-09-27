package com.fao.flashcards.adapter.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.fao.flashcards.application.model.DocumentUpload;

import java.util.List;

/**
 * Repository für DocumentUpload-Entitäten.
 */
@Repository
public interface DocumentUploadRepository extends JpaRepository<DocumentUpload, String> {
    
    /**
     * Findet alle hochgeladenen Dokumente für eine bestimmte Generierungsanfrage.
     * 
     * @param generationRequestId Die ID der Generierungsanfrage
     * @return Eine Liste von hochgeladenen Dokumenten
     */
    List<DocumentUpload> findByGenerationRequestId(String generationRequestId);
}