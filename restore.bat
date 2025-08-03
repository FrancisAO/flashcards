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
set BACKUP_PATH=%USERPROFILE%\flashcards\backups

REM Überprüfe, ob ein Backup-Pfad als Argument angegeben wurde
if "%~1" == "" (
    REM Keine Argumente angegeben, zeige verfügbare Backups an
    echo DEBUG: BACKUP_PATH=%BACKUP_PATH%
    if not exist "%BACKUP_PATH%" (
        echo FEHLER: Das Backup-Verzeichnis existiert nicht: %BACKUP_PATH%
        exit /b 1
    )

    echo Verfügbare Backups:
    set count=0
    
    REM Erstelle eine temporäre Datei mit den Backup-Pfaden
    set temp_file=%TEMP%\backup_list.txt
    if exist "%temp_file%" del "%temp_file%"
    
    for /f "tokens=*" %%F in ('dir /b /o-d "%BACKUP_PATH%\flashcards_backup_*.sql" 2^>nul') do (
        set /a count+=1
        echo !count!^) %%F >> "%temp_file%"
        for %%S in ("%BACKUP_PATH%\%%F") do set size=%%~zS
        echo !count!^) %%F ^(!size! Bytes^)
    )
    
    if !count! equ 0 (
        echo Keine Backups gefunden im Verzeichnis %BACKUP_PATH%
        exit /b 1
    )
    
    echo.
    echo Bitte geben Sie die Nummer des Backups ein, das wiederhergestellt werden soll,
    echo oder drücken Sie Strg+C, um abzubrechen.
    set /p backup_number=Backup-Nummer: 
    
    REM Überprüfe, ob die Eingabe eine gültige Zahl ist
    if !backup_number! LSS 1 (
        echo FEHLER: Ungültige Backup-Nummer.
        exit /b 1
    )
    
    if !backup_number! GTR !count! (
        echo FEHLER: Ungültige Backup-Nummer.
        exit /b 1
    )
    
    REM Hole den Dateinamen aus der temporären Datei
    for /f "tokens=1* delims=)" %%a in ('findstr /b "!backup_number!)" "%temp_file%"') do set backup_file=%%b
    set backup_file=%BACKUP_PATH%\!backup_file:~1!
    
    REM Lösche die temporäre Datei
    del "%temp_file%"
) else (
    REM Backup-Datei wurde als Argument angegeben
    set backup_file=%~1
)

REM Überprüfe, ob die Backup-Datei existiert
if not exist "!backup_file!" (
    echo FEHLER: Die angegebene Backup-Datei existiert nicht: !backup_file!
    exit /b 1
)

echo DEBUG: Backup-Datei: !backup_file!

REM Überprüfe, ob der Postgres-Container läuft
echo Überprüfe, ob der Postgres-Container läuft...
docker ps | findstr flashcards-postgres > nul
if !errorlevel! neq 0 (
    echo Der Postgres-Container 'flashcards-postgres' läuft nicht. Versuche ihn zu starten...
    docker-compose up -d postgres
    
    REM Warte, bis der Container bereit ist
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
)

:postgres_ready
echo Postgres-Container ist bereit.

REM Bestätigung vom Benutzer einholen
echo.
echo ACHTUNG: Die Wiederherstellung wird alle aktuellen Daten in der Datenbank '%POSTGRES_DB%' überschreiben!
set /p confirm=Möchten Sie fortfahren? (j/n): 
if /i not "!confirm!"=="j" (
    echo Wiederherstellung abgebrochen.
    exit /b 0
)

echo.
echo Starte Wiederherstellung aus Backup: !backup_file!

REM Stoppe alle Anwendungen, die auf die Datenbank zugreifen könnten
echo Stoppe alle laufenden Anwendungen, die auf die Datenbank zugreifen...
REM Hier könnten weitere Befehle zum Stoppen von Anwendungen hinzugefügt werden

REM Lösche die bestehende Datenbank und erstelle sie neu
echo Lösche die bestehende Datenbank und erstelle sie neu...
docker exec -i flashcards-postgres psql -U "%POSTGRES_USER%" -c "DROP DATABASE IF EXISTS %POSTGRES_DB% WITH (FORCE);"
if !errorlevel! neq 0 (
    echo FEHLER: Konnte die bestehende Datenbank nicht löschen!
    exit /b 1
)

docker exec -i flashcards-postgres psql -U "%POSTGRES_USER%" -c "CREATE DATABASE %POSTGRES_DB%;"
if !errorlevel! neq 0 (
    echo FEHLER: Konnte die Datenbank nicht neu erstellen!
    exit /b 1
)

REM Stelle das Backup wieder her
echo Stelle Backup wieder her...
type "!backup_file!" | docker exec -i flashcards-postgres psql -U "%POSTGRES_USER%" -d "%POSTGRES_DB%"

REM Überprüfe, ob die Wiederherstellung erfolgreich war
if !errorlevel! equ 0 (
    echo.
    echo Wiederherstellung erfolgreich abgeschlossen!
    echo Die Datenbank '%POSTGRES_DB%' wurde aus dem Backup '!backup_file!' wiederhergestellt.
) else (
    echo.
    echo FEHLER: Die Wiederherstellung ist fehlgeschlagen!
    exit /b 1
)

echo.
echo Wiederherstellungs-Vorgang abgeschlossen.