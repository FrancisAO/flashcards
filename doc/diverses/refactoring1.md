# Refactoring 
- Das Projekt muss in einigen Bereichen entlang einer hexagonalen Architektur refactored werden.

# Anforderungen
- Im pacakge `com.fao.flashcards.adapter.repository` sind mehrere Interfaces die Spring-Boot Annotationen
auweisen sowie Spring Boot Interfaces erweitern. 
- Du sollst für jedes Interface ein korrespondierendes Interface im package `com.fao.flashcards.application.port.out.repository` anlegen,
das aber keine Spring Boot Annotationen aufweist und nicht Spring Boot Klassen erweitert. Die dort abgelegten Interfaces enden aber nicht auf "Repository" sondern auf "OutputPort". 
- Die Methoden der Interfaces, die du im package `com.fao.flashcards.application.port.out.repository` anlegst, sind dieselben wie die der Interfaces im package `com.fao.flashcards.adapter.repository`. 
- Implementiere im package `com.fao.flashcards.adapter.repository` jetzt für jedes Interface aus dem package `com.fao.flashcards.application.port.out.repository` einen Adapter der das jeweilige Interface implementiert. 
- Im Konstruktor dieser Adapter soll das korrespondierende Interface aus dem package `com.fao.flashcards.adapter.repository` injected werden. Der Adapter soll per dependency injection in den Services des packages `com.fao.flashcards.application.service` verwendet werden und dadurch die dort verwendeten Repositories ersetzen. 