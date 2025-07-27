export interface Card {
  id?: string;
  front: string;
  back: string;
  tags: string[];
  createdAt?: string;
  updatedAt?: string;
}

export interface Deck {
  id?: string;
  name: string;
  description?: string;
  tags: string[];
  createdAt?: string;
  updatedAt?: string;
}

export interface DeckWithCards extends Deck {
  cards: Card[];
}

export interface DeckCard {
  deckId: string;
  cardId: string;
}
