# 3. Kontext und Umfang

## Übersicht
Dieses Kapitel beschreibt die Umgebung und den Kontext des Systems. Es definiert die Systemgrenzen und zeigt die Beziehungen zu externen Systemen und Benutzern auf. Der Kontext grenzt das System von seiner Umgebung ab und hilft dabei, den Umfang des Systems zu verstehen.

## Inhalt
In diesem Kapitel werden die Grenzen des Systems, die Schnittstellen zu externen Systemen und Benutzern sowie die fachlichen und technischen Kontexte beschrieben.

## 3.1 Fachlicher Kontext

### Beschreibung

Der fachliche Kontext der Flashcard-Anwendung beschreibt die Beziehungen und Interaktionen mit Benutzern, externen Systemen und Datenquellen aus einer fachlichen Perspektive. Da es sich um eine persönliche Lernanwendung handelt, ist der Kontext relativ überschaubar, aber dennoch wichtig für das Verständnis der Systemgrenzen und -interaktionen.

* **Beschreibung aller relevanten Benutzergruppen und ihrer Rollen**
  * **Einzelnutzer (Entwickler & Anwender)**: Als primäre und einzige Benutzergruppe interagiert der Einzelnutzer direkt mit der Anwendung. Er erstellt, verwaltet und lernt mit den Karteikarten, konfiguriert die Lernalgorithmen nach seinen Bedürfnissen und analysiert seinen Lernfortschritt. Da die Anwendung für den persönlichen Gebrauch konzipiert ist, sind alle Funktionen auf die Bedürfnisse dieses einzelnen Nutzers zugeschnitten.

* **Beschreibung aller Nachbarsysteme und externer Partner**
  * **Externe Lernmaterialquellen**: Digitale Lehrbücher, Online-Kurse, Webseiten und andere Quellen, aus denen der Nutzer Lernmaterialien importieren oder manuell übertragen kann. Diese Quellen liefern den Rohstoff für die Erstellung von Karteikarten.
  * **KI-Dienste**: Externe oder lokal implementierte KI-Systeme, die für die automatische Generierung von Karteikarten, die Analyse von Lernmaterialien und die Erstellung personalisierter Lernpfade genutzt werden. Diese Dienste unterstützen den Nutzer bei der effizienten Erstellung von Lernmaterialien und der Optimierung des Lernprozesses.

* **Beschreibung der fachlichen Schnittstellen und Datenflüsse**
  * **Import von Lernmaterialien**: Datenfluss von externen Quellen in die Anwendung, wobei Rohtexte, strukturierte Daten, Bilder oder bereits formatierte Karteikarten importiert werden können. Dieser Prozess kann manuell durch den Nutzer oder teilautomatisiert durch Import-Funktionen erfolgen.
  * **KI-gestützte Verarbeitung**: Bidirektionaler Datenfluss zwischen der Anwendung und KI-Diensten. Die Anwendung sendet Rohmaterialien oder Lernstatistiken an die KI-Dienste und erhält generierte Karteikarten, Lernempfehlungen oder optimierte Lernpläne zurück.
  * **Export von Karteikarten**: Datenfluss von der Anwendung zu externen Systemen, wobei Karteikarten, Decks oder Lernstatistiken in standardisierten Formaten exportiert werden können. Dies ermöglicht die Sicherung von Daten oder die Nutzung in anderen Systemen.
  * **Lernprozess-Datenfluss**: Interner Datenfluss zwischen dem Nutzer und dem Spaced-Repetition-System. Der Nutzer beantwortet Karteikarten, das System analysiert die Antworten und passt den Wiederholungsplan entsprechend an.

* **Erläuterung der fachlichen Verantwortlichkeiten und Abhängigkeiten**
  * **Verantwortlichkeiten der Anwendung**:
    * Verwaltung und Organisation von Karteikarten und Decks
    * Implementierung und Optimierung des Spaced-Repetition-Algorithmus
    * Analyse und Visualisierung des Lernfortschritts
    * Bereitstellung einer benutzerfreundlichen Oberfläche für alle Lernaktivitäten
    * Sicherung der Lernmaterialien und -fortschritte
  * **Abhängigkeiten der Anwendung**:
    * Abhängigkeit von externen Lernmaterialien als Grundlage für Karteikarten
    * Abhängigkeit von KI-Diensten für die automatische Generierung und Optimierung von Lernmaterialien
    * Abhängigkeit von wissenschaftlichen Erkenntnissen zur Lernpsychologie für die Implementierung effektiver Lernalgorithmen

### Mögliche Visualisierungen (todo)
* Ein Kontextdiagramm könnte die Beziehungen zwischen dem Einzelnutzer, der Flashcard-Anwendung und den externen Systemen (Lernmaterialquellen, KI-Dienste) visualisieren.
* Ein Use-Case-Diagramm könnte die Hauptfunktionen der Anwendung aus Sicht des Nutzers darstellen, wie z.B. "Karteikarten erstellen", "Lernsitzung durchführen", "Lernfortschritt analysieren".
* Ein Datenflussdiagramm könnte die verschiedenen Datenströme zwischen dem Nutzer, der Anwendung und externen Systemen visualisieren.

## 3.2 Technischer Kontext

### Beschreibung

Der technische Kontext der Flashcard-Anwendung beschreibt die technischen Schnittstellen, Protokolle, Hardware-Umgebung und technischen Abhängigkeiten, die für die Implementierung und den Betrieb der Anwendung relevant sind. Da es sich um eine lokale Java-Anwendung für den persönlichen Gebrauch handelt, ist die technische Infrastruktur relativ überschaubar, aber dennoch wichtig für die Architekturentscheidungen.

* **Beschreibung aller technischen Schnittstellen zu externen Systemen**
  * **KI-Dienste-Schnittstellen**: RESTful APIs für die Kommunikation mit externen KI-Diensten oder Bibliotheken für die Integration lokaler KI-Modelle. Diese Schnittstellen ermöglichen die automatische Generierung von Karteikarten, die Analyse von Lernmaterialien und die Optimierung von Lernpfaden. Je nach gewähltem Ansatz können diese Dienste lokal eingebunden oder über das Internet angesprochen werden.
  * **Dateisystem-Schnittstellen**: Zugriff auf das lokale Dateisystem für den Import von Lernmaterialien aus verschiedenen Quellen (Textdateien, PDFs, Bilder) und den Export von Karteikarten und Lernstatistiken. Diese Schnittstellen nutzen die Standard-Java-IO-APIs.
  * **Import/Export-Schnittstellen**: Technische Schnittstellen für den Export erfasster Karteikarten und Decks bzw. für deren Import.
  * **Datenbank-Schnittstellen**: JDBC-Verbindungen zu einer lokalen relationalen Datenbank für die persistente Speicherung von Karteikarten, Decks, Lernfortschritten und Benutzereinstellungen. Diese Schnittstelle ist zentral für die Datenhaltung der Anwendung.

* **Spezifikation der verwendeten Protokolle und Datenformate**
  * **HTTP/HTTPS**: Für die Kommunikation mit externen Webdiensten, insbesondere KI-Diensten oder Online-Ressourcen für Lernmaterialien.
  * **JSON/XML**: Als primäre Datenformate für den Austausch von strukturierten Daten mit externen Systemen und für die Konfiguration der Anwendung.
  * **CSV/TSV**: Für den einfachen Import und Export von tabellarischen Daten wie Karteikarten-Sets oder Lernstatistiken.
  * **SQL**: Für die Interaktion mit der relationalen Datenbank zur Speicherung und Abfrage von Anwendungsdaten.
  * **Markdown/HTML**: Für die Formatierung von Karteikarten-Inhalten, um reichhaltige Textformatierung, Bilder und andere Medien zu unterstützen.

* **Beschreibung der Hardware-Umgebung und Infrastruktur**
  * **Lokaler Rechner**: Die Anwendung läuft auf einem Standard-PC oder Laptop mit ausreichend Arbeitsspeicher und Prozessorleistung für die Java-Laufzeitumgebung und lokale KI-Verarbeitung.
  * **Java Runtime Environment (JRE)**: Mindestens Java 11 oder höher als Laufzeitumgebung für die Anwendung.
  * **Embedded Webserver**: Ein in die Anwendung integrierter Webserver (z.B. Tomcat, Jetty, Undertow) für die Bereitstellung der Webanwendung auf localhost.
  * **Lokale Datenbank**: Eine eingebettete relationale Datenbank (z.B. H2, SQLite, Derby) für die persistente Datenspeicherung ohne externe Datenbankserver.
  * **Lokaler Dateispeicher**: Zugriff auf das lokale Dateisystem für die Speicherung von Medieninhalten, Backups und Import/Export-Dateien.
  * **Netzwerkverbindung**: Optional für den Zugriff auf externe KI-Dienste oder Online-Ressourcen, aber nicht zwingend erforderlich für den Grundbetrieb der Anwendung.

* **Erläuterung der technischen Abhängigkeiten und Einschränkungen**
  * **Abhängigkeiten**:
    * Java-Ökosystem: Abhängigkeit von Java als Programmiersprache und dem JRE als Laufzeitumgebung.
    * Web-Frameworks: Abhängigkeit von Java-basierten Web-Frameworks (z.B. Spring Boot, JavaFX für die GUI oder JavaServer Faces) für die Implementierung der Benutzeroberfläche.
    * ORM-Frameworks: Abhängigkeit vom Objektrelationalem Mapper Hibernate für die Datenbankinteraktion.
    * KI-Bibliotheken: Abhängigkeit von Java-kompatiblen KI-Bibliotheken oder externen KI-Diensten für die intelligenten Funktionen.
    * Spaced-Repetition-Algorithmen: Abhängigkeit von implementierten Algorithmen wie SuperMemo, Anki oder eigenen Varianten.
  * **Einschränkungen**:
    * Lokale Ausführung: Die Anwendung ist auf die Ausführung auf einem einzelnen Rechner beschränkt, ohne native Unterstützung für Geräteübergreifende Synchronisation.
    * Ressourcenlimitierung: Begrenzte Verfügbarkeit von Rechenleistung und Speicher für KI-Funktionen auf dem lokalen Rechner.
    * Offline-Fokus: Primäre Ausrichtung auf Offline-Nutzung mit optionaler Online-Funktionalität für externe Dienste.
    * Einzelbenutzer-Design: Die Architektur ist auf einen einzelnen Benutzer ausgerichtet, ohne Multi-User-Funktionalität oder Kollaborationsmöglichkeiten.

### Mögliche Visualisierungen (todo)
* Ein technisches Kontextdiagramm könnte die verschiedenen technischen Komponenten und ihre Verbindungen darstellen, einschließlich der Java-Anwendung, der eingebetteten Datenbank, des Webservers und externer Dienste.
* Ein Deployment-Diagramm könnte die Verteilung der Komponenten auf der lokalen Hardware visualisieren.
* Sequenzdiagramme könnten die technischen Kommunikationsabläufe zwischen der Anwendung und externen Systemen wie KI-Diensten darstellen.

## 3.3 Abgrenzung des Umfangs

### Beschreibung

Die Abgrenzung des Umfangs definiert klar, welche Funktionen und Verantwortlichkeiten innerhalb und außerhalb der Flashcard-Anwendung liegen. Diese Abgrenzung ist wichtig, um den Entwicklungsfokus zu schärfen und realistische Erwartungen an die Anwendung zu setzen. Da es sich um ein persönliches Lernprojekt handelt, ist eine klare Begrenzung des Funktionsumfangs besonders wichtig, um die Umsetzbarkeit zu gewährleisten.

* **Klare Definition der Systemgrenzen**
  * Die Flashcard-Anwendung ist ein lokales System, das auf einem einzelnen Rechner läuft und für die Nutzung durch eine einzelne Person konzipiert ist.
  * Die Anwendung fokussiert sich auf das Lernen mit Karteikarten nach dem wissenschaftlich fundierten Spaced-Repetition-Prinzip.
  * Das System umfasst die Erstellung, Verwaltung und Nutzung von Karteikarten sowie die Analyse des persönlichen Lernfortschritts.
  * Die Anwendung interagiert mit externen Systemen nur für den Import/Export von Lernmaterialien und die Nutzung von KI-Diensten zur Unterstützung des Lernprozesses.
  * Die Systemgrenze umschließt die Java-Anwendung, die lokale Datenbank und die lokale Dateispeicherung auf dem Rechner des Nutzers.

* **Auflistung der Funktionen, die im System umgesetzt werden**
  * **Karteikartenverwaltung**:
    * Erstellung und Bearbeitung von Karteikarten mit Frage-Antwort-Paaren
    * Unterstützung verschiedener Inhaltstypen (Text, Bilder, Formeln, Code-Snippets)
    * Organisation von Karteikarten in thematischen Decks und Kategorien
    * Tagging und Verschlagwortung von Karteikarten für bessere Auffindbarkeit
  * **Lernalgorithmus**:
    * Implementierung eines wissenschaftlich fundierten Spaced-Repetition-Algorithmus
    * Anpassung der Wiederholungsintervalle basierend auf individueller Lernleistung
    * Priorisierung von Karteikarten basierend auf Schwierigkeitsgrad und Lernfortschritt
    * Verschiedene Lernmodi (z.B. Neues Lernen, Wiederholen, Gemischt)
  * **KI-Unterstützung**:
    * Automatische Generierung von Karteikarten aus Texten oder Lernmaterialien
    * Intelligente Analyse von Lernmustern und Schwierigkeiten
    * Personalisierte Lernempfehlungen und Optimierung des Lernplans
    * Qualitätsprüfung und Verbesserungsvorschläge für selbsterstellte Karteikarten
  * **Lernfortschritt und Statistiken**:
    * Detaillierte Tracking des Lernfortschritts für jede Karteikarte und jedes Deck
    * Visualisierung von Lernkurven und Gedächtnisleistung über Zeit
    * Identifikation von Stärken und Schwächen im Lernprozess
    * Prognosen für zukünftigen Lernaufwand und Wiederholungsbedarf
  * **Import/Export**:
    * Import von Karteikarten aus verschiedenen Formaten (CSV, JSON, proprietäre Formate)
    * Export von Karteikarten und Lernstatistiken für Backup oder Weiterverarbeitung
    * Extraktion von Lernmaterialien aus Texten, PDFs oder Webseiten
  * **Benutzeroberfläche**:
    * Intuitives Web-Interface für alle Funktionen der Anwendung
    * Anpassbare Darstellung und Layouts für verschiedene Lernszenarien
    * Tastaturkürzel und effiziente Workflows für schnelles Lernen
    * Responsive Design für verschiedene Bildschirmgrößen auf dem lokalen Rechner

* **Auflistung der Funktionen, die bewusst nicht im System umgesetzt werden**
  * **Keine Multi-User-Funktionalität**:
    * Keine Unterstützung für mehrere Benutzerkonten
    * Keine Kollaborationsfunktionen für gemeinsames Lernen oder Karteikartenerstellung
    * Keine Freigabe- oder Teilen-Funktionen für Lernmaterialien
  * **Keine Online-Plattform**:
    * Keine Cloud-Synchronisation oder Online-Speicherung von Daten
    * Keine webbasierte Zugänglichkeit von verschiedenen Geräten
    * Keine Echtzeit-Kollaboration oder soziale Funktionen
  * **Keine umfassende Lernplattform**:
    * Keine vollständigen Kurse oder strukturierte Lernpfade über mehrere Themen hinweg
    * Keine Prüfungs- oder Zertifizierungsfunktionen
    * Keine Integration mit formalen Bildungssystemen oder -institutionen
  * **Keine mobile App**:
    * Keine native Unterstützung für mobile Geräte
    * Keine Offline-Synchronisation zwischen verschiedenen Geräten
    * Keine speziell für Mobilgeräte optimierte Benutzeroberfläche
  * **Keine kommerzielle Funktionalität**:
    * Keine Monetarisierungsfunktionen oder Premium-Features
    * Keine Werbung oder Sponsoring-Integration
    * Keine Marktplatzfunktionen für den Kauf oder Verkauf von Lernmaterialien
  * **Keine externe Integration**:
    * Keine tiefe Integration mit sozialen Medien oder Lernplattformen
    * Keine automatische Synchronisation mit externen Kalendern oder Produktivitätstools
    * Keine API für Drittanbieter-Integrationen

* **Begründung für die gewählte Abgrenzung**
  * **Fokus auf Kernfunktionalität**: Die Abgrenzung ermöglicht eine Konzentration auf die Kernfunktionalität des Spaced-Repetition-Lernens, was die Qualität und Effektivität dieser zentralen Funktionen sicherstellt.
  * **Persönlicher Gebrauch**: Da die Anwendung für den persönlichen Gebrauch eines einzelnen Nutzers konzipiert ist, sind Multi-User-Funktionen und Online-Kollaboration nicht erforderlich und würden unnötige Komplexität hinzufügen.
  * **Datenschutz und Kontrolle**: Die lokale Speicherung und Verarbeitung aller Daten gewährleistet maximale Kontrolle über die eigenen Lernmaterialien und -daten sowie Unabhängigkeit von externen Diensten.
  * **Ressourceneffizienz**: Die Beschränkung auf einen lokalen Betrieb und wesentliche Funktionen ermöglicht eine ressourceneffiziente Implementierung, die auch auf älteren oder leistungsschwächeren Rechnern gut funktioniert.
  * **Umsetzbarkeit**: Die klare Begrenzung des Funktionsumfangs erhöht die Wahrscheinlichkeit einer erfolgreichen Implementierung im Rahmen eines persönlichen Projekts mit begrenzten Ressourcen.
  * **Erweiterbarkeit**: Trotz der Abgrenzung ist die Architektur so konzipiert, dass sie in Zukunft bei Bedarf um weitere Funktionen erweitert werden kann, ohne das Kernsystem zu beeinträchtigen.

### Mögliche Visualisierungen (todo)
* Eine In/Out-Liste könnte klar darstellen, welche Funktionen innerhalb und außerhalb des Systemumfangs liegen.
* Eine Feature-Map könnte die umgesetzten Features im Kontext des gesamten möglichen Funktionsspektrums von Lernplattformen visualisieren.
* Ein Systemgrenzen-Diagramm könnte die Grenzen der Anwendung und ihre Schnittstellen zu externen Systemen darstellen.