# User Story
Als Student möchte ich, dass meine Karteikarten dauerhaft gespeichert werden, damit ich nicht bei jedem Systemstart neue Karteikarten anlegen muss.

# Anforderungen
1. Die Entitäten sollen in einer Postgres-Datenbank gespeichert werden.
2. Die Postgres-Datenbank soll in einem Docker-Container laufen. Es soll das Postgres Image mit dem Tag 16.9 verwendet werden.
3. Die Einrichtung der Postgres-DB soll in der Datei `GETTING_STARTED.md` dokumentiert sein.
4. Die Datenbankdaten sollen außerhalb des Containers in einem persistenten Volume gespeichert werden, um Datenverluste bei einem Neustart des Containers zu vermeiden. Der Speicherort des Volumes ist in der Konfigurationsdatei als Pfad `~/flashcards/postgres-data` definiert.
5. Die Passwörter für die Datenbank sollen aus einem Verzeichnis im Home-Verzeichnis geladen werden, z. B. `~/flashcards/application.properties`. Die Properties sind `spring.datasource.username` und `spring.datasource.password`
6. Es soll ein Startskript (z. B. `start.sh`) bereitgestellt werden, das folgende Schritte automatisiert:
   * Start des Postgres-Docker-Containers
   * Start des Spring Boot Backends
   * Start des React Frontends (lokaler Entwicklungsserver oder Produktion je nach Modus)
7. Es soll ein Backup-Skript (z. B. `backup.sh`) bereitgestellt werden, das ein konsistentes Backup der Postgres-Daten erstellt. Der Backup-Speicherort ist über einen Pfad wie `~/flashcards/backups` konfigurierbar.
8. Optional: Ein weiteres Skript (z. B. `restore.sh`) soll zur Wiederherstellung eines zuvor erstellten Backups zur Verfügung stehen.
9. Das System soll über Umgebungsvariablen (z. B. `.env` Datei) konfigurierbar sein, insbesondere für:
   * Datenbankzugang (Benutzername, Passwort, Host, Port)
   * Speicherpfade für Volume, Secrets, Backups
10. Für die Docker-Konfiguration soll ein `docker-compose.yml` bereitgestellt werden, das die Postgres-Datenbank inkl. Volume-Mounts, Passwortdatei und Ports konfiguriert.
11. Die Backend-Anwendung soll automatisch beim Start prüfen, ob die Datenbank verfügbar ist, und erst dann mit dem Startprozess fortfahren.
12. Die Backup-Dateien sollen einen Zeitstempel im Dateinamen enthalten.
13. Das Backend soll Migrationsskripte Flyway verwenden, um die Datenbankstruktur automatisch zu verwalten.
14. Die Anwendung soll bei einem Neustart automatisch wieder mit denselben Daten arbeiten können, ohne dass manuell eingegriffen werden muss.

# Hinweise
1. Auf dem Host (Windows) läuft ein Docker Demon
2. Momentan wird eine H2 Datenbank verwendet. Diese solle ersetzt werden. Es gibt aber keine Daten, die gesichert werden müssen, da hier nur Dummy-Daten verwendet wurden.