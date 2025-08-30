package com.fao.flashcards.adapter.ai;

import com.fao.flashcards.domain.port.AICardGenerationPort;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Adapter für die direkte Kommunikation mit der OpenRouter.ai API ohne Spring AI.
 * Implementiert den AICardGenerationPort und verwendet RestTemplate für die Kommunikation.
 */
@Component
public class DirectOpenRouterAIAdapter implements AICardGenerationPort {
    
    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;
    private static final Logger LOGGER = LoggerFactory.getLogger(DirectOpenRouterAIAdapter.class);

    @Value("${ai.openrouter.api-key}")
    private String apiKey;
    
    @Value("${ai.openrouter.base-url:https://openrouter.ai/api/v1}")
    private String baseUrl;
    
    @Value("${ai.model}")
    private String aiModel;
    
    /**
     * Konstruktor für den DirectOpenRouterAIAdapter.
     */
    public DirectOpenRouterAIAdapter() {
        this.restTemplate = new RestTemplate();
        this.objectMapper = new ObjectMapper();
    }
    
    @Override
    public List<GeneratedCard> generateCardsFromText(String contentText, String prompt, Integer numberOfCards) {
        // Systemanweisung für das KI-Modell
        String systemPrompt = "Du bist ein Assistent, der Karteikarten für Lernzwecke erstellt. "
                + "Erstelle Karteikarten mit einer Vorderseite (Frage/Konzept) und einer Rückseite (Antwort/Erklärung). "
                + "Die Karteikarten sollten die wichtigsten Konzepte und Informationen aus dem bereitgestellten Text abdecken. "
                + "Formatiere die Ausgabe als JSON-Array mit Objekten, die \"front\" und \"back\" Eigenschaften haben. "
                + "Beispiel: [{\"front\": \"Was ist X?\", \"back\": \"X ist Y.\"}, {\"front\": \"Wie funktioniert Z?\", \"back\": \"Z funktioniert durch...\"}]";
        
        // Benutzeranweisung mit dem Inhalt und den Hinweisen
        String userPrompt = "Erstelle Karteikarten basierend auf folgendem Text:\n\n"
                + contentText + "\n\n"
                + "Zusätzliche Hinweise: " + prompt + "\n\n"
                + (numberOfCards != null ? "Erstelle genau " + numberOfCards + " Karteikarten." : "");
        
        // Erstelle die Anfrage an die OpenRouter.ai API
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("model", aiModel);
        requestBody.put("temperature", 0.7);
        
        List<Map<String, Object>> messages = new ArrayList<>();
        
        Map<String, Object> systemMessage = new HashMap<>();
        systemMessage.put("role", "system");
        systemMessage.put("content", systemPrompt);
        messages.add(systemMessage);
        
        Map<String, Object> userMessage = new HashMap<>();
        userMessage.put("role", "user");
        userMessage.put("content", userPrompt);
        messages.add(userMessage);
        
        requestBody.put("messages", messages);
        
        // Setze die Header für die Anfrage
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", "Bearer " + apiKey);
        headers.set("HTTP-Referer", "https://flashcards.example.com");
        headers.set("X-Title", "Flashcards App");
        
        HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(requestBody, headers);
        
        // Sende die Anfrage an die OpenRouter.ai API
        ResponseEntity<String> responseEntity = restTemplate.postForEntity(
                baseUrl + "/chat/completions",
                requestEntity,
                String.class
        );
        
        // Extrahiere die Antwort aus der API-Antwort
        String responseContent = "";
        try {
            JsonNode responseJson = objectMapper.readTree(responseEntity.getBody());
            responseContent = responseJson.path("choices").path(0).path("message").path("content").asText();
            LOGGER.debug("API-Antwort: {}", responseContent);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Fehler beim Parsen der API-Antwort", e);
        }
        
        // Parsen der generierten Karteikarten
        return parseGeneratedCards(responseContent);
    }
    
    /**
     * Parst die JSON-Antwort der KI und erstellt daraus GeneratedCard-Objekte.
     * 
     * @param jsonContent Die JSON-Antwort der KI
     * @return Eine Liste von GeneratedCard-Objekten
     */
    private List<GeneratedCard> parseGeneratedCards(String markdownJson) {
        List<GeneratedCard> generatedCards = new ArrayList<>();
        
        String jsonContent = removeMarkdownFormatting(markdownJson);

        try {
            // Versuche, die Antwort als JSON-Array zu parsen
            JsonNode cardsArray = objectMapper.readTree(jsonContent);
            
            if (cardsArray.isArray()) {
                for (JsonNode cardNode : cardsArray) {
                    if (cardNode.has("front") && cardNode.has("back")) {
                        String front = cardNode.get("front").asText();
                        String back = cardNode.get("back").asText();
                        generatedCards.add(new GeneratedCard(front, back));
                    }
                }
            }
        } catch (JsonProcessingException e) {
            // Wenn das Parsen fehlschlägt, versuche, die Antwort als Text zu interpretieren
            // und extrahiere die Karteikarten manuell
            String[] lines = jsonContent.split("\n");
            GeneratedCard currentCard = null;
            LOGGER.debug("Fehler beim Parsen als JSON", e);
            
            for (String line : lines) {
                line = line.trim();
                
                if (line.startsWith("Vorderseite:") || line.startsWith("Front:")) {
                    if (currentCard != null && currentCard.getFront() != null && currentCard.getBack() != null) {
                        generatedCards.add(currentCard);
                    }
                    currentCard = new GeneratedCard();
                    currentCard.setFront(line.substring(line.indexOf(":") + 1).trim());
                } else if (line.startsWith("Rückseite:") || line.startsWith("Back:")) {
                    if (currentCard != null) {
                        currentCard.setBack(line.substring(line.indexOf(":") + 1).trim());
                    }
                }
            }
            
            // Füge die letzte Karteikarte hinzu, wenn sie vollständig ist
            if (currentCard != null && currentCard.getFront() != null && currentCard.getBack() != null) {
                generatedCards.add(currentCard);
            }
        }
        
        return generatedCards;
    }

    private String removeMarkdownFormatting(String jsonContent) {
        // Entferne Markdown-Formatierungen wie ```json ... ```
        String clearedContent = jsonContent.trim();
        if (jsonContent.startsWith("```") && jsonContent.endsWith("```")) {
            int firstNewline = jsonContent.indexOf("\n");
            if (firstNewline != -1) {
                clearedContent = jsonContent.substring(firstNewline + 1, jsonContent.length() - 3).trim();
            }
        }

        return clearedContent;
    }
}