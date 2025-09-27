package com.fao.flashcards.application.port.dto;

import org.springframework.stereotype.Component;

import com.fao.flashcards.application.model.*;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Mapper-Komponente für die Konvertierung zwischen Domain Entities und DTOs.
 * Stellt zentrale Mapping-Funktionen für die API-Schicht bereit.
 */
@Component
public class DTOMapper {
    
    // ===========================================
    // PROJECT MAPPING
    // ===========================================
    
    /**
     * Konvertiert Project Entity zu ProjectDTO.
     */
    public ProjectDTO toDTO(Project project) {
        if (project == null) {
            return null;
        }
        
        return new ProjectDTO(
            project.getId(),
            project.getName(),
            project.getDescription(),
            project.getTags(),
            project.getFileCount(),
            project.getExtractedTextCount(),
            project.getCreatedAt(),
            project.getUpdatedAt()
        );
    }
    
    /**
     * Konvertiert CreateProjectRequest zu Project Entity.
     */
    public Project toEntity(CreateProjectRequest request) {
        if (request == null) {
            return null;
        }
        
        Project project = new Project(request.getName(), request.getDescription());
        if (request.getTags() != null) {
            project.getTags().addAll(request.getTags());
        }
        return project;
    }
    
    /**
     * Aktualisiert eine Project Entity mit Daten aus UpdateProjectRequest.
     */
    public void updateEntity(Project project, UpdateProjectRequest request) {
        if (project == null || request == null) {
            return;
        }
        
        project.setName(request.getName());
        project.setDescription(request.getDescription());
        
        if (request.getTags() != null) {
            project.getTags().clear();
            project.getTags().addAll(request.getTags());
        }
    }
    
    /**
     * Konvertiert Liste von Projects zu Liste von ProjectDTOs.
     */
    public List<ProjectDTO> toProjectDTOList(List<Project> projects) {
        if (projects == null) {
            return null;
        }
        return projects.stream().map(this::toDTO).collect(Collectors.toList());
    }
    
    // ===========================================
    // PROJECT FILE MAPPING
    // ===========================================
    
    /**
     * Konvertiert ProjectFile Entity zu ProjectFileDTO.
     */
    public ProjectFileDTO toDTO(ProjectFile projectFile) {
        if (projectFile == null) {
            return null;
        }
        
        ProjectFileDTO dto = new ProjectFileDTO(
            projectFile.getProject().getId(),
            projectFile.getOriginalFilename(),
            projectFile.getContentType(),
            projectFile.getFileSize(),
            projectFile.getFileType()
        );
        
        dto.setId(projectFile.getId());
        dto.setUploadedAt(projectFile.getUploadedAt());
        
        // Prüfen ob ExtractedText existiert - das müsste über Repository geprüft werden
        // Hier setzen wir erstmal false, das wird dann in den Services entsprechend gesetzt
        dto.setHasExtractedText(false);
        
        return dto;
    }
    
    /**
     * Konvertiert ProjectFile Entity zu ProjectFileDTO mit ExtractedText-Status.
     */
    public ProjectFileDTO toDTO(ProjectFile projectFile, boolean hasExtractedText) {
        ProjectFileDTO dto = toDTO(projectFile);
        if (dto != null) {
            dto.setHasExtractedText(hasExtractedText);
        }
        return dto;
    }
    
    /**
     * Konvertiert Liste von ProjectFiles zu Liste von ProjectFileDTOs.
     */
    public List<ProjectFileDTO> toProjectFileDTOList(List<ProjectFile> projectFiles) {
        if (projectFiles == null) {
            return null;
        }
        return projectFiles.stream().map(this::toDTO).collect(Collectors.toList());
    }
    
    // ===========================================
    // EXTRACTED TEXT MAPPING
    // ===========================================
    
    /**
     * Konvertiert ExtractedText Entity zu ExtractedTextDTO.
     */
    public ExtractedTextDTO toDTO(ExtractedText extractedText) {
        if (extractedText == null) {
            return null;
        }
        
        return new ExtractedTextDTO(
            extractedText.getId(),
            extractedText.getProjectFile().getId(),
            extractedText.getExtractedContent(),
            extractedText.getEditedContent(),
            extractedText.getExtractionSource(),
            extractedText.getIsEdited(),
            extractedText.getMetadata(),
            extractedText.getExtractedAt(),
            extractedText.getLastEditedAt()
        );
    }
    
    /**
     * Aktualisiert ExtractedText Entity mit Daten aus UpdateTextRequest.
     */
    public void updateEntity(ExtractedText extractedText, UpdateTextRequest request) {
        if (extractedText == null || request == null) {
            return;
        }
        
        extractedText.updateEditedContent(request.getEditedContent());
    }
    
    /**
     * Konvertiert Liste von ExtractedTexts zu Liste von ExtractedTextDTOs.
     */
    public List<ExtractedTextDTO> toExtractedTextDTOList(List<ExtractedText> extractedTexts) {
        if (extractedTexts == null) {
            return null;
        }
        return extractedTexts.stream().map(this::toDTO).collect(Collectors.toList());
    }
    
    // ===========================================
    // OCR RESULT MAPPING
    // ===========================================
    
    /**
     * Konvertiert OCRResult Entity zu OCRResultDTO.
     */
    public OCRResultDTO toDTO(OCRResult ocrResult) {
        if (ocrResult == null) {
            return null;
        }
        
        OCRResultDTO dto = new OCRResultDTO(
            ocrResult.getId(),
            ocrResult.getStatus(),
            ocrResult.getPagesProcessed(),
            ocrResult.getProcessingTimeMs(),
            ocrResult.getModelUsed(),
            ocrResult.getConfidenceScore(),
            ocrResult.getCreatedAt(),
            ocrResult.getErrorDetails()
        );
        
        return dto;
    }
    
    /**
     * Konvertiert Liste von OCRResults zu Liste von OCRResultDTOs.
     */
    public List<OCRResultDTO> toOCRResultDTOList(List<OCRResult> ocrResults) {
        if (ocrResults == null) {
            return null;
        }
        return ocrResults.stream().map(this::toDTO).collect(Collectors.toList());
    }
    
    // ===========================================
    // FILE UPLOAD RESPONSE MAPPING
    // ===========================================
    
    /**
     * Konvertiert ProjectFile zu FileUploadResponse.
     */
    public FileUploadResponse toFileUploadResponse(ProjectFile projectFile) {
        if (projectFile == null) {
            return null;
        }
        
        return new FileUploadResponse(
            projectFile.getId(),
            projectFile.getProject().getId(),
            projectFile.getOriginalFilename(),
            projectFile.getContentType(),
            projectFile.getFileSize(),
            projectFile.getFileType(),
            projectFile.getUploadedAt()
        );
    }
    
    // ===========================================
    // OCR PROCESS RESPONSE MAPPING
    // ===========================================
    
    /**
     * Konvertiert ExtractedText und OCRResult zu OCRProcessResponse für Einzeldatei.
     */
    public OCRProcessResponse toOCRProcessResponse(ExtractedText extractedText, OCRResult ocrResult) {
        if (extractedText == null || ocrResult == null) {
            return OCRProcessResponse.singleFileError("unknown", "unknown", "Unbekannter Fehler");
        }
        
        ProjectFile projectFile = extractedText.getProjectFile();
        
        if (ocrResult.getStatus() == OCRStatus.SUCCESS) {
            return OCRProcessResponse.singleFileSuccess(
                projectFile.getId(),
                projectFile.getOriginalFilename(),
                extractedText.getId(),
                ocrResult.getPagesProcessed(),
                ocrResult.getProcessingTimeMs(),
                ocrResult.getConfidenceScore()
            );
        } else {
            return OCRProcessResponse.singleFileError(
                projectFile.getId(),
                projectFile.getOriginalFilename(),
                ocrResult.getErrorDetails()
            );
        }
    }
    
    /**
     * Konvertiert OCROptions Domain Model zu OCRProcessRequest.OCROptionsDTO.
     */
    public OCRProcessRequest.OCROptionsDTO toOCROptionsDTO(OCROptions options) {
        if (options == null) {
            return null;
        }
        
        OCRProcessRequest.OCROptionsDTO dto = new OCRProcessRequest.OCROptionsDTO();
        // Hier würden die entsprechenden Felder gemappt werden, wenn OCROptions diese hätte
        // Das OCROptions Model müsste entsprechend erweitert werden
        return dto;
    }
    
    /**
     * Konvertiert OCRProcessRequest.OCROptionsDTO zu OCROptions Domain Model.
     */
    public OCROptions toOCROptions(OCRProcessRequest.OCROptionsDTO dto) {
        if (dto == null) {
            return OCROptions.defaultOptions();
        }
        
        // Hier würden die entsprechenden Felder gemappt werden
        // Momentan verwenden wir die Standard-Optionen
        return OCROptions.defaultOptions();
    }
    
    // ===========================================
    // UTILITY METHODS
    // ===========================================
    
    /**
     * Prüft ob ein DTO valid ist (nicht null und grundlegende Validierung).
     */
    public boolean isValidDTO(Object dto) {
        return dto != null;
    }
    
    /**
     * Prüft ob eine Entity valid ist (nicht null und grundlegende Validierung).
     */
    public boolean isValidEntity(Object entity) {
        return entity != null;
    }
}