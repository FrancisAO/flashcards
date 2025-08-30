# 6. Laufzeitsicht (todo)

## 6.7 Zusammenfassung und Ausblick

### Zusammenfassung

In diesem Kapitel wurden die dynamischen Aspekte der Flashcard-Anwendung detailliert beschrieben. Die vier Kernabläufe - Erstellen und Bearbeiten von Karteikarten, Durchführen einer Lernsitzung, Analyse des Lernfortschritts und KI-gestützte Generierung von Karteikarten - zeigen, wie die verschiedenen Komponenten des Systems zur Laufzeit interagieren, um die Funktionalität der Anwendung zu realisieren.

Für jeden Ablauf wurden die beteiligten Bausteine und ihre Rollen, der detaillierte Ablauf, die Datenflüsse sowie die Ausnahme- und Fehlersituationen beschrieben. Diese umfassende Darstellung ermöglicht ein tiefes Verständnis der dynamischen Struktur des Systems und bildet die Grundlage für die Implementierung und Weiterentwicklung der Anwendung.

Die Laufzeitsicht hat gezeigt, wie die in der Bausteinsicht beschriebenen statischen Komponenten zur Laufzeit zusammenarbeiten, um die Anforderungen der Anwendung zu erfüllen. Insbesondere wurde deutlich, wie der Spaced-Repetition-Algorithmus als zentrales Element der Anwendung in den Lernprozess integriert ist und wie die KI-Technologien zur Unterstützung des Lernprozesses eingesetzt werden.

Die beschriebenen Abläufe unterstützen die in Kapitel 1 definierten Qualitätsziele, insbesondere die Benutzerfreundlichkeit, Lerneffizienz, Zuverlässigkeit und KI-Qualität. Sie nutzen die in Kapitel 4 gewählten Technologien und Architekturmuster effektiv und respektieren die in Kapitel 3 definierten Systemgrenzen.

### Ausblick auf die nächsten Kapitel

Die in diesem Kapitel beschriebenen dynamischen Aspekte des Systems bilden die Grundlage für die weiteren Kapitel der Architekturdokumentation:

* **Kapitel 7 - Verteilungssicht**: Beschreibt die physische Verteilung des Systems auf Hardware-Komponenten und die technische Infrastruktur. Für die Flashcard-Anwendung als lokale Einzelbenutzer-Anwendung ist diese Sicht relativ einfach, da alle Komponenten auf dem lokalen Rechner des Nutzers ausgeführt werden.

* **Kapitel 8 - Querschnittliche Konzepte**: Beschreibt übergreifende Konzepte und Lösungsansätze, die in mehreren Teilen des Systems angewendet werden, wie z.B. Persistenz, Benutzeroberfläche, Fehlerbehandlung und Sicherheit.

* **Kapitel 9 - Architekturentscheidungen**: Dokumentiert wichtige Architekturentscheidungen und ihre Begründungen, wie z.B. die Wahl des Spaced-Repetition-Algorithmus, die Integration von KI-Technologien und die Entscheidung für eine lokale Anwendung.

* **Kapitel 10 - Qualität**: Beschreibt, wie die Qualitätsziele der Anwendung erreicht werden und wie die Qualität gemessen und sichergestellt wird.

* **Kapitel 11 - Risiken und technische Schulden**: Identifiziert potenzielle Risiken und technische Schulden der Architektur und beschreibt Strategien zu deren Minimierung.

Die in der Laufzeitsicht beschriebenen Abläufe und Interaktionen werden in diesen Kapiteln aus verschiedenen Perspektiven betrachtet und weiter vertieft, um ein umfassendes Bild der Architektur der Flashcard-Anwendung zu vermitteln.