import axios from 'axios';
import { 
  AIGenerationRequest, 
  CardGenerationRequest, 
  AIGeneratedCard, 
  DocumentUpload,
  Card
} from '../types/models';

/**
 * Startet eine neue Karteikartengenerierung für ein Deck.
 * 
 * @param deckId Die ID des Decks
 * @param request Die Anfrage mit Hinweisen für die KI
 * @returns Die erstellte Generierungsanfrage
 */
export const startGeneration = async (
  deckId: string, 
  request: CardGenerationRequest
): Promise<AIGenerationRequest> => {
  const response = await axios.post(`/api/decks/${deckId}/generate`, request);
  return response.data;
};

/**
 * Lädt ein Dokument für eine Generierungsanfrage hoch.
 * 
 * @param deckId Die ID des Decks
 * @param requestId Die ID der Generierungsanfrage
 * @param file Die hochzuladende Datei
 * @returns Das hochgeladene Dokument
 */
export const uploadDocument = async (
  deckId: string,
  requestId: string,
  file: File
): Promise<DocumentUpload> => {
  const formData = new FormData();
  formData.append('file', file);
  
  const response = await axios.post(
    `/api/decks/${deckId}/generate/${requestId}/documents`,
    formData,
    {
      headers: {
        'Content-Type': 'multipart/form-data'
      }
    }
  );
  
  return response.data;
};

/**
 * Startet den Generierungsprozess für eine Anfrage.
 * 
 * @param deckId Die ID des Decks
 * @param requestId Die ID der Generierungsanfrage
 * @returns Die aktualisierte Generierungsanfrage
 */
export const processGeneration = async (
  deckId: string,
  requestId: string
): Promise<AIGenerationRequest> => {
  const response = await axios.post(`/api/decks/${deckId}/generate/${requestId}/process`);
  return response.data;
};

/**
 * Ruft den Status einer Generierungsanfrage ab.
 * 
 * @param deckId Die ID des Decks
 * @param requestId Die ID der Generierungsanfrage
 * @returns Die Generierungsanfrage mit Status
 */
export const getGenerationStatus = async (
  deckId: string,
  requestId: string
): Promise<AIGenerationRequest> => {
  const response = await axios.get(`/api/decks/${deckId}/generate/${requestId}`);
  return response.data;
};

/**
 * Ruft die generierten Karteikarten für eine Anfrage ab.
 * 
 * @param deckId Die ID des Decks
 * @param requestId Die ID der Generierungsanfrage
 * @returns Die Liste der generierten Karteikarten
 */
export const getGeneratedCards = async (
  deckId: string,
  requestId: string
): Promise<AIGeneratedCard[]> => {
  const response = await axios.get(`/api/decks/${deckId}/generate/${requestId}/cards`);
  return response.data;
};

/**
 * Aktualisiert eine generierte Karteikarte.
 * 
 * @param deckId Die ID des Decks
 * @param requestId Die ID der Generierungsanfrage
 * @param cardId Die ID der generierten Karteikarte
 * @param card Die aktualisierten Daten der Karteikarte
 * @returns Die aktualisierte Karteikarte
 */
export const updateGeneratedCard = async (
  deckId: string,
  requestId: string,
  cardId: string,
  card: Pick<AIGeneratedCard, 'front' | 'back'>
): Promise<AIGeneratedCard> => {
  const response = await axios.put(
    `/api/decks/${deckId}/generate/${requestId}/cards/${cardId}`,
    card
  );
  return response.data;
};

/**
 * Speichert alle generierten Karteikarten im Deck.
 * 
 * @param deckId Die ID des Decks
 * @param requestId Die ID der Generierungsanfrage
 * @returns Die Liste der gespeicherten Karteikarten
 */
export const saveGeneratedCards = async (
  deckId: string,
  requestId: string
): Promise<Card[]> => {
  const response = await axios.post(`/api/decks/${deckId}/generate/${requestId}/save`);
  return response.data;
};