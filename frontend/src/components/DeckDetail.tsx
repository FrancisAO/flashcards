import React, { useState, useEffect } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import {
  Typography,
  Box,
  Button,
  Card,
  CardContent,
  CardActions,
  Grid,
  Chip,
  Dialog,
  DialogActions,
  DialogContent,
  DialogContentText,
  DialogTitle,
  CircularProgress
} from '@mui/material';
import { getDeckWithCards, deleteDeck, removeCardFromDeck } from '../services/deckService';
import { DeckWithCards } from '../types/models';
import GenerateCardsButton from './GenerateCardsButton';

const DeckDetail: React.FC = () => {
  const { id } = useParams<{ id: string }>();
  const navigate = useNavigate();
  const [deck, setDeck] = useState<DeckWithCards | null>(null);
  const [loading, setLoading] = useState<boolean>(true);
  const [error, setError] = useState<string | null>(null);
  const [deleteConfirmOpen, setDeleteConfirmOpen] = useState<boolean>(false);
  const [cardToRemove, setCardToRemove] = useState<string | null>(null);
  
  useEffect(() => {
    const fetchDeck = async () => {
      if (!id) return;
      
      try {
        const data = await getDeckWithCards(id);
        setDeck(data);
        setLoading(false);
      } catch (err) {
        console.error('Failed to fetch deck:', err);
        setError('Fehler beim Laden des Decks. Bitte versuchen Sie es später erneut.');
        setLoading(false);
      }
    };

    fetchDeck();
  }, [id]);

  const handleDeleteDeck = async () => {
    if (!deck?.id) return;
    
    try {
      await deleteDeck(deck.id);
      navigate('/decks');
    } catch (err) {
      console.error('Failed to delete deck:', err);
      alert('Fehler beim Löschen des Decks. Bitte versuchen Sie es erneut.');
    }
  };

  const handleRemoveCard = async (cardId: string) => {
    if (!deck?.id) return;
    
    try {
      await removeCardFromDeck(deck.id, cardId);
      
      // Update local state to reflect the change
      setDeck({
        ...deck,
        cards: deck.cards.filter(card => card.id !== cardId)
      });
      
      setCardToRemove(null);
    } catch (err) {
      console.error('Failed to remove card from deck:', err);
      alert('Fehler beim Entfernen der Karte aus dem Deck. Bitte versuchen Sie es erneut.');
    }
  };

  if (loading) {
    return (
      <Box sx={{ display: 'flex', justifyContent: 'center', mt: 4 }}>
        <CircularProgress />
      </Box>
    );
  }

  if (error) {
    return <Typography color="error">{error}</Typography>;
  }

  if (!deck) {
    return <Typography>Deck nicht gefunden</Typography>;
  }

  return (
    <Box>
      <Box sx={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', mb: 3 }}>
        <Typography variant="h4" component="h1">
          {deck.name}
        </Typography>
        <Box sx={{ display: 'flex', gap: 2 }}>
          <Button
            variant="outlined"
            color="primary"
            onClick={() => navigate(`/cards/new?deckId=${deck.id}`)}
          >
            Karte hinzufügen
          </Button>
          {deck.id && <GenerateCardsButton deckId={deck.id} />}
          <Button
            variant="outlined"
            color="error"
            onClick={() => setDeleteConfirmOpen(true)}
          >
            Deck löschen
          </Button>
        </Box>
      </Box>

      {deck.description && (
        <Typography variant="body1" paragraph>
          {deck.description}
        </Typography>
      )}

      <Box sx={{ mb: 3, display: 'flex', flexWrap: 'wrap', gap: 1 }}>
        {deck.tags?.map((tag) => (
          <Chip key={tag} label={tag} />
        ))}
      </Box>

      <Typography variant="h5" component="h2" sx={{ mb: 2 }}>
        Karteikarten
      </Typography>

      {deck.cards.length === 0 ? (
        <Typography>
          Dieses Deck enthält noch keine Karteikarten. Fügen Sie eine Karte hinzu, um zu beginnen.
        </Typography>
      ) : (
        <Grid container spacing={3}>
          {deck.cards.map((card) => (
            <Grid item xs={12} sm={6} md={4} key={card.id}>
              <Card>
                <CardContent>
                  <Typography variant="h6" component="h3" gutterBottom>
                    {card.front}
                  </Typography>
                  <Typography variant="body2" color="text.secondary">
                    {card.back}
                  </Typography>
                </CardContent>
                <CardActions>
                  <Button 
                    size="small"
                    onClick={() => card.id && setCardToRemove(card.id)}
                    color="error"
                  >
                    Aus Deck entfernen
                  </Button>
                </CardActions>
              </Card>
            </Grid>
          ))}
        </Grid>
      )}

      {/* Delete Deck Confirmation Dialog */}
      <Dialog
        open={deleteConfirmOpen}
        onClose={() => setDeleteConfirmOpen(false)}
      >
        <DialogTitle>Deck löschen?</DialogTitle>
        <DialogContent>
          <DialogContentText>
            Sind Sie sicher, dass Sie das Deck "{deck.name}" löschen möchten? Diese Aktion kann nicht rückgängig gemacht werden.
          </DialogContentText>
        </DialogContent>
        <DialogActions>
          <Button onClick={() => setDeleteConfirmOpen(false)}>Abbrechen</Button>
          <Button onClick={handleDeleteDeck} color="error" autoFocus>
            Löschen
          </Button>
        </DialogActions>
      </Dialog>

      {/* Remove Card Confirmation Dialog */}
      <Dialog
        open={!!cardToRemove}
        onClose={() => setCardToRemove(null)}
      >
        <DialogTitle>Karte aus Deck entfernen?</DialogTitle>
        <DialogContent>
          <DialogContentText>
            Sind Sie sicher, dass Sie diese Karte aus dem Deck entfernen möchten?
          </DialogContentText>
        </DialogContent>
        <DialogActions>
          <Button onClick={() => setCardToRemove(null)}>Abbrechen</Button>
          <Button 
            onClick={() => cardToRemove && handleRemoveCard(cardToRemove)} 
            color="error" 
            autoFocus
          >
            Entfernen
          </Button>
        </DialogActions>
      </Dialog>
    </Box>
  );
};

export default DeckDetail;
