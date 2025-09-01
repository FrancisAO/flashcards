# 📚 Flashcards-Anwendung - Projektübersicht

## 1. Projektübersicht

Die Flashcards-Anwendung ist eine moderne, KI-gestützte Lernplattform, die es Benutzern ermöglicht, digitale Karteikarten zu erstellen, zu verwalten und zu studieren. Die Anwendung nutzt fortschrittliche Technologien wie OCR (Optical Character Recognition) und künstliche Intelligenz zur automatischen Generierung von Lernkarten aus verschiedenen Dokumentformaten.

### Hauptfunktionen
- 📝 **Karteikarten-Management**: Erstellen, bearbeiten und organisieren von Lernkarten
- 📚 **Deck-Verwaltung**: Gruppierung von Karten in thematischen Decks
- 🤖 **KI-gestützte Kartengenerierung**: Automatische Erstellung von Lernkarten mittels AI
- 🔍 **OCR-Integration**: Texterkennung aus gescannten Dokumenten und Bildern
- 📄 **Dokumentenverarbeitung**: Unterstützung für PDF, Word und andere Formate
- 📊 **Studien-Features**: Fortschrittsverfolgung und Lernstatistiken
- 🎯 **Projekt-Management**: Organisation von Lernmaterialien in Projekten

## 2. Technologiestack

### Backend (Java/Spring)
- **Framework**: Spring Boot 3.5.0
- **Java Version**: Java 21
- **Build Tool**: Gradle
- **Datenbank**: PostgreSQL (Produktion), H2 (Entwicklung/Tests)
- **Migration**: Flyway
- **Dokumentenverarbeitung**: Apache PDFBox, Apache POI
- **JSON-Verarbeitung**: Jackson
- **Validation**: Spring Boot Validation
- **Testing**: JUnit 5, Spring Boot Test

### Frontend (React/TypeScript)
- **Framework**: React 18.2.0
- **Sprache**: TypeScript 4.9.5
- **UI-Library**: Material-UI (MUI) 5.14.0
- **Routing**: React Router DOM 6.14.2
- **HTTP-Client**: Axios 1.4.0
- **Build Tool**: React Scripts
- **Testing**: Jest, Testing Library

### Externe Services
- **KI-Integration**: OpenRouter API (Direct Integration)
- **OCR-Service**: Mistral OCR API
- **Dokumenten-Upload**: Multipart File Handling

## 3. Architektur

Das Projekt folgt der **Hexagonal Architecture (Ports & Adapters)** für saubere Schichtentrennung:

```
┌─────────────────┐    ┌─────────────────┐    ┌─────────────────┐
│   Frontend      │◄──►│   Backend API   │◄──►│  External APIs  │
│   (React/TS)    │    │   Controllers   │    │  (AI/OCR/Docs)  │
└─────────────────┘    └─────────────────┘    └─────────────────┘
                              │
                    ┌─────────────────┐
                    │   Domain Core   │
                    │   (Services)    │
                    └─────────────────┘
                              │
                    ┌─────────────────┐
                    │   Adapters      │
                    │ (DB/External)   │
                    └─────────────────┘
```

### Schichten
1. **API Layer** (`api/`): REST-Controller und DTOs
2. **Domain Layer** (`domain/`): Geschäftslogik und Services
3. **Adapter Layer** (`adapter/`): Externe Integrationen
4. **Configuration** (`config/`): Spring-Konfigurationen

## 4. Verzeichnisstruktur

```
flashcards/
├── 📁 src/main/java/com/fao/flashcards/
│   ├── 🎯 api/                      # REST API Layer
│   │   ├── controller/              # REST Controllers
│   │   ├── dto/                     # Data Transfer Objects
│   │   └── exception/               # Exception Handling
│   ├── 🏗️ domain/                   # Domain Layer (Core Business Logic)
│   │   ├── model/                   # Domain Entities
│   │   ├── repository/              # Repository Interfaces
│   │   ├── service/                 # Business Services
│   │   ├── port/                    # Hexagonal Ports
│   │   └── exception/               # Domain Exceptions
│   ├── 🔌 adapter/                  # Adapter Layer (External Integrations)
│   │   ├── ai/                      # AI Service Adapters
│   │   ├── ocr/                     # OCR Service Adapters
│   │   └── document/                # Document Processing
│   └── ⚙️ config/                   # Configuration Classes
├── 📁 frontend/                     # React Frontend
│   ├── src/
│   │   ├── components/              # React Components
│   │   ├── services/                # API Services
│   │   └── types/                   # TypeScript Types
│   └── public/                      # Static Assets
├── 📁 test-files/                   # Test Documents
├── 📁 doc/                          # Documentation
└── 📁 postgres-data/                # Database Files
```

## 5. Kernkomponenten

### Domain Services
- **[`CardService`](src/main/java/com/fao/flashcards/domain/service/CardService.java)**: Verwaltung von Lernkarten
- **[`DeckService`](src/main/java/com/fao/flashcards/domain/service/DeckService.java)**: Deck-Management und Card-Zuordnung
- **Weitere Services**: StudyService, ProjectService

### API Controllers
- **[`CardController`](src/main/java/com/fao/flashcards/api/controller/CardController.java)**: CRUD-Operationen für Karten
- **[`DeckController`](src/main/java/com/fao/flashcards/api/controller/DeckController.java)**: Deck-Management API
- **[`CardGenerationController`](src/main/java/com/fao/flashcards/api/controller/CardGenerationController.java)**: KI-gestützte Kartenerstellung
- **[`OCRController`](src/main/java/com/fao/flashcards/api/controller/OCRController.java)**: OCR-Verarbeitung
- **[`FileController`](src/main/java/com/fao/flashcards/api/controller/FileController.java)**: Datei-Upload/Download

### Domain Models
- **[`Card`](src/main/java/com/fao/flashcards/domain/model/)**: Einzelne Lernkarte (Frage/Antwort)
- **[`Deck`](src/main/java/com/fao/flashcards/domain/model/)**: Sammlung von Karten
- **[`DeckCard`](src/main/java/com/fao/flashcards/domain/model/DeckCard.java)**: Many-to-Many Beziehung
- **`Project`**: Übergeordnete Organisation
- **`StudySession`**: Lernsitzungen und Fortschritt

### Adapters
- **[`DirectOpenRouterAIAdapter`](src/main/java/com/fao/flashcards/adapter/ai/DirectOpenRouterAIAdapter.java)**: KI-Integration für Kartengenerierung
- **[`MistralOCRAdapter`](src/main/java/com/fao/flashcards/adapter/ocr/MistralOCRAdapter.java)**: OCR-Textextraktion
- **[`DocumentProcessingAdapter`](src/main/java/com/fao/flashcards/adapter/document/DocumentProcessingAdapter.java)**: Dokumentverarbeitung

## 6. API-Endpunkte

### Card Management
```http
GET    /api/cards              # Alle Karten abrufen
POST   /api/cards              # Neue Karte erstellen
GET    /api/cards/{id}         # Karte nach ID
PUT    /api/cards/{id}         # Karte aktualisieren
DELETE /api/cards/{id}         # Karte löschen
```

### Deck Management
```http
GET    /api/decks              # Alle Decks abrufen
POST   /api/decks              # Neues Deck erstellen
GET    /api/decks/{id}         # Deck mit Karten
PUT    /api/decks/{id}         # Deck aktualisieren
DELETE /api/decks/{id}         # Deck löschen
POST   /api/decks/{deckId}/cards/{cardId}  # Karte zu Deck hinzufügen
```

### KI & OCR
```http
POST   /api/generate/cards     # KI-Kartengenerierung
POST   /api/ocr/process        # OCR-Verarbeitung
POST   /api/files/upload       # Datei-Upload
```

### Study Features
```http
GET    /api/study/sessions     # Lernsitzungen
POST   /api/study/sessions     # Neue Sitzung starten
PUT    /api/study/progress     # Fortschritt aktualisieren
```

## 7. Datenbank-Design

### Kernentitäten
```sql
Card                           Deck
├── id (Long, PK)             ├── id (Long, PK)
├── question (String)         ├── name (String)
├── answer (String)           ├── description (String)
├── difficulty (Enum)         ├── color (String)
├── tags (String[])           ├── createdAt (LocalDateTime)
├── createdAt (LocalDateTime) └── updatedAt (LocalDateTime)
└── updatedAt (LocalDateTime)

DeckCard (Junction Table)      Project
├── id (Long, PK)             ├── id (Long, PK)
├── deck_id (Long, FK)        ├── name (String)
├── card_id (Long, FK)        ├── description (String)
├── position (Integer)        └── createdAt (LocalDateTime)
└── addedAt (LocalDateTime)
```

### Beziehungen
- **Card ↔ Deck**: Many-to-Many über `DeckCard`
- **Project → Deck**: One-to-Many
- **User → StudySession**: One-to-Many (zukünftig)

## 8. Besondere Features

### 🤖 KI-Integration
- **Service**: OpenRouter API
- **Funktionen**: 
  - Automatische Kartengenerierung aus Text
  - Antwort-Validierung und -Verbesserung
  - Schwierigkeitsgrad-Einschätzung
- **Adapter**: [`DirectOpenRouterAIAdapter`](src/main/java/com/fao/flashcards/adapter/ai/DirectOpenRouterAIAdapter.java)

### 🔍 OCR-Verarbeitung
- **Service**: Mistral OCR API
- **Funktionen**:
  - Texterkennung aus Bildern
  - PDF-Textextraktion
  - Structured JSON Response
- **Adapter**: [`MistralOCRAdapter`](src/main/java/com/fao/flashcards/adapter/ocr/MistralOCRAdapter.java)

### 📄 Dokumentenverarbeitung
- **Formate**: PDF, DOCX, DOC, TXT, Images
- **Libraries**: Apache PDFBox, Apache POI
- **Features**: 
  - Multi-Format-Support
  - Chunk-basierte Verarbeitung
  - Metadaten-Extraktion

### 🔒 Sicherheitsfeatures
- **CORS-Konfiguration**: [`CorsConfig`](src/main/java/com/fao/flashcards/config/CorsConfig.java)
- **Exception Handling**: [`GlobalExceptionHandler`](src/main/java/com/fao/flashcards/api/exception/GlobalExceptionHandler.java)
- **Input Validation**: Spring Boot Validation

## 9. Einstiegspunkte für Entwickler

### 🎯 Backend-Entwicklung
1. **Domain Services**: [`src/main/java/com/fao/flashcards/domain/service/`](src/main/java/com/fao/flashcards/domain/service/)
2. **API Controllers**: [`src/main/java/com/fao/flashcards/api/controller/`](src/main/java/com/fao/flashcards/api/controller/)
3. **Tests**: [`src/test/java/com/fao/flashcards/`](src/test/java/com/fao/flashcards/)

### 🖥️ Frontend-Entwicklung
1. **Components**: [`frontend/src/components/`](frontend/src/components/)
2. **Services**: [`frontend/src/services/`](frontend/src/services/)
3. **Types**: [`frontend/src/types/`](frontend/src/types/)

### 🔌 Neue Adapter
1. **Template**: Bestehende Adapter als Vorlage nutzen
2. **Ports**: Domain-Interfaces definieren
3. **Tests**: Adapter-spezifische Tests schreiben

### 📊 Datenbank-Änderungen
1. **Migrations**: [`src/main/resources/db/`](src/main/resources/db/)
2. **Entities**: Domain Models erweitern
3. **Repositories**: JPA Repository Interfaces

## 10. Quick-Start-Anleitung

### Voraussetzungen
- **Java 21** (OpenJDK empfohlen)
- **Node.js 18+** & npm
- **PostgreSQL 14+** (optional, H2 für lokale Entwicklung)
- **IDE**: VS Code

### Backend starten
```bash
# Repository klonen
git clone <repository-url>
cd flashcards

# Backend starten (Port 8080)
./gradlew bootRun

# Tests ausführen
./gradlew test
./gradlew unitTests  # Nur Unit Tests
```

### Frontend starten
```bash
# Frontend-Verzeichnis
cd frontend

# Dependencies installieren
npm install

# Development Server starten (Port 3000)
npm start

# Build für Produktion
npm run build
```

### Datenbank Setup
```bash
# H2 (automatisch, keine Konfiguration nötig)
# Öffne: http://localhost:8080/h2-console

# PostgreSQL (optional)
# Konfiguration in application.properties
# ./start.bat
```

### API Testing
```bash
# Backend-API testen
curl http://localhost:8080/api/cards
curl http://localhost:8080/api/decks

# Frontend erreichen
http://localhost:3000
```

### Entwicklungsumgebung
1. **Backend**: Spring Boot DevTools aktiviert für Hot Reload
2. **Frontend**: React Hot Reload automatisch aktiv
3. **Database**: H2 Console verfügbar unter `/h2-console`
4. **API Docs**: Swagger/OpenAPI kann integriert werden
5. **Logs**: Detaillierte Logging-Konfiguration in `application.properties` bzw. profil-spezfischen properties.

---

## 📝 Hinweise für Agenten

- **Architektur respektieren**: Hexagonal Architecture beibehalten
- **Tests schreiben**: Jede neue Funktionalität testen
- **Error Handling**: Globale Exception Handler nutzen
- **Documentation**: Code und API dokumentieren
- **Performance**: Lazy Loading und Pagination beachten
- **Security**: Input Validation und CORS konfigurieren

*Diese Übersicht wird regelmäßig aktualisiert. Version: 1.0 - Stand: August 2025*