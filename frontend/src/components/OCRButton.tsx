import React, { useState } from 'react';
import {
  Button,
  IconButton,
  CircularProgress,
  Tooltip,
  Dialog,
  DialogTitle,
  DialogContent,
  DialogActions,
  Typography,
  Alert,
  Box
} from '@mui/material';
import {
  Psychology as PsychologyIcon,
  Settings as SettingsIcon
} from '@mui/icons-material';
import { OCROptions, OCRResult, OCRStatus } from '../types/ocr';
import * as ocrService from '../services/ocrService';
// import OCROptionsModal from './OCROptionsModal';
// import OCRStatusComponent from './OCRStatus';

interface OCRButtonProps {
  fileId: string;
  onOCRComplete?: () => void;
  onOCRStart?: () => void;
  size?: 'small' | 'medium' | 'large';
  variant?: 'contained' | 'outlined' | 'text';
  disabled?: boolean;
}

const OCRButton: React.FC<OCRButtonProps> = ({
  fileId,
  onOCRComplete,
  onOCRStart,
  size = 'medium',
  variant = 'outlined',
  disabled = false
}) => {
  const [isProcessing, setIsProcessing] = useState(false);
  const [ocrResult, setOcrResult] = useState<OCRResult | null>(null);
  const [error, setError] = useState<string | null>(null);
  const [optionsModalOpen, setOptionsModalOpen] = useState(false);
  const [statusDialogOpen, setStatusDialogOpen] = useState(false);
  const [pollInterval, setPollInterval] = useState<NodeJS.Timeout | null>(null);

  const startOCR = async (options?: OCROptions) => {
    try {
      setError(null);
      setIsProcessing(true);
      onOCRStart?.();

      const result = await ocrService.processSingleFile(fileId, options);
      setOcrResult(result);

      // Status-Polling starten
      
      const cleanup = ocrService.pollOCRStatus(
        fileId!,
        (updatedResult) => {
          setOcrResult(updatedResult);
        },
        (finalResult) => {
          setOcrResult(finalResult);
          setIsProcessing(false);
          if (finalResult.status === OCRStatus.SUCCESS) {
            onOCRComplete?.();
          }
          cleanup();
        },
        (error) => {
          setError(error.message);
          setIsProcessing(false);
          cleanup();
        }
      );

    } catch (err) {
      setError(err instanceof Error ? err.message : 'OCR-Verarbeitung fehlgeschlagen');
      setIsProcessing(false);
      console.error('OCR processing error:', err);
    }
  };

  const handleOCRClick = () => {
    startOCR();
  };

  const handleOCRWithOptions = (options: OCROptions) => {
    setOptionsModalOpen(false);
    startOCR(options);
  };

  const handleShowStatus = () => {
    setStatusDialogOpen(true);
  };

  const getButtonContent = () => {
    if (isProcessing) {
      return (
        <>
          <CircularProgress size={16} sx={{ mr: 1 }} />
          {size !== 'small' && 'Verarbeitung...'}
        </>
      );
    }

    if (ocrResult?.status === OCRStatus.SUCCESS) {
      return (
        <>
          <PsychologyIcon sx={{ mr: size === 'small' ? 0 : 1 }} />
          {size !== 'small' && 'Erneut verarbeiten'}
        </>
      );
    }

    return (
      <>
        <PsychologyIcon sx={{ mr: size === 'small' ? 0 : 1 }} />
        {size !== 'small' && 'OCR starten'}
      </>
    );
  };

  const getButtonColor = () => {
    if (ocrResult?.status === OCRStatus.SUCCESS) {
      return 'success' as const;
    }
    if (ocrResult?.status === OCRStatus.FAILED || error) {
      return 'error' as const;
    }
    return 'primary' as const;
  };

  if (size === 'small') {
    return (
      <Box sx={{ display: 'flex', gap: 0.5 }}>
        <Tooltip title={isProcessing ? 'OCR läuft...' : 'OCR starten'}>
          <IconButton
            onClick={handleOCRClick}
            disabled={disabled || isProcessing}
            color={getButtonColor()}
            size="small"
          >
            {isProcessing ? (
              <CircularProgress size={16} />
            ) : (
              <PsychologyIcon fontSize="small" />
            )}
          </IconButton>
        </Tooltip>

        {!isProcessing && (
          <Tooltip title="OCR-Einstellungen">
            <IconButton
              onClick={() => setOptionsModalOpen(true)}
              disabled={disabled}
              size="small"
            >
              <SettingsIcon fontSize="small" />
            </IconButton>
          </Tooltip>
        )}

        {ocrResult && (
          <Tooltip title="OCR-Status anzeigen">
            <IconButton
              onClick={handleShowStatus}
              size="small"
              color={getButtonColor()}
            >
              <PsychologyIcon fontSize="small" />
            </IconButton>
          </Tooltip>
        )}

        {/* OCROptionsModal placeholder */}
        <Dialog
          open={optionsModalOpen}
          onClose={() => setOptionsModalOpen(false)}
        >
          <DialogTitle>OCR-Einstellungen</DialogTitle>
          <DialogContent>
            <Typography>OCR-Einstellungen werden hier implementiert</Typography>
          </DialogContent>
          <DialogActions>
            <Button onClick={() => setOptionsModalOpen(false)}>Abbrechen</Button>
            <Button onClick={() => handleOCRWithOptions({})}>Starten</Button>
          </DialogActions>
        </Dialog>

        <Dialog
          open={statusDialogOpen}
          onClose={() => setStatusDialogOpen(false)}
          maxWidth="sm"
          fullWidth
        >
          <DialogTitle>OCR-Status</DialogTitle>
          <DialogContent>
            {ocrResult && (
              <Box>
                <Typography variant="h6">Status: {ocrResult.status}</Typography>
                <Typography>Extrahierte Texte: {ocrResult.extractedTextCount}</Typography>
                {ocrResult.processingTimeMs && (
                  <Typography>Verarbeitungszeit: {ocrResult.processingTimeMs}ms</Typography>
                )}
                {ocrResult.errorMessage && (
                  <Alert severity="error" sx={{ mt: 1 }}>
                    {ocrResult.errorMessage}
                  </Alert>
                )}
              </Box>
            )}
            {error && (
              <Alert severity="error" sx={{ mt: 2 }}>
                {error}
              </Alert>
            )}
          </DialogContent>
          <DialogActions>
            <Button onClick={() => setStatusDialogOpen(false)}>
              Schließen
            </Button>
          </DialogActions>
        </Dialog>
      </Box>
    );
  }

  return (
    <Box sx={{ display: 'flex', gap: 1, alignItems: 'center' }}>
      <Button
        variant={variant}
        onClick={handleOCRClick}
        disabled={disabled || isProcessing}
        color={getButtonColor()}
        size={size}
        startIcon={getButtonContent()}
      >
        {/* Leerer Content, da Icon bereits im startIcon ist */}
      </Button>

      {!isProcessing && (
        <Tooltip title="OCR-Einstellungen">
          <IconButton
            onClick={() => setOptionsModalOpen(true)}
            disabled={disabled}
            size={size}
          >
            <SettingsIcon />
          </IconButton>
        </Tooltip>
      )}

      {ocrResult && (
        <Tooltip title="OCR-Status anzeigen">
          <IconButton onClick={handleShowStatus} color={getButtonColor()}>
            <PsychologyIcon />
          </IconButton>
        </Tooltip>
      )}

      {error && (
        <Alert severity="error" sx={{ mt: 1 }}>
          <Typography variant="caption">{error}</Typography>
        </Alert>
      )}

      {/* OCROptionsModal placeholder */}
      <Dialog
        open={optionsModalOpen}
        onClose={() => setOptionsModalOpen(false)}
        maxWidth="sm"
        fullWidth
      >
        <DialogTitle>OCR-Einstellungen</DialogTitle>
        <DialogContent>
          <Typography>OCR-Einstellungen werden hier implementiert</Typography>
        </DialogContent>
        <DialogActions>
          <Button onClick={() => setOptionsModalOpen(false)}>Abbrechen</Button>
          <Button onClick={() => handleOCRWithOptions({})} variant="contained">Starten</Button>
        </DialogActions>
      </Dialog>

      <Dialog
        open={statusDialogOpen}
        onClose={() => setStatusDialogOpen(false)}
        maxWidth="md"
        fullWidth
      >
        <DialogTitle>OCR-Verarbeitungsstatus</DialogTitle>
        <DialogContent>
          {ocrResult && (
            <Box>
              <Typography variant="h6">OCR-Status: {ocrResult.status}</Typography>
              <Typography>Extrahierte Texte: {ocrResult.extractedTextCount}</Typography>
              {ocrResult.totalPages && (
                <Typography>Seiten: {ocrResult.totalPages}</Typography>
              )}
              {ocrResult.processingTimeMs && (
                <Typography>Verarbeitungszeit: {ocrResult.processingTimeMs}ms</Typography>
              )}
              {ocrResult.errorMessage && (
                <Alert severity="error" sx={{ mt: 2 }}>
                  {ocrResult.errorMessage}
                </Alert>
              )}
            </Box>
          )}
        </DialogContent>
        <DialogActions>
          <Button onClick={() => setStatusDialogOpen(false)}>
            Schließen
          </Button>
          {ocrResult?.status === OCRStatus.SUCCESS && (
            <Button
              onClick={() => {
                setStatusDialogOpen(false);
                onOCRComplete?.();
              }}
              variant="contained"
            >
              Texte anzeigen
            </Button>
          )}
        </DialogActions>
      </Dialog>
    </Box>
  );
};

export default OCRButton;