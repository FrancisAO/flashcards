# 1. Einführung und Ziele

## Übersicht
Dieses Kapitel beschreibt die grundlegenden Geschäftsziele, die mit dem System erreicht werden sollen, und die wesentlichen Aufgaben, die das System für seine Benutzer erfüllen soll. Es definiert die treibenden Kräfte, die das System beeinflussen, und legt die Qualitätsanforderungen für die Architektur fest.

## Inhalt
In diesem Kapitel werden die grundlegenden Ziele des Projekts, die Anforderungen der wichtigsten Stakeholder und die wichtigsten Qualitätsziele beschrieben. Diese bilden die Grundlage für architektonische Entscheidungen.

## 1.1 Aufgabenstellung

### Beschreibung
Die Flashcard-Anwendung ist ein webbasiertes Lernsystem, das dem Nutzer hilft, Vokabeln, Fachbegriffe und andere Lerninhalte effizient zu lernen und zu wiederholen. Die Anwendung nutzt das Spaced-Repetition-Prinzip als zentrales Lernkonzept, um den Lernprozess zu optimieren und langfristige Gedächtnisleistung zu fördern. Durch KI-gestützte Funktionen bietet die Anwendung personalisierte Lernpfade und automatisierte Karteikartenerstellung für verschiedene Lernbereiche wie Sprachen, Naturwissenschaften, Medizin und mehr.

* **Hauptfunktionen und -aufgaben:**
  * Erstellung und Verwaltung von Karteikarten mit Frage-Antwort-Paaren
  * Organisation von Karteikarten in thematischen Decks
  * KI-gestützte automatische Generierung von Karteikarten aus Texten oder Lernmaterialien
  * Implementierung eines Spaced-Repetition-Algorithmus für optimales Lernen
  * Personalisierte Lernpfade basierend auf individueller Lernleistung
  * Statistiken und Fortschrittsüberwachung für den Nutzer
  * Import/Export-Funktionen für Karteikarten und Decks
  * Benutzerfreundliches Web-Interface für den persönlichen Gebrauch

* **Geschäftlicher Kontext und Hintergrund:**
  * Die Anwendung ist als persönliches Lernprojekt ohne kommerzielle Absichten konzipiert
  * Sie dient ausschließlich dem individuellen Lernfortschritt des Entwicklers und Nutzers
  * Das Projekt entstand aus dem persönlichen Bedürfnis nach einem effizienten Lernsystem, das auf wissenschaftlich fundierten Lernmethoden basiert
  * Die Anwendung läuft lokal (localhost) und ist für die Nutzung durch eine einzelne Person ausgelegt

* **Problemstellung, die durch das System gelöst werden soll:**
  * Schwierigkeit, große Mengen an Informationen effizient zu memorieren
  * Ineffiziente Wiederholungsstrategien, die nicht auf wissenschaftlichen Erkenntnissen basieren
  * Mangel an personalisierten Lernpfaden, die sich an individuelle Stärken und Schwächen anpassen
  * Zeitaufwändige manuelle Erstellung von Lernmaterialien
  * Fehlende Übersicht über den eigenen Lernfortschritt

Die Flashcard-Anwendung löst diese Probleme durch einen wissenschaftlich fundierten Lernalgorithmus, KI-gestützte Automatisierung und personalisierte Anpassung des Lernprozesses, speziell zugeschnitten auf die Bedürfnisse des einzelnen Nutzers.

### Mögliche Visualisierungen (todo)
* Kontextdiagramm zur Darstellung der Systemgrenzen und externen Schnittstellen
* Business Canvas zur Visualisierung des Geschäftsmodells

## 1.2 Qualitätsziele

### Beschreibung
Die folgenden Qualitätsziele definieren die wichtigsten Qualitätsanforderungen an die Flashcard-Anwendung. Sie bilden die Grundlage für architektonische Entscheidungen und sind nach ihrer Bedeutung für den Nutzer priorisiert.

| Qualitätsziel | Priorität | Messbare Kriterien | Begründung |
|---------------|-----------|-------------------|------------|
| Benutzerfreundlichkeit | Sehr hoch | - Intuitive Bedienung ohne Einarbeitungszeit<br>- Effiziente Workflows mit minimalen Klicks<br>- Konsistentes Design | Als einziger Nutzer ist eine auf persönliche Präferenzen zugeschnittene Benutzeroberfläche entscheidend für die tägliche Nutzung. |
| Lerneffizienz | Sehr hoch | - Nachweisbare Verbesserung der Gedächtnisleistung<br>- Optimale Implementierung des Spaced-Repetition-Algorithmus<br>- Anpassung an persönliche Lernkurve | Der Hauptzweck der Anwendung ist die Verbesserung der persönlichen Lerneffizienz durch wissenschaftlich fundierte Methoden. |
| Zuverlässigkeit | Hoch | - Keine Datenverluste<br>- Stabile Funktionalität ohne Abstürze<br>- Regelmäßige Backups | Da die Anwendung für persönliches Lernen genutzt wird, ist die Zuverlässigkeit und Datensicherheit wichtig, um Lernfortschritte nicht zu verlieren. |
| KI-Qualität | Hoch | - Genauigkeit der automatisch generierten Karteikarten<br>- Relevanz der personalisierten Empfehlungen<br>- Lernzeit-Optimierung | Die KI-Funktionen sollen den persönlichen Lernprozess unterstützen und die manuelle Erstellung von Karteikarten erleichtern. |
| Anpassungsfähigkeit | Mittel | - Unterstützung verschiedener Lernbereiche<br>- Flexible Karteikarten-Formate (Text, Bild, Audio)<br>- Anpassbare Lernalgorithmen | Die Anwendung soll für verschiedene persönliche Lernprojekte und -stile geeignet sein. |
| Offline-Funktionalität | Mittel | - (fast) Vollständige Funktionalität ohne Internetverbindung<br>- Lokale Datenspeicherung | Da die Anwendung auf localhost läuft, ist eine zuverlässige Offline-Funktionalität wichtig. |
| Wartbarkeit | Mittel | - Modularer Code<br>- Gute Dokumentation<br>- Einfache Aktualisierbarkeit | Als persönliches Projekt sollte die Anwendung einfach zu warten und zu erweitern sein. |

### Mögliche Visualisierungen (todo)
* Qualitätsbaum oder -matrix zur Darstellung der Qualitätsziele und ihrer Beziehungen
* Priorisierungsmatrix für Qualitätsziele

## 1.3 Stakeholder

### Beschreibung
Die folgende Tabelle beschreibt die relevanten Stakeholder der Flashcard-Anwendung, ihre Rollen, Anforderungen und potenziellen Konflikte. Da es sich um ein persönliches Projekt handelt, ist der Hauptstakeholder der Entwickler und Nutzer in Personalunion.

| Stakeholder | Rolle | Anforderungen und Erwartungen | Einfluss/Interesse | Potenzielle Konflikte |
|-------------|-------|------------------------------|-------------------|---------------------|
| Einzelnutzer (Entwickler & Anwender) | Hauptstakeholder, der die Anwendung entwickelt und nutzt | - Intuitive Bedienung<br>- Effektive Lernmethode<br>- Personalisierte Lernpfade<br>- Einfache Wartung und Erweiterung | Sehr hoch/Sehr hoch | - Entwicklungsaufwand vs. Funktionsumfang<br>- Kurzfristige Implementierung vs. langfristige Codequalität |
| Fachexperten/Literatur | Liefern Informationen über optimale Lernmethoden | - Korrekte Implementierung von Lernalgorithmen<br>- Wissenschaftliche Fundierung | Niedrig/Hoch | - Theoretische Optimalität vs. praktische Umsetzbarkeit |

### Mögliche Visualisierungen (todo)
* Stakeholder-Map zur Darstellung der Beziehungen zwischen Stakeholdern
* Power/Interest-Matrix zur Priorisierung von Stakeholdern