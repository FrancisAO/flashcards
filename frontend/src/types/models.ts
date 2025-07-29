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

/**
 * Interface für eine Karteikarte im Lernmodus.
 */
export interface StudyCard {
  id: string;
  front: string;
  back: string;
  tags: string[];
}

/**
 * Interface für ein Deck im Lernmodus.
 * Enthält die Informationen über das Deck und eine Liste der Karteikarten in zufälliger Reihenfolge.
 */
export interface StudyDeck {
  id: string;
  name: string;
  description: string;
  cards: StudyCard[];
}
