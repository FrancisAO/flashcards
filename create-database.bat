@echo off
setlocal enabledelayedexpansion

echo ===== Datenbank-Erstellungsskript =====

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

echo Überprüfe, ob der Postgres-Container läuft...
docker ps | findstr flashcards-postgres > nul
if %errorlevel% neq 0 (
    echo FEHLER: Postgres-Container ist nicht aktiv!
    echo Bitte stellen Sie sicher, dass der Container gestartet wurde.
    exit /b 1
)

echo Überprüfe, ob die Datenbank '%POSTGRES_DB%' existiert...
for /f %%i in ('docker exec flashcards-postgres psql -U %POSTGRES_USER% -d postgres -tAc "SELECT COUNT(*) FROM pg_database WHERE datname='%POSTGRES_DB%'"') do set DB_COUNT=%%i
if "%DB_COUNT%"=="1" (
    echo Datenbank '%POSTGRES_DB%' existiert bereits.
) else (
    echo Datenbank '%POSTGRES_DB%' existiert nicht. Erstelle Datenbank...
    docker exec flashcards-postgres psql -U %POSTGRES_USER% -d postgres -c "CREATE DATABASE \"%POSTGRES_DB%\""
    if %errorlevel% neq 0 (
        echo FEHLER: Konnte Datenbank '%POSTGRES_DB%' nicht erstellen!
        exit /b 1
    )
    echo Datenbank '%POSTGRES_DB%' wurde erfolgreich erstellt.
)

echo Überprüfe Benutzerberechtigungen...
REM Überprüfe, ob der Benutzer existiert
set USER_READY=0
docker exec flashcards-postgres psql -U %POSTGRES_USER% -d postgres -tAc "SELECT 1 FROM pg_roles WHERE rolname='%POSTGRES_USER%'" | findstr 1 > nul
if %errorlevel% neq 0 (
    echo Benutzer '%POSTGRES_USER%' existiert nicht. Erstelle Benutzer...
    docker exec flashcards-postgres psql -U %POSTGRES_USER% -d postgres -c "CREATE USER %POSTGRES_USER% WITH ENCRYPTED PASSWORD '%POSTGRES_PASSWORD%'"
    if %errorlevel% neq 0 (
        echo FEHLER: Konnte Benutzer '%POSTGRES_USER%' nicht erstellen!
        exit /b 1
    )
    echo Benutzer '%POSTGRES_USER%' wurde erfolgreich erstellt.
    set USER_READY=1
) else (
    echo Benutzer '%POSTGRES_USER%' existiert bereits.
    set USER_READY=1
)

if %USER_READY% equ 1 (
    echo Setze Berechtigungen für Benutzer '%POSTGRES_USER%' auf Datenbank '%POSTGRES_DB%'...
    docker exec flashcards-postgres psql -U %POSTGRES_USER% -d postgres -c "GRANT ALL PRIVILEGES ON DATABASE %POSTGRES_DB% TO %POSTGRES_USER%"
    if %errorlevel% neq 0 (
        echo WARNUNG: Konnte Berechtigungen nicht setzen. Möglicherweise sind sie bereits gesetzt.
    ) else (
        echo Berechtigungen wurden erfolgreich gesetzt.
    )

    echo Setze Schema-Berechtigungen...
    docker exec flashcards-postgres psql -U %POSTGRES_USER% -d %POSTGRES_DB% -c "GRANT ALL ON SCHEMA public TO %POSTGRES_USER%"
    if %errorlevel% neq 0 (
        echo WARNUNG: Konnte Schema-Berechtigungen nicht setzen.
    ) else (
        echo Schema-Berechtigungen wurden erfolgreich gesetzt.
    )
)

echo ===== Datenbank-Erstellungsprozess abgeschlossen =====
exit /b 0