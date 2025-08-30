@echo off
setlocal enabledelayedexpansion

REM Lade Umgebungsvariablen aus .env-Datei
if defined ENV_FILE_PATH (
    if exist "%ENV_FILE_PATH%" (
        echo Lade Umgebungsvariablen aus %ENV_FILE_PATH%
        for /F "tokens=*" %%A in (%ENV_FILE_PATH%) do (
            set line=%%A
            if not "!line:~0,1!"=="#" (
                set !line!
            )
        )
    ) else (
        echo WARNUNG: ENV_FILE_PATH ist definiert, aber die Datei existiert nicht: %ENV_FILE_PATH%
        echo Versuche lokale .env-Datei...
    )
)

if not defined ENV_FILE_PATH (
    if exist .env (
        echo Lade Umgebungsvariablen aus lokaler .env-Datei
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
)

REM Die Pfade werden aus der .env-Datei geladen und nicht überschrieben

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
echo "DEBUG: Aufruf mit USERPROFILE\apps\flashcards: %USERPROFILE%\apps\flashcards"
call :check_and_create_directory "%USERPROFILE%\apps\flashcards"

REM Überprüfe und erstelle die externe Konfigurationsdatei
set EXTERNAL_CONFIG_PATH=%USERPROFILE%\apps\flashcards\application-prod.properties
if not exist "%EXTERNAL_CONFIG_PATH%" (
    echo Externe Konfigurationsdatei %EXTERNAL_CONFIG_PATH% existiert nicht. Wird aus Vorlage erstellt...
    copy src\main\resources\example-application-prod-external.properties "%EXTERNAL_CONFIG_PATH%" >nul
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
docker-compose up -d postgres > %USERPROFILE%\apps\flashcards\docker-compose.log 2>&1

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

REM Erstelle die Datenbank, falls sie nicht existiert
echo Führe Datenbank-Erstellungsskript aus...
call create-database.bat
if %errorlevel% neq 0 (
    echo FEHLER: Datenbank-Erstellungsskript fehlgeschlagen!
    exit /b 1
)
echo Datenbank-Erstellungsskript erfolgreich ausgeführt.

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
