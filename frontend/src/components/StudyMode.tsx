import React, { useState, useEffect } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import { 
  Typography, 
  Box, 
  Card, 
  CardContent, 
  Button, 
  Grid, 
  Paper,
  Chip
} from '@mui/material';
import { StudyDeck, StudyCard } from '../types/models';
import { getRandomizedDeck } from '../services/studyService';

/**
 * Komponente für den Lernmodus.
 * Zeigt die Karten eines ausgewählten Decks in zufälliger Reihenfolge an.
 */
const StudyMode: React.FC = () => {
  const { deckId } = useParams<{ deckId: string }>();
  const navigate = useNavigate();
  
  const [deck, setDeck] = useState<StudyDeck | null>(null);
  const [currentCardIndex, setCurrentCardIndex] = useState<number>(0);
  const [showAnswer, setShowAnswer] = useState<boolean>(false);
  const [loading, setLoading] = useState<boolean>(true);
  const [error, setError] = useState<string | null>(null);
  const [completed, setCompleted] = useState<boolean>(false);

  useEffect(() => {
    const fetchDeck = async () => {
      if (!deckId) {
        setError('Keine Deck-ID angegeben');
        setLoading(false);
        return;
      }

      try {
        const data = await getRandomizedDeck(deckId);
        setDeck(data);
        setLoading(false);
      } catch (err) {
        console.error('Failed to fetch deck:', err);
        setError('Fehler beim Laden des Decks. Bitte versuchen Sie es später erneut.');
        setLoading(false);
      }
    };

    fetchDeck();
  }, [deckId]);

  const handleShowAnswer = () => {
    setShowAnswer(true);
  };

  const handleNextCard = () => {
    if (deck && currentCardIndex < deck.cards.length - 1) {
      setCurrentCardIndex(currentCardIndex + 1);
      setShowAnswer(false);
    } else if (deck && currentCardIndex === deck.cards.length - 1) {
      // Wenn die letzte Karte erreicht wurde, setze completed auf true
      setCompleted(true);
    }
  };

  const handlePreviousCard = () => {
    if (currentCardIndex > 0) {
      setCurrentCardIndex(currentCardIndex - 1);
      setShowAnswer(false);
    }
  };

  const handleBackToDecks = () => {
    navigate('/study');
  };

  const handleFinish = () => {
    // Zurück zur Deck-Auswahl navigieren
    navigate('/study');
  };

  if (loading) {
    return <Typography>Lade Deck...</Typography>;
  }

  if (error) {
    return <Typography color="error">{error}</Typography>;
  }

  if (!deck || deck.cards.length === 0) {
    return (
      <Box>
        <Typography variant="h5" gutterBottom>
          Keine Karten in diesem Deck gefunden
        </Typography>
        <Button variant="contained" onClick={handleBackToDecks}>
          Zurück zur Deck-Auswahl
        </Button>
      </Box>
    );
  }

  const currentCard: StudyCard = deck.cards[currentCardIndex];

  // Wenn alle Karten durchgearbeitet wurden, zeige die Glückwunschseite an
  if (completed) {
    return (
      <Box sx={{ textAlign: 'center', py: 4 }}>
        <Typography variant="h4" component="h1" gutterBottom>
          Herzlichen Glückwunsch!
        </Typography>
        <Typography variant="h5" gutterBottom>
          Du hast alle Karteikarten in "{deck.name}" erfolgreich durchgearbeitet.
        </Typography>
        <Typography variant="body1" sx={{ mb: 4 }}>
          Weiter so mit dem Lernen!
        </Typography>
        <Button
          variant="contained"
          color="primary"
          size="large"
          onClick={handleFinish}
          sx={{ mt: 2 }}
        >
          Fertig
        </Button>
      </Box>
    );
  }

  return (
    <Box>
      <Box sx={{ mb: 3 }}>
        <Typography variant="h4" component="h1">
          Lernen: {deck.name}
        </Typography>
        <Typography variant="subtitle1" color="text.secondary">
          Karte {currentCardIndex + 1} von {deck.cards.length}
        </Typography>
      </Box>

      <Paper elevation={3} sx={{ mb: 4, p: 3 }}>
        <Box sx={{ mb: 2 }}>
          <Typography variant="h6" gutterBottom>
            Vorderseite:
          </Typography>
          <Typography variant="body1">{currentCard.front}</Typography>
        </Box>

        {showAnswer ? (
          <Box>
            <Typography variant="h6" gutterBottom>
              Rückseite:
            </Typography>
            <Typography variant="body1">{currentCard.back}</Typography>
            
            <Box sx={{ mt: 2, display: 'flex', flexWrap: 'wrap', gap: 0.5 }}>
              {currentCard.tags?.map((tag) => (
                <Chip key={tag} label={tag} size="small" />
              ))}
            </Box>
          </Box>
        ) : (
          <Button
            variant="contained"
            color="primary"
            onClick={handleShowAnswer}
            sx={{ mt: 2 }}
          >
            Antwort anzeigen
          </Button>
        )}
      </Paper>

      <Grid container spacing={2} justifyContent="space-between">
        <Grid item>
          <Button
            variant="outlined"
            onClick={handleBackToDecks}
          >
            Zurück zur Deck-Auswahl
          </Button>
        </Grid>
        <Grid item>
          <Box sx={{ display: 'flex', gap: 2 }}>
            <Button
              variant="outlined"
              onClick={handlePreviousCard}
              disabled={currentCardIndex === 0}
            >
              Vorherige Karte
            </Button>
            <Button
              variant="contained"
              onClick={handleNextCard}
              disabled={currentCardIndex === deck.cards.length - 1 && !showAnswer}
            >
              {currentCardIndex === deck.cards.length - 1 ? 'Abschließen' : 'Nächste Karte'}
            </Button>
          </Box>
        </Grid>
      </Grid>
    </Box>
  );
};

export default StudyMode;