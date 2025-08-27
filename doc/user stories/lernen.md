# User Story
Als Student möchte ich in den Lernmodus wechseln, in dem mir die Vorderseite der Karteikarten aus einem ausgeewählten Deck in zufälliger Reihenfolge angezeigt werden, damit ich Lernfortschritte erzielen kann.

# Anforderungen
1. Der Lernmodus soll ein weiterer Eintrag in der Menüleiste sein.
2. Der Student wählt das zul ernende Deck und startet damit den Lernmodus.
3. Es werden nacheinander die Vorderseiten der Karten angezeigt. Ein Klick auf die
Karteikarte dreht diese um und zeigt die Rückseite der Karteikarte.
4. Über einen Button "Weiter" gelangt man zur nächsten Karteikarte.
5. Wenn alle Karteikarten beantwortet wurden, führt der letzte Klick auf den Button "weiter" zu
einer Seite auf der der Lernende zu seinem Lernen beglückwünscht wird. Mit einem Klick auf den
Button "Fertig" gelangt man zurück zur Startseite des Lernmodus.

# Hinweise
1. Das die Karteikarten per Zufall angezeigt werden, ist eine Vorstufe vor der Implementierung des Spaced Repitition Algorithmus.
2. Die Bausteinsicht im Kapitel "5 - Bausteinsicht.md" sowie die Laufzeitsicht im Kapitel "6 - Laufzeitsicht" geben Auskunft
über die Komponenten des Systems und wie diese zusammenhängen. Berücksichtige dies bei der Implementierung des Lernmodus,
halte die Implementierung aber noch einfach, d.h. die folgenden Bausteine/Komponenten müssen noch nicht implementiert werden:
- Lernplan Manager
- Lernfortschritt-Tracker
- Konfiguration und Anpassung
- Statistik und Analyse
Hier muss also nur eine einfache Implementierung des Algorithmus Kern erfolgen.
3. Das Frontend ist im Ordner /frontend implementiert.
4. Das backend ist im Ordner /src implementiert.

