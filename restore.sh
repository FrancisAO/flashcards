#!/bin/bash

# Lade Umgebungsvariablen aus .env-Datei
if [ -f .env ]; then
  export $(grep -v '^#' .env | xargs)
else
  echo "FEHLER: .env-Datei nicht gefunden!"
  exit 1
fi

# Funktion zur Überprüfung, ob ein Pfad existiert
check_path_exists() {
  local path=$1
  # Ersetze ~ mit dem tatsächlichen Home-Verzeichnis
  path="${path/#\~/$HOME}"
  
  if [ ! -f "$path" ]; then
    echo "FEHLER: Die angegebene Backup-Datei existiert nicht: $path"
    return 1
  fi
  return 0
}

# Funktion zur Anzeige der verfügbaren Backups
list_available_backups() {
  local backup_dir="${BACKUP_PATH/#\~/$HOME}"
  
  if [ ! -d "$backup_dir" ]; then
    echo "FEHLER: Das Backup-Verzeichnis existiert nicht: $backup_dir"
    exit 1
  fi
  
  echo "Verfügbare Backups:"
  local count=0
  local backups=()
  
  while IFS= read -r file; do
    count=$((count+1))
    backups+=("$file")
    echo "$count) $(basename "$file") ($(du -h "$file" | cut -f1))"
  done < <(find "$backup_dir" -name "flashcards_backup_*.sql" -type f | sort -r)
  
  if [ $count -eq 0 ]; then
    echo "Keine Backups gefunden im Verzeichnis $backup_dir"
    exit 1
  fi
  
  return 0
}

# Hauptfunktion zur Wiederherstellung eines Backups
restore_backup() {
  local backup_file=$1
  
  # Überprüfe, ob die Backup-Datei existiert
  if ! check_path_exists "$backup_file"; then
    exit 1
  fi
  
  # Überprüfe, ob der Postgres-Container läuft
  echo "Überprüfe, ob der Postgres-Container läuft..."
  if ! docker ps | grep -q flashcards-postgres; then
    echo "Der Postgres-Container 'flashcards-postgres' läuft nicht. Versuche ihn zu starten..."
    docker-compose up -d postgres
    
    # Warte, bis der Container bereit ist
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
    fi
  fi
  
  echo "Postgres-Container ist bereit."
  
  # Bestätigung vom Benutzer einholen
  echo -e "\nACHTUNG: Die Wiederherstellung wird alle aktuellen Daten in der Datenbank '$POSTGRES_DB' überschreiben!"
  read -p "Möchten Sie fortfahren? (j/n): " confirm
  if [[ ! "$confirm" =~ ^[jJ]$ ]]; then
    echo "Wiederherstellung abgebrochen."
    exit 0
  fi
  
  echo -e "\nStarte Wiederherstellung aus Backup: $backup_file"
  
  # Stoppe alle Anwendungen, die auf die Datenbank zugreifen könnten
  echo "Stoppe alle laufenden Anwendungen, die auf die Datenbank zugreifen..."
  # Hier könnten weitere Befehle zum Stoppen von Anwendungen hinzugefügt werden
  
  # Lösche die bestehende Datenbank und erstelle sie neu
  echo "Lösche die bestehende Datenbank und erstelle sie neu..."
  docker exec -i flashcards-postgres psql -U "$POSTGRES_USER" -c "DROP DATABASE IF EXISTS $POSTGRES_DB WITH (FORCE);"
  if [ $? -ne 0 ]; then
    echo "FEHLER: Konnte die bestehende Datenbank nicht löschen!"
    exit 1
  fi
  
  docker exec -i flashcards-postgres psql -U "$POSTGRES_USER" -c "CREATE DATABASE $POSTGRES_DB;"
  if [ $? -ne 0 ]; then
    echo "FEHLER: Konnte die Datenbank nicht neu erstellen!"
    exit 1
  fi
  
  # Stelle das Backup wieder her
  echo "Stelle Backup wieder her..."
  cat "$backup_file" | docker exec -i flashcards-postgres psql -U "$POSTGRES_USER" -d "$POSTGRES_DB"
  
  # Überprüfe, ob die Wiederherstellung erfolgreich war
  if [ $? -eq 0 ]; then
    echo -e "\nWiederherstellung erfolgreich abgeschlossen!"
    echo "Die Datenbank '$POSTGRES_DB' wurde aus dem Backup '$backup_file' wiederhergestellt."
  else
    echo -e "\nFEHLER: Die Wiederherstellung ist fehlgeschlagen!"
    exit 1
  fi
}

# Hauptprogramm
if [ $# -eq 0 ]; then
  # Keine Argumente angegeben, zeige verfügbare Backups an und frage nach Auswahl
  if list_available_backups; then
    echo -e "\nBitte geben Sie die Nummer des Backups ein, das wiederhergestellt werden soll,"
    echo "oder drücken Sie Strg+C, um abzubrechen."
    read -p "Backup-Nummer: " backup_number
    
    # Überprüfe, ob die Eingabe eine Zahl ist
    if ! [[ "$backup_number" =~ ^[0-9]+$ ]]; then
      echo "FEHLER: Ungültige Eingabe. Bitte geben Sie eine Zahl ein."
      exit 1
    fi
    
    # Hole den Pfad des ausgewählten Backups
    backup_dir="${BACKUP_PATH/#\~/$HOME}"
    selected_backup=$(find "$backup_dir" -name "flashcards_backup_*.sql" -type f | sort -r | sed -n "${backup_number}p")
    
    if [ -z "$selected_backup" ]; then
      echo "FEHLER: Ungültige Backup-Nummer."
      exit 1
    fi
    
    # Starte die Wiederherstellung
    restore_backup "$selected_backup"
  fi
else
  # Backup-Datei wurde als Argument angegeben
  restore_backup "$1"
fi

echo -e "\nWiederherstellungs-Vorgang abgeschlossen."