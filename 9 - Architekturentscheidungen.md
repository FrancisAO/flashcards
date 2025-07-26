# 9. Architekturentscheidungen

## Übersicht
Dieses Kapitel dokumentiert die wichtigsten architektonischen Entscheidungen, die während der Entwicklung des Systems getroffen wurden. Es erklärt die Gründe für diese Entscheidungen, die betrachteten Alternativen und die Konsequenzen der getroffenen Wahl.

## Inhalt
In diesem Kapitel werden die wichtigsten Architekturentscheidungen in Form von Architecture Decision Records (ADRs) dokumentiert. Jede Entscheidung wird mit ihrem Kontext, den betrachteten Optionen, der getroffenen Entscheidung und deren Konsequenzen beschrieben.

## 9.1 Architekturentscheidung 1: Wahl von Java mit Spring Boot für das Backend

### Kontext
Die Flashcard-Anwendung ist als persönliche Lernanwendung konzipiert, die auf einem lokalen Rechner läuft und das Spaced-Repetition-Prinzip für effizientes Lernen nutzt. Für die Implementierung des Backends wurde eine Technologie benötigt, die folgende Anforderungen erfüllt:

* **Robustheit und Zuverlässigkeit**: Die Anwendung muss zuverlässig funktionieren, da sie für regelmäßiges Lernen genutzt wird und Lernfortschritte sicher gespeichert werden müssen.
* **Leistungsfähigkeit**: Die Implementierung komplexer Lernalgorithmen und KI-Funktionen erfordert ausreichende Rechenleistung.
* **Offline-Funktionalität**: Die Anwendung soll vollständig offline nutzbar sein, ohne Abhängigkeit von externen Diensten.
* **Wartbarkeit und Erweiterbarkeit**: Als langfristiges persönliches Projekt soll die Anwendung leicht zu warten und um neue Funktionen zu erweitern sein.
* **Plattformunabhängigkeit**: Die Anwendung soll auf verschiedenen Betriebssystemen lauffähig sein.

Die Entscheidung für die Backend-Technologie musste unter Berücksichtigung der begrenzten Ressourcen eines Ein-Personen-Projekts getroffen werden und gleichzeitig die definierten Qualitätsziele unterstützen, insbesondere Benutzerfreundlichkeit, Lerneffizienz und Zuverlässigkeit.

### Betrachtete Alternativen

#### Java mit Spring Boot
**Vorteile:**
* Starke Typisierung und Objektorientierung für robuste Anwendungen
* Umfangreiches Ökosystem mit zahlreichen Bibliotheken und Frameworks
* Plattformunabhängigkeit durch die Java Virtual Machine
* Spring Boot bietet Auto-Konfiguration und vereinfacht die Entwicklung
* Gute Integration mit relationalen Datenbanken über JPA/Hibernate
* Umfangreiche Unterstützung für Dependency Injection und Testbarkeit
* Langfristige Unterstützung und aktive Community

**Nachteile:**
* Höherer Ressourcenverbrauch im Vergleich zu leichtgewichtigeren Alternativen
* Steilere Lernkurve und mehr Boilerplate-Code
* Längere Startzeiten der Anwendung

#### Python mit Django/Flask
**Vorteile:**
* Einfachere Syntax und schnellere Entwicklung für Prototypen
* Hervorragende Unterstützung für KI und maschinelles Lernen
* Große Community und viele Bibliotheken
* Geringerer Boilerplate-Code

**Nachteile:**
* Geringere Typsicherheit kann zu Laufzeitfehlern führen
* Potenziell schlechtere Performance für rechenintensive Aufgaben
* Weniger strukturierte Architektur als Spring Boot
* Weniger reife ORM-Lösungen im Vergleich zu JPA/Hibernate

#### Node.js mit Express
**Vorteile:**
* JavaScript im gesamten Stack (Backend und Frontend)
* Große Anzahl an NPM-Paketen
* Gute Performance für I/O-intensive Anwendungen
* Leichtgewichtige Implementierung

**Nachteile:**
* Asynchrones Programmiermodell kann komplexer sein
* Geringere Typsicherheit ohne TypeScript
* Weniger reife ORM-Lösungen
* Potenziell schlechtere Performance für CPU-intensive Aufgaben wie komplexe Algorithmen

#### Kotlin
**Vorteile:**
* Moderne Sprachfeatures und weniger Boilerplate-Code als Java
* Vollständige Java-Interoperabilität
* Null-Sicherheit und funktionale Programmierung
* Kann mit Spring Boot verwendet werden

**Nachteile:**
* Kleinere Community als Java
* Potenziell steilere Lernkurve für Entwickler ohne Kotlin-Erfahrung
* Weniger Ressourcen und Beispiele verfügbar

### Entscheidung
Die Entscheidung fiel auf **Java 21 mit Spring Boot 3.x** als Backend-Technologie für die Flashcard-Anwendung.

Diese Entscheidung wurde getroffen, weil Java mit Spring Boot die beste Balance zwischen Robustheit, Leistungsfähigkeit, Wartbarkeit und Erweiterbarkeit bietet. Die starke Typisierung von Java reduziert Laufzeitfehler, während das umfangreiche Ökosystem und die Reife des Spring-Frameworks eine solide Grundlage für die Entwicklung komplexer Funktionen bieten.

Die Entscheidung unterstützt direkt die definierten Qualitätsziele:
* **Benutzerfreundlichkeit**: Spring Boot ermöglicht die Entwicklung einer reaktiven und effizienten API.
* **Lerneffizienz**: Java bietet die nötige Leistung für komplexe Spaced-Repetition-Algorithmen.
* **Zuverlässigkeit**: Die Typsicherheit und das Exception-Handling von Java erhöhen die Robustheit.
* **KI-Qualität**: Spring AI ermöglicht die Integration von KI-Funktionen.
* **Anpassungsfähigkeit**: Die modulare Architektur von Spring Boot erleichtert Erweiterungen.

Datum der Entscheidung: Q3 2025
Entscheider: Entwickler der Flashcard-Anwendung

### Konsequenzen

**Positive Konsequenzen:**
* Hohe Codequalität und Wartbarkeit durch starke Typisierung und klare Strukturierung
* Gute Testbarkeit durch Dependency Injection und umfangreiche Test-Frameworks
* Zukunftssicherheit durch langfristige Unterstützung und kontinuierliche Weiterentwicklung von Java und Spring
* Einfache Integration mit relationalen Datenbanken und anderen Systemen
* Umfangreiche Dokumentation und Community-Unterstützung bei Problemen
* Möglichkeit zur Nutzung des gesamten Java-Ökosystems für zusätzliche Funktionen

**Negative Konsequenzen:**
* Höherer initialer Entwicklungsaufwand im Vergleich zu skriptbasierten Sprachen
* Größerer Ressourcenverbrauch, insbesondere Arbeitsspeicher
* Längere Startzeiten der Anwendung, was die Entwicklungszyklen verlangsamen kann
* Steilere Lernkurve für Entwickler ohne Java/Spring-Erfahrung

**Maßnahmen zur Minderung negativer Auswirkungen:**
* Verwendung von Spring Boot DevTools zur Verkürzung der Entwicklungszyklen
* Optimierung der Anwendungskonfiguration für geringeren Ressourcenverbrauch
* Nutzung von Lombok zur Reduzierung von Boilerplate-Code
* Implementierung einer klaren, modularen Architektur zur Verbesserung der Wartbarkeit

**Auswirkungen auf andere Teile des Systems:**
* Die Wahl von Java beeinflusst die Integration mit dem React.js-Frontend, die über eine REST-API erfolgt
* Die Entscheidung für Spring Boot führt zur Verwendung eines eingebetteten Tomcat-Servers
* Die Java-Plattform ermöglicht eine nahtlose Integration mit der H2-Datenbank

**Notwendige Folgeentscheidungen:**
* Auswahl spezifischer Spring-Module für verschiedene Funktionsbereiche
* Entscheidung über die konkrete Implementierung des Spaced-Repetition-Algorithmus in Java
* Festlegung der API-Design-Prinzipien für die Kommunikation mit dem Frontend

### Mögliche Visualisierungen
* Entscheidungsmatrix mit Bewertung der verschiedenen Backend-Technologien anhand der Qualitätsziele
* Architekturdiagramm der Spring Boot-Anwendung mit ihren Hauptkomponenten
* Schichtenmodell der Backend-Architektur (Controller, Service, Repository, Domain)
* Performance-Vergleichsdiagramm der verschiedenen Backend-Technologien

## 9.2 Architekturentscheidung 2: Wahl von React.js für das Frontend

### Kontext
Für die Flashcard-Anwendung wird eine moderne, benutzerfreundliche und reaktive Benutzeroberfläche benötigt, die verschiedene Karteikarten-Typen, Lernmodi und Visualisierungen unterstützt. Die Frontend-Technologie muss folgende Anforderungen erfüllen:

* **Benutzerfreundlichkeit**: Die Oberfläche muss intuitiv bedienbar sein und effiziente Lernworkflows ermöglichen, da Benutzerfreundlichkeit ein sehr hohes Qualitätsziel der Anwendung ist.
* **Reaktivität**: Die Benutzeroberfläche muss sofort auf Benutzerinteraktionen reagieren, um ein flüssiges Lernerlebnis zu gewährleisten.
* **Komponentenbasierte Entwicklung**: Die UI soll modular aufgebaut sein, um die Wiederverwendbarkeit von Komponenten zu ermöglichen und die Wartbarkeit zu verbessern.
* **Unterstützung für komplexe UI-Elemente**: Die Oberfläche muss verschiedene Karteikarten-Typen, Diagramme für Lernstatistiken und interaktive Elemente unterstützen.
* **Integration mit dem Java-Backend**: Die Frontend-Technologie muss gut mit dem Java/Spring Boot-Backend kommunizieren können.
* **Zukunftssicherheit**: Die gewählte Technologie sollte langfristig unterstützt werden und eine aktive Community haben.

Die Entscheidung für die Frontend-Technologie musste unter Berücksichtigung der begrenzten Ressourcen eines Ein-Personen-Projekts getroffen werden und gleichzeitig die definierten Qualitätsziele unterstützen.

### Betrachtete Alternativen

#### React.js
**Vorteile:**
* Komponentenbasierte Architektur für modulare UI-Entwicklung
* Virtuelle DOM-Implementierung für effiziente UI-Updates
* Große Community und umfangreiches Ökosystem an Bibliotheken
* Gute TypeScript-Integration für typsichere Entwicklung
* Flexibilität bei der Architektur und Integration mit anderen Bibliotheken
* Hohe Performance durch effiziente Rendering-Strategien
* Breite Verfügbarkeit von UI-Komponentenbibliotheken wie Material-UI

**Nachteile:**
* Steilere Lernkurve im Vergleich zu einfacheren Frameworks
* Benötigt zusätzliche Bibliotheken für Routing, Zustandsmanagement etc.
* Höhere Komplexität durch JavaScript/TypeScript-Toolchain

#### Angular
**Vorteile:**
* Umfassendes Framework mit integriertem Routing, Formularvalidierung etc.
* Starke Typisierung durch native TypeScript-Integration
* Klare Strukturvorgaben und Konventionen
* Zwei-Wege-Datenbindung für einfachere Formularentwicklung
* Dependency Injection für bessere Testbarkeit

**Nachteile:**
* Steilere Lernkurve und mehr Boilerplate-Code
* Höhere Komplexität und Overhead für kleinere Anwendungen
* Potenziell überengineert für eine einfache Anwendung
* Weniger Flexibilität bei der Architektur

#### Vue.js
**Vorteile:**
* Einfachere Lernkurve als React oder Angular
* Gute Balance zwischen Struktur und Flexibilität
* Integrierte Funktionen für Routing und Zustandsmanagement
* Gute Performance und kleiner Footprint
* Klare und verständliche Dokumentation

**Nachteile:**
* Kleinere Community als React oder Angular
* Weniger fortgeschrittene TypeScript-Integration
* Weniger UI-Komponentenbibliotheken verfügbar
* Geringere Verbreitung in Unternehmen

#### JavaFX
**Vorteile:**
* Native Integration mit Java-Backend ohne separate Webserver
* Konsistente Entwicklungsumgebung (alles in Java)
* Gute Performance als Desktop-Anwendung
* Keine Notwendigkeit für Web-Technologien

**Nachteile:**
* Weniger moderne UI-Komponenten im Vergleich zu Web-Frameworks
* Schwierigere Anpassbarkeit und Styling
* Steilere Lernkurve für UI-Entwicklung
* Weniger aktive Community und Ressourcen
* Eingeschränktere Zukunftsperspektive

### Entscheidung
Die Entscheidung fiel auf **React.js mit TypeScript und Material-UI** als Frontend-Technologie für die Flashcard-Anwendung.

Diese Entscheidung wurde getroffen, weil React.js die beste Balance zwischen Flexibilität, Leistungsfähigkeit, Komponentenwiederverwendbarkeit und Community-Unterstützung bietet. Die komponentenbasierte Architektur von React ermöglicht eine modulare Entwicklung der Benutzeroberfläche, was die Wartbarkeit und Erweiterbarkeit verbessert. Die Verwendung von TypeScript erhöht die Typsicherheit und reduziert potenzielle Fehler, während Material-UI eine umfangreiche Sammlung von UI-Komponenten bietet, die den Material Design-Richtlinien folgen.

Die Entscheidung unterstützt direkt die definierten Qualitätsziele:
* **Benutzerfreundlichkeit**: React ermöglicht die Entwicklung einer intuitiven und reaktiven Benutzeroberfläche mit konsistentem Design durch Material-UI.
* **Lerneffizienz**: Die reaktive Natur von React unterstützt schnelle Feedback-Zyklen beim Lernen.
* **Anpassungsfähigkeit**: Die komponentenbasierte Architektur erleichtert Anpassungen und Erweiterungen.
* **Offline-Funktionalität**: React kann als Progressive Web App konfiguriert werden, um Offline-Funktionen zu unterstützen.

Datum der Entscheidung: Q3 2025
Entscheider: Entwickler der Flashcard-Anwendung

### Konsequenzen

**Positive Konsequenzen:**
* Hohe Flexibilität bei der Gestaltung der Benutzeroberfläche
* Effiziente Entwicklung durch Wiederverwendung von Komponenten
* Gute Performance durch das virtuelle DOM und effiziente Rendering-Strategien
* Starke Community-Unterstützung und umfangreiche Ressourcen
* Zukunftssicherheit durch kontinuierliche Weiterentwicklung von React
* Gute Integration mit dem Java/Spring Boot-Backend über REST-APIs
* Möglichkeit zur Nutzung zahlreicher React-Bibliotheken für spezifische Funktionen

**Negative Konsequenzen:**
* Zusätzliche Komplexität durch JavaScript/TypeScript-Toolchain
* Notwendigkeit eines Webservers für die Bereitstellung der Anwendung
* Höherer initialer Lernaufwand im Vergleich zu einfacheren Frameworks
* Potenzielle Fragmentierung durch schnelle Entwicklung im JavaScript-Ökosystem

**Maßnahmen zur Minderung negativer Auswirkungen:**
* Verwendung von Create React App für eine standardisierte Projektstruktur
* Einführung klarer Coding-Standards und Architekturrichtlinien
* Nutzung von ESLint und Prettier für konsistenten Code-Stil
* Integration des Frontend-Builds in den Maven-Build-Prozess
* Verwendung von TypeScript für bessere Typsicherheit und IDE-Unterstützung

**Auswirkungen auf andere Teile des Systems:**
* Die Wahl von React.js beeinflusst die API-Design-Entscheidungen im Backend
* Die Kommunikation zwischen Frontend und Backend erfolgt über REST-APIs
* Der eingebettete Tomcat-Server wird für die Bereitstellung sowohl des Backends als auch des Frontends verwendet

**Notwendige Folgeentscheidungen:**
* Auswahl einer Bibliothek für das Zustandsmanagement (Redux, MobX, Context API)
* Entscheidung über Routing-Strategien und Seitenstruktur
* Festlegung der Komponentenhierarchie und Datenflussarchitektur
* Auswahl von Bibliotheken für Diagramme und Visualisierungen

### Mögliche Visualisierungen
* Komponentenhierarchie-Diagramm der React-Anwendung
* Datenflussdiagramm zwischen Frontend-Komponenten und Backend-Services
* Wireframes der wichtigsten Benutzeroberflächen
* Vergleichsmatrix der verschiedenen Frontend-Technologien
* Architekturdiagramm der Frontend-Backend-Integration

## 9.3 Architekturentscheidung 3: Wahl einer eingebetteten H2-Datenbank

### Kontext
Für die Flashcard-Anwendung wird eine zuverlässige und effiziente Datenpersistenzlösung benötigt, um Karteikarten, Decks, Lernfortschritte und Benutzereinstellungen zu speichern. Die Datenbanklösung muss folgende Anforderungen erfüllen:

* **Offline-Funktionalität**: Die Anwendung soll vollständig offline nutzbar sein, ohne Abhängigkeit von externen Datenbankservern.
* **Einfache Integration**: Die Datenbank muss gut mit dem Java/Spring Boot-Backend zusammenarbeiten und eine nahtlose Integration ermöglichen.
* **Einfache Einrichtung und Wartung**: Als Ein-Personen-Projekt soll die Datenbank ohne komplexe Konfiguration oder Administration auskommen.
* **Zuverlässigkeit**: Die Daten müssen sicher gespeichert werden, um Lernfortschritte nicht zu verlieren.
* **SQL-Unterstützung**: Die Datenbank sollte SQL-Abfragen unterstützen, um von der Ausdrucksstärke und Flexibilität relationaler Abfragen zu profitieren.
* **Leichtgewichtigkeit**: Die Datenbanklösung sollte ressourcenschonend sein und auf einem lokalen Rechner effizient laufen.

Die Entscheidung für die Datenbanktechnologie musste unter Berücksichtigung der begrenzten Ressourcen eines Ein-Personen-Projekts getroffen werden und gleichzeitig die definierten Qualitätsziele unterstützen, insbesondere Zuverlässigkeit und Offline-Funktionalität.

### Betrachtete Alternativen

#### H2 (eingebettet)
**Vorteile:**
* Vollständig in Java implementiert und einfach in Java-Anwendungen zu integrieren
* Kann im eingebetteten Modus ohne separaten Server betrieben werden
* Unterstützt den SQL-Standard und bietet gute Kompatibilität mit anderen SQL-Datenbanken
* Bietet In-Memory-Modus für Tests und Entwicklung
* Sehr leichtgewichtig und ressourcenschonend
* Hervorragende Integration mit Spring Boot und JPA/Hibernate
* Enthält eine webbasierte Konsole zur Datenbankadministration

**Nachteile:**
* Begrenzte Skalierbarkeit für sehr große Datenmengen
* Weniger Features als vollwertige Datenbanksysteme
* Nicht für Multi-User-Szenarien mit gleichzeitigen Schreibzugriffen optimiert
* Eingeschränkte Werkzeugunterstützung im Vergleich zu etablierten Datenbanksystemen

#### SQLite
**Vorteile:**
* Sehr leichtgewichtig und dateibasiert
* Keine Serverinstallation notwendig
* Weit verbreitet und gut dokumentiert
* Gute Performance für Lesezugriffe
* Plattformunabhängig

**Nachteile:**
* Weniger nahtlose Java-Integration im Vergleich zu H2
* Eingeschränktere SQL-Unterstützung als H2
* Weniger optimale Integration mit JPA/Hibernate
* Begrenzte Unterstützung für gleichzeitige Schreibzugriffe
* Weniger Features für Entwicklung und Tests

#### MySQL/PostgreSQL
**Vorteile:**
* Vollständige Funktionalität eines ausgereiften Datenbanksystems
* Hervorragende Performance und Skalierbarkeit
* Umfangreiche Werkzeugunterstützung
* Robuste Transaktionsunterstützung
* Große Community und umfangreiche Dokumentation

**Nachteile:**
* Erfordert separate Installation und Konfiguration eines Datenbankservers
* Höherer Ressourcenverbrauch
* Komplexere Administration
* Nicht optimal für eine reine Offline-Anwendung
* Überdimensioniert für die Anforderungen einer persönlichen Anwendung

#### MongoDB
**Vorteile:**
* Flexibles Dokumentenmodell ohne festes Schema
* Gute Performance für bestimmte Abfragetypen
* Einfache Skalierbarkeit
* Gute Unterstützung für komplexe Datenstrukturen

**Nachteile:**
* Weniger geeignet für stark strukturierte, relationale Daten wie in der Flashcard-Anwendung
* Erfordert separate Installation und Konfiguration
* Weniger optimale Integration mit JPA
* Nicht-relationales Modell passt weniger gut zu den Entitätsbeziehungen der Anwendung
* Überdimensioniert für die Anforderungen einer persönlichen Anwendung

### Entscheidung
Die Entscheidung fiel auf die **eingebettete H2-Datenbank** als Persistenzlösung für die Flashcard-Anwendung.

Diese Entscheidung wurde getroffen, weil H2 die beste Balance zwischen Einfachheit, Integration mit Spring Boot, SQL-Funktionalität und Ressourceneffizienz bietet. Die Möglichkeit, H2 im eingebetteten Modus zu betreiben, ermöglicht eine vollständige Offline-Funktionalität ohne separate Datenbankinstallation, was ideal für eine persönliche Anwendung ist. Die gute Integration mit Spring Boot und JPA/Hibernate vereinfacht die Entwicklung und reduziert den Konfigurationsaufwand.

Die Entscheidung unterstützt direkt die definierten Qualitätsziele:
* **Zuverlässigkeit**: H2 bietet robuste Transaktionsunterstützung und Datensicherheit.
* **Offline-Funktionalität**: Als eingebettete Datenbank ermöglicht H2 vollständige Offline-Nutzung.
* **Benutzerfreundlichkeit**: Die einfache Einrichtung und Wartung verbessert die Gesamtbenutzererfahrung.
* **Anpassungsfähigkeit**: H2 unterstützt verschiedene Betriebsmodi (eingebettet, Server, In-Memory) für unterschiedliche Szenarien.

Datum der Entscheidung: Q3 2025
Entscheider: Entwickler der Flashcard-Anwendung

### Konsequenzen

**Positive Konsequenzen:**
* Einfache Integration in die Spring Boot-Anwendung mit minimaler Konfiguration
* Keine Notwendigkeit für separate Datenbankinstallation oder -administration
* Vollständige Offline-Funktionalität ohne externe Abhängigkeiten
* Gute Performance für die erwartete Datenmenge einer persönlichen Anwendung
* Flexibilität durch verschiedene Betriebsmodi (eingebettet, Server, In-Memory)
* Einfache Testbarkeit durch In-Memory-Modus für automatisierte Tests
* SQL-Kompatibilität ermöglicht komplexe Abfragen und einfache Migration zu anderen Datenbanken bei Bedarf

**Negative Konsequenzen:**
* Begrenzte Skalierbarkeit für sehr große Datenmengen (was für eine persönliche Anwendung jedoch kein Problem darstellt)
* Weniger Werkzeugunterstützung im Vergleich zu etablierten Datenbanksystemen
* Potenziell langsamere Performance bei sehr komplexen Abfragen im Vergleich zu optimierten Datenbanksystemen
* Eingeschränkte Möglichkeiten für verteilte Szenarien oder Multi-User-Zugriff

**Maßnahmen zur Minderung negativer Auswirkungen:**
* Implementierung eines regelmäßigen Backup-Mechanismus für die Datenbank-Datei
* Optimierung der Datenbankzugriffe durch effiziente Abfragen und Indizierung
* Verwendung von Connection Pooling für optimale Performance
* Monitoring des Datenbankwachstums und Performance-Tuning bei Bedarf

**Auswirkungen auf andere Teile des Systems:**
* Die Wahl von H2 beeinflusst die Datenzugriffsschicht und das Repository-Design
* Die eingebettete Natur der Datenbank vereinfacht die Deployment-Strategie
* Die SQL-Kompatibilität ermöglicht die Verwendung von JPA/Hibernate für das objekt-relationale Mapping

**Notwendige Folgeentscheidungen:**
* Festlegung der Datenbankschema-Migrations-Strategie (z.B. mit Flyway oder Liquibase)
* Entscheidung über Backup- und Recovery-Strategien
* Optimierung der Datenbankindizes für typische Abfragemuster
* Konfiguration der Verbindungspools und Transaktionseinstellungen

### Mögliche Visualisierungen
* ER-Diagramm des Datenbankschemas für die Flashcard-Anwendung
* Architekturdiagramm der Datenzugriffsschicht mit Repository-Pattern
* Performance-Vergleichsdiagramm der verschiedenen Datenbankalternativen
* Sequenzdiagramm typischer Datenbankinteraktionen in der Anwendung

## 9.4 Architekturentscheidung 4: Implementierung des Spaced-Repetition-Algorithmus

### Kontext
Das Kernkonzept der Flashcard-Anwendung ist die Nutzung des Spaced-Repetition-Prinzips für effizientes Lernen. Dieses Prinzip basiert auf der Erkenntnis, dass Informationen besser im Langzeitgedächtnis verankert werden, wenn sie in optimalen Zeitabständen wiederholt werden. Für die Implementierung dieses Lernalgorithmus wurden folgende Anforderungen definiert:

* **Wissenschaftliche Fundierung**: Der Algorithmus soll auf wissenschaftlich fundierten Erkenntnissen zur Gedächtnispsychologie basieren.
* **Adaptivität**: Die Wiederholungsintervalle sollen sich an die individuelle Lernleistung des Nutzers anpassen.
* **Effizienz**: Der Algorithmus soll die Lernzeit minimieren und gleichzeitig die Gedächtnisleistung maximieren.
* **Flexibilität**: Verschiedene Arten von Lernmaterial (Vokabeln, Fakten, Konzepte) sollen optimal unterstützt werden.
* **Erweiterbarkeit**: Der Algorithmus soll erweiterbar sein, um zukünftige Verbesserungen oder Anpassungen zu ermöglichen.
* **Transparenz**: Der Nutzer soll nachvollziehen können, warum bestimmte Karteikarten zu bestimmten Zeitpunkten wiederholt werden.

Die Entscheidung für einen spezifischen Spaced-Repetition-Algorithmus ist zentral für die Lerneffizienz der Anwendung und hat direkte Auswirkungen auf die Kernfunktionalität und das Nutzererlebnis.

### Betrachtete Alternativen

#### SuperMemo-SM2-Algorithmus
**Vorteile:**
* Weit verbreitet und gut dokumentiert
* Wissenschaftlich fundiert mit nachgewiesener Wirksamkeit
* Relativ einfach zu implementieren
* Gute Balance zwischen Komplexität und Effektivität
* Anpassbar durch Modifikation der Parameter
* Bewährt in vielen erfolgreichen Lernapplikationen

**Nachteile:**
* Älterer Algorithmus (1987), neuere Versionen (SM-15+) sind komplexer und potenziell effektiver
* Begrenzte Anpassungsfähigkeit an unterschiedliche Lernmaterialien
* Keine Berücksichtigung von Kontext oder semantischen Beziehungen zwischen Karteikarten
* Relativ einfaches Bewertungsmodell (0-5 Skala)

#### Anki-Algorithmus (modifizierter SM2)
**Vorteile:**
* Praktisch bewährt durch die große Nutzerbasis von Anki
* Enthält Verbesserungen gegenüber dem ursprünglichen SM2
* Gute Dokumentation und Open-Source-Implementierung verfügbar
* Unterstützt verschiedene Karteikarten-Typen
* Bietet Flexibilität durch Deck-spezifische Einstellungen

**Nachteile:**
* Weniger wissenschaftlich dokumentiert als SuperMemo
* Teilweise komplexere Implementierung
* Einige Anpassungen basieren auf Heuristiken statt empirischer Forschung
* Weniger optimiert für bestimmte Lerndomänen

#### Leitner-System
**Vorteile:**
* Sehr einfach zu verstehen und zu implementieren
* Intuitiv für Nutzer durch das Konzept von "Boxen" oder "Stufen"
* Geringer Rechenaufwand
* Gut geeignet für physische Karteikarten und digitale Umsetzung

**Nachteile:**
* Weniger präzise als algorithmusbasierte Ansätze
* Feste Wiederholungsintervalle statt adaptiver Anpassung
* Weniger effizient für langfristiges Lernen
* Begrenzte Personalisierung

#### Eigene Implementierung mit maschinellem Lernen
**Vorteile:**
* Maximale Anpassung an individuelle Lernmuster
* Potenzial für höhere Lerneffizienz durch Nutzung moderner ML-Techniken
* Möglichkeit, semantische Beziehungen zwischen Karteikarten zu berücksichtigen
* Kontinuierliche Verbesserung durch Lernen aus Nutzungsdaten

**Nachteile:**
* Hohe Komplexität in Entwicklung und Wartung
* Erfordert große Datenmengen für effektives Training
* Schwieriger zu erklären und nachzuvollziehen für Nutzer
* Höherer Rechenaufwand
* Risiko von Überanpassung oder unerwarteten Ergebnissen

### Entscheidung
Die Entscheidung fiel auf den **SuperMemo-SM2-Algorithmus als Basis mit eigenen Anpassungen** für die Implementierung des Spaced-Repetition-Systems in der Flashcard-Anwendung.

Diese Entscheidung wurde getroffen, weil der SM2-Algorithmus eine solide, wissenschaftlich fundierte Grundlage bietet, die gleichzeitig einfach genug für eine effiziente Implementierung und spätere Anpassung ist. Die eigenen Anpassungen umfassen:

1. Erweiterung der Bewertungsskala für differenzierteres Feedback
2. Dynamische Anpassung der Algorithmus-Parameter basierend auf Lernstatistiken
3. Berücksichtigung des Karteikarten-Typs bei der Berechnung der Wiederholungsintervalle
4. Integration von Kontext-Faktoren wie Tageszeit, Lernumgebung und vorherige Lernleistung

Die Entscheidung unterstützt direkt die definierten Qualitätsziele:
* **Lerneffizienz**: Der SM2-Algorithmus ist auf optimale Lerneffizienz ausgerichtet.
* **Benutzerfreundlichkeit**: Die Transparenz und Nachvollziehbarkeit des Algorithmus verbessert das Nutzererlebnis.
* **Anpassungsfähigkeit**: Die gewählte Basis erlaubt schrittweise Verbesserungen und Anpassungen.
* **Zuverlässigkeit**: Der bewährte Algorithmus bietet vorhersehbare und stabile Ergebnisse.

Datum der Entscheidung: Q3 2025
Entscheider: Entwickler der Flashcard-Anwendung

### Konsequenzen

**Positive Konsequenzen:**
* Solide wissenschaftliche Grundlage für die Kernfunktionalität der Anwendung
* Gute Balance zwischen Implementierungsaufwand und Lerneffizienz
* Transparenz und Nachvollziehbarkeit des Lernprozesses für den Nutzer
* Möglichkeit zur schrittweisen Verbesserung und Anpassung des Algorithmus
* Einfache Integration in die Java-Codebasis
* Geringer Rechenaufwand, was die Performance der Anwendung verbessert
* Bewährter Ansatz mit vorhersehbaren Ergebnissen

**Negative Konsequenzen:**
* Möglicherweise nicht so effizient wie neuere, komplexere Algorithmen (z.B. SuperMemo-SM17+)
* Begrenzte Berücksichtigung von semantischen Beziehungen zwischen Karteikarten
* Erfordert manuelle Kalibrierung und Anpassung der Parameter
* Weniger personalisiert als ML-basierte Ansätze

**Maßnahmen zur Minderung negativer Auswirkungen:**
* Implementierung eines Monitoring-Systems zur kontinuierlichen Evaluation der Lerneffizienz
* Regelmäßige Überprüfung und Anpassung der Algorithmus-Parameter basierend auf Nutzungsdaten
* Modulare Implementierung, die zukünftige Erweiterungen oder den Austausch des Algorithmus erleichtert
* Integration von A/B-Tests für verschiedene Algorithmus-Varianten

**Auswirkungen auf andere Teile des Systems:**
* Die Wahl des Algorithmus beeinflusst das Datenmodell für Karteikarten und Lernfortschritte
* Die Benutzeroberfläche muss die vom Algorithmus verwendete Bewertungsskala unterstützen
* Die Statistik- und Visualisierungskomponenten müssen die vom Algorithmus generierten Daten interpretieren können
* Die Datenbankstruktur muss die für den Algorithmus relevanten Informationen effizient speichern

**Notwendige Folgeentscheidungen:**
* Festlegung der genauen Bewertungsskala und Bewertungskriterien
* Entscheidung über die Visualisierung der Wiederholungsintervalle für den Nutzer
* Festlegung der Strategie zur Evaluation und Verbesserung des Algorithmus
* Entscheidung über die Integration von Lernkontext-Faktoren

### Mögliche Visualisierungen
* Flussdiagramm des SM2-Algorithmus mit den implementierten Anpassungen
* Grafische Darstellung der Wiederholungsintervalle für verschiedene Bewertungsstufen
* Vergleichsdiagramm der Lerneffizienz verschiedener Algorithmus-Varianten
* Klassendiagramm der Algorithmus-Implementierung und Integration in die Anwendung
* Vergessenskurve mit und ohne Spaced-Repetition zur Verdeutlichung der Effektivität