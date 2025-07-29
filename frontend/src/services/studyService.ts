import axios from 'axios';
import { StudyDeck } from '../types/models';

const API_URL = '/api/v1/study';

/**
 * Ruft alle Karten eines Decks in zufälliger Reihenfolge ab.
 * 
 * @param deckId Die ID des Decks
 * @returns Ein StudyDeck mit den Karten in zufälliger Reihenfolge
 */
export const getRandomizedDeck = async (deckId: string): Promise<StudyDeck> => {
  const response = await axios.get(`${API_URL}/decks/${deckId}/cards`);
  return response.data;
};