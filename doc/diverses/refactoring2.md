# Problembeschreibung
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

Es geht jetzt darum, dass die Services in den packages application.service jedes Bausteins keine
@Service bzw. @Component Annotationen mehr haben.

# Aufgabe
Mache nun die folgenden Dinge:
1. Erstelle im adapter.launch package des Bausteins "boostrap" eine [Baustein]BeanConfig Klasse
Beispiel: OcrBeanConfig (die gibt es schon, sie muss aber noch erweitert werden). 
Erstelle die Klasse nur, wenn es diese noch nicht gibt.
3. Die Konfigurationsklassen sind mit @Configuration (org.springframework.context.annotation.Configuration) annotiert.

2. Erstelle in application.port.in ein Interface [Baustein]BeanFactory. Beispiel: OcrBeanFactory. Das Interface hat create-Methoden die als Rückgabewert die 
für jeden Service, der in diesem Baustein ist und ...
3. Erstelle in application.service eine Klasse [Baustein]BeanFactoryImpl die dieses Interface implementiert.
2. Die BeanConfig soll die Services erzeugen und dem Dependency Injection Mechanismus
