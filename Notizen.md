# Abschnitte & Kapitel die für Implementierung relevant sind

Abschnitt 1.2 Qualitätsziele
Kapitel 4 Lösungsstrategien
Kapitel 5.4 - Bausteine des Algorithmus
- Features für MVP auswählen
- Statistik und Analyse ↔ Konfiguration und Anpassung: Sehr anspruchsvoll, evtl. streichen
Kapitel 5.5 Schnittstellen
- DB Schnittstelle: hier fehlt scheinbar
das Repo für die Statistik und die dazugehörigen Tabellen. 
Kapitel 7 Externe Dienste wie Wissensdatenbanken. Anbindung der externen Dienste über Adapter-Architektur. Circuit Breaker Pattern
Kapitel 9 Architekturentscheidungen
Kapitel 10 Qualität

# Todo
Kapitel 5.1 - whitebox sichten der Bausteine

# MVP 1
Frontend
- Menü
    - Neues Deck anlegen
    - Decks anzeigen
    - Eine Karte einem Deck hinzufügen

Backend
- CRUD eines Decks
- CRUD einer Karte

Datenbank
Implementierung eines regelmäßigen Backup-Mechanismus für die Datenbank-Datei
Festlegung der Datenbankschema-Migrations-Strategie auf Flyway.
