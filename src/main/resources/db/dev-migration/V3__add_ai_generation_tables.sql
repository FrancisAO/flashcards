-- V3__add_ai_generation_tables.sql

-- Tabelle für KI-Generierungsanfragen
CREATE TABLE ai_generation_requests (
    id VARCHAR(36) PRIMARY KEY,
    deck_id VARCHAR(36) NOT NULL,
    prompt TEXT,
    number_of_cards INTEGER,
    status VARCHAR(20) NOT NULL,
    created_at TIMESTAMP,
    updated_at TIMESTAMP,
    FOREIGN KEY (deck_id) REFERENCES decks(id)
);

-- Tabelle für hochgeladene Dokumente
CREATE TABLE document_uploads (
    id VARCHAR(36) PRIMARY KEY,
    generation_request_id VARCHAR(36) NOT NULL,
    original_filename VARCHAR(255) NOT NULL,
    content_type VARCHAR(100) NOT NULL,
    file_size BIGINT,
    storage_path VARCHAR(255),
    extracted_text TEXT,
    uploaded_at TIMESTAMP,
    FOREIGN KEY (generation_request_id) REFERENCES ai_generation_requests(id) ON DELETE CASCADE
);

-- Tabelle für generierte Karteikarten
CREATE TABLE ai_generated_cards (
    id VARCHAR(36) PRIMARY KEY,
    generation_request_id VARCHAR(36) NOT NULL,
    front TEXT NOT NULL,
    back TEXT NOT NULL,
    is_edited BOOLEAN DEFAULT FALSE,
    is_saved BOOLEAN DEFAULT FALSE,
    card_id VARCHAR(36),
    created_at TIMESTAMP,
    updated_at TIMESTAMP,
    FOREIGN KEY (generation_request_id) REFERENCES ai_generation_requests(id) ON DELETE CASCADE,
    FOREIGN KEY (card_id) REFERENCES cards(id)
);

-- Indizes für bessere Performance
CREATE INDEX idx_ai_generation_requests_deck_id ON ai_generation_requests(deck_id);
CREATE INDEX idx_document_uploads_generation_request_id ON document_uploads(generation_request_id);
CREATE INDEX idx_ai_generated_cards_generation_request_id ON ai_generated_cards(generation_request_id);
CREATE INDEX idx_ai_generated_cards_card_id ON ai_generated_cards(card_id);