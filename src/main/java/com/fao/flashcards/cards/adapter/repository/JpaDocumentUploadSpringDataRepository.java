package com.fao.flashcards.cards.adapter.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.fao.flashcards.cards.model.DocumentUpload;

/**
 * Repository für DocumentUpload-Entitäten.
 */
@Repository
public interface JpaDocumentUploadSpringDataRepository extends JpaRepository<DocumentUpload, String> {

    /**
     * Findet alle hochgeladenen Dokumente für eine bestimmte Generierungsanfrage.
     * 
     * @param generationRequestId Die ID der Generierungsanfrage
     * @return Eine Liste von hochgeladenen Dokumenten
     */
    List<DocumentUpload> findByGenerationRequestId(String generationRequestId);
}