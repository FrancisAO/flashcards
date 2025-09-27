package com.fao.flashcards.application.port.dto;

import com.fao.flashcards.application.model.FileType;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Response DTO für Datei-Upload-Operationen.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class FileUploadResponse {
    
    private String fileId;
    private String projectId;
    private String originalFilename;
    private String contentType;
    private Long fileSize;
    private FileType fileType;
    
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime uploadedAt;
    
    private String message;
    private boolean success;
    
    /**
     * Konstruktor für erfolgreiche Uploads.
     */
    public FileUploadResponse(String fileId, String projectId, String originalFilename, 
                             String contentType, Long fileSize, FileType fileType, 
                             LocalDateTime uploadedAt) {
        this.fileId = fileId;
        this.projectId = projectId;
        this.originalFilename = originalFilename;
        this.contentType = contentType;
        this.fileSize = fileSize;
        this.fileType = fileType;
        this.uploadedAt = uploadedAt;
        this.success = true;
        this.message = "Datei erfolgreich hochgeladen";
    }
    
    /**
     * Erstellt eine erfolgreiche Response aus ProjectFileDTO.
     */
    public static FileUploadResponse success(ProjectFileDTO projectFile) {
        return new FileUploadResponse(
            projectFile.getId(),
            projectFile.getProjectId(),
            projectFile.getOriginalFilename(),
            projectFile.getContentType(),
            projectFile.getFileSize(),
            projectFile.getFileType(),
            projectFile.getUploadedAt()
        );
    }
    
    /**
     * Erstellt eine Fehler-Response.
     */
    public static FileUploadResponse error(String message) {
        FileUploadResponse response = new FileUploadResponse();
        response.setSuccess(false);
        response.setMessage(message);
        return response;
    }
    
    /**
     * Gibt die Dateigröße in menschenlesbarem Format zurück.
     */
    public String getFormattedFileSize() {
        if (fileSize == null) {
            return "0 B";
        }
        
        long bytes = fileSize;
        if (bytes < 1024) {
            return bytes + " B";
        }
        
        int exp = (int) (Math.log(bytes) / Math.log(1024));
        String pre = "KMGTPE".charAt(exp - 1) + "";
        
        return String.format("%.1f %sB", bytes / Math.pow(1024, exp), pre);
    }
    
    /**
     * Gibt die Dateiendung zurück.
     */
    public String getFileExtension() {
        if (originalFilename == null || originalFilename.isEmpty()) {
            return "";
        }
        
        int lastDotIndex = originalFilename.lastIndexOf('.');
        if (lastDotIndex == -1 || lastDotIndex == originalFilename.length() - 1) {
            return "";
        }
        
        return originalFilename.substring(lastDotIndex + 1).toLowerCase();
    }
}