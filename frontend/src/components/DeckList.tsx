import React, { useState, useEffect } from 'react';
import { Link as RouterLink } from 'react-router-dom';
import { Typography, Box, Button, Grid, Card, CardContent, CardActions, Chip } from '@mui/material';
import { Deck } from '../types/models';
import { getAllDecks } from '../services/deckService';

const DeckList: React.FC = () => {
  const [decks, setDecks] = useState<Deck[]>([]);
  const [loading, setLoading] = useState<boolean>(true);
  const [error, setError] = useState<string | null>(null);

  useEffect(() => {
    const fetchDecks = async () => {
      try {
        const data = await getAllDecks();
        setDecks(data);
        setLoading(false);
      } catch (err) {
        console.error('Failed to fetch decks:', err);
        setError('Fehler beim Laden der Decks. Bitte versuchen Sie es später erneut.');
        setLoading(false);
      }
    };

    fetchDecks();
  }, []);

  if (loading) {
    return <Typography>Lade Decks...</Typography>;
  }

  if (error) {
    return <Typography color="error">{error}</Typography>;
  }

  return (
    <Box>
      <Box sx={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', mb: 3 }}>
        <Typography variant="h4" component="h1">
          Karteikartendecks
        </Typography>
        <Button 
          variant="contained" 
          component={RouterLink} 
          to="/decks/new"
        >
          Neues Deck erstellen
        </Button>
      </Box>

      {decks.length === 0 ? (
        <Typography>Keine Decks gefunden. Erstellen Sie ein neues Deck, um zu beginnen.</Typography>
      ) : (
        <Grid container spacing={3}>
          {decks.map((deck) => (
            <Grid item xs={12} sm={6} md={4} key={deck.id}>
              <Card sx={{ height: '100%', display: 'flex', flexDirection: 'column' }}>
                <CardContent sx={{ flexGrow: 1 }}>
                  <Typography variant="h5" component="h2" gutterBottom>
                    {deck.name}
                  </Typography>
                  <Typography variant="body2" color="text.secondary" paragraph>
                    {deck.description || 'Keine Beschreibung'}
                  </Typography>
                  <Box sx={{ display: 'flex', flexWrap: 'wrap', gap: 0.5 }}>
                    {deck.tags?.map((tag) => (
                      <Chip key={tag} label={tag} size="small" />
                    ))}
                  </Box>
                </CardContent>
                <CardActions>
                  <Button 
                    size="small" 
                    component={RouterLink} 
                    to={`/decks/${deck.id}`}
                  >
                    Öffnen
                  </Button>
                </CardActions>
              </Card>
            </Grid>
          ))}
        </Grid>
      )}
    </Box>
  );
};

export default DeckList;
