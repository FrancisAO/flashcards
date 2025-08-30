import React, { useState, useEffect } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import {
  Box,
  Typography,
  Button,
  Container,
  Grid,
  Paper,
  CircularProgress,
  Alert,
  Divider,
  IconButton,
  Tooltip
} from '@mui/material';
import ArrowBackIcon from '@mui/icons-material/ArrowBack';
import SaveIcon from '@mui/icons-material/Save';
import GeneratedCardItem from './GeneratedCardItem';
import EditGeneratedCardModal from './EditGeneratedCardModal';
import {
  getGenerationStatus,
  getGeneratedCards,
  updateGeneratedCard,
  saveGeneratedCards
} from '../services/cardGenerationService';
import { AIGeneratedCard, AIGenerationRequest, AIGenerationStatus } from '../types/models';

/**
 * Seite zur Anzeige der generierten Karteikarten.
 * Zeigt die generierten Karteikarten an und bietet die Möglichkeit, sie zu bearbeiten und im Deck zu speichern.
 */
const GeneratedCardsPage: React.FC = () => {
  const { deckId, requestId } = useParams<{ deckId: string; requestId: string }>();
  const navigate = useNavigate();
  
  const [generationRequest, setGenerationRequest] = useState<AIGenerationRequest | null>(null);
  const [generatedCards, setGeneratedCards] = useState<AIGeneratedCard[]>([]);
  const [selectedCard, setSelectedCard] = useState<AIGeneratedCard | null>(null);
  const [editModalOpen, setEditModalOpen] = useState<boolean>(false);
  
  const [isLoading, setIsLoading] = useState<boolean>(true);
  const [isSaving, setIsSaving] = useState<boolean>(false);
  const [isEditing, setIsEditing] = useState<boolean>(false);
  const [error, setError] = useState<string | null>(null);
  const [editError, setEditError] = useState<string | null>(null);
  const [saveSuccess, setSaveSuccess] = useState<boolean>(false);
  
  // Polling-Intervall für den Status der Generierungsanfrage (in ms)
  const POLLING_INTERVAL = 5000;
  
  useEffect(() => {
    if (!deckId || !requestId) return;
    
    const fetchGenerationStatus = async () => {
      try {
        const response = await getGenerationStatus(deckId, requestId);
        setGenerationRequest(response);
        
        // Wenn die Generierung abgeschlossen ist, Karteikarten abrufen
        if (response.status === AIGenerationStatus.COMPLETED) {
          fetchGeneratedCards();
        } else if (response.status === AIGenerationStatus.PROCESSING) {
          // Wenn die Generierung noch läuft, nach einem Intervall erneut abfragen
          setTimeout(fetchGenerationStatus, POLLING_INTERVAL);
        } else if (response.status === AIGenerationStatus.FAILED) {
          setError('Die Karteikartengenerierung ist fehlgeschlagen. Bitte versuchen Sie es erneut.');
          setIsLoading(false);
        }
      } catch (err) {
        console.error('Fehler beim Abrufen des Generierungsstatus:', err);
        setError('Fehler beim Abrufen des Generierungsstatus. Bitte versuchen Sie es später erneut.');
        setIsLoading(false);
      }
    };
    
    const fetchGeneratedCards = async () => {
      try {
        const cards = await getGeneratedCards(deckId, requestId);
        setGeneratedCards(cards);
        setIsLoading(false);
      } catch (err) {
        console.error('Fehler beim Abrufen der generierten Karteikarten:', err);
        setError('Fehler beim Abrufen der generierten Karteikarten. Bitte versuchen Sie es später erneut.');
        setIsLoading(false);
      }
    };
    
    fetchGenerationStatus();
    
    // Cleanup-Funktion
    return () => {
      // Polling stoppen
    };
  }, [deckId, requestId]);
  
  const handleEditCard = (card: AIGeneratedCard) => {
    setSelectedCard(card);
    setEditModalOpen(true);
    setEditError(null);
  };
  
  const handleCloseEditModal = () => {
    setEditModalOpen(false);
    setSelectedCard(null);
    setEditError(null);
  };
  
  const handleSaveCardEdit = async (cardId: string, front: string, back: string) => {
    if (!deckId || !requestId) return;
    
    setIsEditing(true);
    setEditError(null);
    
    try {
      const updatedCard = await updateGeneratedCard(deckId, requestId, cardId, { front, back });
      
      // Aktualisiere die Karteikarte in der lokalen Liste
      setGeneratedCards((prevCards) =>
        prevCards.map((card) => (card.id === cardId ? updatedCard : card))
      );
      
      setEditModalOpen(false);
      setSelectedCard(null);
    } catch (err) {
      console.error('Fehler beim Aktualisieren der Karteikarte:', err);
      setEditError('Fehler beim Aktualisieren der Karteikarte. Bitte versuchen Sie es erneut.');
    } finally {
      setIsEditing(false);
    }
  };
  
  const handleSaveAllCards = async () => {
    if (!deckId || !requestId) return;
    
    setIsSaving(true);
    setError(null);
    
    try {
      await saveGeneratedCards(deckId, requestId);
      setSaveSuccess(true);
      
      // Aktualisiere die lokale Liste, um anzuzeigen, dass die Karteikarten gespeichert wurden
      setGeneratedCards((prevCards) =>
        prevCards.map((card) => ({ ...card, saved: true }))
      );
      
      // Nach kurzer Verzögerung zurück zur Deck-Detailseite navigieren
      setTimeout(() => {
        navigate(`/decks/${deckId}`);
      }, 2000);
    } catch (err) {
      console.error('Fehler beim Speichern der Karteikarten:', err);
      setError('Fehler beim Speichern der Karteikarten. Bitte versuchen Sie es erneut.');
      setIsSaving(false);
    }
  };
  
  const handleBack = () => {
    navigate(`/decks/${deckId}`);
  };
  
  // Rendere Ladeindikator, wenn die Daten noch geladen werden
  if (isLoading) {
    return (
      <Container maxWidth="md">
        <Paper sx={{ p: 4, mt: 4, mb: 4, textAlign: 'center' }}>
          <CircularProgress size={60} sx={{ mb: 3 }} />
          <Typography variant="h6">
            {generationRequest?.status === AIGenerationStatus.PROCESSING
              ? 'Karteikarten werden generiert...'
              : 'Lade generierte Karteikarten...'}
          </Typography>
          {generationRequest?.status === AIGenerationStatus.PROCESSING && (
            <Typography variant="body2" color="text.secondary" sx={{ mt: 1 }}>
              Dieser Vorgang kann einige Minuten dauern, abhängig von der Anzahl und Größe der Dokumente.
            </Typography>
          )}
        </Paper>
      </Container>
    );
  }
  
  return (
    <Container maxWidth="lg">
      <Paper sx={{ p: 4, mt: 4, mb: 4 }}>
        <Box sx={{ display: 'flex', alignItems: 'center', mb: 3 }}>
          <IconButton onClick={handleBack} sx={{ mr: 1 }}>
            <ArrowBackIcon />
          </IconButton>
          <Typography variant="h4" component="h1">
            Generierte Karteikarten
          </Typography>
        </Box>
        
        {error && (
          <Alert severity="error" sx={{ mb: 3 }}>
            {error}
          </Alert>
        )}
        
        {saveSuccess && (
          <Alert severity="success" sx={{ mb: 3 }}>
            Karteikarten wurden erfolgreich im Deck gespeichert. Sie werden zur Deck-Übersicht weitergeleitet...
          </Alert>
        )}
        
        {generationRequest && (
          <Box sx={{ mb: 3 }}>
            <Typography variant="subtitle1" gutterBottom>
              Hinweise für die KI:
            </Typography>
            <Paper variant="outlined" sx={{ p: 2, backgroundColor: 'background.default' }}>
              <Typography variant="body2">{generationRequest.prompt}</Typography>
            </Paper>
          </Box>
        )}
        
        <Box sx={{ display: 'flex', justifyContent: 'flex-end', mb: 3 }}>
          <Tooltip title="Alle Karteikarten im Deck speichern">
            <span>
              <Button
                variant="contained"
                color="primary"
                startIcon={<SaveIcon />}
                onClick={handleSaveAllCards}
                disabled={isSaving || generatedCards.length === 0 || saveSuccess}
              >
                {isSaving ? 'Speichern...' : 'Alle Karteikarten speichern'}
              </Button>
            </span>
          </Tooltip>
        </Box>
        
        <Divider sx={{ mb: 3 }} />
        
        {generatedCards.length === 0 ? (
          <Typography variant="body1" sx={{ textAlign: 'center', py: 4 }}>
            Keine Karteikarten generiert. Bitte versuchen Sie es erneut mit anderen Hinweisen oder Dokumenten.
          </Typography>
        ) : (
          <Grid container spacing={3}>
            {generatedCards.map((card) => (
              <Grid item xs={12} sm={6} md={4} key={card.id}>
                <GeneratedCardItem
                  card={card}
                  onEdit={handleEditCard}
                />
              </Grid>
            ))}
          </Grid>
        )}
      </Paper>
      
      <EditGeneratedCardModal
        open={editModalOpen}
        card={selectedCard}
        onClose={handleCloseEditModal}
        onSave={handleSaveCardEdit}
        isLoading={isEditing}
        error={editError}
      />
    </Container>
  );
};

export default GeneratedCardsPage;