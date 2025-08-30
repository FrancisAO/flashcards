package com.fao.flashcards.domain.repository;

import com.fao.flashcards.domain.model.DocumentUpload;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

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