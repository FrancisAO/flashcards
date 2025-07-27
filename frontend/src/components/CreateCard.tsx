import React, { useState, useEffect } from 'react';
import { useNavigate, useLocation } from 'react-router-dom';
import { 
  Typography, 
  Box, 
  Button, 
  TextField, 
  Paper, 
  Chip,
  InputAdornment,
  IconButton,
  FormControl,
  InputLabel,
  Select,
  MenuItem,
  SelectChangeEvent
} from '@mui/material';
import AddIcon from '@mui/icons-material/Add';
import { createCard } from '../services/cardService';
import { addCardToDeck, getAllDecks } from '../services/deckService';
import { Card, Deck, DeckCard } from '../types/models';

const CreateCard: React.FC = () => {
  const navigate = useNavigate();
  const location = useLocation();
  const queryParams = new URLSearchParams(location.search);
  const initialDeckId = queryParams.get('deckId');
  
  const [front, setFront] = useState<string>('');
  const [back, setBack] = useState<string>('');
  const [tagInput, setTagInput] = useState<string>('');
  const [tags, setTags] = useState<string[]>([]);
  const [selectedDeckId, setSelectedDeckId] = useState<string>(initialDeckId || '');
  const [decks, setDecks] = useState<Deck[]>([]);
  const [frontError, setFrontError] = useState<string>('');
  const [backError, setBackError] = useState<string>('');
  const [isSubmitting, setIsSubmitting] = useState<boolean>(false);

  useEffect(() => {
    const fetchDecks = async () => {
      try {
        const data = await getAllDecks();
        setDecks(data);
      } catch (err) {
        console.error('Failed to fetch decks:', err);
      }
    };

    fetchDecks();
  }, []);

  const addTag = () => {
    if (tagInput.trim() !== '' && !tags.includes(tagInput.trim())) {
      setTags([...tags, tagInput.trim()]);
      setTagInput('');
    }
  };

  const removeTag = (tagToRemove: string) => {
    setTags(tags.filter(tag => tag !== tagToRemove));
  };

  const handleDeckChange = (event: SelectChangeEvent) => {
    setSelectedDeckId(event.target.value);
  };

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    
    let hasErrors = false;
    
    if (front.trim() === '') {
      setFrontError('Die Vorderseite der Karte darf nicht leer sein');
      hasErrors = true;
    }
    
    if (back.trim() === '') {
      setBackError('Die Rückseite der Karte darf nicht leer sein');
      hasErrors = true;
    }
    
    if (hasErrors) {
      return;
    }
    
    setIsSubmitting(true);
    
    try {
      const newCard: Card = {
        front: front.trim(),
        back: back.trim(),
        tags: tags
      };
      
      const createdCard = await createCard(newCard);
      
      // If a deck is selected, add the card to the deck
      if (selectedDeckId) {
        const deckCard: DeckCard = {
          deckId: selectedDeckId,
          cardId: createdCard.id!
        };
        
        await addCardToDeck(deckCard);
        navigate(`/decks/${selectedDeckId}`);
      } else {
        navigate('/cards');
      }
    } catch (error) {
      console.error('Fehler beim Erstellen der Karte:', error);
      alert('Fehler beim Erstellen der Karte. Bitte versuchen Sie es erneut.');
      setIsSubmitting(false);
    }
  };

  return (
    <Box>
      <Typography variant="h4" component="h1" gutterBottom>
        Neue Karteikarte erstellen
      </Typography>
      
      <Paper sx={{ p: 3, maxWidth: 600, mx: 'auto' }}>
        <form onSubmit={handleSubmit}>
          <TextField 
            fullWidth
            label="Vorderseite"
            variant="outlined"
            value={front}
            onChange={(e) => {
              setFront(e.target.value);
              if (e.target.value.trim() !== '') {
                setFrontError('');
              }
            }}
            error={!!frontError}
            helperText={frontError}
            margin="normal"
            required
            multiline
            rows={3}
          />
          
          <TextField 
            fullWidth
            label="Rückseite"
            variant="outlined"
            value={back}
            onChange={(e) => {
              setBack(e.target.value);
              if (e.target.value.trim() !== '') {
                setBackError('');
              }
            }}
            error={!!backError}
            helperText={backError}
            margin="normal"
            required
            multiline
            rows={3}
          />
          
          <FormControl fullWidth margin="normal">
            <InputLabel id="deck-select-label">Zum Deck hinzufügen</InputLabel>
            <Select
              labelId="deck-select-label"
              id="deck-select"
              value={selectedDeckId}
              label="Zum Deck hinzufügen"
              onChange={handleDeckChange}
            >
              <MenuItem value="">
                <em>Kein Deck auswählen</em>
              </MenuItem>
              {decks.map((deck) => (
                <MenuItem key={deck.id} value={deck.id}>
                  {deck.name}
                </MenuItem>
              ))}
            </Select>
          </FormControl>
          
          <TextField 
            fullWidth
            label="Tags hinzufügen"
            variant="outlined"
            value={tagInput}
            onChange={(e) => setTagInput(e.target.value)}
            margin="normal"
            InputProps={{
              endAdornment: (
                <InputAdornment position="end">
                  <IconButton onClick={addTag} edge="end">
                    <AddIcon />
                  </IconButton>
                </InputAdornment>
              ),
            }}
            onKeyPress={(e) => {
              if (e.key === 'Enter') {
                e.preventDefault();
                addTag();
              }
            }}
          />
          
          <Box sx={{ mt: 2, mb: 3, display: 'flex', flexWrap: 'wrap', gap: 1 }}>
            {tags.map((tag) => (
              <Chip 
                key={tag} 
                label={tag} 
                onDelete={() => removeTag(tag)} 
              />
            ))}
          </Box>
          
          <Box sx={{ display: 'flex', justifyContent: 'flex-end', gap: 2, mt: 2 }}>
            <Button 
              variant="outlined" 
              onClick={() => navigate('/cards')}
            >
              Abbrechen
            </Button>
            <Button 
              type="submit" 
              variant="contained" 
              disabled={isSubmitting}
            >
              {isSubmitting ? 'Wird erstellt...' : 'Karte erstellen'}
            </Button>
          </Box>
        </form>
      </Paper>
    </Box>
  );
};

export default CreateCard;
