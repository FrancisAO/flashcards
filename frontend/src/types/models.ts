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

/**
 * Interface für eine KI-generierte Karteikarte.
 */
export interface AIGeneratedCard {
  id?: string;
  front: string;
  back: string;
  edited: boolean;
  saved: boolean;
  cardId?: string;
  createdAt?: string;
  updatedAt?: string;
}

/**
 * Status einer Generierungsanfrage.
 */
export enum AIGenerationStatus {
  CREATED = 'CREATED',
  PROCESSING = 'PROCESSING',
  COMPLETED = 'COMPLETED',
  FAILED = 'FAILED'
}

/**
 * Interface für eine Generierungsanfrage.
 */
export interface AIGenerationRequest {
  id?: string;
  deckId: string;
  prompt: string;
  numberOfCards?: number;
  status: string;
  documentCount: number;
  generatedCardCount: number;
  createdAt?: string;
  updatedAt?: string;
}

/**
 * Interface für die Anfrage zur Karteikartengenerierung.
 */
export interface CardGenerationRequest {
  prompt: string;
  numberOfCards?: number;
}

/**
 * Interface für ein hochgeladenes Dokument.
 */
export interface DocumentUpload {
  id?: string;
  originalFilename: string;
  contentType: string;
  fileSize: number;
  uploadedAt?: string;
}
