package com.fao.flashcards.application.port.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * DTO für Projekt-Statistiken
 * Entspricht dem ProjectStatistics Interface aus dem Frontend
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProjectStatisticsDTO {
    
    /**
     * Gesamtanzahl der Projekte
     */
    private Long totalProjects;
    
    /**
     * Gesamtanzahl der hochgeladenen Dateien
     */
    private Long totalFiles;
    
    /**
     * Gesamtanzahl der extrahierten Texte
     */
    private Long totalExtractedTexts;

    /**
     * Gesamtgröße der Dateien in Byte
     */
    private Long totalFileSize;

    /**
     * Durchschnittliche Anzahl von Texten pro Datei
     */
    private Double averageTextsPerFile;
    
    /**
     * Meist verwendete Tags
     */
    private List<String> mostUsedTags;
    
    /**
     * Kürzliche Aktivitäten
     */
    private RecentActivityDTO recentActivity;
    
    /**
     * DTO für kürzliche Aktivitäten
     */
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class RecentActivityDTO {
        
        /**
         * Anzahl erstellter Projekte im Zeitraum
         */
        private Long projectsCreated;
        
        /**
         * Anzahl hochgeladener Dateien im Zeitraum
         */
        private Long filesUploaded;
        
        /**
         * Anzahl extrahierter Texte im Zeitraum
         */
        private Long textsExtracted;
        
        /**
         * Zeitraum (z.B. "last_7_days", "last_30_days")
         */
        private String period;
    }
}