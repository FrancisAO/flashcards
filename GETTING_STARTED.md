# Erste Schritte mit dem Flashcards-System

Dieses Dokument enthält Anweisungen, um das Flashcards-System mit PostgreSQL-Datenbank zu starten und zu verwenden.

## Voraussetzungen

Stellen Sie sicher, dass Sie folgende Software installiert haben:

- Docker und Docker Compose
- Java Development Kit (JDK) 21 oder höher
- Node.js (v16 oder höher)
- npm (wird mit Node.js installiert)
- Git (optional)
- Betriebssystem: Windows mit Docker Daemon

## Einrichtung der PostgreSQL-Datenbank

Das Flashcards-System verwendet PostgreSQL als Datenbank, die in einem Docker-Container ausgeführt wird. Die Konfiguration erfolgt über Docker Compose und Umgebungsvariablen.

### Docker-Compose-Konfiguration

Die `docker-compose.yml`-Datei definiert den PostgreSQL-Dienst mit folgenden Eigenschaften:

```yaml
services:
  postgres:
    image: postgres:16.9
    container_name: flashcards-postgres
    restart: unless-stopped
    environment:
      - POSTGRES_USER=${POSTGRES_USER}
      - POSTGRES_PASSWORD=${POSTGRES_PASSWORD}
      - POSTGRES_DB=${POSTGRES_DB}
    ports:
      - "${POSTGRES_PORT}:5432"
    volumes:
      - ${POSTGRES_DATA_PATH}:/var/lib/postgresql/data
    healthcheck:
      test: ["CMD-SHELL", "pg_isready -U ${POSTGRES_USER} -d ${POSTGRES_DB}"]
      interval: 10s
      timeout: 5s
      retries: 5
      start_period: 10s
    networks:
      - flashcards-network
```

Diese Konfiguration:
- Verwendet das offizielle PostgreSQL 16.9 Docker-Image
- Benennt den Container als `flashcards-postgres`
- Konfiguriert die Datenbank mit Umgebungsvariablen aus der `.env`-Datei
- Mappt den PostgreSQL-Port auf den Host
- Speichert die Datenbankdaten in einem persistenten Volume
- Definiert eine Healthcheck-Konfiguration zur Überprüfung der Datenbankverbindung
- Verwendet ein eigenes Netzwerk für die Kommunikation

### Erforderliche Verzeichnisse

Die folgenden Verzeichnisse werden für die Anwendung benötigt und automatisch durch die Startskripte erstellt:

- `%USERPROFILE%\flashcards\postgres-data`: Speicherort für die PostgreSQL-Datenbankdaten
- `%USERPROFILE%\flashcards\backups`: Speicherort für Datenbank-Backups
- `%USERPROFILE%\flashcards`: Speicherort für die externe Konfigurationsdatei

### Umgebungsvariablen in der .env-Datei

Die `.env`-Datei enthält die Konfiguration für die PostgreSQL-Datenbank:

```
# PostgreSQL Konfiguration
POSTGRES_USER=flashcards_user
POSTGRES_PASSWORD=changeme
POSTGRES_DB=flashcards
POSTGRES_HOST=localhost
POSTGRES_PORT=5432

# Pfade für Daten und Backups
POSTGRES_DATA_PATH=%USERPROFILE%\flashcards\postgres-data
BACKUP_PATH=%USERPROFILE%\flashcards\backups
```

**Wichtig**: Ändern Sie das Passwort (`POSTGRES_PASSWORD`) vor der Verwendung in einer Produktionsumgebung.

### Externe Konfigurationsdatei

Die Anwendung lädt Datenbank-Credentials aus einer externen Konfigurationsdatei, die unter `~/flashcards/application.properties` gespeichert wird. Diese Datei wird automatisch aus der Vorlage `src/main/resources/example-external-config.properties` erstellt, wenn sie nicht existiert.

Inhalt der externen Konfigurationsdatei:

```properties
# PostgreSQL Credentials
spring.datasource.username=flashcards_user
spring.datasource.password=changeme
```

**Wichtig**: Passen Sie die Credentials an Ihre Umgebung an und stellen Sie sicher, dass diese Datei nicht im Repository gespeichert wird.

## Verwendung der Skripte

### Startskript (start.sh/start.bat)

Das Startskript automatisiert den gesamten Prozess zum Starten der Anwendung:

1. Lädt Umgebungsvariablen aus der `.env`-Datei
2. Erstellt die erforderlichen Verzeichnisse, falls sie nicht existieren
3. Erstellt die externe Konfigurationsdatei, falls sie nicht existiert
4. Startet den PostgreSQL-Docker-Container
5. Wartet, bis der PostgreSQL-Container bereit ist
6. Startet das Spring Boot-Backend
7. Startet das React-Frontend

Unter Linux/macOS:

```bash
./start.sh
```

Unter Windows (powershell):

```bash
$env:ENV_FILE_PATH="C:\Pfad\zu\meiner\env-datei.env"
start.bat
```
oder nur
```
start.bat
```
Im unteren Fall wird eine lokale .env-Datei gesucht, in der die relevanten Daten für die Verbindung zur Postgres enthalten sind (siehe Abschnitt Umgebungsvariablen).
### Backup-Skript (backup.sh)

Das Backup-Skript erstellt ein Backup der PostgreSQL-Datenbank:

1. Lädt Umgebungsvariablen aus der `.env`-Datei
2. Erstellt das Backup-Verzeichnis, falls es nicht existiert
3. Erstellt einen Zeitstempel für den Backup-Dateinamen
4. Überprüft, ob der PostgreSQL-Container läuft
5. Erstellt das Backup mit `pg_dump`
6. Zeigt eine Liste der vorhandenen Backups an

Verwendung:

```bash
./backup.sh
```

Das Backup wird im konfigurierten Backup-Verzeichnis (`BACKUP_PATH` in der `.env`-Datei) mit einem Zeitstempel im Dateinamen gespeichert.

### Restore-Skript (restore.sh)

Das Restore-Skript stellt ein Backup der PostgreSQL-Datenbank wieder her:

1. Lädt Umgebungsvariablen aus der `.env`-Datei
2. Zeigt eine Liste der verfügbaren Backups an, wenn kein Backup-Pfad angegeben wurde
3. Überprüft, ob der PostgreSQL-Container läuft, und startet ihn bei Bedarf
4. Löscht die bestehende Datenbank und erstellt sie neu
5. Stellt das Backup wieder her

Verwendung ohne Angabe eines Backup-Pfads (zeigt eine Liste der verfügbaren Backups an):

```bash
./restore.sh
```

Verwendung mit Angabe eines Backup-Pfads:

```bash
./restore.sh /pfad/zum/backup.sql
```

**Achtung**: Die Wiederherstellung überschreibt alle aktuellen Daten in der Datenbank!

## Konfiguration

### Umgebungsvariablen

Die Anwendung kann über Umgebungsvariablen in der `.env`-Datei konfiguriert werden:

| Variable | Beschreibung | Standardwert |
|----------|--------------|--------------|
| POSTGRES_USER | PostgreSQL-Benutzername | flashcards_user |
| POSTGRES_PASSWORD | PostgreSQL-Passwort | changeme |
| POSTGRES_DB | PostgreSQL-Datenbankname | flashcards |
| POSTGRES_HOST | PostgreSQL-Host | localhost |
| POSTGRES_PORT | PostgreSQL-Port | 5432 |
| POSTGRES_DATA_PATH | Pfad für PostgreSQL-Daten | ~/flashcards/postgres-data |
| BACKUP_PATH | Pfad für Datenbank-Backups | ~/flashcards/backups |

### Externe Konfigurationsdatei

Die externe Konfigurationsdatei (`~/flashcards/application.properties`) enthält die Datenbank-Credentials für die Spring Boot-Anwendung:

| Eigenschaft | Beschreibung | Standardwert |
|-------------|--------------|--------------|
| spring.datasource.username | Datenbank-Benutzername | postgres |
| spring.datasource.password | Datenbank-Passwort | postgres |

### Sicherheitshinweise

- Ändern Sie die Standard-Passwörter in der `.env`-Datei und der externen Konfigurationsdatei.
- Speichern Sie keine sensiblen Daten im Repository.
- Die externe Konfigurationsdatei sollte in `.gitignore` aufgenommen werden.
- Verwenden Sie in einer Produktionsumgebung sichere Passwörter und beschränken Sie den Datenbankzugriff.

## Fehlerbehebung

### Häufige Probleme und Lösungen

#### Docker-Container startet nicht

Problem: Der PostgreSQL-Docker-Container startet nicht oder ist nicht erreichbar.

Lösungen:
- Überprüfen Sie, ob Docker läuft: `docker ps`
- Überprüfen Sie die Docker-Logs: `docker logs flashcards-postgres`
- Stellen Sie sicher, dass der Port 5432 nicht bereits verwendet wird: `netstat -an | grep 5432`
- Starten Sie den Container manuell: `docker-compose up -d postgres`

#### Datenbankverbindung fehlgeschlagen

Problem: Die Anwendung kann keine Verbindung zur Datenbank herstellen.

Lösungen:
- Überprüfen Sie, ob der PostgreSQL-Container läuft: `docker ps | grep flashcards-postgres`
- Überprüfen Sie die Datenbank-Credentials in der externen Konfigurationsdatei
- Überprüfen Sie die Datenbankverbindung manuell: `docker exec -it flashcards-postgres psql -U flashcards_user -d flashcards`
- Überprüfen Sie die Spring Boot-Logs auf Verbindungsfehler

#### Verzeichnisse können nicht erstellt werden

Problem: Die Skripte können die erforderlichen Verzeichnisse nicht erstellen.

Lösungen:
- Überprüfen Sie die Berechtigungen im Home-Verzeichnis
- Erstellen Sie die Verzeichnisse manuell:
  ```bash
  mkdir -p ~/flashcards/postgres-data
  mkdir -p ~/flashcards/backups
  mkdir -p ~/flashcards
  ```

### Überprüfung der Datenbankverbindung

Sie können die Datenbankverbindung manuell überprüfen:

```bash
docker exec -it flashcards-postgres pg_isready -U flashcards_user -d flashcards
```

Oder verbinden Sie sich direkt mit der Datenbank:

```bash
docker exec -it flashcards-postgres psql -U flashcards_user -d flashcards
```

### Logs und Debugging

#### Docker-Logs

```bash
docker logs flashcards-postgres
```

#### Spring Boot-Logs

Die Spring Boot-Logs werden in der Konsole angezeigt, wenn Sie die Anwendung mit `./gradlew bootRun` starten.

Sie können das Log-Level in der `application.properties`-Datei anpassen:

```properties
logging.level.com.fao.flashcards=DEBUG
logging.level.org.springframework.web=INFO
logging.level.org.hibernate=INFO
logging.level.org.flywaydb=DEBUG
```

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
