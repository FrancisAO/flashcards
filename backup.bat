@echo off
setlocal enabledelayedexpansion

REM Springe zum Hauptteil des Skripts und überspringe die Funktionsdefinitionen
goto :main

REM Funktion zur Überprüfung und Erstellung von Verzeichnissen
:check_and_create_directory
set dir=%~1
echo DEBUG: Verzeichnis-Pfad: !dir!

if not exist "!dir!" (
    echo Verzeichnis !dir! existiert nicht. Wird erstellt...
    mkdir "!dir!" 2>nul
    if !errorlevel! equ 0 (
        echo Verzeichnis !dir! erfolgreich erstellt.
    ) else (
        echo FEHLER: Konnte Verzeichnis !dir! nicht erstellen!
        exit /b 1
    )
) else (
    echo Verzeichnis !dir! existiert bereits.
)
goto :eof

:main
REM Lade Umgebungsvariablen aus .env-Datei
if exist .env (
    for /F "tokens=*" %%A in (.env) do (
        set line=%%A
        if not "!line:~0,1!"=="#" (
            set !line!
        )
    )
) else (
    echo FEHLER: .env-Datei nicht gefunden!
    exit /b 1
)

REM Expandiere die Pfade mit dem tatsächlichen USERPROFILE
set BACKUP_PATH=%USERPROFILE%\flashcards\backups

REM Überprüfe und erstelle das Backup-Verzeichnis
echo Überprüfe Backup-Verzeichnis...
echo DEBUG: BACKUP_PATH=%BACKUP_PATH%
call :check_and_create_directory "%BACKUP_PATH%"

REM Erstelle einen Zeitstempel für den Backup-Dateinamen
for /f "tokens=2 delims==" %%I in ('wmic os get localdatetime /format:list') do set datetime=%%I
set TIMESTAMP=%datetime:~0,8%_%datetime:~8,6%
set BACKUP_FILE=%BACKUP_PATH%\flashcards_backup_%TIMESTAMP%.sql

REM Überprüfe, ob der Postgres-Container läuft
echo Überprüfe, ob der Postgres-Container läuft...
docker ps | findstr flashcards-postgres > nul
if !errorlevel! neq 0 (
    echo FEHLER: Der Postgres-Container 'flashcards-postgres' läuft nicht!
    echo Bitte starten Sie den Container mit 'docker-compose up -d postgres' und versuchen Sie es erneut.
    exit /b 1
)

REM Erstelle das Backup
echo Erstelle Backup der Postgres-Datenbank...
echo Backup-Datei: %BACKUP_FILE%

REM Verwende pg_dump über docker exec, um ein Backup zu erstellen
docker exec -i flashcards-postgres pg_dump -U "%POSTGRES_USER%" -d "%POSTGRES_DB%" > "%BACKUP_FILE%"

REM Überprüfe, ob das Backup erfolgreich erstellt wurde
if !errorlevel! equ 0 (
    for %%F in ("%BACKUP_FILE%") do set size=%%~zF
    if !size! gtr 0 (
        echo Backup erfolgreich erstellt: %BACKUP_FILE%
        echo Backup-Größe: !size! Bytes
    ) else (
        echo FEHLER: Backup-Datei ist leer!
        del "%BACKUP_FILE%" 2>nul
        exit /b 1
    )
) else (
    echo FEHLER: Backup konnte nicht erstellt werden!
    if exist "%BACKUP_FILE%" (
        del "%BACKUP_FILE%" 2>nul
    )
    exit /b 1
)

REM Optional: Liste der vorhandenen Backups anzeigen
echo.
echo Vorhandene Backups:
dir /b "%BACKUP_PATH%\flashcards_backup_*.sql" 2>nul || echo Keine weiteren Backups gefunden.

echo.
echo Backup-Vorgang abgeschlossen.