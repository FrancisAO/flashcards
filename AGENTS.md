# Das System
Die Flashcard-Anwendung ist ein webbasiertes Lernsystem, das dem Nutzer hilft, Vokabeln, Fachbegriffe und andere Lerninhalte effizient zu lernen.

Wesentliche Features:
- Extrahieren von Text aus Bildern und das Erzeugen neuer Lerninhalte aus diesen Texten mit OCR.
- Anlegen von Karteikarten und Wiederholen der Karteikarten mit dem Spaced-Repitition-Algorithmus.
- Automatisches Generieren von Karteikarten mit KI.

# Architektur
- Das System besteht aus einem backend und einem frontend. Das backend ist im src-Ordner, das Frontend im frontend-Ordner.

## Backend-Architektur
- Für das backend weist eine hexagonale Architektur auf
- Das System ist in Bausteinen (main building block) gruppiert. Ein Baustein
hat eine klare Verantwortlichkeit und Schnittstellen (Ports). 
- Jeder Baustein ist in einem eigenen package unterhalb von com.fao.flashcards. Beispiele:
    - com.fao.flashcards.bootstrap: Verantwortlich für alles was mit dem Start des Systems zu tun hat
    - com.fao.flashcards.cards: Verantwortlich für die Verwaltung von Karteikarten
    - com.fao.flashcards.learning: Verantwortlich für das Lernen
    - com.fao.flashcards.ocr: Verantwortlich für die Extraktion von Text aus Bildern
- Jeder Baustein ist intern hexagonal strutkuriert. Beispiel "cards":
    - adapter-package: Enthält die Adapter für den Zugriff auf andere Bausteine und externe Systeme.
    - application: Enthält die Geschäftslogik ohne Abhängigkeiten zu externen Systemen oder anderen Bausteinen
    - model: Enthält das Model
- Bausteine bieten Input-Ports an und verwenden Output-Ports. Input-Ports werden von externen Sytemen oder anderen Bautseinen verwendet, um auf den jeweiligen Baustein zuzugreifen. Output-Ports werden vom Baustein selbst verwendet, um auf externe Systeme oder andere Bausteine zuzugreifen.
- Das application-package enthält input und output ports sowie ein domain-pacakge. Im domain-package ist die Geschäftslogik, d.h. die Services. Beispiel:
    - application.port.in: Input-Ports für den Zugriff auf den Baustein. Enthält nur Interfaces. Die Services im domain-package implementieren diese Ports. Die Input-Ports werden von Verwendern per Dependency Injection eingebunden.
    - application.port.out: Output-Ports, die von den Services im domain-package verwendet werden, um auf externe Systeme und andere Bausteine zuzugreifen. Die Output-Ports werden von den Adaptern im adapter-package implementiert. Die Output-Ports werden nicht von anderen Bausteinen verwendet.
- Das Ziel bei der Verwendung einer Hexagonalen Architektur ist es, die Geschäftslogik unabhängig von externen Systemen entwickeln zu können. Als externes System wird u.a. aufgefasst:
     - Frameworks wie Spring und Hibernate
     - Externe Systeme die z.B. über REST angesprochen werden
- Desweiteren sollen Bausteine unabhängig voneinander weiterentwickelt werden
können. Die Unterteilung von Bausteinen in separate packages mit eigener 
hexagonaler Architektur dient dem Zweck, die Bausteine zu einem späteren Zeitpunkt leichter aus dem Projekt lösen und in einer anderen Struktur 
(z.B. Microservice) fortführen zu können.

# Coding Style
- Methodenlängen: Höchstens 50 Zeilen
- Kommentare: 
    - Schreibe nur Java-Doc Kommentare, keine inline-Kommentare
    - Schreibe Kommentare für alle Klassen und public Methoden
- Eine Methode darf höchstens 3 Parameter haben.
- Ein Konstruktor darf höchstens 3 Parameter haben, es sei denn die Parameter werden per Dependency Injection injiziert
- Verwende nur Constructor-Injection wenn du Spring DI verwendest

# Testing
- Teste alle public Methoden
- Bevorzuge Unit-Tests vor Integrationstests
