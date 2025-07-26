# MVP1 - Plan für das Flashcards-System

## Inhaltsverzeichnis
1. [Überblick über den MVP](#überblick-über-den-mvp)
2. [Technologie-Stack für den MVP](#technologie-stack-für-den-mvp)
3. [Architektur für den MVP](#architektur-für-den-mvp)
4. [Datenmodell für den MVP](#datenmodell-für-den-mvp)
5. [API-Design für den MVP](#api-design-für-den-mvp)
6. [Frontend-Komponenten für den MVP](#frontend-komponenten-für-den-mvp)
7. [Implementierungsplan für den MVP](#implementierungsplan-für-den-mvp)
8. [Qualitätssicherungsmaßnahmen für den MVP](#qualitätssicherungsmaßnahmen-für-den-mvp)

## Überblick über den MVP

Der MVP (Minimum Viable Product) des Flashcards-Systems konzentriert sich auf die grundlegenden Funktionen zur Erstellung und Verwaltung von Karteikarten und Decks. Er bietet eine einfache, aber funktionale Benutzeroberfläche, die es dem Nutzer ermöglicht, Karteikarten zu erstellen, zu organisieren und zu verwalten.

### Ziele des MVP
1. Implementierung der grundlegenden Funktionalität zur Verwaltung von Karteikarten und Decks
2. Bereitstellung einer intuitiven und benutzerfreundlichen Oberfläche
3. Schaffung einer soliden Grundlage für zukünftige Erweiterungen (wie den Spaced-Repetition-Algorithmus und KI-Funktionen)

### Funktionsumfang des MVP

**Frontend:**
- Menü mit folgenden Optionen:
  - Neues Deck anlegen
  - Decks anzeigen
  - Eine Karte einem Deck hinzufügen
- Einfache Navigation zwischen Decks und Karteikarten
- Grundlegende Benutzeroberfläche mit Material-UI

**Backend:**
- CRUD-Operationen für Decks (Create, Read, Update, Delete)
- CRUD-Operationen für Karteikarten
- REST-API für die Kommunikation zwischen Frontend und Backend
- Persistente Datenspeicherung in einer H2-Datenbank

### Nicht im MVP enthalten
- Spaced-Repetition-Lernalgorithmus
- KI-gestützte Funktionen
- Erweiterte Statistiken und Analysen
- Import/Export-Funktionen
- Medienunterstützung (Bilder, Audio, Formeln)

## Technologie-Stack für den MVP

Basierend auf der arc42-Dokumentation wird der MVP mit folgenden Technologien implementiert:

### Backend
- **Programmiersprache**: Java 21
- **Framework**: Spring Boot 3.5
- **Datenbank**: H2 (eingebettete relationale Datenbank)
- **ORM**: Hibernate/JPA für objekt-relationales Mapping
- **Webserver**: Embedded Tomcat (in Spring Boot integriert)
- **Build-Tool**: Gradle
- **Testing**: JUnit 5 & Mockito
- **Logging**: Logback

### Frontend
- **Framework**: React.js
- **Sprache**: TypeScript
- **UI-Bibliothek**: Material-UI
- **HTTP-Client**: Axios für die Kommunikation mit dem Backend
- **State Management**: React Hooks (useState, useContext)
- **Routing**: React Router

### Entwicklungswerkzeuge
- **IDE**: VSCode 
- **Versionskontrolle**: Git
- **Code-Qualität**: ESLint (Frontend), Checkstyle (Backend)

### Begründung der Technologieauswahl

Die gewählten Technologien unterstützen die wichtigsten Qualitätsziele des Projekts:

1. **Benutzerfreundlichkeit (sehr hoch)**:
   - React.js und Material-UI ermöglichen eine intuitive, responsive Benutzeroberfläche mit konsistentem Design
   - TypeScript verbessert die Codequalität und reduziert Fehler in der UI

2. **Zuverlässigkeit (hoch)**:
   - Java's Typsicherheit und Exception-Handling reduzieren Laufzeitfehler
   - H2 bietet zuverlässige Datenspeicherung mit Transaktionssicherheit
   - JUnit und Mockito ermöglichen umfassende Tests zur Qualitätssicherung

3. **Wartbarkeit (mittel)**:
   - Spring Boot fördert eine saubere Architektur mit Trennung der Zuständigkeiten
   - TypeScript bietet starke Typisierung für bessere Codequalität
   - Gradle vereinfacht das Dependency-Management und den Build-Prozess

## Architektur für den MVP

Die Architektur des MVP folgt den in der arc42-Dokumentation definierten Architekturmustern und -prinzipien, insbesondere der Schichtenarchitektur mit klarer Trennung von Verantwortlichkeiten.

### Gesamtarchitektur

Die Anwendung ist in zwei Hauptteile gegliedert:
1. **Frontend**: Eine React-basierte Single-Page-Application (SPA)
2. **Backend**: Ein Spring Boot-basierter REST-Service

Die Kommunikation zwischen Frontend und Backend erfolgt über eine REST-API mit JSON als Datenformat.

```
┌─────────────────┐      HTTP/REST      ┌─────────────────┐
│                 │ ◄─────────────────► │                 │
│     Frontend    │      (JSON)         │     Backend     │
│    (React.js)   │                     │  (Spring Boot)  │
│                 │                     │                 │
└─────────────────┘                     └─────────────────┘
                                               │
                                               │ JPA/Hibernate
                                               ▼
                                        ┌─────────────────┐
                                        │                 │
                                        │   Datenbank     │
                                        │      (H2)       │
                                        │                 │
                                        └─────────────────┘
```

### Backend-Architektur

Das Backend folgt einer klassischen Schichtenarchitektur mit folgenden Schichten:

1. **Präsentationsschicht** (Controller-Schicht):
   - REST-Controller für die API-Endpunkte
   - Request/Response-Mapping
   - Validierung von Eingabedaten

2. **Anwendungsschicht** (Service-Schicht):
   - Implementierung der Geschäftslogik
   - Orchestrierung von Datenzugriffen
   - Transaktionsmanagement

3. **Datenzugriffsschicht** (Repository-Schicht):
   - Implementierung des Repository-Patterns
   - Datenbankzugriff über JPA/Hibernate
   - Mapping zwischen Entitäten und Domänenobjekten

4. **Domänenschicht**:
   - Domänenmodell mit Entitäten und Value Objects
   - Geschäftsregeln und -logik

```
┌─────────────────────────────────────────────────────────┐
│                      Backend                            │
│                                                         │
│  ┌─────────────────┐                                    │
│  │  REST-Controller│                                    │
│  │  (Präsentation) │                                    │
│  └────────┬────────┘                                    │
│           │                                             │
│           ▼                                             │
│  ┌─────────────────┐                                    │
│  │    Services     │                                    │
│  │   (Anwendung)   │                                    │
│  └────────┬────────┘                                    │
│           │                                             │
│           ▼                                             │
│  ┌─────────────────┐        ┌─────────────────┐        │
│  │   Repositories  │        │  Domänenmodell  │        │
│  │  (Datenzugriff) │◄──────►│    (Domäne)     │        │
│  └────────┬────────┘        └─────────────────┘        │
│           │                                             │
└───────────┼─────────────────────────────────────────────┘
            │
            ▼
┌─────────────────┐
│                 │
│   Datenbank     │
│      (H2)       │
│                 │
└─────────────────┘
```

### Frontend-Architektur

Das Frontend folgt einer komponentenbasierten Architektur mit React:

1. **Präsentationskomponenten**:
   - UI-Komponenten für die Darstellung (z.B. Buttons, Formulare, Karten)
   - Styling mit Material-UI

2. **Container-Komponenten**:
   - Verwaltung des Zustands und der Logik
   - Kommunikation mit dem Backend über Axios

3. **Routing**:
   - Navigation zwischen verschiedenen Ansichten
   - Implementiert mit React Router

4. **Services**:
   - API-Clients für die Kommunikation mit dem Backend
   - Datenverarbeitung und -transformation

```
┌─────────────────────────────────────────────────────────┐
│                      Frontend                           │
│                                                         │
│  ┌─────────────────┐        ┌─────────────────┐        │
│  │     Routing     │        │     Services    │        │
│  │  (React Router) │        │  (API-Clients)  │        │
│  └─────────────────┘        └────────┬────────┘        │
│                                      │                  │
│                                      │ HTTP/REST        │
│                                      ▼                  │
│  ┌─────────────────┐        ┌─────────────────┐        │
│  │    Container    │        │  Präsentations- │        │
│  │   Komponenten   │◄──────►│   Komponenten   │        │
│  │  (mit Zustand)  │        │  (ohne Zustand) │        │
│  └─────────────────┘        └─────────────────┘        │
│                                                         │
└─────────────────────────────────────────────────────────┘
```

### Angewendete Architekturprinzipien

1. **Separation of Concerns (SoC)**:
   - Klare Trennung von Verantwortlichkeiten zwischen verschiedenen Komponenten und Schichten
   - Jede Komponente hat einen klar definierten Zweck und Aufgabenbereich

2. **SOLID-Prinzipien**:
   - Single Responsibility Principle (SRP): Jede Klasse hat nur eine Verantwortung
   - Open/Closed Principle (OCP): Komponenten sind offen für Erweiterung, aber geschlossen für Modifikation
   - Dependency Inversion Principle (DIP): Abhängigkeit von Abstraktionen statt konkreten Implementierungen

3. **Dependency Injection**:
   - Verwendung von Spring's Dependency Injection für lose Kopplung
   - Verbesserte Testbarkeit durch die Möglichkeit, Abhängigkeiten zu mocken

4. **Repository-Pattern**:
   - Abstraktion des Datenzugriffs
   - Zentralisierte Datenzugriffslogik
   - Verbesserte Testbarkeit

## Datenmodell für den MVP

Für den MVP werden wir ein einfaches, aber erweiterungsfähiges Datenmodell implementieren, das die grundlegenden Entitäten und ihre Beziehungen abbildet. Das Datenmodell konzentriert sich auf die Kernentitäten des Flashcards-Systems: Karteikarten (Cards) und Decks.

### Entitäten

#### 1. Card (Karteikarte)

Die Karteikarte ist die grundlegende Lerneinheit im System und enthält eine Frage (Vorderseite) und eine Antwort (Rückseite).

```java
@Entity
@Table(name = "cards")
public class Card {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    
    @Column(nullable = false)
    private String front;
    
    @Column(nullable = false)
    private String back;
    
    @ElementCollection
    private List<String> tags;
    
    @Column(name = "created_at")
    private LocalDateTime createdAt;
    
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
    
    // Getter und Setter
}
```

#### 2. Deck

Ein Deck ist eine Sammlung von Karteikarten zu einem bestimmten Thema.

```java
@Entity
@Table(name = "decks")
public class Deck {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    
    @Column(nullable = false)
    private String name;
    
    private String description;
    
    @ElementCollection
    private List<String> tags;
    
    @Column(name = "created_at")
    private LocalDateTime createdAt;
    
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
    
    // Getter und Setter
}
```

#### 3. DeckCard (Verknüpfungstabelle)

Diese Entität stellt die Many-to-Many-Beziehung zwischen Decks und Cards dar.

```java
@Entity
@Table(name = "deck_cards")
public class DeckCard {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    
    @ManyToOne
    @JoinColumn(name = "deck_id", nullable = false)
    private Deck deck;
    
    @ManyToOne
    @JoinColumn(name = "card_id", nullable = false)
    private Card card;
    
    @Column(name = "added_at")
    private LocalDateTime addedAt;
    
    // Getter und Setter
}
```

### Datenbankschema

Das Datenbankschema für den MVP besteht aus folgenden Tabellen:

#### 1. CARDS

| Spalte      | Typ           | Beschreibung                     |
|-------------|---------------|---------------------------------|
| id          | VARCHAR(36)   | Primärschlüssel                 |
| front       | TEXT          | Vorderseite der Karteikarte     |
| back        | TEXT          | Rückseite der Karteikarte       |
| created_at  | TIMESTAMP     | Erstellungszeitpunkt            |
| updated_at  | TIMESTAMP     | Letzter Aktualisierungszeitpunkt|

#### 2. CARD_TAGS

| Spalte      | Typ           | Beschreibung                     |
|-------------|---------------|---------------------------------|
| card_id     | VARCHAR(36)   | Fremdschlüssel zu CARDS         |
| tag         | VARCHAR(255)  | Tag-Wert                        |

#### 3. DECKS

| Spalte      | Typ           | Beschreibung                     |
|-------------|---------------|---------------------------------|
| id          | VARCHAR(36)   | Primärschlüssel                 |
| name        | VARCHAR(255)  | Name des Decks                  |
| description | TEXT          | Beschreibung des Decks          |
| created_at  | TIMESTAMP     | Erstellungszeitpunkt            |
| updated_at  | TIMESTAMP     | Letzter Aktualisierungszeitpunkt|

#### 4. DECK_TAGS

| Spalte      | Typ           | Beschreibung                     |
|-------------|---------------|---------------------------------|
| deck_id     | VARCHAR(36)   | Fremdschlüssel zu DECKS         |
| tag         | VARCHAR(255)  | Tag-Wert                        |

#### 5. DECK_CARDS

| Spalte      | Typ           | Beschreibung                     |
|-------------|---------------|---------------------------------|
| id          | VARCHAR(36)   | Primärschlüssel                 |
| deck_id     | VARCHAR(36)   | Fremdschlüssel zu DECKS         |
| card_id     | VARCHAR(36)   | Fremdschlüssel zu CARDS         |
| added_at    | TIMESTAMP     | Zeitpunkt des Hinzufügens       |

### ER-Diagramm

```
┌─────────────┐       ┌─────────────┐       ┌─────────────┐
│             │       │             │       │             │
│    CARDS    │◄──────┤  DECK_CARDS │──────►│    DECKS    │
│             │       │             │       │             │
└──────┬──────┘       └─────────────┘       └──────┬──────┘
       │                                           │
       │                                           │
       ▼                                           ▼
┌─────────────┐                            ┌─────────────┐
│             │                            │             │
│  CARD_TAGS  │                            │  DECK_TAGS  │
│             │                            │             │
└─────────────┘                            └─────────────┘
```

### DTOs (Data Transfer Objects)

Für die Kommunikation zwischen Frontend und Backend werden folgende DTOs verwendet:

#### CardDTO

```java
public class CardDTO {
    private String id;
    private String front;
    private String back;
    private List<String> tags;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    
    // Getter und Setter
}
```

#### DeckDTO

```java
public class DeckDTO {
    private String id;
    private String name;
    private String description;
    private List<String> tags;
    private int cardCount;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    
    // Getter und Setter
}
```

#### DeckWithCardsDTO

```java
public class DeckWithCardsDTO {
    private String id;
    private String name;
    private String description;
    private List<String> tags;
    private List<CardDTO> cards;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    
    // Getter und Setter
}
```

### Erweiterbarkeit des Datenmodells

Das Datenmodell ist bewusst einfach gehalten, um den Anforderungen des MVP zu entsprechen, bietet aber gleichzeitig eine solide Grundlage für zukünftige Erweiterungen:

1. **Lernalgorithmus**: In zukünftigen Versionen können Felder wie `difficulty`, `nextReviewDate` und `reviewHistory` zur Card-Entität hinzugefügt werden, um den Spaced-Repetition-Algorithmus zu unterstützen.

2. **Medieninhalte**: Das Modell kann um eine `MediaItem`-Entität erweitert werden, um Bilder, Audio und Formeln zu unterstützen.

3. **Benutzerprofile**: Wenn die Anwendung in Zukunft mehrere Benutzer unterstützen soll, kann eine `User`-Entität hinzugefügt werden.

## API-Design für den MVP

Für den MVP implementieren wir eine REST-API, die die grundlegenden CRUD-Operationen (Create, Read, Update, Delete) für Karteikarten und Decks ermöglicht. Die API folgt den REST-Prinzipien und verwendet JSON als Datenformat.

### API-Endpunkte

#### 1. Karteikarten-Management

| HTTP-Methode | Endpunkt | Beschreibung | Request-Body | Response |
|--------------|----------|--------------|--------------|----------|
| GET | `/api/v1/cards` | Alle Karteikarten abrufen | - | Array von CardDTO |
| GET | `/api/v1/cards/{id}` | Eine spezifische Karteikarte abrufen | - | CardDTO |
| POST | `/api/v1/cards` | Neue Karteikarte erstellen | CardDTO (ohne id) | CardDTO (mit id) |
| PUT | `/api/v1/cards/{id}` | Karteikarte aktualisieren | CardDTO | CardDTO |
| DELETE | `/api/v1/cards/{id}` | Karteikarte löschen | - | 204 No Content |

#### 2. Deck-Management

| HTTP-Methode | Endpunkt | Beschreibung | Request-Body | Response |
|--------------|----------|--------------|--------------|----------|
| GET | `/api/v1/decks` | Alle Decks abrufen | - | Array von DeckDTO |
| GET | `/api/v1/decks/{id}` | Ein spezifisches Deck abrufen | - | DeckDTO |
| GET | `/api/v1/decks/{id}/cards` | Alle Karteikarten eines Decks abrufen | - | Array von CardDTO |
| POST | `/api/v1/decks` | Neues Deck erstellen | DeckDTO (ohne id) | DeckDTO (mit id) |
| PUT | `/api/v1/decks/{id}` | Deck aktualisieren | DeckDTO | DeckDTO |
| DELETE | `/api/v1/decks/{id}` | Deck löschen | - | 204 No Content |

#### 3. Karteikarten zu Decks hinzufügen/entfernen

| HTTP-Methode | Endpunkt | Beschreibung | Request-Body | Response |
|--------------|----------|--------------|--------------|----------|
| POST | `/api/v1/decks/{deckId}/cards` | Karteikarte zu Deck hinzufügen | CardDTO oder card_id | 204 No Content |
| DELETE | `/api/v1/decks/{deckId}/cards/{cardId}` | Karteikarte aus Deck entfernen | - | 204 No Content |

### Datenstrukturen

Die API verwendet die im Datenmodell definierten DTOs für die Kommunikation:

#### CardDTO (für Anfragen und Antworten)

```json
{
  "id": "string",
  "front": "string",
  "back": "string",
  "tags": ["string"],
  "createdAt": "2023-01-01T12:00:00Z",
  "updatedAt": "2023-01-01T12:00:00Z"
}
```

#### DeckDTO (für Anfragen und Antworten)

```json
{
  "id": "string",
  "name": "string",
  "description": "string",
  "tags": ["string"],
  "cardCount": 0,
  "createdAt": "2023-01-01T12:00:00Z",
  "updatedAt": "2023-01-01T12:00:00Z"
}
```

#### DeckWithCardsDTO (für Antworten)

```json
{
  "id": "string",
  "name": "string",
  "description": "string",
  "tags": ["string"],
  "cards": [
    {
      "id": "string",
      "front": "string",
      "back": "string",
      "tags": ["string"],
      "createdAt": "2023-01-01T12:00:00Z",
      "updatedAt": "2023-01-01T12:00:00Z"
    }
  ],
  "createdAt": "2023-01-01T12:00:00Z",
  "updatedAt": "2023-01-01T12:00:00Z"
}
```

### Fehlerbehandlung

Die API verwendet standardmäßige HTTP-Statuscodes zur Signalisierung von Erfolg oder Fehlern:

- 200 OK: Erfolgreiche Anfrage
- 201 Created: Ressource erfolgreich erstellt
- 204 No Content: Erfolgreiche Anfrage ohne Rückgabewert
- 400 Bad Request: Ungültige Anfrage (z.B. fehlerhafte Parameter)
- 404 Not Found: Ressource nicht gefunden
- 500 Internal Server Error: Serverfehler

Fehlermeldungen werden im folgenden Format zurückgegeben:

```json
{
  "status": 400,
  "message": "Validation failed",
  "timestamp": "2023-01-01T12:00:00Z",
  "details": [
    "Front field is required",
    "Back field is required"
  ]
}
```

### Implementierung im Backend

Die API-Endpunkte werden im Backend durch Spring Boot REST-Controller implementiert:

#### CardController

```java
@RestController
@RequestMapping("/api/v1/cards")
public class CardController {
    
    private final CardService cardService;
    
    @Autowired
    public CardController(CardService cardService) {
        this.cardService = cardService;
    }
    
    @GetMapping
    public ResponseEntity<List<CardDTO>> getAllCards() {
        return ResponseEntity.ok(cardService.getAllCards());
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<CardDTO> getCardById(@PathVariable String id) {
        return ResponseEntity.ok(cardService.getCardById(id));
    }
    
    @PostMapping
    public ResponseEntity<CardDTO> createCard(@RequestBody @Valid CardDTO cardDTO) {
        return ResponseEntity.status(HttpStatus.CREATED).body(cardService.createCard(cardDTO));
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<CardDTO> updateCard(@PathVariable String id, @RequestBody @Valid CardDTO cardDTO) {
        return ResponseEntity.ok(cardService.updateCard(id, cardDTO));
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCard(@PathVariable String id) {
        cardService.deleteCard(id);
        return ResponseEntity.noContent().build();
    }
}
```

#### DeckController

```java
@RestController
@RequestMapping("/api/v1/decks")
public class DeckController {
    
    private final DeckService deckService;
    
    @Autowired
    public DeckController(DeckService deckService) {
        this.deckService = deckService;
    }
    
    @GetMapping
    public ResponseEntity<List<DeckDTO>> getAllDecks() {
        return ResponseEntity.ok(deckService.getAllDecks());
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<DeckDTO> getDeckById(@PathVariable String id) {
        return ResponseEntity.ok(deckService.getDeckById(id));
    }
    
    @GetMapping("/{id}/cards")
    public ResponseEntity<List<CardDTO>> getCardsByDeckId(@PathVariable String id) {
        return ResponseEntity.ok(deckService.getCardsByDeckId(id));
    }
    
    @PostMapping
    public ResponseEntity<DeckDTO> createDeck(@RequestBody @Valid DeckDTO deckDTO) {
        return ResponseEntity.status(HttpStatus.CREATED).body(deckService.createDeck(deckDTO));
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<DeckDTO> updateDeck(@PathVariable String id, @RequestBody @Valid DeckDTO deckDTO) {
        return ResponseEntity.ok(deckService.updateDeck(id, deckDTO));
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDeck(@PathVariable String id) {
        deckService.deleteDeck(id);
        return ResponseEntity.noContent().build();
    }
    
    @PostMapping("/{deckId}/cards")
    public ResponseEntity<Void> addCardToDeck(@PathVariable String deckId, @RequestBody Map<String, String> payload) {
        String cardId = payload.get("cardId");
        deckService.addCardToDeck(deckId, cardId);
        return ResponseEntity.noContent().build();
    }
    
    @DeleteMapping("/{deckId}/cards/{cardId}")
    public ResponseEntity<Void> removeCardFromDeck(@PathVariable String deckId, @PathVariable String cardId) {
        deckService.removeCardFromDeck(deckId, cardId);
        return ResponseEntity.noContent().build();
    }
}
```

### Validierung

Die API implementiert Validierung für alle eingehenden Daten:

- Pflichtfelder (z.B. front und back für Karteikarten, name für Decks)
- Formatvalidierung (z.B. gültige UUIDs für IDs)
- Geschäftsregeln (z.B. keine doppelten Karteikarten in einem Deck)

Die Validierung erfolgt sowohl auf API-Ebene (mit Bean Validation) als auch auf Service-Ebene (für komplexere Geschäftsregeln).

## Frontend-Komponenten für den MVP

Das Frontend des MVP wird mit React.js, TypeScript und Material-UI implementiert. Es bietet eine intuitive Benutzeroberfläche für die Verwaltung von Karteikarten und Decks. Im Folgenden werden die wichtigsten Komponenten und ihre Funktionen beschrieben.

### Komponentenstruktur

Die Frontend-Anwendung ist in folgende Hauptkomponenten gegliedert:

1. **App-Komponente**: Die Wurzelkomponente der Anwendung, die das Routing und das Layout verwaltet.
2. **Layout-Komponenten**: Komponenten für das grundlegende Layout der Anwendung (Header, Navigation, Footer).
3. **Seiten-Komponenten**: Komponenten für die verschiedenen Ansichten der Anwendung.
4. **Funktionale Komponenten**: Wiederverwendbare Komponenten für spezifische Funktionen.
5. **Service-Komponenten**: Komponenten für die Kommunikation mit dem Backend.

### Detaillierte Komponentenbeschreibung

#### 1. App-Komponente

```tsx
// App.tsx
import React from 'react';
import { BrowserRouter as Router, Routes, Route } from 'react-router-dom';
import { ThemeProvider, CssBaseline } from '@mui/material';
import theme from './theme';
import Layout from './components/layout/Layout';
import HomePage from './pages/HomePage';
import DeckListPage from './pages/DeckListPage';
import DeckDetailPage from './pages/DeckDetailPage';
import CardListPage from './pages/CardListPage';
import CardFormPage from './pages/CardFormPage';
import DeckFormPage from './pages/DeckFormPage';

const App: React

```

## Implementierungsplan für den MVP

Der Implementierungsplan gliedert die Entwicklung des MVP in logische und aufeinander aufbauende Schritte.

### 1. Projekt-Setup

*   **Backend-Projektinitialisierung**:
    *   Erstellung eines neuen Spring Boot-Projekts mit Java 21 und Gradle.
    *   Konfiguration der grundlegenden Projektstruktur, Abhängigkeiten (Spring Web, Spring Data JPA, H2 Database, Lombok) und Build-Skripte.
*   **Frontend-Projektinitialisierung**:
    *   Erstellung eines neuen React-Projekts mit TypeScript und Vite (oder Create React App).
    *   Einrichtung der grundlegenden Projektstruktur, Abhängigkeiten (React Router, Material-UI, Axios) und Konfigurationsdateien.
*   **Versionskontrolle**: Fortführung des aktuellen Git-Repositories

### 2. Backend-Entwicklung

*   **Datenmodellierung und JPA-Konfiguration**:
    *   Definition der `Card` und `Deck` Entitäten mit den entsprechenden Feldern und Annotationen.
    *   Erstellung der `DeckCard`-Entität zur Abbildung der Many-to-Many-Beziehung.
    *   Konfiguration der H2-Datenbank und JPA/Hibernate-Einstellungen in `application.properties`.
*   **Repository-Implementierung**:
    *   Erstellung von `CardRepository` und `DeckRepository` mit Spring Data JPA-Interfaces.
    *   Implementierung von Methoden für CRUD-Operationen und spezifische Abfragen (z.B. Karten eines Decks abrufen).
*   **Service-Implementierung**:
    *   Erstellung von `CardService` und `DeckService` zur Kapselung der Geschäftslogik.
    *   Implementierung von Methoden für die Erstellung, Abfrage, Aktualisierung und Löschung von Karten und Decks.
    *   Implementierung der Logik zum Hinzufügen und Entfernen von Karten zu/aus Decks.
*   **Controller-Implementierung**:
    *   Erstellung von `CardController` und `DeckController` mit REST-Endpunkten.
    *   Implementierung der API-Endpunkte für CRUD-Operationen und die Verwaltung von Karten in Decks.
    *   Integration von DTOs (Data Transfer Objects) für die Datenübertragung.
*   **Validierung**:
    *   Implementierung von Bean Validation für DTOs (z.B. `@NotNull`, `@Size`).
    *   Hinzufügen von Service-Level-Validierungen für Geschäftsregeln.

### 3. Frontend-Entwicklung

*   **Projektstruktur und Basis-Layout**:
    *   Einrichtung der Ordnerstruktur für Komponenten, Seiten, Services und Themes.
    *   Implementierung von Basis-Layout-Komponenten (z.B. `Layout`, `Header`, `Navigation`) mit Material-UI.
*   **Routing-Konfiguration**:
    *   Einrichtung von Routen mit React Router für die verschiedenen Seiten (z.B. Home, Deck-Liste, Deck-Detail, Karten-Formular).
*   **API-Service-Schicht**:
    *   Erstellung eines Axios-basierten API-Clients zur Kommunikation mit dem Backend.
    *   Definition von Funktionen für alle Backend-API-Aufrufe (GET, POST, PUT, DELETE für Karten und Decks).
*   **Komponentenentwicklung**:
    *   `DeckListPage`: Anzeige einer Liste aller Decks, Abruf über API-Service.
    *   `DeckFormPage`: Formular zum Erstellen und Bearbeiten von Decks, mit Validierung und API-Integration.
    *   `CardListPage` (oder integriert in `DeckDetailPage`): Anzeige der Karten eines spezifischen Decks.
    *   `CardFormPage`: Formular zum Erstellen und Bearbeiten von Karten, mit Validierung und API-Integration.
    *   Implementierung der Logik zum Hinzufügen/Entfernen von Karten zu/aus Decks über die UI.
*   **State Management**:
    *   Verwendung von React Hooks (`useState`, `useContext`) zur Verwaltung des lokalen und globalen Zustands.

### 4. Integration und Lokales Deployment

*   **End-to-End-Integration**:
    *   Sicherstellung, dass Frontend und Backend korrekt miteinander kommunizieren.
    *   Testen der vollständigen User Flows (z.B. Deck erstellen -> Karte hinzufügen -> Deck anzeigen).
*   **Lokales Deployment**:
    *   Starten des Spring Boot-Backends.
    *   Starten der React-Entwicklungsumgebung.
    *   Testen der Anwendung in einer lokalen Umgebung.

### 5. Code-Qualität und Refactoring

*   **Code-Reviews**: Durchführung von Peer-Code-Reviews für Backend- und Frontend-Code.
*   **Statische Code-Analyse**: Konfiguration und Ausführung von ESLint (Frontend) und Checkstyle (Backend) zur Sicherstellung von Code-Qualitätsstandards.
*   **Refactoring**: Basierend auf Code-Reviews und Analysen werden notwendige Refactorings durchgeführt, um die Lesbarkeit, Wartbarkeit und Leistung zu verbessern.

## Qualitätssicherungsmaßnahmen für den MVP

Um die Qualität und Zuverlässigkeit des MVP sicherzustellen, werden folgende Qualitätssicherungsmaßnahmen implementiert:

### 1. Backend-Tests

*   **Unit-Tests**:
    *   Verwendung von JUnit 5 und Mockito zur Isolierung und Überprüfung einzelner Komponenten (Services, Controller).
    *   Testen der Geschäftslogik in den Services, um Korrektheit und Vollständigkeit sicherzustellen.
    *   Testen der API-Endpunkte in den Controllern, um sicherzustellen, dass sie korrekt auf Anfragen reagieren und die erwarteten Daten zurückgeben.
*   **Integrationstests**:
    *   Verwendung von Spring Boot Test (`@SpringBootTest`) zur Überprüfung der Interaktion zwischen verschiedenen Schichten (Controller, Service, Repository).
    *   Testen der Datenbankinteraktionen mit der H2-Datenbank, um die Datenpersistenz und -abfrage zu validieren.
    *   Testen der gesamten API-Endpunkte, um sicherzustellen, dass sie wie erwartet funktionieren.
*   **API-Vertragstests (Optional für MVP)**:
    *   Falls erforderlich, Implementierung von Tests (z.B. mit Spring Cloud Contract), um sicherzustellen, dass die API-Verträge zwischen Frontend und Backend eingehalten werden.

### 2. Frontend-Tests

*   **Unit-Tests**:
    *   Verwendung von Jest und React Testing Library zur Überprüfung einzelner React-Komponenten und Hilfsfunktionen.
    *   Testen der UI-Komponenten auf korrekte Darstellung und Benutzerinteraktion.
    *   Testen von Utility-Funktionen und Hooks.
*   **Integrationstests**:
    *   Testen der Interaktion zwischen mehreren Komponenten und der Zustandsverwaltung (z.B. wie sich Änderungen in einer Komponente auf andere auswirken).
    *   Testen der API-Aufrufe und der Datenverarbeitung im Frontend.
*   **End-to-End (E2E)-Tests**:
    *   Verwendung von Tools wie Cypress oder Playwright zur Simulation von Benutzerflüssen durch die gesamte Anwendung.
    *   Testen von kritischen User Journeys, z.B. Erstellen eines Decks, Hinzufügen einer Karte, Anzeigen der Karten eines Decks.

### 3. Code-Qualität

*   **Statische Code-Analyse**:
    *   Konfiguration und Ausführung von ESLint für das Frontend zur Sicherstellung von konsistentem Code-Stil und zur Erkennung potenzieller Fehler.
    *   Konfiguration und Ausführung von Checkstyle für das Backend zur Durchsetzung von Java-Code-Konventionen.
*   **Code-Reviews**:
    *   Regelmäßige Code-Reviews durch Teammitglieder, um die Codequalität zu verbessern, Wissen zu teilen und potenzielle Probleme frühzeitig zu erkennen.

### 4. Manuelle Tests

*   **Benutzerakzeptanztests (UAT)**:
    *   Durchführung manueller Tests durch Stakeholder oder Tester, um sicherzustellen, dass die Anwendung die definierten MVP-Anforderungen erfüllt.
    *   Überprüfung der Funktionalität, Benutzerfreundlichkeit und des Gesamtdesigns.
*   **Exploratives Testen**:
    *   Ad-hoc-Tests, um unerwartete Fehler oder Probleme zu finden, die durch automatisierte Tests möglicherweise nicht abgedeckt werden.

### 5. Deployment-Tests

*   **Lokale Deployment-Tests**:
    *   Testen des Build- und Deployment-Prozesses in einer lokalen Umgebung, um sicherzustellen, dass die Anwendung korrekt gestartet und ausgeführt werden kann.

Diese Maßnahmen stellen sicher, dass der MVP robust, zuverlässig und gemäß den Anforderungen entwickelt wird.