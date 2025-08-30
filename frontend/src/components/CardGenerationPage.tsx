import React, { useState } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import {
  Box,
  Typography,
  TextField,
  Button,
  Paper,
  Container,
  Divider,
  CircularProgress,
  Alert,
  Stepper,
  Step,
  StepLabel,
  InputAdornment,
  IconButton
} from '@mui/material';
import ArrowBackIcon from '@mui/icons-material/ArrowBack';
import HelpOutlineIcon from '@mui/icons-material/HelpOutline';
import DocumentUpload from './DocumentUpload';
import {
  startGeneration,
  uploadDocument,
  processGeneration
} from '../services/cardGenerationService';
import { DocumentUpload as DocumentUploadType, CardGenerationRequest } from '../types/models';

/**
 * Seite für die Eingabe von Hinweisen und Dokumenten-Upload zur Karteikartengenerierung.
 */
const CardGenerationPage: React.FC = () => {
  const { deckId } = useParams<{ deckId: string }>();
  const navigate = useNavigate();
  
  const [activeStep, setActiveStep] = useState<number>(0);
  const [prompt, setPrompt] = useState<string>('');
  const [numberOfCards, setNumberOfCards] = useState<number>(10);
  const [documents, setDocuments] = useState<DocumentUploadType[]>([]);
  const [requestId, setRequestId] = useState<string | null>(null);
  
  const [isLoading, setIsLoading] = useState<boolean>(false);
  const [uploadLoading, setUploadLoading] = useState<boolean>(false);
  const [error, setError] = useState<string | null>(null);
  const [uploadError, setUploadError] = useState<string | null>(null);

  const steps = ['Hinweise eingeben', 'Dokumente hochladen', 'Generierung starten'];

  const handlePromptChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    setPrompt(e.target.value);
  };

  const handleNumberOfCardsChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    const value = parseInt(e.target.value);
    if (!isNaN(value) && value > 0) {
      setNumberOfCards(value);
    }
  };

  const handleBack = () => {
    if (activeStep === 0) {
      // Zurück zur Deck-Detailseite
      navigate(`/decks/${deckId}`);
    } else {
      setActiveStep((prevStep) => prevStep - 1);
    }
  };

  const handleNext = async () => {
    if (activeStep === 0) {
      // Erste Anfrage zur Karteikartengenerierung erstellen
      await createGenerationRequest();
    } else if (activeStep === 1) {
      // Weiter zum nächsten Schritt (Generierung starten)
      setActiveStep((prevStep) => prevStep + 1);
    } else if (activeStep === 2) {
      // Generierungsprozess starten
      await startGenerationProcess();
    }
  };

  const createGenerationRequest = async () => {
    if (!deckId) return;
    
    setIsLoading(true);
    setError(null);
    
    try {
      const request: CardGenerationRequest = {
        prompt,
        numberOfCards
      };
      
      const response = await startGeneration(deckId, request);
      if (response.id) {
        setRequestId(response.id);
        setActiveStep((prevStep) => prevStep + 1);
      } else {
        throw new Error('Keine Request-ID erhalten');
      }
    } catch (err) {
      console.error('Fehler beim Erstellen der Generierungsanfrage:', err);
      setError('Fehler beim Erstellen der Generierungsanfrage. Bitte versuchen Sie es erneut.');
    } finally {
      setIsLoading(false);
    }
  };

  const handleDocumentUpload = async (file: File): Promise<DocumentUploadType> => {
    if (!deckId || !requestId) {
      throw new Error('Deck-ID oder Request-ID fehlt');
    }
    
    setUploadLoading(true);
    setUploadError(null);
    
    try {
      const uploadedDocument = await uploadDocument(deckId, requestId, file);
      setDocuments((prevDocuments) => [...prevDocuments, uploadedDocument]);
      return uploadedDocument;
    } catch (err) {
      console.error('Fehler beim Hochladen des Dokuments:', err);
      setUploadError('Fehler beim Hochladen des Dokuments. Bitte versuchen Sie es erneut.');
      throw err;
    } finally {
      setUploadLoading(false);
    }
  };

  const startGenerationProcess = async () => {
    if (!deckId || !requestId) return;
    
    setIsLoading(true);
    setError(null);
    
    try {
      await processGeneration(deckId, requestId);
      // Weiterleitung zur Ergebnisseite
      navigate(`/decks/${deckId}/generate/${requestId}/results`);
    } catch (err) {
      console.error('Fehler beim Starten des Generierungsprozesses:', err);
      setError('Fehler beim Starten des Generierungsprozesses. Bitte versuchen Sie es erneut.');
      setIsLoading(false);
    }
  };

  const isNextDisabled = () => {
    if (activeStep === 0) {
      return prompt.trim() === '';
    }
    if (activeStep === 1) {
      return documents.length === 0;
    }
    return false;
  };

  return (
    <Container maxWidth="md">
      <Paper sx={{ p: 4, mt: 4, mb: 4 }}>
        <Box sx={{ display: 'flex', alignItems: 'center', mb: 3 }}>
          <IconButton onClick={handleBack} sx={{ mr: 1 }}>
            <ArrowBackIcon />
          </IconButton>
          <Typography variant="h4" component="h1">
            Karteikarten generieren
          </Typography>
        </Box>

        <Stepper activeStep={activeStep} sx={{ mb: 4 }}>
          {steps.map((label) => (
            <Step key={label}>
              <StepLabel>{label}</StepLabel>
            </Step>
          ))}
        </Stepper>

        {error && (
          <Alert severity="error" sx={{ mb: 3 }}>
            {error}
          </Alert>
        )}

        {activeStep === 0 && (
          <Box>
            <Typography variant="h6" gutterBottom>
              Hinweise für die KI
            </Typography>
            <Typography variant="body2" color="text.secondary" paragraph>
              Geben Sie Hinweise für das KI-Modell ein, um die Generierung der Karteikarten zu steuern.
              Je spezifischer Ihre Hinweise sind, desto besser werden die generierten Karteikarten.
            </Typography>
            
            <TextField
              fullWidth
              label="Hinweise für die KI"
              multiline
              rows={6}
              value={prompt}
              onChange={handlePromptChange}
              placeholder="z.B. Erstelle Karteikarten zum Thema Programmierung mit Java. Fokussiere auf grundlegende Konzepte wie Klassen, Objekte und Vererbung."
              variant="outlined"
              margin="normal"
              InputProps={{
                endAdornment: (
                  <InputAdornment position="end">
                    <IconButton
                      aria-label="Hilfe zu Hinweisen"
                      edge="end"
                      onClick={() => alert('Geben Sie hier spezifische Anweisungen für die KI ein. Je detaillierter Ihre Hinweise sind, desto besser werden die generierten Karteikarten auf Ihre Bedürfnisse zugeschnitten sein.')}
                    >
                      <HelpOutlineIcon />
                    </IconButton>
                  </InputAdornment>
                ),
              }}
            />
            
            <TextField
              label="Anzahl der Karteikarten"
              type="number"
              value={numberOfCards}
              onChange={handleNumberOfCardsChange}
              variant="outlined"
              margin="normal"
              InputProps={{
                inputProps: { min: 1, max: 50 }
              }}
              sx={{ width: '200px' }}
            />
          </Box>
        )}

        {activeStep === 1 && (
          <DocumentUpload
            onUpload={handleDocumentUpload}
            uploadedDocuments={documents}
            isLoading={uploadLoading}
            error={uploadError}
          />
        )}

        {activeStep === 2 && (
          <Box>
            <Typography variant="h6" gutterBottom>
              Generierung starten
            </Typography>
            <Typography variant="body1" paragraph>
              Sie haben folgende Informationen für die Karteikartengenerierung angegeben:
            </Typography>
            
            <Box sx={{ mb: 3 }}>
              <Typography variant="subtitle1">Hinweise für die KI:</Typography>
              <Paper variant="outlined" sx={{ p: 2, mb: 2, backgroundColor: 'background.default' }}>
                <Typography variant="body2">{prompt}</Typography>
              </Paper>
              
              <Typography variant="subtitle1">Anzahl der Karteikarten: {numberOfCards}</Typography>
              
              <Typography variant="subtitle1" sx={{ mt: 2 }}>
                Hochgeladene Dokumente: {documents.length}
              </Typography>
              <Box component="ul" sx={{ pl: 2 }}>
                {documents.map((doc) => (
                  <Typography component="li" key={doc.id}>
                    {doc.originalFilename}
                  </Typography>
                ))}
              </Box>
            </Box>
            
            <Typography variant="body1" paragraph>
              Klicken Sie auf "Generierung starten", um den Prozess zu beginnen.
              Dies kann je nach Anzahl und Größe der Dokumente einige Zeit dauern.
            </Typography>
          </Box>
        )}

        <Divider sx={{ my: 3 }} />

        <Box sx={{ display: 'flex', justifyContent: 'space-between' }}>
          <Button
            onClick={handleBack}
            variant="outlined"
          >
            {activeStep === 0 ? 'Abbrechen' : 'Zurück'}
          </Button>
          <Button
            onClick={handleNext}
            variant="contained"
            disabled={isNextDisabled() || isLoading}
            endIcon={isLoading ? <CircularProgress size={20} /> : null}
          >
            {activeStep === steps.length - 1 ? 'Generierung starten' : 'Weiter'}
          </Button>
        </Box>
      </Paper>
    </Container>
  );
};

export default CardGenerationPage;