import React, { useState } from 'react';
import {
  Dialog,
  DialogTitle,
  DialogContent,
  DialogActions,
  TextField,
  Button,
  Box,
  Chip,
  Typography,
  Alert,
  CircularProgress
} from '@mui/material';
import { Add as AddIcon } from '@mui/icons-material';
import { Project, CreateProjectRequest } from '../types/ocr';
import * as projectService from '../services/projectService';

interface CreateProjectModalProps {
  open: boolean;
  onClose: () => void;
  onProjectCreated: (project: Project) => void;
}

const CreateProjectModal: React.FC<CreateProjectModalProps> = ({
  open,
  onClose,
  onProjectCreated
}) => {
  const [formData, setFormData] = useState<CreateProjectRequest>({
    name: '',
    description: '',
    tags: []
  });
  const [newTag, setNewTag] = useState('');
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState<string | null>(null);
  const [nameError, setNameError] = useState<string | null>(null);

  const handleInputChange = (field: keyof CreateProjectRequest) => 
    (event: React.ChangeEvent<HTMLInputElement>) => {
      setFormData(prev => ({
        ...prev,
        [field]: event.target.value
      }));
      
      // Name-Validierung zurücksetzen
      if (field === 'name' && nameError) {
        setNameError(null);
      }
      
      // Fehler zurücksetzen
      if (error) {
        setError(null);
      }
    };

  const handleAddTag = () => {
    const tag = newTag.trim();
    if (tag && !formData.tags.includes(tag)) {
      setFormData(prev => ({
        ...prev,
        tags: [...prev.tags, tag]
      }));
      setNewTag('');
    }
  };

  const handleRemoveTag = (tagToRemove: string) => {
    setFormData(prev => ({
      ...prev,
      tags: prev.tags.filter(tag => tag !== tagToRemove)
    }));
  };

  const handleTagKeyPress = (event: React.KeyboardEvent) => {
    if (event.key === 'Enter') {
      event.preventDefault();
      handleAddTag();
    }
  };

  const validateForm = async (): Promise<boolean> => {
    setError(null);
    setNameError(null);

    // Name-Validierung
    if (!formData.name.trim()) {
      setNameError('Projektname ist erforderlich');
      return false;
    }

    if (formData.name.length < 3) {
      setNameError('Projektname muss mindestens 3 Zeichen lang sein');
      return false;
    }

    if (formData.name.length > 100) {
      setNameError('Projektname darf maximal 100 Zeichen lang sein');
      return false;
    }

    // Name-Eindeutigkeit prüfen
    try {
      const validation = await projectService.validateProjectName(formData.name.trim());
      if (!validation.isValid) {
        setNameError(validation.message || 'Projektname ist bereits vergeben');
        return false;
      }
    } catch (err) {
      console.error('Error validating project name:', err);
      // Bei Validierungsfehlern fortfahren - Backend wird finale Validierung durchführen
    }

    // Beschreibung-Validierung (optional)
    if (formData.description && formData.description.length > 500) {
      setError('Beschreibung darf maximal 500 Zeichen lang sein');
      return false;
    }

    // Tags-Validierung
    if (formData.tags.length > 10) {
      setError('Maximal 10 Tags erlaubt');
      return false;
    }

    for (const tag of formData.tags) {
      if (tag.length > 30) {
        setError('Tags dürfen maximal 30 Zeichen lang sein');
        return false;
      }
    }

    return true;
  };

  const handleSubmit = async () => {
    const isValid = await validateForm();
    if (!isValid) return;

    try {
      setLoading(true);
      setError(null);

      const projectRequest: CreateProjectRequest = {
        name: formData.name.trim(),
        description: formData.description?.trim() || undefined,
        tags: formData.tags
      };

      const newProject = await projectService.createProject(projectRequest);
      onProjectCreated(newProject);
      handleClose();
    } catch (err) {
      const errorMessage = err instanceof Error ? err.message : 'Fehler beim Erstellen des Projekts';
      setError(errorMessage);
      console.error('Error creating project:', err);
    } finally {
      setLoading(false);
    }
  };

  const handleClose = () => {
    if (!loading) {
      setFormData({
        name: '',
        description: '',
        tags: []
      });
      setNewTag('');
      setError(null);
      setNameError(null);
      onClose();
    }
  };

  const isFormValid = formData.name.trim().length >= 3 && !nameError;

  return (
    <Dialog
      open={open}
      onClose={handleClose}
      maxWidth="md"
      fullWidth
      PaperProps={{
        sx: { minHeight: '400px' }
      }}
    >
      <DialogTitle>
        Neues Projekt erstellen
      </DialogTitle>

      <DialogContent dividers>
        <Box sx={{ display: 'flex', flexDirection: 'column', gap: 3 }}>
          {/* Projektname */}
          <TextField
            label="Projektname"
            value={formData.name}
            onChange={handleInputChange('name')}
            error={!!nameError}
            helperText={nameError || 'Eindeutiger Name für das Projekt'}
            required
            fullWidth
            autoFocus
            disabled={loading}
            placeholder="z.B. Dokumentendigitalisierung 2024"
          />

          {/* Beschreibung */}
          <TextField
            label="Beschreibung"
            value={formData.description}
            onChange={handleInputChange('description')}
            multiline
            rows={3}
            fullWidth
            disabled={loading}
            placeholder="Optionale Beschreibung des Projekts..."
            helperText={`${formData.description?.length || 0}/500 Zeichen`}
          />

          {/* Tags */}
          <Box>
            <Typography variant="subtitle2" gutterBottom>
              Tags
            </Typography>
            
            {/* Tag-Eingabe */}
            <Box sx={{ display: 'flex', gap: 1, mb: 2 }}>
              <TextField
                size="small"
                placeholder="Tag hinzufügen..."
                value={newTag}
                onChange={(e) => setNewTag(e.target.value)}
                onKeyPress={handleTagKeyPress}
                disabled={loading || formData.tags.length >= 10}
                sx={{ flexGrow: 1 }}
              />
              <Button
                onClick={handleAddTag}
                disabled={!newTag.trim() || loading || formData.tags.length >= 10}
                variant="outlined"
                startIcon={<AddIcon />}
              >
                Hinzufügen
              </Button>
            </Box>

            {/* Tag-Liste */}
            {formData.tags.length > 0 && (
              <Box sx={{ display: 'flex', flexWrap: 'wrap', gap: 1 }}>
                {formData.tags.map((tag) => (
                  <Chip
                    key={tag}
                    label={tag}
                    onDelete={() => handleRemoveTag(tag)}
                    disabled={loading}
                    variant="outlined"
                  />
                ))}
              </Box>
            )}

            <Typography variant="caption" color="text.secondary">
              {formData.tags.length}/10 Tags • Tags helfen beim Organisieren und Finden von Projekten
            </Typography>
          </Box>

          {/* Fehlermeldung */}
          {error && (
            <Alert severity="error">
              {error}
            </Alert>
          )}
        </Box>
      </DialogContent>

      <DialogActions sx={{ px: 3, py: 2 }}>
        <Button
          onClick={handleClose}
          disabled={loading}
          color="inherit"
        >
          Abbrechen
        </Button>
        
        <Button
          onClick={handleSubmit}
          variant="contained"
          disabled={!isFormValid || loading}
          startIcon={loading ? <CircularProgress size={16} /> : undefined}
        >
          {loading ? 'Erstelle...' : 'Projekt erstellen'}
        </Button>
      </DialogActions>
    </Dialog>
  );
};

export default CreateProjectModal;