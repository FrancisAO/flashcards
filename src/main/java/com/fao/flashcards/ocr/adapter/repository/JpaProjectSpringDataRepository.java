package com.fao.flashcards.ocr.adapter.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.fao.flashcards.ocr.model.Project;

/**
 * Repository für OCR-Projekte.
 * Bietet CRUD-Operationen und spezielle Abfragen für Projekt-Management.
 */
@Repository
public interface JpaProjectSpringDataRepository extends JpaRepository<Project, String> {

       /**
        * Findet Projekte nach Namen (case-insensitive, partial match).
        */
       @Query("SELECT p FROM Project p WHERE LOWER(p.name) LIKE LOWER(CONCAT('%', :name, '%'))")
       List<Project> findByNameContainingIgnoreCase(@Param("name") String name);

       /**
        * Findet Projekte nach Namen mit Paginierung.
        */
       @Query("SELECT p FROM Project p WHERE LOWER(p.name) LIKE LOWER(CONCAT('%', :name, '%'))")
       Page<Project> findByNameContainingIgnoreCase(@Param("name") String name, Pageable pageable);

       /**
        * Findet Projekte die bestimmte Tags enthalten.
        */
       @Query("SELECT DISTINCT p FROM Project p JOIN p.tags t WHERE t IN :tags")
       List<Project> findByTagsIn(@Param("tags") Set<String> tags);

       /**
        * Findet Projekte die alle angegebenen Tags enthalten.
        */
       @Query("SELECT p FROM Project p WHERE SIZE(p.tags) >= :tagCount AND " +
                     "(SELECT COUNT(t) FROM Project p2 JOIN p2.tags t WHERE p2.id = p.id AND t IN :tags) = :tagCount")
       List<Project> findByAllTags(@Param("tags") Set<String> tags, @Param("tagCount") long tagCount);

       /**
        * Findet Projekte die einen bestimmten Tag enthalten mit Paginierung.
        */
       @Query("SELECT DISTINCT p FROM Project p JOIN p.tags t WHERE t = :tag")
       Page<Project> findByTag(@Param("tag") String tag, Pageable pageable);

       /**
        * Findet alle Projekte sortiert nach Erstellungsdatum (neueste zuerst).
        */
       List<Project> findAllByOrderByCreatedAtDesc();

       /**
        * Findet alle Projekte mit Paginierung, sortiert nach Erstellungsdatum.
        */
       Page<Project> findAllByOrderByCreatedAtDesc(Pageable pageable);

       /**
        * Findet Projekte die nach einem bestimmten Datum erstellt wurden.
        */
       List<Project> findByCreatedAtAfter(LocalDateTime dateTime);

       /**
        * Findet Projekte mit einer bestimmten Mindestanzahl von Dateien.
        */
       @Query("SELECT p FROM Project p WHERE p.fileCount >= :minFileCount")
       List<Project> findByFileCountGreaterThanEqual(@Param("minFileCount") int minFileCount);

       /**
        * Findet Projekte mit extrahierten Texten.
        */
       @Query("SELECT p FROM Project p WHERE p.extractedTextCount > 0")
       List<Project> findProjectsWithExtractedTexts();

       /**
        * Findet Projekte ohne extrahierte Texte.
        */
       @Query("SELECT p FROM Project p WHERE p.extractedTextCount = 0")
       List<Project> findProjectsWithoutExtractedTexts();

       /**
        * Sucht Projekte nach Name oder Beschreibung.
        */
       @Query("SELECT p FROM Project p WHERE " +
                     "LOWER(p.name) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
                     "LOWER(p.description) LIKE LOWER(CONCAT('%', :searchTerm, '%'))")
       List<Project> searchByNameOrDescription(@Param("searchTerm") String searchTerm);

       /**
        * Sucht Projekte nach Name oder Beschreibung mit Paginierung.
        */
       @Query("SELECT p FROM Project p WHERE " +
                     "LOWER(p.name) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
                     "LOWER(p.description) LIKE LOWER(CONCAT('%', :searchTerm, '%'))")
       Page<Project> searchByNameOrDescription(@Param("searchTerm") String searchTerm, Pageable pageable);

       /**
        * Zählt Projekte mit einem bestimmten Tag.
        */
       @Query("SELECT COUNT(DISTINCT p) FROM Project p JOIN p.tags t WHERE t = :tag")
       long countByTag(@Param("tag") String tag);

       /**
        * Findet alle verwendeten Tags über alle Projekte.
        */
       @Query("SELECT DISTINCT t FROM Project p JOIN p.tags t ORDER BY t")
       List<String> findAllUsedTags();

       /**
        * Prüft ob ein Projekt mit dem gegebenen Namen bereits existiert.
        */
       boolean existsByName(String name);

       /**
        * Findet ein Projekt nach Namen (exakte Übereinstimmung).
        */
       Optional<Project> findByName(String name);
}