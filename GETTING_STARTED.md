# Erste Schritte mit dem Flashcards-System

Dieses Dokument enthält Anweisungen, um das Flashcards-System zu starten und zu verwenden.

## Voraussetzungen

Stellen Sie sicher, dass Sie folgende Software installiert haben:

- Java Development Kit (JDK) 21 oder höher
- Node.js (v16 oder höher)
- npm (wird mit Node.js installiert)
- Git (optional)

## Backend starten

1. Öffnen Sie ein Terminal-Fenster im Hauptverzeichnis des Projekts.

2. Führen Sie den folgenden Befehl aus, um das Backend zu starten:

```bash
./gradlew bootRun
```

Unter Windows:

```bash
gradlew bootRun
```

3. Das Backend wird auf Port 8080 gestartet. Sie können die H2-Konsole unter http://localhost:8080/h2-console aufrufen.

### Zugangsdaten für die H2-Datenbank:

- JDBC URL: jdbc:h2:mem:flashcarddb
- Benutzername: sa
- Passwort: password

## Frontend starten

1. Öffnen Sie ein neues Terminal-Fenster und wechseln Sie in das Frontend-Verzeichnis:

```bash
cd frontend
```

2. Installieren Sie die benötigten Abhängigkeiten (nur beim ersten Start notwendig):

```bash
npm install
```

3. Starten Sie den Frontend-Entwicklungsserver:

```bash
npm start
```

4. Das Frontend wird automatisch im Standardbrowser unter http://localhost:3000 geöffnet.

## Beide Komponenten mit einem Befehl starten

Unter Linux/macOS können Sie einfach den folgenden Befehl ausführen:

```bash
./start.sh
```

Unter Windows:

```bash
start.bat
```

## Erste Schritte mit der Anwendung

1. Öffnen Sie http://localhost:3000 in Ihrem Browser.

2. Auf der Startseite haben Sie folgende Optionen:
   - Decks verwalten: Hier können Sie alle vorhandenen Decks einsehen und neue Decks erstellen.
   - Karten erstellen: Hier können Sie neue Karteikarten erstellen und optional einem Deck hinzufügen.

3. Die Demo-Daten enthalten bereits einige Beispiel-Decks und Karten, die Sie erkunden können.

## API-Endpunkte

Das Backend stellt folgende REST-API-Endpunkte bereit:

### Karten:
- `GET /api/cards`: Alle Karten abrufen
- `GET /api/cards/{id}`: Eine bestimmte Karte abrufen
- `POST /api/cards`: Neue Karte erstellen
- `PUT /api/cards/{id}`: Karte aktualisieren
- `DELETE /api/cards/{id}`: Karte löschen

### Decks:
- `GET /api/decks`: Alle Decks abrufen
- `GET /api/decks/{id}`: Ein bestimmtes Deck abrufen
- `GET /api/decks/{id}/cards`: Ein Deck mit all seinen Karten abrufen
- `POST /api/decks`: Neues Deck erstellen
- `PUT /api/decks/{id}`: Deck aktualisieren
- `DELETE /api/decks/{id}`: Deck löschen
- `POST /api/decks/add-card`: Karte zu einem Deck hinzufügen
- `DELETE /api/decks/{deckId}/cards/{cardId}`: Karte aus einem Deck entfernen
