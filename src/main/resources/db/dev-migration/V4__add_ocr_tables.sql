-- V4__add_ocr_tables.sql
-- Migration für OCR Domain Models: Project, ProjectFile, ExtractedText, OCRResult

-- Tabelle für OCR-Projekte
CREATE TABLE projects (
    id VARCHAR(36) PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    description TEXT,
    file_count INTEGER NOT NULL DEFAULT 0,
    extracted_text_count INTEGER NOT NULL DEFAULT 0,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL
);

-- Tabelle für Projekt-Tags (ElementCollection aus Project.java)
CREATE TABLE project_tags (
    project_id VARCHAR(36) NOT NULL,
    tag VARCHAR(255) NOT NULL,
    PRIMARY KEY (project_id, tag),
    FOREIGN KEY (project_id) REFERENCES projects (id) ON DELETE CASCADE
);

-- Tabelle für Projektdateien
CREATE TABLE project_files (
    id VARCHAR(36) PRIMARY KEY,
    project_id VARCHAR(36) NOT NULL,
    original_filename VARCHAR(500) NOT NULL,
    content_type VARCHAR(100) NOT NULL,
    file_size BIGINT NOT NULL,
    storage_path VARCHAR(1000) NOT NULL,
    file_type VARCHAR(20) NOT NULL,
    uploaded_at TIMESTAMP NOT NULL,
    FOREIGN KEY (project_id) REFERENCES projects (id) ON DELETE CASCADE,
    CONSTRAINT chk_project_files_file_type CHECK (file_type IN ('IMAGE', 'DOCUMENT'))
);

-- Tabelle für extrahierte Texte
CREATE TABLE extracted_texts (
    id VARCHAR(36) PRIMARY KEY,
    project_file_id VARCHAR(36) NOT NULL,
    extracted_content TEXT,
    edited_content TEXT,
    extraction_source VARCHAR(20) NOT NULL,
    is_edited BOOLEAN NOT NULL DEFAULT FALSE,
    metadata TEXT,
    extracted_at TIMESTAMP NOT NULL,
    last_edited_at TIMESTAMP,
    FOREIGN KEY (project_file_id) REFERENCES project_files (id) ON DELETE CASCADE,
    CONSTRAINT chk_extracted_texts_extraction_source CHECK (extraction_source IN ('IMAGE', 'DOCUMENT'))
);

-- Tabelle für OCR-Verarbeitungsergebnisse
CREATE TABLE ocr_results (
    id VARCHAR(36) PRIMARY KEY,
    extracted_text_id VARCHAR(36) NOT NULL,
    status VARCHAR(20) NOT NULL,
    pages_processed INTEGER,
    processing_time_ms BIGINT,
    api_response TEXT,
    error_details TEXT,
    model_used VARCHAR(100),
    confidence_score DECIMAL(5,4),
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL,
    FOREIGN KEY (extracted_text_id) REFERENCES extracted_texts (id) ON DELETE CASCADE,
    CONSTRAINT chk_ocr_results_status CHECK (status IN ('PENDING', 'PROCESSING', 'SUCCESS', 'FAILED'))
);

-- Indizes für bessere Performance
CREATE INDEX idx_projects_name ON projects(name);
CREATE INDEX idx_projects_created_at ON projects(created_at);

CREATE INDEX idx_project_tags_project_id ON project_tags(project_id);
CREATE INDEX idx_project_tags_tag ON project_tags(tag);

CREATE INDEX idx_project_files_project_id ON project_files(project_id);
CREATE INDEX idx_project_files_file_type ON project_files(file_type);
CREATE INDEX idx_project_files_uploaded_at ON project_files(uploaded_at);

CREATE INDEX idx_extracted_texts_project_file_id ON extracted_texts(project_file_id);
CREATE INDEX idx_extracted_texts_extraction_source ON extracted_texts(extraction_source);
CREATE INDEX idx_extracted_texts_is_edited ON extracted_texts(is_edited);
CREATE INDEX idx_extracted_texts_extracted_at ON extracted_texts(extracted_at);

CREATE INDEX idx_ocr_results_extracted_text_id ON ocr_results(extracted_text_id);
CREATE INDEX idx_ocr_results_status ON ocr_results(status);
CREATE INDEX idx_ocr_results_created_at ON ocr_results(created_at);