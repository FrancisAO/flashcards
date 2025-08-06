# User Story
Ich möchte, dass die Datenbank, die in einer "application.properties", "application-dev.properties" oder "application-prod.properties"
mit dem Property spring.datasource.url referenziert wird, beim Start der Anwendung angelegt wird, wenn sie nicht existiert.
Die zu verwendende Datenbank ist eine PostgresDB, die in einem Docker-Container läuft.

# Hinweise
1. Passwort und Username der Datenbank werden in den Properties spring.datasource.username bzw.
spring.datasource.password in den Properties hinterlegt. 
2. Spring Boot muss Lese- und Schreibrechte in der Datenbank haben.
3. Es gibt ein Startskript für die Anwendung: start.bat
4. Das Start-Skript startet via docker-compose.yml den Docker-Container in dem eine PostgresDB angelegt werden kann, das backend und das frontend.
5. Das backend ist im Ordner src
