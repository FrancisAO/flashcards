package com.fao.flashcards.cards.application.port.in;

import java.io.IOException;
import java.util.List;

import com.fao.flashcards.cards.application.port.out.DocumentProcessingPort;
import com.fao.flashcards.cards.model.AIGeneratedCard;
import com.fao.flashcards.cards.model.AIGenerationRequest;
import com.fao.flashcards.cards.model.Card;
import com.fao.flashcards.cards.model.DocumentUpload;
import com.fao.flashcards.shared.application.port.in.UploadableFile;

import jakarta.persistence.EntityNotFoundException;

public interface CardGenerationInputPort {

        /**
         * Erstellt eine neue Generierungsanfrage für ein Deck.
         * 
         * @param deckId        Die ID des Decks
         * @param prompt        Hinweise für das KI-Modell
         * @param numberOfCards Die gewünschte Anzahl an Karteikarten (optional)
         * @return Die erstellte Generierungsanfrage
         */
        AIGenerationRequest createGenerationRequest(String deckId, String prompt, Integer numberOfCards);

        /**
         * Lädt ein Dokument für eine Generierungsanfrage hoch und extrahiert den Text.
         * 
         * @param requestId Die ID der Generierungsanfrage
         * @param file      Die hochzuladende Datei
         * @return Das hochgeladene Dokument
         * @throws EntityNotFoundException                            wenn die
         *                                                            Generierungsanfrage
         *                                                            nicht gefunden
         *                                                            wird
         * @throws IOException                                        wenn beim Lesen
         *                                                            der Datei ein
         *                                                            Fehler auftritt
         * @throws DocumentProcessingPort.DocumentProcessingException wenn bei der
         *                                                            Textextraktion ein
         *                                                            Fehler auftritt
         */
        DocumentUpload uploadDocument(String requestId, UploadableFile file)
                        throws EntityNotFoundException, IOException, DocumentProcessingPort.DocumentProcessingException;

        /**
         * Verarbeitet eine Generierungsanfrage und generiert Karteikarten.
         * 
         * @param requestId Die ID der Generierungsanfrage
         * @return Die aktualisierte Generierungsanfrage
         * @throws EntityNotFoundException wenn die Generierungsanfrage nicht gefunden
         *                                 wird
         */
        AIGenerationRequest processGenerationRequest(String requestId) throws EntityNotFoundException;

        /**
         * Ruft eine Generierungsanfrage ab.
         * 
         * @param requestId Die ID der Generierungsanfrage
         * @return Die Generierungsanfrage
         * @throws EntityNotFoundException wenn die Generierungsanfrage nicht gefunden
         *                                 wird
         */
        AIGenerationRequest getGenerationRequest(String requestId) throws EntityNotFoundException;

        /**
         * Ruft alle generierten Karteikarten für eine Anfrage ab.
         * 
         * @param requestId Die ID der Generierungsanfrage
         * @return Eine Liste von generierten Karteikarten
         * @throws EntityNotFoundException wenn die Generierungsanfrage nicht gefunden
         *                                 wird
         */
        List<AIGeneratedCard> getGeneratedCards(String requestId) throws EntityNotFoundException;

        /**
         * Aktualisiert eine generierte Karteikarte.
         * 
         * @param requestId Die ID der Generierungsanfrage
         * @param cardId    Die ID der generierten Karteikarte
         * @param front     Die neue Vorderseite
         * @param back      Die neue Rückseite
         * @return Die aktualisierte Karteikarte
         * @throws EntityNotFoundException wenn die Karteikarte nicht gefunden wird
         */
        AIGeneratedCard updateGeneratedCard(String requestId, String cardId, String front, String back)
                        throws EntityNotFoundException;

        /**
         * Speichert alle generierten Karteikarten im Deck.
         * 
         * @param deckId    Die ID des Decks
         * @param requestId Die ID der Generierungsanfrage
         * @return Eine Liste der gespeicherten Karteikarten
         * @throws EntityNotFoundException wenn die Generierungsanfrage oder das Deck
         *                                 nicht gefunden wird
         */
        List<Card> saveGeneratedCards(String deckId, String requestId) throws EntityNotFoundException;

}