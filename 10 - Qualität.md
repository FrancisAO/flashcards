## 10.4 Sicherheit

### Beschreibung

Obwohl die Flashcard-Anwendung als lokale Anwendung für einen einzelnen Nutzer konzipiert ist, sind Sicherheitsaspekte dennoch relevant, insbesondere im Hinblick auf den Schutz der Lernmaterialien und -fortschritte sowie die Integrität der Anwendung. Die Sicherheitsanforderungen konzentrieren sich auf den Schutz der lokalen Daten und die Sicherheit bei der Interaktion mit externen Diensten.

#### Konkrete Sicherheitsanforderungen

1. **Datenschutz**:
   * Schutz der persönlichen Lernmaterialien vor unbefugtem Zugriff
   * Sichere Speicherung sensibler Daten (z.B. API-Schlüssel für externe Dienste)
   * Kontrolle über die Weitergabe von Daten an externe Dienste

2. **Datenintegrität**:
   * Schutz vor Datenverlust oder -beschädigung
   * Konsistente Datenbankzustände auch bei unerwarteten Abbrüchen
   * Validierung von Eingabedaten zur Vermeidung von Inkonsistenzen

3. **Kommunikationssicherheit**:
   * Sichere Kommunikation mit externen KI-Diensten
   * Schutz vor Man-in-the-Middle-Angriffen bei Netzwerkkommunikation
   * Sichere Übertragung von Lernmaterialien bei Import/Export

4. **Anwendungssicherheit**:
   * Schutz vor bekannten Sicherheitslücken in verwendeten Bibliotheken
   * Robustheit gegen Fehleingaben und unerwartete Zustände
   * Sichere Konfiguration der Anwendung und Datenbank

#### Architekturmaßnahmen zur Erfüllung der Sicherheitsanforderungen

1. **Datenschutzmaßnahmen**:
   * Verschlüsselung der Datenbank-Datei mit einem vom Nutzer festgelegten Passwort
   * Sichere Speicherung von API-Schlüsseln in einer verschlüsselten Konfigurationsdatei
   * Transparente Darstellung der Datennutzung bei Interaktion mit externen Diensten
   * Implementierung von Datenschutzeinstellungen zur Kontrolle der Datenfreigabe

2. **Datenintegritätsmaßnahmen**:
   * Transaktionsmanagement in der H2-Datenbank für atomare Operationen
   * Regelmäßige automatische Backups der Datenbank
   * Implementierung von Datenvalidierungsschichten im Backend
   * Konsistenzprüfungen bei Datenimporten und -exporten

3. **Kommunikationssicherheitsmaßnahmen**:
   * Verwendung von HTTPS für alle externen API-Aufrufe
   * Implementierung von TLS-Zertifikatsvalidierung
   * Sichere Authentifizierung bei externen Diensten
   * Verschlüsselung sensibler Daten bei der Übertragung

4. **Anwendungssicherheitsmaßnahmen**:
   * Regelmäßige Aktualisierung der verwendeten Bibliotheken und Frameworks
   * Implementierung von Input-Validierung und Escaping
   * Sichere Standardkonfiguration der H2-Datenbank
   * Logging von Sicherheitsereignissen für Analyse und Audit

#### Sicherheitstests und -überwachung

1. **Sicherheitstests**:
   * Regelmäßige Überprüfung auf bekannte Sicherheitslücken in Abhängigkeiten
   * Penetrationstests für die Kommunikation mit externen Diensten
   * Validierung der Datenintegrität nach Systemabstürzen

2. **Sicherheitsüberwachung**:
   * Logging von Zugriffsversuchen auf geschützte Ressourcen
   * Überwachung der Datenintegrität
   * Erkennung von ungewöhnlichen Zugriffsmustern

Die Sicherheitsanforderungen und -maßnahmen sind auf die spezifischen Bedürfnisse einer lokalen Einzelbenutzer-Anwendung zugeschnitten. Der Fokus liegt auf dem Schutz der persönlichen Daten des Nutzers und der Sicherstellung der Datenintegrität, während gleichzeitig eine sichere Interaktion mit externen Diensten gewährleistet wird.

## 10.5 Wartbarkeit

### Beschreibung

Die Wartbarkeit der Flashcard-Anwendung ist ein wichtiger Qualitätsaspekt, der die langfristige Entwicklung und Pflege der Anwendung beeinflusst. Obwohl die Wartbarkeit in der Priorisierung der Qualitätsziele eine niedrigere Priorität hat, ist sie dennoch entscheidend für die nachhaltige Weiterentwicklung der Anwendung. Die Wartbarkeitsanforderungen konzentrieren sich auf die Modularität, Testbarkeit und Dokumentation des Systems.

#### Konkrete Wartbarkeitsanforderungen

1. **Modularität und Erweiterbarkeit**:
   * Klare Trennung von Verantwortlichkeiten in der Architektur
   * Einfache Erweiterbarkeit um neue Funktionen
   * Minimale Abhängigkeiten zwischen Komponenten
   * Austauschbarkeit einzelner Komponenten ohne Auswirkungen auf das Gesamtsystem

2. **Testbarkeit**:
   * Hohe Testabdeckung für kritische Komponenten
   * Automatisierte Tests auf verschiedenen Ebenen (Unit, Integration, System)
   * Einfache Testbarkeit durch Dependency Injection und klare Schnittstellen
   * Reproduzierbare Testumgebungen

3. **Dokumentation**:
   * Umfassende Dokumentation der Architektur und Designentscheidungen
   * Klare API-Dokumentation für alle Komponenten
   * Aktuelle und konsistente Codekommentare
   * Dokumentation von Abhängigkeiten und externen Schnittstellen

4. **Codequalität**:
   * Einhaltung von Coding-Standards und Best Practices
   * Geringe Codekomplexität und hohe Lesbarkeit
   * Konsistente Namenskonventionen und Strukturierung
   * Vermeidung von Code-Duplikation

#### Architekturmaßnahmen zur Erfüllung der Wartbarkeitsanforderungen

1. **Maßnahmen für Modularität und Erweiterbarkeit**:
   * Implementierung einer klaren Schichtenarchitektur im Backend (Controller, Service, Repository, Domain)
   * Verwendung des Dependency Injection-Patterns in Spring Boot
   * Komponentenbasierte Architektur im React-Frontend
   * Definition klarer Schnittstellen zwischen Modulen
   * Einsatz von Design Patterns für flexible und erweiterbare Strukturen

2. **Maßnahmen für Testbarkeit**:
   * Implementierung einer umfassenden Teststrategie mit JUnit und Mockito
   * Integration von Jest und React Testing Library für Frontend-Tests
   * Einrichtung einer CI/CD-Pipeline für automatisierte Tests
   * Verwendung von In-Memory-Datenbank für Integrationstests
   * Implementierung von Testfixtures und Testdatengeneratoren

3. **Maßnahmen für Dokumentation**:
   * Verwendung von JavaDoc und JSDoc für Code-Dokumentation
   * Erstellung und Pflege einer Architektur- und Designdokumentation
   * Dokumentation von API-Endpunkten mit Swagger/OpenAPI
   * Pflege eines Entwicklerhandbuchs mit Setup- und Entwicklungsanleitungen
   * Dokumentation von Datenbankschema und Datenmodell

4. **Maßnahmen für Codequalität**:
   * Einrichtung von Linting-Tools (ESLint, Checkstyle)
   * Code-Reviews als Teil des Entwicklungsprozesses
   * Regelmäßige Refactoring-Zyklen
   * Einsatz von statischer Codeanalyse
   * Überwachung von Code-Metriken (Komplexität, Testabdeckung)

#### Messung und Verbesserung der Wartbarkeit

1. **Wartbarkeitsmetriken**:
   * Testabdeckung (mindestens 80% für kritische Komponenten)
   * Zyklomatische Komplexität (< 10 pro Methode)
   * Abhängigkeitsmetriken (geringe Kopplung, hohe Kohäsion)
   * Dokumentationsabdeckung (100% für öffentliche APIs)
   * Anzahl bekannter technischer Schulden

2. **Verbesserungsprozesse**:
   * Regelmäßige Architektur- und Code-Reviews
   * Kontinuierliche Verbesserung durch Refactoring
   * Systematische Erfassung und Abarbeitung technischer Schulden
   * Regelmäßige Aktualisierung von Abhängigkeiten und Frameworks

Die Wartbarkeitsanforderungen und -maßnahmen sind darauf ausgerichtet, die langfristige Entwicklung und Pflege der Flashcard-Anwendung zu erleichtern. Durch die Implementierung einer modularen Architektur, umfassender Tests, guter Dokumentation und hoher Codequalität wird sichergestellt, dass die Anwendung auch in Zukunft effizient weiterentwickelt und angepasst werden kann.

## 10.6 Benutzbarkeit

### Beschreibung

Die Benutzbarkeit ist eines der höchstpriorisierten Qualitätsziele der Flashcard-Anwendung. Eine intuitive, effiziente und angenehme Benutzeroberfläche ist entscheidend für den Erfolg der Anwendung, da sie direkt die Lerneffizienz und Nutzerzufriedenheit beeinflusst. Die Benutzbarkeitanforderungen konzentrieren sich auf die Erlernbarkeit, Effizienz, Fehlertoleranz und Zufriedenheit des Nutzers.

#### Konkrete Benutzbarkeitanforderungen

1. **Erlernbarkeit**:
   * Intuitive Benutzeroberfläche ohne Einarbeitungszeit
   * Selbsterklärende Funktionen und Workflows
   * Konsistente Interaktionsmuster und Terminologie
   * Kontextsensitive Hilfe und Tooltips für komplexere Funktionen

2. **Effizienz**:
   * Minimale Anzahl von Klicks für häufige Aktionen
   * Unterstützung von Tastaturkürzeln für alle wichtigen Funktionen
   * Optimierte Workflows für Karteikartenerstellung und Lernsitzungen
   * Schnelle Reaktionszeiten der Benutzeroberfläche

3. **Fehlertoleranz**:
   * Verständliche Fehlermeldungen mit Lösungsvorschlägen
   * Vermeidung von Datenverlusten bei Benutzerfehlern
   * Undo/Redo-Funktionalität für wichtige Aktionen
   * Validierung von Eingaben mit klarem Feedback

4. **Zufriedenheit**:
   * Ästhetisch ansprechendes Design
   * Konsistente und moderne Benutzeroberfläche
   * Positive Verstärkung durch Feedback und Gamification-Elemente
   * Personalisierbare Benutzeroberfläche

#### Architekturmaßnahmen zur Erfüllung der Benutzbarkeitanforderungen

1. **Maßnahmen für Erlernbarkeit**:
   * Implementierung einer intuitiven Benutzeroberfläche mit Material-UI
   * Entwicklung eines konsistenten Designsystems
   * Integration von kontextsensitiven Hilfefunktionen
   * Implementierung von interaktiven Einführungen für neue Nutzer

2. **Maßnahmen für Effizienz**:
   * Optimierung der Frontend-Performance für schnelle Reaktionszeiten
   * Implementierung von Tastaturkürzeln für alle wichtigen Aktionen
   * Entwicklung optimierter Workflows für häufige Aufgaben
   * Vorabladen von Daten für nahtlose Übergänge zwischen Ansichten

3. **Maßnahmen für Fehlertoleranz**:
   * Implementierung von Validierungslogik im Frontend und Backend
   * Entwicklung eines konsistenten Fehlerbehandlungssystems
   * Integration von Undo/Redo-Funktionalität für kritische Aktionen
   * Automatische Speicherung von Entwürfen und Zwischenständen

4. **Maßnahmen für Zufriedenheit**:
   * Entwicklung eines ästhetisch ansprechenden UI-Designs
   * Integration von Animationen und Übergängen für ein flüssiges Erlebnis
   * Implementierung von Gamification-Elementen für Lernfortschritte
   * Bereitstellung von Anpassungsoptionen für die Benutzeroberfläche

#### Benutzbarkeitstest und -verbesserung

1. **Testmethoden**:
   * Usability-Tests mit repräsentativen Aufgaben
   * Heuristische Evaluation der Benutzeroberfläche
   * Analyse von Nutzungsmustern und -metriken
   * A/B-Tests für alternative Designlösungen

2. **Verbesserungsprozesse**:
   * Kontinuierliche Verbesserung basierend auf Nutzerfeedback
   * Regelmäßige Überprüfung und Optimierung von Benutzerworkflows
   * Iterative Weiterentwicklung der Benutzeroberfläche
   * Benchmarking gegen Best Practices und Standards

Die Benutzbarkeitanforderungen und -maßnahmen sind darauf ausgerichtet, eine intuitive, effiziente und angenehme Benutzererfahrung zu schaffen. Durch die Implementierung einer benutzerfreundlichen Oberfläche mit React.js und Material-UI, optimierten Workflows und einer fehlertoleranten Gestaltung wird sichergestellt, dass die Anwendung den hohen Anforderungen an Benutzerfreundlichkeit gerecht wird und den Lernprozess optimal unterstützt.

Die Benutzbarkeit ist eng mit anderen Qualitätszielen wie Lerneffizienz und Performance verknüpft, da eine intuitive und reaktionsschnelle Benutzeroberfläche direkt zur Effizienz des Lernprozesses beiträgt und die Zufriedenheit des Nutzers erhöht.