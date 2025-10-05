package com.fao.flashcards.ocr.adapter.beans;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.fao.flashcards.ocr.application.port.out.ExtractedTextRepository;
import com.fao.flashcards.ocr.application.port.out.ProjectFileRepository;
import com.fao.flashcards.ocr.application.port.out.ProjectRepository;
import com.fao.flashcards.ocr.application.service.ProjectService;

@Configuration
public class OcrBeanConfig {

    public OcrBeanConfig() {
        super();
    }

    @Bean
    public ProjectService projectService(ProjectRepository projectOutputPort,
            ProjectFileRepository projectFileOutputPort,
            ExtractedTextRepository extractedTextOutputPort) {
        return new ProjectService(projectOutputPort, projectFileOutputPort, extractedTextOutputPort);
    }

}
