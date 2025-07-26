import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { 
  Typography, 
  Box, 
  Button, 
  TextField, 
  Paper, 
  Chip,
  InputAdornment,
  IconButton
} from '@mui/material';
import AddIcon from '@mui/icons-material/Add';
import { createDeck } from '../services/deckService';
import { Deck } from '../types/models';

const CreateDeck: React.FC = () => {
  const navigate = useNavigate();
  const [name, setName] = useState<string>('');
  const [description, setDescription] = useState<string>('');
  const [tagInput, setTagInput] = useState<string>('');
  const [tags, setTags] = useState<string[]>([]);
  const [nameError, setNameError] = useState<string>('');
  const [isSubmitting, setIsSubmitting] = useState<boolean>(false);

  const addTag = () => {
    if (tagInput.trim() !== '' && !tags.includes(tagInput.trim())) {
      setTags([...tags, tagInput.trim()]);
      setTagInput('');
    }
  };

  const removeTag = (tagToRemove: string) => {
    setTags(tags.filter(tag => tag !== tagToRemove));
  };

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    
    if (name.trim() === '') {
      setNameError('Der Name des Decks darf nicht leer sein');
      return;
    }
    
    setIsSubmitting(true);
    
    try {
      const newDeck: Deck = {
        name: name.trim(),
        description: description.trim(),
        tags: tags
      };
      
      const createdDeck = await createDeck(newDeck);
      navigate(`/decks/${createdDeck.id}`);
    } catch (error) {
      console.error('Fehler beim Erstellen des Decks:', error);
      alert('Fehler beim Erstellen des Decks. Bitte versuchen Sie es erneut.');
      setIsSubmitting(false);
    }
  };

  return (
    <Box>
      <Typography variant="h4" component="h1" gutterBottom>
        Neues Deck erstellen
      </Typography>
      
      <Paper sx={{ p: 3, maxWidth: 600, mx: 'auto' }}>
        <form onSubmit={handleSubmit}>
          <TextField 
            fullWidth
            label="Name des Decks"
            variant="outlined"
            value={name}
            onChange={(e) => {
              setName(e.target.value);
              if (e.target.value.trim() !== '') {
                setNameError('');
              }
            }}
            error={!!nameError}
            helperText={nameError}
            margin="normal"
            required
          />
          
          <TextField 
            fullWidth
            label="Beschreibung"
            variant="outlined"
            value={description}
            onChange={(e) => setDescription(e.target.value)}
            margin="normal"
            multiline
            rows={4}
          />
          
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
              onClick={() => navigate('/decks')}
            >
              Abbrechen
            </Button>
            <Button 
              type="submit" 
              variant="contained" 
              disabled={isSubmitting}
            >
              {isSubmitting ? 'Wird erstellt...' : 'Deck erstellen'}
            </Button>
          </Box>
        </form>
      </Paper>
    </Box>
  );
};

export default CreateDeck;
