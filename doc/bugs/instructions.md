# Problembeschreibung
Ein Projekt kann angelegt werden. Wenn dieses jedoch geöffnet wird, wird im Frontend die Meldung "Request failed with status code 500" angezeigt.
Im Log des Backend (Datei "backend-log.txt") kann man erkennen, dass ein GET auf /api/v1/projects/statistics gemacht wird.
Die Exception "jakarta.persistence.EntityNotFoundException: Projekt mit ID statistics nicht gefunden" wird dann im backend geschmissen.
Der Log-Eintrag wird im ProjectController erzeugt. Es scheint so, als würde der "statistics" als "projectId" interpretiert werden.
Im Frontend wird im "projectService.ts" dieser Endpunkt aufgerufen.

# Aufgabe
Korrigere diesen Fehler, sodass gespeicherte Projekte geöffnet werden können.

# Hinweise
Die Dateien, in denen die Ursache für den Fehler liegen kann, wurden neu erzeugt. Sie sind in der Datei "changelog.txt" aufgelistet.