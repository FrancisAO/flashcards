# User Story
Als Student möchte ich alle Karteikarten finden können, die ein oder mehrere Tags haben.

# Anforderungen
1. Ein Student sollte die Verfügbaren Tags sehen können und per Klick ein oder mehrere auswählen können. Mit jedem Klick auf ein Tag werden die Karten angezeigt, die dieses
Tag und alle anderen ausgewählten Tags haben.
2. Im Menü auf der Landingpage sollte ein neuer Eintrag "Suchen" auf die Seite
zum Suchen nach Karten angezeigt werden.

# Hinweise
1. In frontend/src/services/cardService.ts wird die Funktion getCardsByTag exportiert, die
aber noch nirgendwo verwendet wird.
2. Der src/java/com/fao/flashcards/api/controller/CardController.java hat ein GET-Mapping
um alle Karten mit einem Tag zu ermitteln, in der Methode getCardsByTag.
