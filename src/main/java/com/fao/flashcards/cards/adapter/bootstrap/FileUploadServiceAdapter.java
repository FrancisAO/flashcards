package com.fao.flashcards.cards.adapter.bootstrap;

import java.io.IOException;
import java.util.List;

import org.springframework.validation.annotation.Validated;

import com.fao.flashcards.cards.adapter.web.MultipartFileAdapter;
import com.fao.flashcards.cards.application.port.in.FileUploadInputPort;
import com.fao.flashcards.cards.application.service.FileUploadService;
import com.fao.flashcards.ocr.model.ProjectFile;
import com.fao.flashcards.shared.model.pagination.Page;
import com.fao.flashcards.shared.model.pagination.PageRequest;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Validated
public class FileUploadServiceAdapter implements FileUploadInputPort {

    private FileUploadService fileUploadService;

    FileUploadServiceAdapter(FileUploadService fileUploadService) {
        this.fileUploadService = fileUploadService;
    }

    @Override
    public ProjectFile uploadFile(@NotBlank String projectId, @NotNull MultipartFileAdapter file) throws IOException {
        return fileUploadService.uploadFile(projectId, file);
    }

    @Override
    public void deleteFile(@NotBlank String fileId) throws IOException {
        fileUploadService.deleteFile(fileId);
    }

    @Override
    public boolean fileExists(@NotBlank String fileId) {
        return fileUploadService.fileExists(fileId);
    }

    @Override
    public ProjectFile getFileInfo(@NotBlank String fileId) {
        return fileUploadService.getFileInfo(fileId);
    }

    @Override
    public List<ProjectFile> getProjectFiles(@NotBlank String projectId) {
        return fileUploadService.getProjectFiles(projectId);
    }

    @Override
    public boolean physicalFileExists(@NotBlank String fileId) {
        return fileUploadService.physicalFileExists(fileId);
    }

    @Override
    public long getTotalProjectFileSize(@NotBlank String projectId) {
        return fileUploadService.getTotalProjectFileSize(projectId);
    }

    @Override
    public Page<ProjectFile> searchProjectFiles(String projectId, String filename, String fileType, PageRequest pageable) {
        return fileUploadService.searchProjectFiles(projectId, filename, fileType, pageable);
    }

    @Override
    public Page<ProjectFile> getProjectFiles(String projectId, PageRequest pageable) {
        return fileUploadService.getProjectFiles(projectId, pageable);
    }

}
