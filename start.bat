@echo off
setlocal enabledelayedexpansion

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
set POSTGRES_DATA_PATH=%USERPROFILE%\flashcards\postgres-data
set BACKUP_PATH=%USERPROFILE%\flashcards\backups

REM Debug-Ausgabe der Umgebungsvariablen
echo "DEBUG: POSTGRES_DATA_PATH=%POSTGRES_DATA_PATH%"
echo "DEBUG: BACKUP_PATH=%BACKUP_PATH%"
echo "DEBUG: USERPROFILE=%USERPROFILE%"

REM Springe zum Hauptteil des Skripts
goto :main

REM Funktion zur Überprüfung und Erstellung von Verzeichnissen
:check_and_create_directory
set dir=%~1
REM Keine Ersetzung mehr notwendig, da wir bereits %USERPROFILE% in .env verwenden

echo "DEBUG: Verzeichnis-Pfad: !dir!"
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
REM Überprüfe und erstelle die erforderlichen Verzeichnisse
echo Überprüfe erforderliche Verzeichnisse...
echo "DEBUG: Aufruf mit POSTGRES_DATA_PATH: %POSTGRES_DATA_PATH%"
call :check_and_create_directory "%POSTGRES_DATA_PATH%"
echo "DEBUG: Aufruf mit BACKUP_PATH: %BACKUP_PATH%"
call :check_and_create_directory "%BACKUP_PATH%"
echo "DEBUG: Aufruf mit USERPROFILE\flashcards: %USERPROFILE%\flashcards"
call :check_and_create_directory "%USERPROFILE%\flashcards"

REM Überprüfe und erstelle die externe Konfigurationsdatei
set EXTERNAL_CONFIG_PATH=%USERPROFILE%\flashcards\application.properties
if not exist "%EXTERNAL_CONFIG_PATH%" (
    echo Externe Konfigurationsdatei %EXTERNAL_CONFIG_PATH% existiert nicht. Wird aus Vorlage erstellt...
    copy src\main\resources\example-external-config.properties "%EXTERNAL_CONFIG_PATH%" >nul
    if !errorlevel! equ 0 (
        echo Externe Konfigurationsdatei erfolgreich erstellt.
        echo HINWEIS: Bitte überprüfen Sie die Datei %EXTERNAL_CONFIG_PATH% und passen Sie die Konfiguration bei Bedarf an.
    ) else (
        echo FEHLER: Konnte externe Konfigurationsdatei nicht erstellen!
        exit /b 1
    )
) else (
    echo Externe Konfigurationsdatei %EXTERNAL_CONFIG_PATH% existiert bereits.
)

REM Starte den Postgres-Docker-Container
echo Starte Postgres-Docker-Container...
docker-compose --env-file %USERPROFILE%\apps\flashcards\.env up -d postgres > %USERPROFILE%\apps\flashcards\docker-compose.log 2>&1

REM Warte, bis der Postgres-Container bereit ist
echo Warte, bis der Postgres-Container bereit ist...
set attempt=0
set max_attempts=30

:wait_for_postgres
set /a attempt+=1
docker exec flashcards-postgres pg_isready -U "%POSTGRES_USER%" -d "%POSTGRES_DB%" >nul 2>&1
if !errorlevel! equ 0 (
    goto postgres_ready
)
if !attempt! equ !max_attempts! (
    echo FEHLER: Postgres-Container konnte nicht gestartet werden!
    exit /b 1
)
echo Warte auf Postgres... (!attempt!/!max_attempts!)
timeout /t 2 /nobreak >nul
goto wait_for_postgres

:postgres_ready
echo Postgres-Container ist bereit.

REM Starte das Spring Boot Backend in einem separaten Fenster
echo Starte das Flashcards-Backend...
start "Flashcards Backend" cmd /c "gradlew bootRun > %USERPROFILE%\apps\flashcards\backend.log 2>&1"

REM Warte, bis das Backend gestartet ist
echo Warte, bis das Backend gestartet ist...
set attempt=0
set max_attempts=30

:wait_for_backend
set /a attempt+=1
curl -s http://localhost:8080/actuator/health >nul 2>&1
if !errorlevel! equ 0 (
    goto backend_ready
)
if !attempt! equ !max_attempts! (
    echo WARNUNG: Backend scheint nicht zu starten. Versuche trotzdem, das Frontend zu starten...
    goto start_frontend
)
echo Warte auf Backend... (!attempt!/!max_attempts!)
timeout /t 2 /nobreak >nul
goto wait_for_backend

:backend_ready
echo Backend ist bereit.

:start_frontend
REM Navigiere zum Frontend-Verzeichnis und starte die React-App
echo Starte das Flashcards-Frontend...
cd frontend
npm start

echo Flashcards-Anwendung wurde beendet.
