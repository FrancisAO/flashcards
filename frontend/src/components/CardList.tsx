import React, { useState, useEffect } from 'react';
import { Link as RouterLink } from 'react-router-dom';
import { Typography, Box, Button, Grid, Card, CardContent, CardActions, Chip } from '@mui/material';
import { Card as CardModel } from '../types/models';
import { getAllCards } from '../services/cardService';

const CardList: React.FC = () => {
  const [cards, setCards] = useState<CardModel[]>([]);
  const [loading, setLoading] = useState<boolean>(true);
  const [error, setError] = useState<string | null>(null);

  useEffect(() => {
    const fetchCards = async () => {
      try {
        const data = await getAllCards();
        setCards(data);
        setLoading(false);
      } catch (err) {
        console.error('Failed to fetch cards:', err);
        setError('Fehler beim Laden der Karten. Bitte versuchen Sie es später erneut.');
        setLoading(false);
      }
    };

    fetchCards();
  }, []);

  if (loading) {
    return <Typography>Lade Karten...</Typography>;
  }

  if (error) {
    return <Typography color="error">{error}</Typography>;
  }

  return (
    <Box>
      <Box sx={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', mb: 3 }}>
        <Typography variant="h4" component="h1">
          Alle Karteikarten
        </Typography>
        <Button 
          variant="contained" 
          component={RouterLink} 
          to="/cards/new"
        >
          Neue Karte erstellen
        </Button>
      </Box>

      {cards.length === 0 ? (
        <Typography>Keine Karteikarten gefunden. Erstellen Sie eine neue Karte, um zu beginnen.</Typography>
      ) : (
        <Grid container spacing={3}>
          {cards.map((card) => (
            <Grid item xs={12} sm={6} md={4} key={card.id}>
              <Card sx={{ height: '100%', display: 'flex', flexDirection: 'column' }}>
                <CardContent sx={{ flexGrow: 1 }}>
                  <Typography variant="h6" component="h2" gutterBottom>
                    Vorderseite:
                  </Typography>
                  <Typography variant="body1" paragraph>
                    {card.front}
                  </Typography>
                  <Typography variant="h6" component="h3" gutterBottom>
                    Rückseite:
                  </Typography>
                  <Typography variant="body1" paragraph>
                    {card.back}
                  </Typography>
                  <Box sx={{ display: 'flex', flexWrap: 'wrap', gap: 0.5 }}>
                    {card.tags?.map((tag) => (
                      <Chip key={tag} label={tag} size="small" />
                    ))}
                  </Box>
                </CardContent>
                <CardActions>
                  <Button 
                    size="small" 
                    component={RouterLink} 
                    to={`/cards/${card.id}/edit`}
                  >
                    Bearbeiten
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

export default CardList;
