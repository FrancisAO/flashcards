# 5. Bausteinsicht

## Übersicht
Dieses Kapitel beschreibt die statische Zerlegung des Systems in Bausteine (Module, Komponenten, Subsysteme, Klassen, Schnittstellen, Pakete, Bibliotheken, Frameworks, Schichten, Partitionen, Tiers, Funktionen, Makros, Operationen, Datenstrukturen, ...) sowie deren Beziehungen untereinander.

## Inhalt
Die Bausteinsicht zeigt die statische Struktur des Systems und wie es in Bausteine zerlegt ist. Sie dokumentiert die Komponenten des Systems, ihre Verantwortlichkeiten und ihre Beziehungen zueinander.

## 5.1 Whitebox Gesamtsystem

## 5.4 Whitebox Lernalgorithmus-Modul

### Beschreibung
Das Lernalgorithmus-Modul ist eine zentrale Komponente der Flashcard-Anwendung und verantwortlich für die Implementierung des wissenschaftlich fundierten Spaced-Repetition-Algorithmus. Dieses Modul steuert, wann und in welcher Reihenfolge Karteikarten dem Benutzer präsentiert werden, um den Lernprozess zu optimieren und die langfristige Gedächtnisleistung zu maximieren.

Das Lernalgorithmus-Modul ist in mehrere Komponenten unterteilt, die jeweils spezifische Aufgaben im Rahmen des Spaced-Repetition-Lernens übernehmen. Die Zerlegung folgt dem Prinzip der Separation of Concerns und ermöglicht eine flexible Anpassung und Erweiterung des Algorithmus.

#### Übersicht über die Komponenten des Lernalgorithmus-Moduls

Das Lernalgorithmus-Modul besteht aus folgenden Hauptkomponenten:

1. **Algorithmus-Kern**: Implementiert den grundlegenden Spaced-Repetition-Algorithmus (basierend auf SuperMemo-SM2) und berechnet die optimalen Wiederholungsintervalle.

2. **Lernplan-Manager**: Verwaltet den Lernplan des Benutzers, plant Wiederholungen und priorisiert Karteikarten basierend auf verschiedenen Faktoren.

3. **Lernfortschritt-Tracker**: Erfasst und analysiert die Lernleistung des Benutzers über Zeit und liefert Daten für die Anpassung des Algorithmus.

4. **Konfiguration und Anpassung**: Ermöglicht die Anpassung des Algorithmus an individuelle Lernpräferenzen und -ziele des Benutzers.

5. **Statistik und Analyse**: Berechnet und visualisiert Lernstatistiken und Prognosen für den zukünftigen Lernaufwand.

#### Beschreibung der Verantwortlichkeiten jeder Komponente

**Algorithmus-Kern:**
* Implementierung des SuperMemo-SM2-Algorithmus als Basis für Spaced Repetition
* Berechnung der optimalen Wiederholungsintervalle basierend auf der Schwierigkeit der Karteikarte und der Qualität der Antwort
* Anpassung der Schwierigkeitsfaktoren für jede Karteikarte basierend auf der Lernhistorie
* Bereitstellung einer flexiblen Schnittstelle für die Integration alternativer Algorithmen oder Erweiterungen
* Verarbeitung der Benutzerantworten und Umwandlung in algorithmische Parameter

**Lernplan-Manager:**
* Erstellung und Verwaltung des täglichen Lernplans basierend auf fälligen Wiederholungen und neuen Karteikarten
* Priorisierung von Karteikarten basierend auf Fälligkeit, Schwierigkeit und Benutzerzielen
* Verwaltung verschiedener Lernmodi (z.B. Neues Lernen, Wiederholen, Gemischt)
* Anpassung des Lernplans an verfügbare Lernzeit und Benutzervorlieben
* Koordination mit dem Algorithmus-Kern zur Bestimmung der optimalen Karteikarten-Reihenfolge

**Lernfortschritt-Tracker:**
* Erfassung und Speicherung aller Lerninteraktionen und -ergebnisse
* Berechnung von Lernkurven und Gedächtnisleistung über Zeit
* Identifikation von Mustern und Trends im Lernverhalten
* Erkennung von Stärken und Schwächen im Lernprozess
* Bereitstellung von Daten für die Anpassung des Algorithmus und die Generierung von Statistiken

**Konfiguration und Anpassung:**
* Bereitstellung einer Schnittstelle zur Konfiguration des Lernalgorithmus
* Verwaltung von Benutzereinstellungen wie tägliche Lernziele, maximale Anzahl neuer Karten, Wiederholungslimits
* Anpassung der Algorithmus-Parameter an individuelle Lernpräferenzen (z.B. konservative vs. aggressive Intervalle)
* Unterstützung verschiedener Lernstrategien und -szenarien (z.B. Prüfungsvorbereitung vs. langfristiges Lernen)
* Integration von Feedback-Mechanismen zur kontinuierlichen Verbesserung des Algorithmus
* Bereitstellung von Vorlagen für verschiedene Lernszenarien (z.B. Sprachenlernen, Faktenwissen, Programmierkonzepte)
* Kalibrierung des Algorithmus basierend auf historischen Lerndaten und Benutzerverhalten

**Statistik und Analyse:**
* Berechnung von Lernstatistiken wie Retention Rate, Erfolgsquote, Lerneffizienz
* Visualisierung von Lernfortschritt und -prognosen
* Generierung von Berichten über Lernverhalten und -erfolge
* Vorhersage des zukünftigen Wiederholungsaufwands
* Identifikation von Optimierungspotentialen im Lernprozess

#### Beschreibung der Schnittstellen zwischen den Komponenten

**Algorithmus-Kern ↔ Lernplan-Manager:**
* Der Lernplan-Manager ruft den Algorithmus-Kern auf, um die nächsten Wiederholungsintervalle für beantwortete Karteikarten zu berechnen.
* Der Algorithmus-Kern liefert Schwierigkeitsbewertungen für Karteikarten, die der Lernplan-Manager zur Priorisierung nutzt.
* Schnittstelle: `SchedulingService` mit Methoden wie `calculateNextReview(Card card, AnswerQuality quality)` und `getCardDifficulty(Card card)`.

**Lernplan-Manager ↔ Lernfortschritt-Tracker:**
* Der Lernplan-Manager informiert den Lernfortschritt-Tracker über durchgeführte Lernaktivitäten.
* Der Lernfortschritt-Tracker liefert historische Lerndaten, die der Lernplan-Manager für die Planung nutzt.
* Schnittstelle: `LearningHistoryService` mit Methoden wie `recordLearningActivity(LearningActivity activity)` und `getLearningHistory(Card card)`.

**Algorithmus-Kern ↔ Lernfortschritt-Tracker:**
* Der Algorithmus-Kern nutzt vom Lernfortschritt-Tracker bereitgestellte historische Daten zur Optimierung der Algorithmus-Parameter.
* Der Lernfortschritt-Tracker erhält vom Algorithmus-Kern berechnete Parameter für die Analyse des Lernfortschritts.
* Schnittstelle: `AlgorithmOptimizationService` mit Methoden wie `optimizeParameters(LearningHistory history)` und `getOptimizedParameters()`.

**Konfiguration und Anpassung ↔ Algorithmus-Kern:**
* Die Komponente Konfiguration und Anpassung liefert benutzerspezifische Konfigurationsparameter an den Algorithmus-Kern.
* Der Algorithmus-Kern informiert die Konfigurationskomponente über die Auswirkungen von Parameteränderungen.
* Schnittstelle: `AlgorithmConfigurationService` mit Methoden wie `setAlgorithmParameters(AlgorithmParameters params)` und `getEffectiveParameters()`.

**Konfiguration und Anpassung ↔ Lernplan-Manager:**
* Die Konfigurationskomponente liefert Benutzereinstellungen wie tägliche Lernziele an den Lernplan-Manager.
* Der Lernplan-Manager informiert die Konfigurationskomponente über die Machbarkeit von Lernzielen.
* Schnittstelle: `LearningGoalService` mit Methoden wie `setDailyGoals(LearningGoals goals)` und `getFeasibilityAnalysis(LearningGoals goals)`.

**Lernfortschritt-Tracker ↔ Statistik und Analyse:**
* Der Lernfortschritt-Tracker liefert Rohdaten an die Statistik-Komponente zur Analyse und Visualisierung.
* Die Statistik-Komponente definiert, welche Daten vom Lernfortschritt-Tracker erfasst werden sollen.
* Schnittstelle: `StatisticsDataService` mit Methoden wie `getLearningData(TimeRange range, StatisticsType type)` und `registerDataCollector(DataCollector collector)`.

**Statistik und Analyse ↔ Konfiguration und Anpassung:**
* Die Statistik-Komponente liefert Analysen, die für die Optimierung der Konfiguration genutzt werden können.
* Die Konfigurationskomponente definiert, welche Statistiken für die Anpassung relevant sind.
* Schnittstelle: `OptimizationService` mit Methoden wie `getOptimizationSuggestions()` und `applyOptimizedConfiguration(OptimizationSuggestion suggestion)`.

#### Begründung für die gewählte Zerlegung

Die Zerlegung des Lernalgorithmus-Moduls in die beschriebenen Komponenten bietet mehrere Vorteile:

1. **Separation of Concerns**: Jede Komponente hat eine klar definierte Verantwortlichkeit, was die Komplexität reduziert und die Wartbarkeit verbessert.

2. **Flexibilität und Erweiterbarkeit**: Durch die modulare Struktur können einzelne Komponenten unabhängig voneinander angepasst oder ausgetauscht werden. So könnte beispielsweise der Algorithmus-Kern durch eine alternative Implementierung ersetzt werden, ohne dass andere Komponenten geändert werden müssen.

3. **Testbarkeit**: Die klare Trennung der Verantwortlichkeiten erleichtert das Testen der einzelnen Komponenten in Isolation.

4. **Anpassbarkeit**: Die dedizierte Komponente für Konfiguration und Anpassung ermöglicht eine flexible Anpassung des Algorithmus an individuelle Bedürfnisse, ohne den Kern-Algorithmus zu verändern.

5. **Datenfluss**: Die definierten Schnittstellen zwischen den Komponenten ermöglichen einen kontrollierten Datenfluss, der die Integrität und Konsistenz der Lernalgorithmus-Logik sicherstellt.

6. **Evolutionäre Entwicklung**: Die modulare Struktur unterstützt eine schrittweise Verbesserung und Erweiterung des Lernalgorithmus basierend auf Nutzungserfahrungen und neuen wissenschaftlichen Erkenntnissen.

7. **Domänenorientierung**: Die Zerlegung spiegelt die natürlichen Aspekte des Spaced-Repetition-Lernens wider (Algorithmus, Planung, Fortschrittsverfolgung, Anpassung, Analyse), was das Verständnis und die Weiterentwicklung erleichtert.

### Mögliche Visualisierungen (todo)

* **Komponentendiagramm des Lernalgorithmus-Moduls**: Visualisierung der Komponenten und ihrer Beziehungen, einschließlich der definierten Schnittstellen.

* **Klassendiagramm der Kernklassen**: Detaillierte Darstellung der wichtigsten Klassen innerhalb jeder Komponente und ihrer Beziehungen.

* **Sequenzdiagramm für den Lernprozess**: Darstellung des Zusammenspiels der verschiedenen Komponenten während einer typischen Lernsitzung, von der Auswahl der Karteikarten bis zur Aktualisierung des Lernplans.

* **Aktivitätsdiagramm für die Algorithmus-Logik**: Visualisierung des Entscheidungsprozesses im Algorithmus-Kern bei der Berechnung von Wiederholungsintervallen.

* **Zustandsdiagramm für Karteikarten**: Darstellung der verschiedenen Zustände, die eine Karteikarte im Lernprozess durchläuft, und der Übergänge zwischen diesen Zuständen.

* **Datenflussdiagramm**: Visualisierung des Datenflusses zwischen den Komponenten des Lernalgorithmus-Moduls und anderen Modulen der Anwendung.

## 5.5 Wichtige Schnittstellen

### Beschreibung
Die Flashcard-Anwendung verfügt über mehrere wichtige Schnittstellen, die für das Verständnis der Architektur und die Interaktion zwischen den verschiedenen Komponenten des Systems von zentraler Bedeutung sind. Diese Schnittstellen ermöglichen die Kommunikation zwischen Frontend und Backend, den Zugriff auf die Datenbank und die Integration mit KI-Diensten.

#### Übersicht über die wichtigsten Schnittstellen

1. **REST-API zwischen Frontend und Backend**: Die primäre Schnittstelle für die Kommunikation zwischen dem React-Frontend und dem Spring Boot-Backend.

2. **Datenbankschnittstelle**: Die Schnittstelle für den Zugriff auf die H2-Datenbank zur persistenten Speicherung von Karteikarten, Decks und Lernfortschritten.

3. **KI-API-Integration**: Die Schnittstelle für die Integration mit KI-Diensten zur automatischen Generierung von Karteikarten und Optimierung des Lernprozesses.

4. **Lernalgorithmus-API**: Die interne Schnittstelle für die Interaktion mit dem Spaced-Repetition-Algorithmus.

5. **Import/Export-Schnittstelle**: Die Schnittstelle für den Import und Export von Karteikarten und Lernmaterialien.

#### Detaillierte Beschreibung der Schnittstellen

##### 1. REST-API zwischen Frontend und Backend

Die REST-API ist die zentrale Schnittstelle für die Kommunikation zwischen dem React-Frontend und dem Spring Boot-Backend. Sie folgt den REST-Prinzipien und verwendet JSON als Datenformat.

**Hauptendpunkte:**

* **Karteikarten-Management**:
  * `GET /api/v1/cards`: Abrufen aller Karteikarten oder gefiltert nach Parametern
  * `GET /api/v1/cards/{id}`: Abrufen einer spezifischen Karteikarte
  * `POST /api/v1/cards`: Erstellen einer neuen Karteikarte
  * `PUT /api/v1/cards/{id}`: Aktualisieren einer bestehenden Karteikarte
  * `DELETE /api/v1/cards/{id}`: Löschen einer Karteikarte

* **Deck-Management**:
  * `GET /api/v1/decks`: Abrufen aller Decks
  * `GET /api/v1/decks/{id}`: Abrufen eines spezifischen Decks mit seinen Karteikarten
  * `POST /api/dv1/ecks`: Erstellen eines neuen Decks
  * `PUT /api/v1/decks/{id}`: Aktualisieren eines bestehenden Decks
  * `DELETE /api/v1/decks/{id}`: Löschen eines Decks
  * `POST /api/v1/decks/{id}/cards`: Hinzufügen einer Karteikarte zu einem Deck
  * `DELETE /api/v1/decks/{deckId}/cards/{cardId}`: Entfernen einer Karteikarte aus einem Deck

* **Lernprozess**:
  * `GET /api/v1/study/next`: Abrufen der nächsten zu lernenden Karteikarte
  * `POST /api/v1/study/answer`: Übermitteln einer Antwort für eine Karteikarte
  * `GET /api/v1/study/due`: Abrufen der Anzahl fälliger Karteikarten
  * `GET /api/v1/study/stats`: Abrufen von Lernstatistiken

* **Konfiguration**:
  * `GET /api/v1/config`: Abrufen der aktuellen Konfiguration
  * `PUT /api/v1/`: Aktualisieren der Konfiguration

* **KI-Integration**:
  * `POST /api/v1/ai/generate`: Generieren von Karteikarten aus einem Text
  * `POST /api/v1/ai/optimize`: Optimieren einer bestehenden Karteikarte

**Datenstrukturen:**

```json
// Karteikarte (Card)
{
  "id": "string",
  "front": "string",
  "back": "string",
  "tags": ["string"],
  "mediaItems": [
    {
      "type": "IMAGE | AUDIO | FORMULA",
      "content": "string",
      "position": "FRONT | BACK"
    }
  ],
  "difficulty": "number",
  "nextReviewDate": "date",
  "createdAt": "date",
  "updatedAt": "date"
}

// Deck
{
  "id": "string",
  "name": "string",
  "description": "string",
  "cardCount": "number",
  "dueCardCount": "number",
  "tags": ["string"],
  "createdAt": "date",
  "updatedAt": "date"
}

// Lernantwort (StudyAnswer)
{
  "cardId": "string",
  "quality": "number", // 0-5, entsprechend der SuperMemo-Skala
  "timeSpent": "number", // in Millisekunden
  "timestamp": "date"
}

// Konfiguration (Config)
{
  "dailyNewCards": "number",
  "dailyReviewLimit": "number",
  "learningSteps": ["number"], // in Minuten
  "algorithmParameters": {
    "initialEaseFactor": "number",
    "easeFactorModifier": "number",
    "intervalModifier": "number"
  }
}
```

**Fehlerbehandlung:**

Die API verwendet standardmäßige HTTP-Statuscodes zur Signalisierung von Erfolg oder Fehlern:
* 200 OK: Erfolgreiche Anfrage
* 201 Created: Ressource erfolgreich erstellt
* 400 Bad Request: Ungültige Anfrage (z.B. fehlerhafte Parameter)
* 404 Not Found: Ressource nicht gefunden
* 500 Internal Server Error: Serverfehler

Fehlermeldungen werden im folgenden Format zurückgegeben:
```json
{
  "status": "number",
  "message": "string",
  "timestamp": "date",
  "details": ["string"]
}
```

**Authentifizierung:**

Da es sich um eine lokale Einzelbenutzer-Anwendung handelt, ist keine Authentifizierung erforderlich. Die API ist nur lokal verfügbar und nicht über das Internet zugänglich.

##### 2. Datenbankschnittstelle

Die Datenbankschnittstelle verwendet JPA (Java Persistence API) und Hibernate für den Zugriff auf die H2-Datenbank. Sie abstrahiert die Datenbankzugriffslogik durch das Repository-Pattern.

**Hauptrepositories:**

* **CardRepository**:
  * `findAll()`: Alle Karteikarten abrufen
  * `findById(String id)`: Karteikarte nach ID abrufen
  * `findByTags(List<String> tags)`: Karteikarten nach Tags filtern
  * `findDueCards(Date date, int limit)`: Fällige Karteikarten abrufen
  * `save(Card card)`: Karteikarte speichern oder aktualisieren
  * `delete(Card card)`: Karteikarte löschen

* **DeckRepository**:
  * `findAll()`: Alle Decks abrufen
  * `findById(String id)`: Deck nach ID abrufen
  * `findByTags(List<String> tags)`: Decks nach Tags filtern
  * `save(Deck deck)`: Deck speichern oder aktualisieren
  * `delete(Deck deck)`: Deck löschen

* **StudyLogRepository**:
  * `findByCardId(String cardId)`: Lernprotokolle für eine Karteikarte abrufen
  * `findByDateRange(Date start, Date end)`: Lernprotokolle in einem Zeitraum abrufen
  * `save(StudyLog log)`: Lernprotokoll speichern

* **ConfigRepository**:
  * `findCurrent()`: Aktuelle Konfiguration abrufen
  * `save(Config config)`: Konfiguration speichern

**Datenbankschema:**

Das Datenbankschema umfasst folgende Haupttabellen:
* `CARDS`: Speichert Karteikarten mit ihren Inhalten und Metadaten
* `DECKS`: Speichert Decks und ihre Metadaten
* `DECK_CARDS`: Verknüpfungstabelle zwischen Decks und Karteikarten
* `MEDIA_ITEMS`: Speichert Medieninhalte für Karteikarten
* `TAGS`: Speichert Tags für Karteikarten und Decks
* `STUDY_LOGS`: Speichert Lernprotokolle mit Antwortqualität und Zeitstempel
* `CONFIG`: Speichert die Anwendungskonfiguration

**Transaktionsmanagement:**

Die Datenbankschnittstelle verwendet Spring's deklaratives Transaktionsmanagement, um die Datenintegrität zu gewährleisten. Alle Datenbankoperationen werden in Transaktionen ausgeführt, die bei Fehlern automatisch zurückgerollt werden.

**Fehlerbehandlung:**

Datenbankfehler werden durch eine einheitliche Exception-Hierarchie behandelt:
* `RepositoryException`: Basisklasse für alle Repository-Fehler
* `EntityNotFoundException`: Entität nicht gefunden
* `DataIntegrityException`: Verletzung der Datenintegrität
* `PersistenceException`: Allgemeiner Persistenzfehler

##### 3. KI-API-Integration

Die KI-API-Integration ermöglicht die Nutzung von KI-Diensten für die automatische Generierung von Karteikarten und die Optimierung des Lernprozesses. Sie unterstützt sowohl lokale KI-Modelle (über Spring AI) als auch externe KI-Dienste (über REST-APIs).

**Hauptfunktionen:**

* **Karteikarten-Generierung**:
  * Extraktion von Schlüsselkonzepten aus Texten
  * Generierung von Frage-Antwort-Paaren
  * Erstellung von Multiple-Choice-Fragen
  * Generierung von Lückentexten

* **Lernoptimierung**:
  * Analyse von Lernmustern und Schwierigkeiten
  * Personalisierte Lernempfehlungen
  * Optimierung von Lernplänen
  * Identifikation von Wissenslücken

* **Qualitätsprüfung**:
  * Bewertung der Qualität von Karteikarten
  * Verbesserungsvorschläge für selbsterstellte Karteikarten
  * Identifikation von Redundanzen und Überlappungen

**Schnittstellen:**

* **Verwendung von KI (Spring AI)**:
  ```java
  public interface AIService {
    List<Card> generateCards(String text, GenerationOptions options);
    List<Suggestion> optimizeCard(Card card);
    QualityAssessment assessQuality(Card card);
    List<LearningRecommendation> generateRecommendations(LearningHistory history);
  }
  ```

**Fehlerbehandlung:**

* `AIServiceException`: Basisklasse für alle KI-Dienst-Fehler
* `GenerationException`: Fehler bei der Generierung von Karteikarten
* `ExternalServiceException`: Fehler bei der Kommunikation mit externen KI-Diensten

**Fallback-Strategien:**

Bei Nichtverfügbarkeit externer KI-Dienste werden Fallback-Strategien implementiert:
* Wechsel zu anderen KI Diensten 
* Caching von häufig genutzten KI-Ergebnissen
* Degradation zu regelbasierten Algorithmen

##### 4. Lernalgorithmus-API

Die Lernalgorithmus-API ist die interne Schnittstelle für die Interaktion mit dem Spaced-Repetition-Algorithmus. Sie wird von den Service-Klassen des Backends genutzt, um den Lernprozess zu steuern.

**Hauptschnittstellen:**

* **SpacedRepetitionService**:
  ```java
  public interface SpacedRepetitionService {
    ReviewSchedule scheduleReview(Card card, AnswerQuality quality);
    List<Card> getDueCards(int limit, StudyOptions options);
    CardDifficulty calculateDifficulty(Card card, LearningHistory history);
    LearningStatistics calculateStatistics(TimeRange range);
  }
  ```

* **AlgorithmConfigurationService**:
  ```java
  public interface AlgorithmConfigurationService {
    AlgorithmParameters getDefaultParameters();
    AlgorithmParameters getCurrentParameters();
    void updateParameters(AlgorithmParameters parameters);
    ParameterImpact simulateParameterChange(AlgorithmParameters parameters);
  }
  ```

**Datenstrukturen:**

```java
// Antwortqualität (AnswerQuality)
public enum AnswerQuality {
  AGAIN(0),      // Komplett vergessen
  HARD(1),       // Schwer zu erinnern
  GOOD(2),       // Mit Anstrengung erinnert
  EASY(3);       // Leicht erinnert
  
  private final int value;
  // ...
}

// Wiederholungsplan (ReviewSchedule)
public class ReviewSchedule {
  private Date nextReviewDate;
  private double easeFactor;
  private int interval;
  private ReviewState state;
  // ...
}

// Lernzustand (ReviewState)
public enum ReviewState {
  NEW,           // Neue Karteikarte
  LEARNING,      // Im Lernprozess
  REVIEWING,     // In regelmäßiger Wiederholung
  RELEARNING     // Vergessen und wird neu gelernt
}

// Algorithmus-Parameter (AlgorithmParameters)
public class AlgorithmParameters {
  private double initialEaseFactor;
  private double minimumEaseFactor;
  private double easeFactorModifier;
  private double intervalModifier;
  private int maximumInterval;
  // ...
}
```

**Fehlerbehandlung:**

* `AlgorithmException`: Basisklasse für alle Algorithmus-Fehler
* `InvalidParameterException`: Ungültige Algorithmus-Parameter
* `SchedulingException`: Fehler bei der Berechnung von Wiederholungsintervallen

**Konfigurationsmöglichkeiten:**

Die Lernalgorithmus-API bietet umfangreiche Konfigurationsmöglichkeiten:
* Anpassung der Algorithmus-Parameter (Ease-Faktor, Intervall-Modifikator, etc.)
* Definition von Lernzielen (tägliche neue Karteikarten, maximale Wiederholungen)
* Konfiguration verschiedener Lernmodi (Neues Lernen, Wiederholen, Gemischt)
* Anpassung der Bewertungsskala und ihrer Auswirkungen auf den Algorithmus

##### 5. Import/Export-Schnittstelle

Die Import/Export-Schnittstelle ermöglicht den Austausch von Karteikarten und Lernmaterialien mit anderen Systemen und Formaten. Sie unterstützt verschiedene Dateiformate und Quellen.

**Hauptfunktionen:**

* **Import**:
  * Import von Karteikarten aus verschiedenen Formaten (CSV, JSON, XML)
  * Extraktion von Lernmaterialien aus Texten, PDFs oder Webseiten
  * Batch-Import mehrerer Karteikarten oder Decks

* **Export**:
  * Export von Karteikarten in verschiedene Formate (CSV, JSON, XML)
  * Export von Lernstatistiken und -fortschritten
  * Backup der gesamten Datenbank oder ausgewählter Decks

**Schnittstellen:**

* **ImportService**:
  ```java
  public interface ImportService {
    ImportResult importFromFile(File file, ImportOptions options);
    ImportResult importFromText(String text, ImportOptions options);
    ImportResult importFromUrl(URL url, ImportOptions options);
    List<ImportFormat> getSupportedImportFormats();
  }
  ```

* **ExportService**:
  ```java
  public interface ExportService {
    File exportToFile(ExportSelection selection, ExportFormat format, ExportOptions options);
    String exportToString(ExportSelection selection, ExportFormat format, ExportOptions options);
    List<ExportFormat> getSupportedExportFormats();
  }
  ```

**Datenstrukturen:**

```java
// Import-Optionen (ImportOptions)
public class ImportOptions {
  private boolean createNewDeck;
  private String targetDeckId;
  private boolean overwriteExisting;
  private boolean importMedia;
  private boolean importTags;
  // ...
}

// Export-Auswahl (ExportSelection)
public class ExportSelection {
  private List<String> deckIds;
  private List<String> cardIds;
  private boolean includeReviewHistory;
  private boolean includeMedia;
  private TimeRange creationDateRange;
  // ...
}

// Import-Ergebnis (ImportResult)
public class ImportResult {
  private int importedCards;
  private int skippedCards;
  private int failedCards;
  private List<String> warnings;
  private List<String> errors;
  private List<Card> importedCardsList;
  // ...
}
```

**Fehlerbehandlung:**

* `ImportExportException`: Basisklasse für alle Import/Export-Fehler
* `UnsupportedFormatException`: Nicht unterstütztes Dateiformat
* `ParseException`: Fehler beim Parsen der Importdatei
* `ExportFailedException`: Fehler beim Exportieren von Daten

**Unterstützte Formate:**

* **Import**:
  * CSV/TSV mit verschiedenen Spaltenformaten
  * JSON mit definiertem Schema
  * Markdown mit spezieller Formatierung
  * Einfacher Text mit Trennzeichen

* **Export**:
  * CSV/TSV mit konfigurierbaren Spalten
  * JSON mit vollständigen Metadaten
  * Markdown für einfache Lesbarkeit
  * PDF für Druckausgabe

### Mögliche Visualisierungen (todo)

* **API-Schnittstellendiagramm**: Visualisierung der verschiedenen APIs und ihrer Beziehungen zueinander.

* **Sequenzdiagramm für API-Aufrufe**: Darstellung typischer Interaktionen zwischen Frontend und Backend über die REST-API.

* **Datenbankschema-Diagramm**: Visualisierung der Tabellen und ihrer Beziehungen in der Datenbank.

* **Komponentendiagramm der KI-Integration**: Darstellung der Komponenten und Datenflüsse in der KI-API-Integration.

* **Datenflussdiagramm für Import/Export**: Visualisierung des Datenflusses beim Import und Export von Karteikarten.