# User Stories
## Karteikarten generieren
Als Student möchte ich mir Karteikarten auf Basis von bereitgestellten Ressourcen
(Dokumente) von einer KI erzeugen lassen.

### Fachliche Anforderungen
- Die Generierungsfunktion soll in einem bestehenden Deck angeboten werden
- Die Generierungsfunktion wird über den folgenden Prozess abgebildet:
-- Zunächst über einen Button der auf eine neue Seite führt
-- Auf der Seite können in einem Textfeld Hinweise für das KI-Modell angegeben werden
-- Darüber hinaus können Dokumente (docx, pdf, text-basiert) hochgeladen werden
-- Über einen Button kann dann die Generierung der Karteikarten angestoßen werden
-- Danach wird auf eine weitere Seite weitergeleitet auf der die Karteikarten dann
bereitgestellt werden. 
-- Auf der Seite mit den bereitgestellten Karten ist ein Button zum Speichern aller Karteikarten
im Deck. Wenn der Button betätigt wird, werden die generierten Karteikarten im Deck persistiert udn es wird auf die Seite des Decks weitergeleitet.
-- Die Karteikarten werden mit Vorder- und Rückseite dargestellt, sind aber nur
über einen Button editierbar (Stiftsymbol). Nach der Editierung einer Karteikarte
kann diese gespeichert werden.

### Technische Anforderungen
- Für die Anbindung der KI wird Spring AI in der Version 1.0.1 verwendet. API Doku: https://docs.spring.io/spring-ai/reference/api/index.html
- Als KI-Provider wird Openrouter.ai verwendet. Doku für die API: https://openrouter.ai/docs/quickstart
- Im Backend gibt es ein Property, dass festlegt, welches KI-Modell für die Generierung von Text für Karteikarten auf Basis von
Text verwendet wird. Dieses Property hat z.B. den Wert "gpt-4o".
- Der API-Key für OpenRouter, den du verwenden kannst, ist abc


## Spaced Repitition Algorithmus
Als Student möchte ich, dass das Lernen der Karteikarten im Lernmodus auf Basis des Spaced
Repetition Algorithmus vollzogen wird.
### Anforderungen
Spaced Repetition Algorithmus
- Datei "4 - Lösungsstrategie.md" Kapitel 4.4 Abschnitt ####Konkrete Maßnahmen und Techniken **für Lerneffizienz**
- Datei "5 - Bausteinsicht.md" Kapitel 5.4 Abschnitt #### Übersicht über die Komponenten des Lernalgorihmus-Moduls 5. **Statistik und Analyse**
**Algorithmus-Kern:**

Als Student möchte ich Statistiken über meinen Lernfortschritt einsehen können.

Als Student möchte ich mir ein Deck mit Karteikarten vollständig von einer KI
generieren lassen können, ohne Ressourcen bereitzustellen.

Als Student möchte ich mir Lernpfade mit Hilfe einer KI generieren lassen können.