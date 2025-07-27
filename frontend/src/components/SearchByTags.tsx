import React, { useState, useEffect } from 'react';
import {
  Box,
  Typography,
  Chip,
  Grid,
  Card,
  CardContent,
  Alert,
  CircularProgress
} from '@mui/material';
import { getAllTags, getCardsByTags } from '../services/cardService';
import { Card as CardModel } from '../types/models';

const SearchByTags: React.FC = () => {
  const [availableTags, setAvailableTags] = useState<string[]>([]);
  const [selectedTags, setSelectedTags] = useState<string[]>([]);
  const [filteredCards, setFilteredCards] = useState<CardModel[]>([]);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState<string | null>(null);

  useEffect(() => {
    loadAvailableTags();
  }, []);

  useEffect(() => {
    if (selectedTags.length > 0) {
      loadCardsByTags();
    } else {
      setFilteredCards([]);
    }
  }, [selectedTags]);

  const loadAvailableTags = async () => {
    try {
      setLoading(true);
      const tags = await getAllTags();
      setAvailableTags(tags);
    } catch (error) {
      setError('Fehler beim Laden der verfügbaren Tags');
      console.error('Error loading tags:', error);
    } finally {
      setLoading(false);
    }
  };

  const loadCardsByTags = async () => {
    try {
      setLoading(true);
      const cards = await getCardsByTags(selectedTags);
      setFilteredCards(cards);
    } catch (error) {
      setError('Fehler beim Laden der Karten');
      console.error('Error loading cards by tags:', error);
    } finally {
      setLoading(false);
    }
  };

  const handleTagClick = (tag: string) => {
    if (selectedTags.includes(tag)) {
      setSelectedTags(selectedTags.filter(t => t !== tag));
    } else {
      setSelectedTags([...selectedTags, tag]);
    }
  };

  const clearSelection = () => {
    setSelectedTags([]);
    setFilteredCards([]);
  };

  if (loading && availableTags.length === 0) {
    return (
      <Box display="flex" justifyContent="center" alignItems="center" minHeight="200px">
        <CircularProgress />
      </Box>
    );
  }

  return (
    <Box sx={{ p: 3 }}>
      <Typography variant="h4" component="h1" gutterBottom>
        Suchen nach Tags
      </Typography>

      {error && (
        <Alert severity="error" sx={{ mb: 3 }}>
          {error}
        </Alert>
      )}

      <Box sx={{ mb: 4 }}>
        <Typography variant="h6" gutterBottom>
          Verfügbare Tags
        </Typography>
        <Typography variant="body2" color="text.secondary" sx={{ mb: 2 }}>
          Klicken Sie auf Tags, um Karten zu filtern. Mehrere Tags werden mit UND verknüpft.
        </Typography>
        <Box sx={{ display: 'flex', flexWrap: 'wrap', gap: 1, mb: 2 }}>
          {availableTags.map((tag) => (
            <Chip
              key={tag}
              label={tag}
              onClick={() => handleTagClick(tag)}
              color={selectedTags.includes(tag) ? 'primary' : 'default'}
              variant={selectedTags.includes(tag) ? 'filled' : 'outlined'}
              clickable
            />
          ))}
        </Box>
        
        {selectedTags.length > 0 && (
          <Box sx={{ display: 'flex', alignItems: 'center', gap: 2 }}>
            <Typography variant="body2">
              Ausgewählte Tags: {selectedTags.join(', ')}
            </Typography>
            <Chip
              label="Auswahl löschen"
              onClick={clearSelection}
              color="secondary"
              size="small"
              variant="outlined"
            />
          </Box>
        )}
      </Box>

      {loading && selectedTags.length > 0 && (
        <Box display="flex" justifyContent="center" sx={{ mb: 3 }}>
          <CircularProgress size={24} />
        </Box>
      )}

      {selectedTags.length > 0 && (
        <Box>
          <Typography variant="h6" gutterBottom>
            Gefundene Karten ({filteredCards.length})
          </Typography>
          
          {filteredCards.length === 0 && !loading ? (
            <Alert severity="info">
              Keine Karten mit den ausgewählten Tags gefunden.
            </Alert>
          ) : (
            <Grid container spacing={2}>
              {filteredCards.map((card) => (
                <Grid item xs={12} sm={6} md={4} key={card.id}>
                  <Card>
                    <CardContent>
                      <Typography variant="h6" gutterBottom noWrap>
                        {card.front}
                      </Typography>
                      <Typography variant="body2" color="text.secondary" paragraph>
                        {card.back}
                      </Typography>
                      <Box sx={{ display: 'flex', flexWrap: 'wrap', gap: 0.5 }}>
                        {card.tags.map((tag) => (
                          <Chip
                            key={tag}
                            label={tag}
                            size="small"
                            color={selectedTags.includes(tag) ? 'primary' : 'default'}
                            variant="outlined"
                          />
                        ))}
                      </Box>
                    </CardContent>
                  </Card>
                </Grid>
              ))}
            </Grid>
          )}
        </Box>
      )}
    </Box>
  );
};

export default SearchByTags;
