-- Tabelle für Karten
CREATE TABLE cards (
    id VARCHAR(255) PRIMARY KEY,
    front TEXT NOT NULL,
    back TEXT NOT NULL,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL
);

-- Tabelle für Karten-Tags
CREATE TABLE card_tags (
    card_id VARCHAR(255) NOT NULL,
    tag VARCHAR(255) NOT NULL,
    PRIMARY KEY (card_id, tag),
    FOREIGN KEY (card_id) REFERENCES cards (id) ON DELETE CASCADE
);

-- Tabelle für Decks
CREATE TABLE decks (
    id VARCHAR(255) PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    description TEXT,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL
);

-- Tabelle für Deck-Tags
CREATE TABLE deck_tags (
    deck_id VARCHAR(255) NOT NULL,
    tag VARCHAR(255) NOT NULL,
    PRIMARY KEY (deck_id, tag),
    FOREIGN KEY (deck_id) REFERENCES decks (id) ON DELETE CASCADE
);

-- Tabelle für die Zuordnung von Karten zu Decks
CREATE TABLE deck_cards (
    id VARCHAR(255) PRIMARY KEY,
    deck_id VARCHAR(255) NOT NULL,
    card_id VARCHAR(255) NOT NULL,
    added_at TIMESTAMP NOT NULL,
    FOREIGN KEY (deck_id) REFERENCES decks (id) ON DELETE CASCADE,
    FOREIGN KEY (card_id) REFERENCES cards (id) ON DELETE CASCADE
);