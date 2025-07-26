# 4. Lösungsstrategie

## Übersicht
Dieses Kapitel beschreibt die grundlegenden Entscheidungen und Lösungsansätze, die die Architektur des Systems prägen. Es stellt die Verbindung zwischen den Anforderungen und Randbedingungen einerseits und den konkreten Architekturentscheidungen andererseits her.

## Inhalt
In diesem Kapitel werden die wichtigsten strategischen Entscheidungen und Lösungsansätze dokumentiert, die die Grundlage für die Architektur des Systems bilden.

## 4.1 Technologieentscheidungen

### Beschreibung

Die Technologieentscheidungen für die Flashcard-Anwendung wurden unter Berücksichtigung der Qualitätsziele, des persönlichen Nutzungskontexts und der langfristigen Wartbarkeit getroffen. Als lokale Anwendung für den persönlichen Gebrauch steht die Zuverlässigkeit, Lerneffizienz und Benutzerfreundlichkeit im Vordergrund.

#### Übersicht der gewählten Technologien

**Backend:**
* **Java 21**: Als Hauptprogrammiersprache für das Backend aufgrund der Plattformunabhängigkeit, Typsicherheit und umfangreichen Ökosystems.
* **Spring Boot 3.x**: Als Framework für die Entwicklung der Anwendung, das eine schnelle Entwicklung, Dependency Injection und umfangreiche Integrationen ermöglicht.
* **H2 Datenbank**: Als eingebettete relationale Datenbank für die lokale Datenspeicherung ohne externe Datenbankserver.
* **Hibernate/JPA**: Für das objekt-relationale Mapping und die Datenbankinteraktion.
* **Apache Lucene**: Für effiziente Volltextsuche in den Karteikarten und Lernmaterialien.
* **Spring AI 1.1.0-SNAPSHOT**: Für die Integration von KI-Funktionen.

**Frontend:**
* **React.js**: Für die Entwicklung einer reaktiven und modernen Benutzeroberfläche.
* **TypeScript**: Für typsichere JavaScript-Entwicklung und bessere Wartbarkeit.
* **Material-UI**: Als UI-Komponentenbibliothek für ein konsistentes und ansprechendes Design.
* **Chart.js**: Für die Visualisierung von Lernstatistiken und Fortschrittsdiagrammen.
* **Axios**: Für die HTTP-Kommunikation zwischen Frontend und Backend.

**Infrastruktur:**
* **Embedded Tomcat**: Als in Spring Boot integrierter Webserver für die Bereitstellung der Anwendung.
* **Gradle**: Als Build-Tool und für das Dependency-Management.
* **JUnit 5 & Mockito**: Für automatisierte Tests und Qualitätssicherung.
* **Logback**: Für das Logging und die Fehlerdiagnose.

#### Begründung für die Auswahl der Technologien

**Java als Hauptprogrammiersprache:**
* **Plattformunabhängigkeit**: Java-Anwendungen können auf verschiedenen Betriebssystemen ausgeführt werden, was die Flexibilität erhöht.
* **Typsicherheit**: Das starke Typsystem von Java reduziert Laufzeitfehler und erhöht die Zuverlässigkeit der Anwendung.
* **Umfangreiches Ökosystem**: Java bietet eine Vielzahl von Bibliotheken und Frameworks, die die Entwicklung beschleunigen und die Integration verschiedener Funktionen erleichtern.
* **Leistungsfähigkeit**: Java bietet ausreichende Performance für rechenintensive Aufgaben wie die KI-gestützte Karteikartengenerierung und den Spaced-Repetition-Algorithmus.
* **Langfristige Unterstützung**: Java wird kontinuierlich weiterentwickelt und hat eine große Community, was die langfristige Wartbarkeit sicherstellt.

**Spring Boot als Framework:**
* **Produktivität**: Spring Boot ermöglicht eine schnelle Entwicklung durch Auto-Konfiguration und Starter-Pakete.
* **Modularität**: Die komponentenbasierte Architektur von Spring erleichtert die Strukturierung der Anwendung und die Trennung von Zuständigkeiten.
* **Integrationen**: Spring Boot bietet nahtlose Integration mit verschiedenen Technologien wie Datenbanken, Caching und Sicherheit.
* **Testbarkeit**: Spring unterstützt umfangreiche Testmöglichkeiten, was die Qualitätssicherung erleichtert.

**H2 als eingebettete Datenbank:**
* **Einfache Einrichtung**: H2 kann ohne separate Installation als eingebettete Datenbank verwendet werden.
* **SQL-Kompatibilität**: H2 unterstützt den SQL-Standard und bietet gleichzeitig spezifische Optimierungen.
* **In-Memory-Option**: Für Testszenarien kann H2 im Arbeitsspeicher betrieben werden, was die Entwicklung beschleunigt.
* **Datensicherheit**: H2 bietet Funktionen für Backup und Wiederherstellung, was die Datensicherheit erhöht.

**React.js für das Frontend:**
* **Komponentenbasierte Architektur**: React ermöglicht die Entwicklung wiederverwendbarer UI-Komponenten, was die Wartbarkeit verbessert.
* **Virtuelle DOM**: Die effiziente Aktualisierung des DOM durch React verbessert die Performance der Benutzeroberfläche.
* **Reaktivität**: React ermöglicht eine reaktive Benutzeroberfläche, die sofort auf Benutzerinteraktionen reagiert.
* **Große Community**: Die umfangreiche Community und das Ökosystem von React bieten Lösungen für viele UI-Herausforderungen.

#### Beziehung zu den Qualitätszielen

Die gewählten Technologien unterstützen direkt die in Kapitel 1 definierten Qualitätsziele:

**Benutzerfreundlichkeit (sehr hoch):**
* React.js und Material-UI ermöglichen eine intuitive, responsive Benutzeroberfläche mit konsistentem Design.
* TypeScript verbessert die Codequalität und reduziert Fehler in der UI.
* Spring Boot ermöglicht die Entwicklung einer API, die effiziente Workflows unterstützt.

**Lerneffizienz (sehr hoch):**
* Java bietet die nötige Leistung für die Implementierung komplexer Spaced-Repetition-Algorithmen.
* Spring AI ermöglicht die Integration von KI-Funktionen für personalisierte Lernpfade.
* H2 und Hibernate gewährleisten eine effiziente Datenspeicherung und -abfrage für Lernstatistiken.

**Zuverlässigkeit (hoch):**
* Java's Typsicherheit und Exception-Handling reduzieren Laufzeitfehler.
* H2 bietet zuverlässige Datenspeicherung mit Transaktionssicherheit.
* JUnit und Mockito ermöglichen umfassende Tests zur Qualitätssicherung.
* Logback unterstützt detailliertes Logging für die Fehlerdiagnose.

**KI-Qualität (hoch):**
* Spring AI ermöglicht die Integration hochwertiger KI-Modelle für die Karteikartengenerierung.
* Java bietet die nötige Leistung für lokale KI-Verarbeitung.

**Anpassungsfähigkeit (mittel):**
* Die modulare Architektur von Spring Boot ermöglicht einfache Erweiterungen.
* React-Komponenten können flexibel wiederverwendet und angepasst werden.
* Die Trennung von Frontend und Backend ermöglicht unabhängige Änderungen.

**Offline-Funktionalität (mittel):**
* H2 als eingebettete Datenbank ermöglicht vollständige Offline-Funktionalität.
* React kann als Progressive Web App konfiguriert werden, um Offline-Funktionen zu unterstützen.

**Wartbarkeit (mittel):**
* Java und TypeScript bieten starke Typisierung für bessere Codequalität.
* Spring Boot fördert eine saubere Architektur mit Trennung der Zuständigkeiten.
* Gradle vereinfacht das Dependency-Management und den Build-Prozess.
* JUnit und Mockito ermöglichen automatisierte Tests für einfachere Wartung.

#### Betrachtete Alternativen und Gründe für deren Ablehnung

**Alternative Backend-Technologien:**

* **Python mit Django/Flask:**
  * **Vorteile**: Einfachere KI-Integration, schnellere Entwicklung für Prototypen.
  * **Nachteile**: Geringere Typsicherheit, potenziell schlechtere Performance für den Spaced-Repetition-Algorithmus, weniger strukturierte Architektur.
  * **Ablehnungsgrund**: Die Typsicherheit und strukturierte Architektur von Java wurden für ein langfristiges Projekt als wichtiger erachtet.

* **Node.js mit Express:**
  * **Vorteile**: JavaScript im gesamten Stack, potenziell schnellere Entwicklung.
  * **Nachteile**: Asynchrones Programmiermodell kann komplexer sein, geringere Typsicherheit ohne TypeScript, weniger reife ORM-Lösungen.
  * **Ablehnungsgrund**: Java bietet bessere Unterstützung für komplexe Domänenmodelle und Datenbankinteraktionen.

* **Kotlin:**
  * **Vorteile**: Moderne Sprachfeatures, Java-Interoperabilität, weniger Boilerplate-Code.
  * **Nachteile**: Kleinere Community als Java, potenziell steilere Lernkurve.
  * **Ablehnungsgrund**: Die größere Community und umfangreichere Dokumentation von Java wurden als Vorteil für ein persönliches Projekt angesehen.

**Alternative Datenbanken:**

* **SQLite:**
  * **Vorteile**: Sehr leichtgewichtig, einfache Dateibasierte Speicherung.
  * **Nachteile**: Weniger Features als H2, eingeschränktere SQL-Unterstützung.
  * **Ablehnungsgrund**: H2 bietet bessere Integration mit Java/Hibernate und mehr Funktionen bei ähnlicher Einfachheit.

* **MongoDB:**
  * **Vorteile**: Flexibles Dokumentenmodell, gute Skalierbarkeit.
  * **Nachteile**: Überdimensioniert für eine lokale Einzelbenutzer-Anwendung, weniger strukturierte Daten.
  * **Ablehnungsgrund**: Relationale Datenbanken passen besser zum strukturierten Charakter der Karteikarten und Lernstatistiken.

**Alternative Frontend-Technologien:**

* **Angular:**
  * **Vorteile**: Umfassenderes Framework, stärkere Konventionen.
  * **Nachteile**: Steilere Lernkurve, mehr Boilerplate-Code, potenziell überengineert für eine einfache Anwendung.
  * **Ablehnungsgrund**: React bietet mehr Flexibilität und eine flachere Lernkurve für ein persönliches Projekt.

* **Vue.js:**
  * **Vorteile**: Einfachere Lernkurve, gute Balance zwischen Struktur und Flexibilität.
  * **Nachteile**: Kleinere Community als React, weniger fortgeschrittene Typunterstützung.
  * **Ablehnungsgrund**: Die größere Community und bessere TypeScript-Integration von React wurden bevorzugt.

* **JavaFX:**
  * **Vorteile**: Native Java-Integration, Desktop-Anwendung ohne Webserver.
  * **Nachteile**: Weniger moderne UI-Komponenten, schwierigere Anpassbarkeit, steilere Lernkurve.
  * **Ablehnungsgrund**: Die Weboberfläche bietet mehr Flexibilität in der Gestaltung und bessere Zukunftssicherheit.

### Mögliche Visualisierungen (todo)
* Ein Technologie-Stack-Diagramm könnte die verschiedenen Schichten der Anwendung (Frontend, Backend, Datenbank) und die jeweils eingesetzten Technologien visualisieren.
* Eine Entscheidungsmatrix könnte die Bewertung verschiedener Technologiealternativen anhand der Qualitätsziele darstellen.
* Ein Radar-Diagramm könnte zeigen, wie gut die gewählten Technologien die verschiedenen Qualitätsziele unterstützen.

## 4.2 Architekturmuster und -prinzipien

### Beschreibung

Die Architektur der Flashcard-Anwendung basiert auf bewährten Mustern und Prinzipien, die eine klare Strukturierung, gute Wartbarkeit und Erweiterbarkeit gewährleisten. Die gewählten Architekturmuster und -prinzipien unterstützen die definierten Qualitätsziele und ermöglichen eine effiziente Entwicklung der Anwendung.

#### Übersicht der angewendeten Architekturmuster

**Schichtenarchitektur (Layered Architecture):**
Die Anwendung ist in klar definierte Schichten unterteilt, wobei jede Schicht spezifische Verantwortlichkeiten hat und nur mit angrenzenden Schichten kommuniziert:

* **Präsentationsschicht**: Enthält die React-basierte Benutzeroberfläche und die REST-Controller für die API.
* **Anwendungsschicht**: Enthält die Geschäftslogik, Services und den Spaced-Repetition-Algorithmus.
* **Domänenschicht**: Definiert die Kernentitäten und Geschäftsregeln der Anwendung.
* **Datenzugriffsschicht**: Verantwortlich für die Persistenz und den Datenbankzugriff.

**Model-View-Controller (MVC):**
Innerhalb der Präsentationsschicht wird das MVC-Muster angewendet:

* **Model**: Repräsentiert die Daten und Geschäftslogik der Anwendung.
* **View**: Die React-Komponenten, die die Benutzeroberfläche darstellen.
* **Controller**: Die REST-Controller, die Anfragen entgegennehmen und an die Services weiterleiten.

**Repository-Pattern:**
Für den Datenzugriff wird das Repository-Pattern verwendet:

* Abstrahiert die Datenzugriffslogik von der Geschäftslogik.
* Bietet eine einheitliche Schnittstelle für den Zugriff auf Daten, unabhängig von der konkreten Datenquelle.
* Erleichtert das Testen durch die Möglichkeit, Repositories zu mocken.

**Dependency Injection:**
Spring Boot's Dependency Injection wird genutzt, um:

* Lose Kopplung zwischen Komponenten zu erreichen.
* Die Testbarkeit zu verbessern.
* Die Konfiguration von Komponenten zu zentralisieren.

**Service-Pattern:**
Die Geschäftslogik ist in Services organisiert:

* Jeder Service ist für einen bestimmten Funktionsbereich verantwortlich (z.B. KarteikartenverwaltungsService, LernalgorithmusService).
* Services kapseln komplexe Geschäftslogik und orchestrieren die Interaktion zwischen verschiedenen Komponenten.

**Event-Driven Architecture (teilweise):**
Für bestimmte Aspekte der Anwendung wird ein ereignisbasierter Ansatz verwendet:

* Lernfortschritte und Benutzerinteraktionen werden als Ereignisse modelliert.
* Der Spaced-Repetition-Algorithmus reagiert auf diese Ereignisse, um den Lernplan anzupassen.
* Ermöglicht eine lose Kopplung zwischen der Benutzerinteraktion und der Algorithmus-Logik.

#### Beschreibung der wichtigsten Architekturprinzipien

**Separation of Concerns (SoC):**
* Klare Trennung von Verantwortlichkeiten zwischen verschiedenen Komponenten und Schichten.
* Jede Komponente hat einen klar definierten Zweck und Aufgabenbereich.
* Reduziert Komplexität und verbessert Wartbarkeit durch Modularisierung.

**SOLID-Prinzipien:**
* **Single Responsibility Principle (SRP)**: Jede Klasse hat nur eine Verantwortung, z.B. ist der KarteikartenService nur für die Verwaltung von Karteikarten zuständig.
* **Open/Closed Principle (OCP)**: Komponenten sind offen für Erweiterung, aber geschlossen für Modifikation, z.B. können neue Karteikarten-Typen hinzugefügt werden, ohne bestehenden Code zu ändern.
* **Liskov Substitution Principle (LSP)**: Subtypen können ihre Basistypen ersetzen, z.B. können verschiedene Implementierungen des Repository-Interfaces ausgetauscht werden.
* **Interface Segregation Principle (ISP)**: Spezifische, klientenorientierte Interfaces statt großer, allgemeiner Interfaces, z.B. separate Repositories für verschiedene Entitäten.
* **Dependency Inversion Principle (DIP)**: Abhängigkeit von Abstraktionen statt konkreten Implementierungen, z.B. Services hängen von Repository-Interfaces ab, nicht von konkreten Implementierungen.

**Domain-Driven Design (DDD) Konzepte:**
* Fokus auf die Domäne und Geschäftslogik.
* Verwendung einer Ubiquitous Language (einheitliche Sprache) in Code und Dokumentation.
* Klare Abgrenzung des Domänenmodells von technischen Aspekten.
* Verwendung von Value Objects, Entities und Aggregates zur Modellierung der Domäne.

**Fail-Fast-Prinzip:**
* Frühzeitiges Erkennen und Melden von Fehlern.
* Validierung von Eingaben an den Systemgrenzen.
* Klare Fehlerbehandlung und aussagekräftige Fehlermeldungen.

**Don't Repeat Yourself (DRY):**
* Vermeidung von Code-Duplikation durch Abstraktion und Wiederverwendung.
* Zentralisierung von gemeinsam genutzter Logik in Services oder Utilities.

**Convention over Configuration:**
* Nutzung von Spring Boot's Konventionen zur Reduzierung von Konfigurationsaufwand.
* Standardisierte Namenskonventionen und Projektstruktur.

#### Begründung für die Auswahl der Muster und Prinzipien

**Schichtenarchitektur:**
* **Begründung**: Die klare Trennung von Verantwortlichkeiten erleichtert die Wartung und das Verständnis der Anwendung. Jede Schicht kann unabhängig entwickelt, getestet und geändert werden.
* **Vorteile**: Verbesserte Wartbarkeit, klare Struktur, einfachere Testbarkeit, Möglichkeit zur Parallelentwicklung.

**Repository-Pattern:**
* **Begründung**: Die Abstraktion des Datenzugriffs ermöglicht es, die Datenzugriffslogik von der Geschäftslogik zu trennen und erleichtert das Testen.
* **Vorteile**: Austauschbarkeit der Datenquelle, verbesserte Testbarkeit, zentralisierte Datenzugriffslogik.

**Dependency Injection:**
* **Begründung**: Ermöglicht lose Kopplung und verbessert die Testbarkeit durch die Möglichkeit, Abhängigkeiten zu mocken.
* **Vorteile**: Flexibilität bei der Konfiguration, verbesserte Testbarkeit, reduzierte Abhängigkeiten zwischen Komponenten.

**Service-Pattern:**
* **Begründung**: Kapselt komplexe Geschäftslogik und orchestriert die Interaktion zwischen verschiedenen Komponenten.
* **Vorteile**: Klare Verantwortlichkeiten, Wiederverwendbarkeit von Geschäftslogik, verbesserte Testbarkeit.

**SOLID-Prinzipien:**
* **Begründung**: Diese Prinzipien fördern eine saubere, wartbare und erweiterbare Codestruktur.
* **Vorteile**: Verbesserte Wartbarkeit, einfachere Erweiterbarkeit, reduzierte Fehleranfälligkeit.

**Domain-Driven Design:**
* **Begründung**: Der Fokus auf die Domäne und Geschäftslogik ist besonders wichtig für eine Anwendung, die komplexe Lernalgorithmen implementiert.
* **Vorteile**: Besseres Verständnis der Domäne, klarere Modellierung, engere Abstimmung zwischen Code und Fachlichkeit.

#### Beziehung zu den Qualitätszielen

Die gewählten Architekturmuster und -prinzipien unterstützen direkt die in Kapitel 1 definierten Qualitätsziele:

**Benutzerfreundlichkeit (sehr hoch):**
* Die MVC-Architektur ermöglicht eine klare Trennung zwischen Datenmodell und Präsentation, was eine flexible und benutzerfreundliche UI ermöglicht.
* Die Schichtenarchitektur erlaubt es, die Präsentationsschicht unabhängig zu optimieren, ohne die Geschäftslogik zu beeinflussen.

**Lerneffizienz (sehr hoch):**
* Das Service-Pattern ermöglicht die Kapselung komplexer Lernalgorithmen in dedizierten Services.
* Domain-Driven Design hilft, die Lerndomäne präzise zu modellieren und den Spaced-Repetition-Algorithmus optimal zu implementieren.
* Die ereignisbasierte Architektur unterstützt die dynamische Anpassung des Lernplans basierend auf Benutzerinteraktionen.

**Zuverlässigkeit (hoch):**
* Das Fail-Fast-Prinzip hilft, Fehler frühzeitig zu erkennen und zu behandeln.
* Die Schichtenarchitektur isoliert Fehler in einzelnen Schichten und verhindert deren Ausbreitung.
* Das Repository-Pattern zentralisiert den Datenzugriff und reduziert potenzielle Fehlerquellen.

**KI-Qualität (hoch):**
* Die Service-Architektur ermöglicht die Integration von KI-Diensten als separate, austauschbare Komponenten.
* Die klare Trennung von Verantwortlichkeiten erleichtert die Integration und Verbesserung von KI-Funktionen.

**Anpassungsfähigkeit (mittel):**
* SOLID-Prinzipien, insbesondere das Open/Closed Principle, fördern die Erweiterbarkeit der Anwendung.
* Die Schichtenarchitektur ermöglicht Änderungen in einer Schicht, ohne andere Schichten zu beeinflussen.
* Dependency Injection erleichtert den Austausch von Komponenten.

**Offline-Funktionalität (mittel):**
* Das Repository-Pattern abstrahiert den Datenzugriff und ermöglicht eine nahtlose Offline-Funktionalität.
* Die Schichtenarchitektur unterstützt die klare Trennung zwischen Online- und Offline-Funktionalität.

**Wartbarkeit (mittel):**
* Separation of Concerns und SOLID-Prinzipien verbessern die Wartbarkeit durch klare Strukturierung und Verantwortlichkeiten.
* DRY-Prinzip reduziert Redundanz und erleichtert Änderungen.
* Convention over Configuration reduziert den Konfigurationsaufwand und fördert Konsistenz.

### Mögliche Visualisierungen (todo)
* Ein Architekturübersichtsdiagramm könnte die verschiedenen Schichten und ihre Beziehungen darstellen.
* Ein Komponentendiagramm könnte die wichtigsten Komponenten und ihre Interaktionen visualisieren.
* Ein Klassendiagramm könnte die Anwendung der SOLID-Prinzipien und des Repository-Patterns zeigen.
* Ein Sequenzdiagramm könnte den Ablauf einer typischen Lerninteraktion durch die verschiedenen Architekturschichten darstellen.

## 4.3 Organisatorische Entscheidungen

### Beschreibung

Die organisatorischen Entscheidungen für die Flashcard-Anwendung sind auf die Bedürfnisse eines Ein-Personen-Projekts zugeschnitten. Sie sollen einen effizienten Entwicklungsprozess ermöglichen, der trotz begrenzter Ressourcen zu einem qualitativ hochwertigen Ergebnis führt. Die gewählten Prozesse und Strukturen unterstützen die kontinuierliche Verbesserung der Anwendung und die Anpassung an neue Anforderungen.

#### Beschreibung des Entwicklungsprozesses

Die Entwicklung der Flashcard-Anwendung ist an die Bedürfnisse eines Ein-Personen-Projekts angepasst:

**Inkrementelle Entwicklung:**
* Die Anwendung wird inkrementell entwickelt, beginnend mit einem Minimum Viable Product (MVP), das die grundlegenden Funktionen für das Lernen mit Karteikarten bietet.
* Weitere Funktionen werden schrittweise hinzugefügt, basierend auf den Erfahrungen mit der Nutzung der Anwendung.
* Dieser Ansatz ermöglicht es, frühzeitig Wert zu schaffen und die Anwendung kontinuierlich zu verbessern.

**Kontinuierliche Integration:**
* Neue Funktionen werden kontinuierlich in die Anwendung integriert und getestet.
* Automatisierte Tests werden verwendet, um die Qualität der Anwendung sicherzustellen.
* Dieser Ansatz reduziert das Risiko von Integrationsfehlern und ermöglicht schnelles Feedback.

**Persönliches Kanban:**
* Ein einfaches Kanban-Board wird verwendet, um den Überblick über anstehende, laufende und abgeschlossene Aufgaben zu behalten.
* Aufgaben werden in die Kategorien "To Do", "In Progress" und "Done" eingeteilt.
* Die Anzahl der gleichzeitig bearbeiteten Aufgaben wird begrenzt, um Fokus und Produktivität zu fördern.
* Dieser Ansatz bietet Transparenz über den Fortschritt und hilft, Engpässe zu identifizieren.

#### Struktur und Organisation

Als Ein-Personen-Projekt hat die Flashcard-Anwendung eine einfache Organisationsstruktur:

**Ein-Personen-Projekt:**
* Der Entwickler ist gleichzeitig Architekt, Entwickler, Tester und Endnutzer der Anwendung.
* Diese Konstellation bietet den Vorteil kurzer Feedback-Schleifen und schneller Entscheidungsprozesse.
* Die Herausforderung liegt darin, alle notwendigen Rollen effektiv zu erfüllen und eine objektive Perspektive zu bewahren.

**Externe Beratung bei Bedarf:**
* Bei komplexen technischen Herausforderungen oder Architekturentscheidungen kann externe Beratung hinzugezogen werden.
* Dies kann durch Fachliteratur oder Online-Communities bewerkstelligt werden.
* Dieser Ansatz erweitert die verfügbaren Perspektiven und Expertise.

**Dokumentationsgetriebene Entwicklung:**
* Die Architektur und wichtige Entscheidungen werden dokumentiert, um Konsistenz und Nachvollziehbarkeit zu gewährleisten.
* Die Dokumentation dient als Referenz für zukünftige Entwicklungen und hilft, den Überblick über das Gesamtsystem zu behalten.
* Dieser Ansatz unterstützt die langfristige Wartbarkeit und Weiterentwicklung der Anwendung.

**Werkzeugunterstützung:**
* Verschiedene Werkzeuge werden eingesetzt, um die Effizienz und Qualität der Entwicklung zu verbessern:
  * Git für die Versionskontrolle
  * Gradle für das Build-Management
  * JUnit für automatisierte Tests
  * IDE mit integrierten Codeanalyse-Tools (VSCode)
  * Kanban-Board für das Aufgabenmanagement
* Diese Werkzeuge kompensieren teilweise die begrenzten personellen Ressourcen.

#### Verantwortlichkeiten und Rollen

Obwohl es sich um ein Ein-Personen-Projekt handelt, werden verschiedene Rollen und Verantwortlichkeiten definiert, um alle Aspekte der Entwicklung abzudecken:
**Produktverantwortlicher:**
* Definition der Anforderungen und Funktionen
* Priorisierung der Entwicklungsaufgaben
* Bewertung des Nutzens und der Qualität der implementierten Funktionen
* Diese Rolle stellt sicher, dass die Anwendung den Bedürfnissen des Nutzers entspricht.

**Architekt:**
* Entwurf der Gesamtarchitektur
* Technologieauswahl
* Definition von Architekturprinzipien und -richtlinien
* Diese Rolle gewährleistet eine konsistente und zukunftssichere Architektur.

**Entwickler:**
* Implementierung der Funktionen
* Erstellung und Pflege des Quellcodes
* Durchführung von Unit-Tests
* Diese Rolle setzt die Anforderungen in funktionierenden Code um.

**Tester:**
* Durchführung von Integrations- und Systemtests
* Validierung der implementierten Funktionen
* Identifikation von Fehlern und Verbesserungsmöglichkeiten
* Diese Rolle stellt die Qualität und Zuverlässigkeit der Anwendung sicher.

**Benutzer:**
* Nutzung der Anwendung im Alltag
* Feedback zur Benutzerfreundlichkeit und Funktionalität
* Identifikation von Verbesserungsmöglichkeiten aus Nutzerperspektive
* Diese Rolle liefert wertvolles Feedback für die kontinuierliche Verbesserung.

**Zeitliche Trennung der Rollen:**
* Um Objektivität zu gewährleisten, werden die verschiedenen Rollen zeitlich getrennt ausgeübt.
* Beispielsweise wird die Testerrolle erst nach Abschluss der Entwicklungsarbeit eingenommen.
* Dieser Ansatz fördert eine kritische Perspektive und hilft, Fehler zu identifizieren.

#### Entscheidungsprozesse für architektonische Änderungen

Für architektonische Änderungen wurde ein strukturierter Entscheidungsprozess definiert:

**Identifikation des Änderungsbedarfs:**
* Änderungsbedarf kann aus verschiedenen Quellen stammen:
  * Neue funktionale Anforderungen
  * Qualitätsprobleme oder technische Schulden
  * Technologische Weiterentwicklung
  * Erkenntnisse aus der Nutzung der Anwendung
* Jeder identifizierte Änderungsbedarf wird dokumentiert und bewertet.

**Bewertung der Auswirkungen:**
* Die Auswirkungen einer Änderung werden systematisch bewertet:
  * Einfluss auf bestehende Funktionen
  * Auswirkungen auf die Qualitätsziele
  * Technischer Aufwand und Risiken
  * Langfristige Vorteile und Nachteile
* Diese Bewertung bildet die Grundlage für die Entscheidungsfindung.

**Entscheidungsfindung:**
* Für wichtige architektonische Entscheidungen wird das Architecture Decision Record (ADR) Format verwendet.
* Jede Entscheidung wird dokumentiert mit:
  * Kontext und Problemstellung
  * Betrachtete Alternativen
  * Entscheidung und Begründung
  * Konsequenzen und Risiken
* Dieser Ansatz fördert durchdachte Entscheidungen und erleichtert das spätere Nachvollziehen.

**Umsetzung und Validierung:**
* Architektonische Änderungen werden schrittweise umgesetzt, um Risiken zu minimieren.
* Nach der Umsetzung wird die Änderung validiert, um sicherzustellen, dass sie die erwarteten Vorteile bringt.
* Bei Bedarf werden Anpassungen vorgenommen.
* Dieser iterative Ansatz reduziert das Risiko von Fehlentscheidungen.

**Regelmäßige Architektur-Reviews:**
* In regelmäßigen Abständen wird die Gesamtarchitektur kritisch überprüft.
* Technische Schulden und Verbesserungsmöglichkeiten werden identifiziert.
* Basierend auf dieser Überprüfung werden Maßnahmen zur Verbesserung der Architektur geplant.
* Dieser Ansatz stellt sicher, dass die Architektur langfristig wartbar und erweiterbar bleibt.

### Mögliche Visualisierungen
* Ein Prozessdiagramm könnte den agilen Entwicklungsprozess mit wöchentlichen Iterationen visualisieren.
* Ein Kanban-Board-Beispiel könnte die Organisation der Aufgaben darstellen.
* Ein Diagramm könnte die verschiedenen Rollen und ihre Interaktionen zeigen.
* Ein Flussdiagramm könnte den Entscheidungsprozess für architektonische Änderungen darstellen.

## 4.4 Qualitätsstrategien

### Beschreibung

Die Qualitätsstrategien definieren konkrete Ansätze und Maßnahmen zur Erreichung der in Kapitel 1 festgelegten Qualitätsziele. Sie bilden die Brücke zwischen den abstrakten Qualitätszielen und ihrer praktischen Umsetzung in der Architektur und Implementierung der Flashcard-Anwendung. Für jedes Qualitätsziel werden spezifische Strategien, Maßnahmen und Metriken definiert.

#### Strategien zur Erreichung der Qualitätsziele

**Benutzerfreundlichkeit (sehr hoch):**

* **Strategie: User-Centered Design**
  * Die Benutzeroberfläche wird konsequent aus der Perspektive des Nutzers gestaltet, mit Fokus auf intuitive Bedienung und effiziente Workflows.
  * Der Entwickler nimmt regelmäßig die Rolle des Nutzers ein, um die Anwendung aus dieser Perspektive zu bewerten.
  * Die Benutzeroberfläche wird iterativ verbessert, basierend auf den Erfahrungen aus der täglichen Nutzung.

* **Strategie: Konsistentes Design**
  * Einheitliche Gestaltungsprinzipien werden über die gesamte Anwendung hinweg angewendet.
  * Material Design Guidelines werden als Basis für das UI-Design verwendet.
  * Konsistente Terminologie und Interaktionsmuster werden in der gesamten Anwendung verwendet.

* **Strategie: Effiziente Workflows**
  * Die häufigsten Nutzungsszenarien werden mit minimalen Klicks und Interaktionen ermöglicht.
  * Tastaturkürzel werden für alle wichtigen Funktionen implementiert.
  * Die Benutzeroberfläche passt sich an die Lerngewohnheiten des Nutzers an.

**Lerneffizienz (sehr hoch):**

* **Strategie: Wissenschaftlich fundierte Lernalgorithmen**
  * Der Spaced-Repetition-Algorithmus basiert auf wissenschaftlichen Erkenntnissen zur optimalen Gedächtnisleistung.
  * Die Parameter des Algorithmus werden basierend auf der individuellen Lernleistung angepasst.
  * Verschiedene Lernmodi werden für unterschiedliche Lernziele und -situationen angeboten.

* **Strategie: Personalisierte Lernpfade**
  * Die Lernpfade werden basierend auf der individuellen Lernhistorie und -leistung angepasst.
  * Schwierigere Karteikarten werden häufiger wiederholt als leichtere.
  * Die Anwendung identifiziert Muster in den Lernfehlern und passt die Lernstrategie entsprechend an.

* **Strategie: Optimale Karteikarten-Gestaltung**
  * Karteikarten werden nach kognitiven Prinzipien gestaltet, um die Gedächtnisleistung zu maximieren.
  * Die KI-Unterstützung hilft bei der Erstellung effektiver Frage-Antwort-Paare.
  * Multimediale Inhalte werden gezielt eingesetzt, um verschiedene Lerntypen zu unterstützen.

**Zuverlässigkeit (hoch):**

* **Strategie: Robuste Fehlerbehandlung**
  * Alle potenziellen Fehlerquellen werden identifiziert und mit entsprechenden Fehlerbehandlungsroutinen versehen.
  * Die Anwendung ist in der Lage, sich von Fehlern zu erholen, ohne Daten zu verlieren.
  * Fehler werden dem Nutzer in verständlicher Form kommuniziert.

* **Strategie: Datenintegrität und -sicherheit**
  * Regelmäßige automatische Backups der Lernmaterialien und -fortschritte werden erstellt.
  * Transaktionssicherheit wird für alle Datenbankoperationen gewährleistet.
  * Datenvalidierung erfolgt sowohl im Frontend als auch im Backend.

* **Strategie: Umfassende Tests**
  * Automatisierte Tests decken alle kritischen Funktionen der Anwendung ab.
  * Integrationstests prüfen das Zusammenspiel der verschiedenen Komponenten.
  * Manuelle Tests simulieren reale Nutzungsszenarien.

**KI-Qualität (hoch):**

* **Strategie: Hybrides KI-Modell**
  * Verwendung externer KI-Dienste.
  * Fallback-Mechanismen für den Fall, dass KI-Dienste nicht verfügbar sind.
  * Kontinuierliche Verbesserung in der Verwendung der KI-Modelle basierend auf Nutzungsdaten (z.B. bessere Prompts).

* **Strategie: KI-Transparenz**
  * Klare Kennzeichnung von KI-generierten Inhalten.
  * Möglichkeit zur manuellen Überprüfung und Korrektur von KI-Ergebnissen.
  * Erklärbare KI-Entscheidungen, insbesondere bei Lernempfehlungen.

* **Strategie: KI-Qualitätssicherung**
  * Regelmäßige Evaluation der KI-Ergebnisse anhand definierter Qualitätskriterien.
  * Feedback-Schleife zur Verbesserung der KI-Modelle.
  * Vergleich mit manuell erstellten Inhalten als Benchmark.

**Anpassungsfähigkeit (mittel):**

* **Strategie: Modulare Architektur**
  * Die Anwendung ist in unabhängige Module unterteilt, die einzeln angepasst oder ausgetauscht werden können.
  * Klar definierte Schnittstellen zwischen den Modulen ermöglichen flexible Anpassungen.
  * Erweiterungspunkte sind in der Architektur vorgesehen.

* **Strategie: Konfigurierbarkeit**
  * Wichtige Aspekte der Anwendung sind über Konfigurationsdateien anpassbar.
  * Benutzereinstellungen ermöglichen die Anpassung der Anwendung an individuelle Präferenzen.
  * Lernalgorithmen sind parametrisierbar und können an verschiedene Lernszenarien angepasst werden.

* **Strategie: Unterstützung verschiedener Inhaltstypen**
  * Die Anwendung unterstützt verschiedene Arten von Lerninhalten (Text, Bilder, Formeln, Code).
  * Neue Inhaltstypen können durch Erweiterungen hinzugefügt werden.
  * Import- und Export-Funktionen unterstützen verschiedene Formate.

**Offline-Funktionalität (mittel):**

* **Strategie: Lokale Datenspeicherung**
  * Alle Daten werden primär lokal gespeichert und verarbeitet.
  * Die Anwendung ist vollständig offline nutzbar.
  * Synchronisationsmechanismen sind für zukünftige Erweiterungen vorgesehen.

* **Strategie: Ressourceneffizienz**
  * Die Anwendung ist für die Ausführung auf einem einzelnen Rechner optimiert.
  * Ressourcenintensive Operationen werden asynchron ausgeführt, um die Benutzeroberfläche reaktiv zu halten.
  * Caching-Mechanismen werden eingesetzt, um die Performance zu verbessern.

* **Strategie: Progressive Enhancement**
  * Die Grundfunktionen der Anwendung sind ohne Internetverbindung verfügbar.
  * Erweiterte Funktionen, die externe Dienste nutzen, werden als optionale Erweiterungen implementiert.
  * Die Anwendung passt sich dynamisch an die verfügbaren Ressourcen an.

**Wartbarkeit (mittel):**

* **Strategie: Clean Code**
  * Der Code folgt etablierten Best Practices und Coding Standards.
  * Komplexe Logik wird durch klare Kommentare und Dokumentation erläutert.
  * Code-Reviews werden regelmäßig durchgeführt, auch im Ein-Personen-Projekt.

* **Strategie: Umfassende Dokumentation**
  * Die Architektur und wichtige Entscheidungen werden dokumentiert.
  * Technische Dokumentation wird parallel zum Code erstellt und gepflegt.
  * Nutzerhandbücher und Hilfe-Funktionen werden für die Anwendung bereitgestellt.

* **Strategie: Technische Schulden-Management**
  * Technische Schulden werden explizit dokumentiert und priorisiert.
  * Regelmäßige Refactoring-Zyklen sind im Entwicklungsprozess vorgesehen.
  * Die Codequalität wird durch statische Codeanalyse überwacht.

#### Konkrete Maßnahmen und Techniken

**Für Benutzerfreundlichkeit:**
* Implementierung eines responsiven UI-Designs mit Material-UI
* Durchführung regelmäßiger Usability-Tests durch den Entwickler in der Rolle des Nutzers
* Implementierung von Tastaturkürzeln für alle häufig genutzten Funktionen
* Entwicklung eines konsistenten Farbschemas und Designsystems
* Implementierung von Tooltips und kontextsensitiver Hilfe
* Optimierung der Ladezeiten und Reaktionsgeschwindigkeit der UI

**Für Lerneffizienz:**
* Implementierung des SuperMemo-SM2-Algorithmus als Basis für Spaced Repetition
* Entwicklung eines adaptiven Algorithmus, der sich an individuelle Lernmuster anpasst
* Integration von Mnemotechniken und kognitiven Lernstrategien
* Implementierung verschiedener Fragetypen (Multiple Choice, Freitext, Lückentext)
* Entwicklung von Statistiken und Visualisierungen zum Lernfortschritt
* Integration von Gamification-Elementen zur Motivation

**Für Zuverlässigkeit:**
* Implementierung eines automatischen Backup-Systems mit regelmäßigen Sicherungen
* Entwicklung einer robusten Fehlerbehandlung mit detailliertem Logging
* Implementierung von Transaktionssicherheit für Datenbankoperationen
* Durchführung umfassender Tests (Unit-Tests, Integrationstests, Systemtests)
* Implementierung von Validierungsroutinen für alle Benutzereingaben
* Entwicklung von Wiederherstellungsmechanismen für den Fall von Datenkorruption

**Für KI-Qualität:**
* Implementierung von Feedback-Mechanismen zur Verbesserung der KI-Ergebnisse
* Entwicklung von Qualitätsprüfungen für KI-generierte Inhalte
* Integration von Erklärungskomponenten für KI-Entscheidungen
* Implementierung von Fallback-Strategien für den Fall von KI-Fehlern
* Regelmäßige Evaluation und Feinabstimmung der KI-Modelle

**Für Anpassungsfähigkeit:**
* Entwicklung einer Plugin-Architektur für Erweiterungen
* Implementierung eines umfassenden Konfigurationssystems
* Entwicklung von Import/Export-Funktionen für verschiedene Formate
* Implementierung von Themes und anpassbaren Layouts
* Entwicklung von APIs für die Integration mit anderen Systemen
* Unterstützung verschiedener Medientypen in Karteikarten

**Für Offline-Funktionalität:**
* Implementierung einer lokalen Datenbank mit H2
* Entwicklung von Caching-Mechanismen für häufig genutzte Daten
* Optimierung der Anwendung für ressourcenbeschränkte Umgebungen
* Implementierung asynchroner Verarbeitung für ressourcenintensive Operationen
* Entwicklung von Offline-First-Strategien für alle Kernfunktionen
* Implementierung von Synchronisationsmechanismen für zukünftige Erweiterungen

**Für Wartbarkeit:**
* Anwendung von SOLID-Prinzipien und Clean Code-Praktiken
* Erstellung einer umfassenden Architekturdokumentation
* Implementierung von Logging und Monitoring
* Durchführung regelmäßiger Code-Reviews und Refactorings
* Verwendung von statischer Codeanalyse und Linting-Tools
* Erstellung von Nutzerhandbüchern und technischer Dokumentation

#### Metriken und Messverfahren

**Für Benutzerfreundlichkeit:**
* **Metriken:**
  * Anzahl der Klicks/Interaktionen für häufige Aufgaben
  * Zeit für die Durchführung typischer Workflows
  * Subjektive Zufriedenheitsbewertung nach der Nutzung
* **Messverfahren:**
  * Usability-Tests mit definierten Aufgaben
  * Tracking von Benutzerinteraktionen
  * Regelmäßige Selbstevaluation der Benutzerfreundlichkeit

**Für Lerneffizienz:**
* **Metriken:**
  * Gedächtnisleistung über Zeit (Retention Rate)
  * Lerngeschwindigkeit (Anzahl neuer Karteikarten pro Zeiteinheit)
  * Fehlerrate bei Wiederholungen
* **Messverfahren:**
  * Analyse der Lernstatistiken über verschiedene Zeiträume
  * Vergleich mit etablierten Lernmethoden
  * Tracking des langfristigen Gedächtnisses durch gezielte Tests

**Für Zuverlässigkeit:**
* **Metriken:**
  * Anzahl der Abstürze oder Fehler pro Nutzungsstunde
  * Datenverlustrate
  * Wiederherstellungszeit nach Fehlern
* **Messverfahren:**
  * Automatisiertes Fehler-Logging
  * Stresstests unter verschiedenen Bedingungen
  * Simulierte Fehlersituationen und Recovery-Tests

**Für KI-Qualität:**
* **Metriken:**
  * Genauigkeit der KI-generierten Karteikarten
  * Relevanz der personalisierten Empfehlungen
  * Nutzerakzeptanz von KI-generierten Inhalten
* **Messverfahren:**
  * Vergleich mit manuell erstellten Karteikarten
  * A/B-Tests mit verschiedenen KI-Modellen
  * Feedback-Analyse zu KI-generierten Inhalten

**Für Anpassungsfähigkeit:**
* **Metriken:**
  * Zeit für die Integration neuer Funktionen
  * Anzahl der unterstützten Inhaltstypen und Formate
  * Konfigurationsoptionen und Anpassungsmöglichkeiten
* **Messverfahren:**
  * Tracking der Entwicklungszeit für neue Features
  * Evaluation der Nutzung verschiedener Anpassungsoptionen
  * Bewertung der Erweiterbarkeit durch Code-Reviews

**Für Offline-Funktionalität:**
* **Metriken:**
  * Funktionsumfang ohne Internetverbindung
  * Ressourcenverbrauch (CPU, RAM, Speicherplatz)
  * Startzeit und Reaktionsgeschwindigkeit
* **Messverfahren:**
  * Funktionale Tests im Offline-Modus
  * Performance-Monitoring unter verschiedenen Bedingungen
  * Ressourcenverbrauch-Tracking über längere Nutzungsperioden

**Für Wartbarkeit:**
* **Metriken:**
  * Code-Komplexität (zyklomatische Komplexität, Coupling)
  * Testabdeckung
  * Dokumentationsumfang und -qualität
* **Messverfahren:**
  * Statische Codeanalyse
  * Regelmäßige Architektur-Reviews
  * Tracking der Zeit für Fehlerbehebungen und Änderungen

### Mögliche Visualisierungen (todo)
* Ein Qualitätsbaum könnte die Beziehungen zwischen Qualitätszielen und konkreten Strategien visualisieren.
* Eine Heatmap könnte zeigen, wie stark verschiedene Architekturentscheidungen die Qualitätsziele unterstützen.
* Ein Radar-Diagramm könnte den aktuellen Stand der Qualitätsziele darstellen.
* Ein Prozessdiagramm könnte den Qualitätssicherungsprozess visualisieren.