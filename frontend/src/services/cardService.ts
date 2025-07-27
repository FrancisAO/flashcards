import axios from 'axios';
import { Card } from '../types/models';

const API_URL = '/api/v1/cards';

export const getAllCards = async (): Promise<Card[]> => {
  const response = await axios.get(API_URL);
  return response.data;
};

export const getCardById = async (id: string): Promise<Card> => {
  const response = await axios.get(`${API_URL}/${id}`);
  return response.data;
};

export const createCard = async (card: Card): Promise<Card> => {
  const response = await axios.post(API_URL, card);
  return response.data;
};

export const updateCard = async (id: string, card: Card): Promise<Card> => {
  const response = await axios.put(`${API_URL}/${id}`, card);
  return response.data;
};

export const deleteCard = async (id: string): Promise<void> => {
  await axios.delete(`${API_URL}/${id}`);
};

export const getCardsByTag = async (tag: string): Promise<Card[]> => {
  const response = await axios.get(`${API_URL}/tag/${tag}`);
  return response.data;
};

export const getCardsByTags = async (tags: string[]): Promise<Card[]> => {
  const response = await axios.post(`${API_URL}/search`, tags);
  return response.data;
};

export const getAllTags = async (): Promise<string[]> => {
  const response = await axios.get(`${API_URL}/tags`);
  return response.data;
};
