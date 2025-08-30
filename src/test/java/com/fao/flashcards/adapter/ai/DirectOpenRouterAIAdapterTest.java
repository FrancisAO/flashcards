package com.fao.flashcards.adapter.ai;

import com.fao.flashcards.domain.port.AICardGenerationPort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests für den DirectOpenRouterAIAdapter.
 * Diese Tests erfordern eine gültige API-Konfiguration in der application.properties-Datei.
 */
@SpringBootTest
@TestPropertySource(locations = "classpath:application.properties")
public class DirectOpenRouterAIAdapterTest {
    
    private DirectOpenRouterAIAdapter aiAdapter;
    
    @Value("${ai.openrouter.api-key}")
    private String apiKey;
    
    @Value("${ai.openrouter.base-url:https://openrouter.ai/api/v1}")
    private String baseUrl;
    
    @Value("${ai.model}")
    private String aiModel;
    
    @BeforeEach
    public void setUp() {
        aiAdapter = new DirectOpenRouterAIAdapter();
        
        // Setze die Werte manuell, da Spring sie nicht automatisch injizieren kann
        try {
            java.lang.reflect.Field apiKeyField = DirectOpenRouterAIAdapter.class.getDeclaredField("apiKey");
            apiKeyField.setAccessible(true);
            apiKeyField.set(aiAdapter, apiKey);
            
            java.lang.reflect.Field baseUrlField = DirectOpenRouterAIAdapter.class.getDeclaredField("baseUrl");
            baseUrlField.setAccessible(true);
            baseUrlField.set(aiAdapter, baseUrl);
            
            java.lang.reflect.Field aiModelField = DirectOpenRouterAIAdapter.class.getDeclaredField("aiModel");
            aiModelField.setAccessible(true);
            aiModelField.set(aiAdapter, aiModel);
        } catch (Exception e) {
            fail("Fehler beim Setzen der Felder: " + e.getMessage());
        }
    }
    
    /**
     * Test für die manuelle Erstellung einer Karteikarte.
     * Dieser Test überprüft, ob die GeneratedCard-Klasse korrekt funktioniert,
     * ohne eine tatsächliche API-Anfrage zu senden.
     */
    @Test
    public void testGeneratedCardCreation() {
        // Erstelle eine Beispiel-Karteikarte
        AICardGenerationPort.GeneratedCard card = new AICardGenerationPort.GeneratedCard(
            "Was ist eine Klasse in Java?",
            "Eine Klasse in Java ist eine Vorlage für Objekte, die Daten und Methoden enthält."
        );
        
        // Überprüfe, ob die Karteikarte korrekt erstellt wurde
        assertNotNull(card, "Karteikarte ist null");
        assertNotNull(card.getFront(), "Vorderseite ist null");
        assertFalse(card.getFront().isEmpty(), "Vorderseite ist leer");
        assertNotNull(card.getBack(), "Rückseite ist null");
        assertFalse(card.getBack().isEmpty(), "Rückseite ist leer");
        
        // Überprüfe den Inhalt der Karteikarte
        assertEquals("Was ist eine Klasse in Java?", card.getFront(), "Vorderseite hat nicht den erwarteten Inhalt");
        assertEquals("Eine Klasse in Java ist eine Vorlage für Objekte, die Daten und Methoden enthält.",
                    card.getBack(), "Rückseite hat nicht den erwarteten Inhalt");
    }
    
    /**
     * Test für die parseGeneratedCards-Methode mit einem gültigen JSON-String.
     * Dieser Test verwendet Reflection, um die private Methode aufzurufen.
     */
    @Test
    public void testParseGeneratedCards() throws Exception {
        // Erstelle einen gültigen JSON-String für Karteikarten
        String jsonContent = "[{\"front\":\"Was ist eine Klasse in Java?\",\"back\":\"Eine Klasse in Java ist eine Vorlage für Objekte, die Daten und Methoden enthält.\"}," +
                             "{\"front\":\"Was ist Vererbung?\",\"back\":\"Vererbung ermöglicht es, eine neue Klasse auf der Grundlage einer bestehenden Klasse zu erstellen.\"}]";
        
        // Rufe die private parseGeneratedCards-Methode über Reflection auf
        java.lang.reflect.Method parseMethod = DirectOpenRouterAIAdapter.class.getDeclaredMethod("parseGeneratedCards", String.class);
        parseMethod.setAccessible(true);
        List<AICardGenerationPort.GeneratedCard> generatedCards = (List<AICardGenerationPort.GeneratedCard>) parseMethod.invoke(aiAdapter, jsonContent);
        
        // Überprüfe, ob Karteikarten generiert wurden
        assertNotNull(generatedCards, "Generierte Karteikarten sind null");
        assertFalse(generatedCards.isEmpty(), "Keine Karteikarten generiert");
        assertEquals(2, generatedCards.size(), "Anzahl der generierten Karteikarten ist nicht korrekt");
        
        // Überprüfe den Inhalt der ersten Karteikarte
        AICardGenerationPort.GeneratedCard firstCard = generatedCards.get(0);
        assertEquals("Was ist eine Klasse in Java?", firstCard.getFront(), "Vorderseite der ersten Karteikarte hat nicht den erwarteten Inhalt");
        assertEquals("Eine Klasse in Java ist eine Vorlage für Objekte, die Daten und Methoden enthält.",
                    firstCard.getBack(), "Rückseite der ersten Karteikarte hat nicht den erwarteten Inhalt");
}
}