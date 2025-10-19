package com.fao.flashcards.ocr.adapter.bootstrap;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import com.fao.flashcards.ocr.application.port.in.FileUploadConfig;
import com.fao.flashcards.ocr.application.port.in.FileUploadInputPort;
import com.fao.flashcards.ocr.application.port.in.OCRInputPort;
import com.fao.flashcards.ocr.application.port.in.OcrProjectInputPort;
import com.fao.flashcards.ocr.application.port.out.ExtractedTextRepository;
import com.fao.flashcards.ocr.application.port.out.OCRProcessingPort;
import com.fao.flashcards.ocr.application.port.out.OCRResultRepository;
import com.fao.flashcards.ocr.application.port.out.ProjectFileRepository;
import com.fao.flashcards.ocr.application.port.out.ProjectRepository;
import com.fao.flashcards.ocr.application.service.FileUploadService;
import com.fao.flashcards.ocr.application.service.OCRService;
import com.fao.flashcards.ocr.application.service.ProjectService;

@Configuration
public class OcrBeanConfig {

    public OcrBeanConfig() {
        super();
    }

    @Bean
    public OcrProjectInputPort projectService(ProjectRepository projectOutputPort,
            ProjectFileRepository projectFileOutputPort,
            ExtractedTextRepository extractedTextOutputPort) {
        return new ProjectService(projectOutputPort, projectFileOutputPort, extractedTextOutputPort);
    }

    @Bean
    public OCRInputPort ocrService(OCRProcessingPort ocrProcessingPort,
            ExtractedTextRepository extractedTextRepository,
            OCRResultRepository ocrResultRepository,
            ProjectFileRepository projectFileRepository,
            OcrProjectInputPort projectService) {
        return new OCRService(ocrProcessingPort, extractedTextRepository, ocrResultRepository,
                projectFileRepository, projectService);
    }

    
    @Bean
    public FileUploadService fileUploadService(
            ProjectFileRepository projectFileRepository,
            ProjectRepository projectRepository,
            OcrProjectInputPort ocrProjectInputPort, FileUploadConfig fileUploadConfig) {
        return new FileUploadService(projectFileRepository, projectRepository, ocrProjectInputPort, fileUploadConfig);
    }

    @Bean
    @Primary
    public FileUploadInputPort fileUploadInputPort(FileUploadService fileUploadService) {
        return new FileUploadServiceAdapter(fileUploadService);
    }

}
