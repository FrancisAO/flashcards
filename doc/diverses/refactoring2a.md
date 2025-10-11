# Informationen über das System
Das System besteht aus 4 Bausteinen
- cards: com.fao.flashcards.cards
- learning: com.fao.flashcards.learing
- ocr: com.fao.flashcards.ocr
- bootstrap: com.fao.flashcards.bootstrap
Jeder Baustein wurde entsprechend einer hexagonalen Architektur strukturiert. Das heißt,
jeder Baustein kann die folgenden packages haben:
- adapter
- application
- model

Das package "application" kann die sub-packages "port" und "services" haben. 
"port" ist unterteilt in "in" und "out" für die Input-Ports und Output-Ports.
Input-Ports werden von anderen Bausteinen referenziert, um mit diesem Baustein
zu kommunzieren. Output-Ports werden von Services im "services" package verwendet,
um mit Bausteinen (oder externen Systemen) zu kommunzieren. Diese Output-Ports
werden dann von Adaptern im "adapter" package implementiert.

# Problembeschreibung
Im package "service" des Bausteins "cards" sind einige Services, die von den Adaptern dieses Bausteins verwendet werden.  

# Tech Stack

## Frontend
- **Framework**: React 18.2.0 mit TypeScript
- **UI Components**: Material-UI (MUI) 5.14.0
- **Styling**: Emotion (CSS-in-JS)
- **Routing**: React Router DOM 6.14.2
- **HTTP Client**: Axios 1.4.0
- **Testing**: Jest, React Testing Library
- **Build Tool**: React Scripts 5.0.1

## Backend
- **Framework**: Spring Boot 3.5.0
- **Language**: Java 21
- **Database**: PostgreSQL (Production), H2 (Development/Testing)
- **ORM**: Spring Data JPA
- **Migration**: Flyway
- **Document Processing**: Apache PDFBox 3.0.5, Apache POI 5.2.3
- **JSON Processing**: Jackson
- **Code Reduction**: Lombok
- **Testing**: JUnit 5, ArchUnit
- **Build Tool**: Gradle
