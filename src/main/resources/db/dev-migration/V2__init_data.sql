-- Einige Beispiel-Karten einfügen
INSERT INTO cards (id, front, back, created_at, updated_at) VALUES 
('1', 'Was ist Kava?', 'Java ist eine objektorientierte Programmiersprache, die plattformunabhängig ist.', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('2', 'Was ist Spring Boot?', 'Spring Boot ist ein Framework, das die Entwicklung von Spring-basierten Anwendungen vereinfacht.', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('3', 'Was ist React?', 'React ist eine JavaScript-Bibliothek zum Erstellen von Benutzeroberflächen.', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('4', 'Was ist TypeScript?', 'TypeScript ist eine von Microsoft entwickelte Programmiersprache, die ein Superset von JavaScript ist und statische Typisierung hinzufügt.', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

-- Tags für die Karten
INSERT INTO card_tags (card_id, tag) VALUES
('1', 'Programmierung'),
('1', 'Java'),
('2', 'Programmierung'),
('2', 'Java'),
('2', 'Spring'),
('3', 'Programmierung'),
('3', 'JavaScript'),
('3', 'Frontend'),
('4', 'Programmierung'),
('4', 'JavaScript'),
('4', 'TypeScript');

-- Einige Beispiel-Decks einfügen
INSERT INTO decks (id, name, description, created_at, updated_at) VALUES
('1', 'Programmiersprachen', 'Eine Sammlung von Karteikarten über verschiedene Programmiersprachen', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('2', 'Frameworks und Bibliotheken', 'Eine Sammlung von Karteikarten über Frameworks und Bibliotheken', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

-- Tags für die Decks
INSERT INTO deck_tags (deck_id, tag) VALUES
('1', 'Programmierung'),
('1', 'Sprachen'),
('2', 'Programmierung'),
('2', 'Frameworks');

-- Karten zu Decks zuordnen
INSERT INTO deck_cards (id, deck_id, card_id, added_at) VALUES
('1', '1', '1', CURRENT_TIMESTAMP),
('2', '1', '4', CURRENT_TIMESTAMP),
('3', '2', '2', CURRENT_TIMESTAMP),
('4', '2', '3', CURRENT_TIMESTAMP);