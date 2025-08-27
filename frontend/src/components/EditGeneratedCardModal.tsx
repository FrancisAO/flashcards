import React, { useState, useEffect } from 'react';
import {
  Dialog,
  DialogTitle,
  DialogContent,
  DialogActions,
  TextField,
  Button,
  Box,
  Typography,
  CircularProgress,
  Alert
} from '@mui/material';
import { AIGeneratedCard } from '../types/models';

interface EditGeneratedCardModalProps {
  open: boolean;
  card: AIGeneratedCard | null;
  onClose: () => void;
  onSave: (cardId: string, front: string, back: string) => Promise<void>;
  isLoading?: boolean;
  error?: string | null;
}

/**
 * Modal-Komponente zur Bearbeitung einer generierten Karteikarte.
 */
const EditGeneratedCardModal: React.FC<EditGeneratedCardModalProps> = ({
  open,
  card,
  onClose,
  onSave,
  isLoading = false,
  error = null
}) => {
  const [front, setFront] = useState<string>('');
  const [back, setBack] = useState<string>('');
  const [frontError, setFrontError] = useState<string | null>(null);
  const [backError, setBackError] = useState<string | null>(null);

  useEffect(() => {
    if (card) {
      setFront(card.front);
      setBack(card.back);
      setFrontError(null);
      setBackError(null);
    }
  }, [card]);

  const handleFrontChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    setFront(e.target.value);
    if (e.target.value.trim() === '') {
      setFrontError('Die Vorderseite darf nicht leer sein');
    } else {
      setFrontError(null);
    }
  };

  const handleBackChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    setBack(e.target.value);
    if (e.target.value.trim() === '') {
      setBackError('Die Rückseite darf nicht leer sein');
    } else {
      setBackError(null);
    }
  };

  const handleSave = async () => {
    // Validierung
    let isValid = true;
    
    if (front.trim() === '') {
      setFrontError('Die Vorderseite darf nicht leer sein');
      isValid = false;
    }
    
    if (back.trim() === '') {
      setBackError('Die Rückseite darf nicht leer sein');
      isValid = false;
    }
    
    if (!isValid || !card?.id) return;
    
    await onSave(card.id, front, back);
  };

  return (
    <Dialog 
      open={open} 
      onClose={onClose}
      fullWidth
      maxWidth="md"
    >
      <DialogTitle>Karteikarte bearbeiten</DialogTitle>
      
      <DialogContent>
        {error && (
          <Alert severity="error" sx={{ mb: 2 }}>
            {error}
          </Alert>
        )}
        
        <Box sx={{ mt: 1 }}>
          <Typography variant="subtitle1" gutterBottom>
            Vorderseite
          </Typography>
          <TextField
            fullWidth
            multiline
            rows={4}
            value={front}
            onChange={handleFrontChange}
            error={!!frontError}
            helperText={frontError}
            disabled={isLoading}
            variant="outlined"
            margin="normal"
          />
          
          <Typography variant="subtitle1" gutterBottom sx={{ mt: 2 }}>
            Rückseite
          </Typography>
          <TextField
            fullWidth
            multiline
            rows={4}
            value={back}
            onChange={handleBackChange}
            error={!!backError}
            helperText={backError}
            disabled={isLoading}
            variant="outlined"
            margin="normal"
          />
        </Box>
      </DialogContent>
      
      <DialogActions sx={{ px: 3, pb: 2 }}>
        <Button 
          onClick={onClose} 
          disabled={isLoading}
        >
          Abbrechen
        </Button>
        <Button 
          onClick={handleSave} 
          variant="contained" 
          disabled={isLoading || !!frontError || !!backError}
          startIcon={isLoading ? <CircularProgress size={20} /> : null}
        >
          Speichern
        </Button>
      </DialogActions>
    </Dialog>
  );
};

export default EditGeneratedCardModal;