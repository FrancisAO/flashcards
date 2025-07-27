# Flashcards-System

Ein System zur Erstellung und Verwaltung von Karteikarten, das beim effizienten Lernen unterstützt.

## Überblick

Das Flashcards-System ermöglicht es Benutzern, Karteikarten zu erstellen, zu organisieren und zu verwalten. Es bietet eine intuitive Benutzeroberfläche für die Arbeit mit Karteikarten und Decks.

## Funktionen

- Erstellen, Anzeigen, Bearbeiten und Löschen von Karteikarten
- Organisieren von Karteikarten in thematischen Decks
- Hinzufügen und Entfernen von Karten aus Decks
- Tagging-System für Karten und Decks zur besseren Organisation

## Technologie-Stack

### Backend
- Java 21
- Spring Boot 3.5
- H2-Datenbank
- Hibernate/JPA
- Gradle

### Frontend
- React.js
- TypeScript
- Material-UI
- React Router
- Axios

## Erste Schritte

### Voraussetzungen
- Java 21 JDK
- Node.js und npm
- Git

### Installation und Ausführung des Backends

1. Klone das Repository:
```
git clone <repository-url>
cd flashcards
```

2. Starte das Backend:
```
./gradlew bootRun
```
Das Backend läuft dann unter http://localhost:8080

### Installation und Ausführung des Frontends

1. Navigiere zum Frontend-Verzeichnis:
```
cd frontend
```

2. Installiere die Abhängigkeiten:
```
npm install
```

3. Starte den Entwicklungsserver:
```
npm start
```
Das Frontend läuft dann unter http://localhost:3000

## API-Dokumentation

### Cards API

- `GET /api/cards`: Alle Karten abrufen
- `GET /api/cards/{id}`: Karte mit ID abrufen
- `POST /api/cards`: Neue Karte erstellen
- `PUT /api/cards/{id}`: Karte aktualisieren
- `DELETE /api/cards/{id}`: Karte löschen
- `GET /api/cards/tag/{tag}`: Karten mit Tag abrufen

### Decks API

- `GET /api/decks`: Alle Decks abrufen
- `GET /api/decks/{id}`: Deck mit ID abrufen
- `GET /api/decks/{id}/cards`: Deck mit allen Karten abrufen
- `POST /api/decks`: Neues Deck erstellen
- `PUT /api/decks/{id}`: Deck aktualisieren
- `DELETE /api/decks/{id}`: Deck löschen
- `POST /api/decks/add-card`: Karte zu Deck hinzufügen
- `DELETE /api/decks/{deckId}/cards/{cardId}`: Karte aus Deck entfernen
- `GET /api/decks/tag/{tag}`: Decks mit Tag abrufen

## Lizenz

Dieses Projekt ist unter der MIT-Lizenz lizenziert - siehe die [LICENSE](LICENSE) Datei für Details.
