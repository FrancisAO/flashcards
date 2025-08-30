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

# Überprüfe und erstelle das Backup-Verzeichnis
echo "Überprüfe Backup-Verzeichnis..."
check_and_create_directory "$BACKUP_PATH"

# Erstelle einen Zeitstempel für den Backup-Dateinamen
TIMESTAMP=$(date +"%Y%m%d_%H%M%S")
BACKUP_FILE="${BACKUP_PATH/#\~/$HOME}/flashcards_backup_${TIMESTAMP}.sql"

# Überprüfe, ob der Postgres-Container läuft
echo "Überprüfe, ob der Postgres-Container läuft..."
if ! docker ps | grep -q flashcards-postgres; then
  echo "FEHLER: Der Postgres-Container 'flashcards-postgres' läuft nicht!"
  echo "Bitte starten Sie den Container mit 'docker-compose up -d postgres' und versuchen Sie es erneut."
  exit 1
fi

# Erstelle das Backup
echo "Erstelle Backup der Postgres-Datenbank..."
echo "Backup-Datei: $BACKUP_FILE"

# Verwende pg_dump über docker exec, um ein Backup zu erstellen
docker exec -i flashcards-postgres pg_dump -U "$POSTGRES_USER" -d "$POSTGRES_DB" > "$BACKUP_FILE"

# Überprüfe, ob das Backup erfolgreich erstellt wurde
if [ $? -eq 0 ] && [ -s "$BACKUP_FILE" ]; then
  echo "Backup erfolgreich erstellt: $BACKUP_FILE"
  echo "Backup-Größe: $(du -h "$BACKUP_FILE" | cut -f1)"
else
  echo "FEHLER: Backup konnte nicht erstellt werden!"
  # Lösche die möglicherweise leere oder fehlerhafte Backup-Datei
  if [ -f "$BACKUP_FILE" ]; then
    rm "$BACKUP_FILE"
  fi
  exit 1
fi

# Optional: Liste der vorhandenen Backups anzeigen
echo -e "\nVorhandene Backups:"
ls -lh "${BACKUP_PATH/#\~/$HOME}"/flashcards_backup_*.sql 2>/dev/null || echo "Keine weiteren Backups gefunden."

echo -e "\nBackup-Vorgang abgeschlossen."