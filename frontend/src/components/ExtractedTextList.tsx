import React, { useState, useEffect, useCallback } from 'react';
import {
  Box,
  Paper,
  List,
  ListItem,
  ListItemText,
  ListItemSecondaryAction,
  IconButton,
  Typography,
  Chip,
  Button,
  CircularProgress,
  Alert,
  Dialog,
  DialogTitle,
  DialogContent,
  DialogActions,
  TextField,
  Menu,
  MenuItem,
  Accordion,
  AccordionSummary,
  AccordionDetails,
  Divider
} from '@mui/material';
import {
  ExpandMore as ExpandMoreIcon,
  Edit as EditIcon,
  Delete as DeleteIcon,
  MoreVert as MoreVertIcon,
  Save as SaveIcon,
  Cancel as CancelIcon,
  FileCopy as CopyIcon,
  Download as DownloadIcon
} from '@mui/icons-material';
import { ExtractedText, ExtractionSource } from '../types/ocr';
import * as ocrService from '../services/ocrService';

interface ExtractedTextListProps {
  projectId: string;
  fileId?: string;
  onTextUpdated?: () => void;
  onTextDeleted?: () => void;
}

const ExtractedTextList: React.FC<ExtractedTextListProps> = ({
  projectId,
  fileId,
  onTextUpdated,
  onTextDeleted
}) => {
  const [texts, setTexts] = useState<ExtractedText[]>([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);
  const [editingTextId, setEditingTextId] = useState<string | null>(null);
  const [editingContent, setEditingContent] = useState('');
  const [anchorEl, setAnchorEl] = useState<null | HTMLElement>(null);
  const [selectedText, setSelectedText] = useState<ExtractedText | null>(null);
  const [deleteDialogOpen, setDeleteDialogOpen] = useState(false);
  const [textToDelete, setTextToDelete] = useState<ExtractedText | null>(null);

  // Texte laden
  const loadTexts = useCallback(async () => {
    try {
      setLoading(true);
      setError(null);
      
      let response;
      if (fileId) {
        response = await ocrService.getExtractedTextsByFile(fileId);
        setTexts(response);
      } else {
        response = await ocrService.getExtractedTextsByProject(projectId);
        setTexts(response);
      }
    } catch (err) {
      setError(err instanceof Error ? err.message : 'Fehler beim Laden der Texte');
      console.error('Error loading texts:', err);
    } finally {
      setLoading(false);
    }
  }, [projectId, fileId]);

  useEffect(() => {
    loadTexts();
  }, [loadTexts]);

  const handleMenuOpen = (event: React.MouseEvent<HTMLElement>, text: ExtractedText) => {
    setAnchorEl(event.currentTarget);
    setSelectedText(text);
  };

  const handleMenuClose = () => {
    setAnchorEl(null);
    setSelectedText(null);
  };

  const handleEditStart = (text: ExtractedText) => {
    setEditingTextId(text.id!);
    setEditingContent(text.content);
    handleMenuClose();
  };

  const handleEditSave = async (textId: string) => {
    try {
      const updatedText = await ocrService.updateExtractedText(textId, {
        content: editingContent
      });
      
      setTexts(prev => prev.map(t => t.id === textId ? updatedText : t));
      setEditingTextId(null);
      setEditingContent('');
      onTextUpdated?.();
    } catch (err) {
      setError(err instanceof Error ? err.message : 'Fehler beim Speichern des Texts');
    }
  };

  const handleEditCancel = () => {
    setEditingTextId(null);
    setEditingContent('');
  };

  const handleDeleteClick = (text: ExtractedText) => {
    setTextToDelete(text);
    setDeleteDialogOpen(true);
    handleMenuClose();
  };

  const handleDeleteConfirm = async () => {
    if (!textToDelete) return;

    try {
      await ocrService.deleteExtractedText(textToDelete.id!);
      setTexts(prev => prev.filter(t => t.id !== textToDelete.id));
      onTextDeleted?.();
      setDeleteDialogOpen(false);
      setTextToDelete(null);
    } catch (err) {
      setError(err instanceof Error ? err.message : 'Fehler beim Löschen des Texts');
    }
  };

  const handleCopyText = (content: string) => {
    navigator.clipboard.writeText(content);
    handleMenuClose();
  };

  const handleExportTexts = async () => {
    try {
      const blob = fileId 
        ? await ocrService.exportFileTexts(fileId, 'txt')
        : await ocrService.exportExtractedTexts(projectId, 'txt');
      
      const url = URL.createObjectURL(blob);
      const a = document.createElement('a');
      a.href = url;
      a.download = fileId ? `file-texts.txt` : `project-texts.txt`;
      document.body.appendChild(a);
      a.click();
      document.body.removeChild(a);
      URL.revokeObjectURL(url);
    } catch (err) {
      setError(err instanceof Error ? err.message : 'Fehler beim Exportieren der Texte');
    }
  };

  const getSourceColor = (source: ExtractionSource) => {
    switch (source) {
      case ExtractionSource.OCR:
        return 'primary';
      case ExtractionSource.MANUAL:
        return 'secondary';
      case ExtractionSource.IMPORTED:
        return 'info';
      default:
        return 'default';
    }
  };

  const getSourceLabel = (source: ExtractionSource) => {
    switch (source) {
      case ExtractionSource.OCR:
        return 'OCR';
      case ExtractionSource.MANUAL:
        return 'Manuell';
      case ExtractionSource.IMPORTED:
        return 'Importiert';
      default:
        return source;
    }
  };

  const formatDate = (dateString: string): string => {
    return new Date(dateString).toLocaleString('de-DE');
  };

  const calculateStats = (content: string) => {
    const words = content.trim().split(/\s+/).filter(word => word.length > 0);
    return {
      characters: content.length,
      words: words.length,
      lines: content.split('\n').length
    };
  };

  if (loading) {
    return (
      <Box sx={{ display: 'flex', justifyContent: 'center', py: 4 }}>
        <CircularProgress />
      </Box>
    );
  }

  if (error) {
    return (
      <Alert severity="error" sx={{ mt: 2 }}>
        {error}
      </Alert>
    );
  }

  if (texts.length === 0) {
    return (
      <Paper sx={{ p: 4, textAlign: 'center' }}>
        <Typography variant="h6" color="text.secondary" gutterBottom>
          Keine extrahierten Texte vorhanden
        </Typography>
        <Typography variant="body2" color="text.secondary">
          Führen Sie OCR-Verarbeitung auf Ihren Dateien durch, um Texte zu extrahieren.
        </Typography>
      </Paper>
    );
  }

  return (
    <Box>
      {/* Export Button */}
      <Box sx={{ mb: 2, display: 'flex', justifyContent: 'flex-end' }}>
        <Button
          startIcon={<DownloadIcon />}
          onClick={handleExportTexts}
          variant="outlined"
        >
          Alle Texte exportieren
        </Button>
      </Box>

      {/* Text List */}
      {texts.map((text, index) => {
        const stats = calculateStats(text.content);
        const isEditing = editingTextId === text.id;

        return (
          <Accordion key={text.id} sx={{ mb: 1 }}>
            <AccordionSummary expandIcon={<ExpandMoreIcon />}>
              <Box sx={{ display: 'flex', alignItems: 'center', gap: 2, width: '100%' }}>
                <Typography variant="subtitle1" sx={{ flexGrow: 1 }}>
                  Text {index + 1}
                  {text.pageNumber && ` (Seite ${text.pageNumber})`}
                </Typography>
                
                <Box sx={{ display: 'flex', gap: 1 }}>
                  <Chip
                    label={getSourceLabel(text.source)}
                    color={getSourceColor(text.source)}
                    size="small"
                  />
                  <Chip
                    label={`${stats.words} Wörter`}
                    size="small"
                    variant="outlined"
                  />
                  {text.confidence && (
                    <Chip
                      label={`${Math.round(text.confidence * 100)}% Genauigkeit`}
                      size="small"
                      variant="outlined"
                      color={text.confidence > 0.8 ? 'success' : text.confidence > 0.6 ? 'warning' : 'error'}
                    />
                  )}
                </Box>

                <IconButton
                  onClick={(e) => {
                    e.stopPropagation();
                    handleMenuOpen(e, text);
                  }}
                  size="small"
                >
                  <MoreVertIcon />
                </IconButton>
              </Box>
            </AccordionSummary>

            <AccordionDetails>
              {isEditing ? (
                <Box>
                  <TextField
                    fullWidth
                    multiline
                    rows={6}
                    value={editingContent}
                    onChange={(e) => setEditingContent(e.target.value)}
                    variant="outlined"
                    sx={{ mb: 2 }}
                  />
                  <Box sx={{ display: 'flex', gap: 1, justifyContent: 'flex-end' }}>
                    <Button
                      startIcon={<CancelIcon />}
                      onClick={handleEditCancel}
                    >
                      Abbrechen
                    </Button>
                    <Button
                      startIcon={<SaveIcon />}
                      onClick={() => handleEditSave(text.id!)}
                      variant="contained"
                    >
                      Speichern
                    </Button>
                  </Box>
                </Box>
              ) : (
                <Box>
                  <Typography 
                    variant="body1" 
                    sx={{ 
                      whiteSpace: 'pre-wrap',
                      fontFamily: 'monospace',
                      backgroundColor: 'grey.50',
                      p: 2,
                      borderRadius: 1,
                      mb: 2
                    }}
                  >
                    {text.content}
                  </Typography>

                  <Divider sx={{ my: 2 }} />

                  <Box sx={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center' }}>
                    <Box>
                      <Typography variant="caption" color="text.secondary">
                        Erstellt: {formatDate(text.createdAt!)}
                      </Typography>
                      {text.updatedAt !== text.createdAt && (
                        <Typography variant="caption" color="text.secondary" sx={{ ml: 2 }}>
                          Bearbeitet: {formatDate(text.updatedAt!)}
                        </Typography>
                      )}
                    </Box>
                    
                    <Box sx={{ display: 'flex', gap: 1 }}>
                      <Typography variant="caption" color="text.secondary">
                        {stats.characters} Zeichen • {stats.words} Wörter • {stats.lines} Zeilen
                      </Typography>
                    </Box>
                  </Box>
                </Box>
              )}
            </AccordionDetails>
          </Accordion>
        );
      })}

      {/* Kontext-Menü */}
      <Menu
        anchorEl={anchorEl}
        open={Boolean(anchorEl)}
        onClose={handleMenuClose}
        PaperProps={{
          elevation: 3,
          sx: { minWidth: 180 }
        }}
      >
        <MenuItem onClick={() => selectedText && handleEditStart(selectedText)}>
          <EditIcon sx={{ mr: 1 }} fontSize="small" />
          Bearbeiten
        </MenuItem>
        
        <MenuItem onClick={() => selectedText && handleCopyText(selectedText.content)}>
          <CopyIcon sx={{ mr: 1 }} fontSize="small" />
          Kopieren
        </MenuItem>
        
        <MenuItem onClick={() => selectedText && handleDeleteClick(selectedText)} sx={{ color: 'error.main' }}>
          <DeleteIcon sx={{ mr: 1 }} fontSize="small" />
          Löschen
        </MenuItem>
      </Menu>

      {/* Löschen-Bestätigung */}
      <Dialog
        open={deleteDialogOpen}
        onClose={() => setDeleteDialogOpen(false)}
        maxWidth="sm"
        fullWidth
      >
        <DialogTitle>Text löschen</DialogTitle>
        <DialogContent>
          <Typography>
            Sind Sie sicher, dass Sie diesen extrahierten Text löschen möchten?
            Diese Aktion kann nicht rückgängig gemacht werden.
          </Typography>
          {textToDelete && (
            <Box sx={{ mt: 2, p: 2, backgroundColor: 'grey.50', borderRadius: 1 }}>
              <Typography variant="body2" sx={{ fontFamily: 'monospace' }}>
                {textToDelete.content.substring(0, 200)}
                {textToDelete.content.length > 200 && '...'}
              </Typography>
            </Box>
          )}
        </DialogContent>
        <DialogActions>
          <Button onClick={() => setDeleteDialogOpen(false)}>
            Abbrechen
          </Button>
          <Button onClick={handleDeleteConfirm} color="error" variant="contained">
            Löschen
          </Button>
        </DialogActions>
      </Dialog>
    </Box>
  );
};

export default ExtractedTextList;