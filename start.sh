#!/bin/bash

# Lade Umgebungsvariablen aus .env-Datei
if [ -f .env ]; then
  export $(grep -v '^#' .env | xargs)
else
  echo "FEHLER: .env-Datei nicht gefunden!"
  exit 1
fi

# Funktion zur Überprüfung und Erstellung von Verzeichnissen
check_and_create_directory() {
  local dir=$1
  # Ersetze ~ mit dem tatsächlichen Home-Verzeichnis
  dir="${dir/#\~/$HOME}"
  
  if [ ! -d "$dir" ]; then
    echo "Verzeichnis $dir existiert nicht. Wird erstellt..."
    mkdir -p "$dir"
    if [ $? -eq 0 ]; then
      echo "Verzeichnis $dir erfolgreich erstellt."
    else
      echo "FEHLER: Konnte Verzeichnis $dir nicht erstellen!"
      exit 1
    fi
  else
    echo "Verzeichnis $dir existiert bereits."
  fi
}

# Überprüfe und erstelle die erforderlichen Verzeichnisse
echo "Überprüfe erforderliche Verzeichnisse..."
check_and_create_directory "$POSTGRES_DATA_PATH"
check_and_create_directory "$BACKUP_PATH"
check_and_create_directory "$HOME/flashcards"

# Überprüfe und erstelle die externe Konfigurationsdatei
EXTERNAL_CONFIG_PATH="$HOME/flashcards/application.properties"
if [ ! -f "$EXTERNAL_CONFIG_PATH" ]; then
  echo "Externe Konfigurationsdatei $EXTERNAL_CONFIG_PATH existiert nicht. Wird aus Vorlage erstellt..."
  cp src/main/resources/example-external-config.properties "$EXTERNAL_CONFIG_PATH"
  if [ $? -eq 0 ]; then
    echo "Externe Konfigurationsdatei erfolgreich erstellt."
    echo "HINWEIS: Bitte überprüfen Sie die Datei $EXTERNAL_CONFIG_PATH und passen Sie die Konfiguration bei Bedarf an."
  else
    echo "FEHLER: Konnte externe Konfigurationsdatei nicht erstellen!"
    exit 1
  fi
else
  echo "Externe Konfigurationsdatei $EXTERNAL_CONFIG_PATH existiert bereits."
fi

# Starte den Postgres-Docker-Container
echo "Starte Postgres-Docker-Container..."
docker-compose up -d postgres

# Warte, bis der Postgres-Container bereit ist
echo "Warte, bis der Postgres-Container bereit ist..."
attempt=0
max_attempts=30
until docker exec flashcards-postgres pg_isready -U "$POSTGRES_USER" -d "$POSTGRES_DB" > /dev/null 2>&1 || [ $attempt -eq $max_attempts ]
do
  attempt=$((attempt+1))
  echo "Warte auf Postgres... ($attempt/$max_attempts)"
  sleep 2
done

if [ $attempt -eq $max_attempts ]; then
  echo "FEHLER: Postgres-Container konnte nicht gestartet werden!"
  exit 1
else
  echo "Postgres-Container ist bereit."
fi

# Starte das Spring Boot Backend
echo "Starte das Flashcards-Backend..."
./gradlew bootRun &
BACKEND_PID=$!

# Warte, bis das Backend gestartet ist
echo "Warte, bis das Backend gestartet ist..."
attempt=0
max_attempts=30
until curl -s http://localhost:8080/actuator/health > /dev/null 2>&1 || [ $attempt -eq $max_attempts ]
do
  attempt=$((attempt+1))
  echo "Warte auf Backend... ($attempt/$max_attempts)"
  sleep 2
done

if [ $attempt -eq $max_attempts ]; then
  echo "WARNUNG: Backend scheint nicht zu starten. Versuche trotzdem, das Frontend zu starten..."
else
  echo "Backend ist bereit."
fi

# Navigiere zum Frontend-Verzeichnis und starte die React-App
echo "Starte das Flashcards-Frontend..."
cd frontend
npm start

# Wenn das Frontend beendet wird, beende auch das Backend
echo "Frontend wurde beendet. Beende Backend..."
kill $BACKEND_PID

echo "Flashcards-Anwendung wurde beendet."
