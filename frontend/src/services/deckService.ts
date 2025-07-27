import axios from 'axios';
import { Deck, DeckWithCards, DeckCard } from '../types/models';

const API_URL = '/api/v1/decks';

export const getAllDecks = async (): Promise<Deck[]> => {
  const response = await axios.get(API_URL);
  return response.data;
};

export const getDeckById = async (id: string): Promise<Deck> => {
  const response = await axios.get(`${API_URL}/${id}`);
  return response.data;
};

export const getDeckWithCards = async (id: string): Promise<DeckWithCards> => {
  const response = await axios.get(`${API_URL}/${id}/cards`);
  return response.data;
};

export const createDeck = async (deck: Deck): Promise<Deck> => {
  const response = await axios.post(API_URL, deck);
  return response.data;
};

export const updateDeck = async (id: string, deck: Deck): Promise<Deck> => {
  const response = await axios.put(`${API_URL}/${id}`, deck);
  return response.data;
};

export const deleteDeck = async (id: string): Promise<void> => {
  await axios.delete(`${API_URL}/${id}`);
};

export const addCardToDeck = async (deckCard: DeckCard): Promise<void> => {
  await axios.post(`${API_URL}/add-card`, deckCard);
};

export const removeCardFromDeck = async (deckId: string, cardId: string): Promise<void> => {
  await axios.delete(`${API_URL}/${deckId}/cards/${cardId}`);
};

export const getDecksByTag = async (tag: string): Promise<Deck[]> => {
  const response = await axios.get(`${API_URL}/tag/${tag}`);
  return response.data;
};
