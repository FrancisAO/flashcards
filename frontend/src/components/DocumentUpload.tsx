import React, { useState } from 'react';
import { 
  Box, 
  Button, 
  Typography, 
  List, 
  ListItem, 
  ListItemText, 
  ListItemSecondaryAction, 
  IconButton,
  Paper,
  CircularProgress,
  Alert
} from '@mui/material';
import CloudUploadIcon from '@mui/icons-material/CloudUpload';
import DeleteIcon from '@mui/icons-material/Delete';
import { DocumentUpload as DocumentUploadType } from '../types/models';

interface DocumentUploadProps {
  onUpload: (file: File) => Promise<DocumentUploadType>;
  uploadedDocuments: DocumentUploadType[];
  onRemove?: (documentId: string) => void;
  isLoading?: boolean;
  error?: string | null;
}

/**
 * Komponente für den Upload von Dokumenten.
 * Ermöglicht das Hochladen von Dokumenten und zeigt eine Liste der hochgeladenen Dokumente an.
 */
const DocumentUpload: React.FC<DocumentUploadProps> = ({
  onUpload,
  uploadedDocuments,
  onRemove,
  isLoading = false,
  error = null
}) => {
  const [dragActive, setDragActive] = useState<boolean>(false);

  const handleDrag = (e: React.DragEvent<HTMLDivElement>) => {
    e.preventDefault();
    e.stopPropagation();
    
    if (e.type === 'dragenter' || e.type === 'dragover') {
      setDragActive(true);
    } else if (e.type === 'dragleave') {
      setDragActive(false);
    }
  };

  const handleDrop = (e: React.DragEvent<HTMLDivElement>) => {
    e.preventDefault();
    e.stopPropagation();
    setDragActive(false);
    
    if (e.dataTransfer.files && e.dataTransfer.files.length > 0) {
      handleFiles(e.dataTransfer.files);
    }
  };

  const handleFileInput = (e: React.ChangeEvent<HTMLInputElement>) => {
    if (e.target.files && e.target.files.length > 0) {
      handleFiles(e.target.files);
    }
  };

  const handleFiles = (files: FileList) => {
    // Nur das erste Dokument hochladen
    const file = files[0];
    
    // Prüfen, ob es sich um ein unterstütztes Dateiformat handelt
    const supportedTypes = ['application/pdf', 'application/vnd.openxmlformats-officedocument.wordprocessingml.document', 'text/plain'];
    if (!supportedTypes.includes(file.type)) {
      alert('Nur PDF, DOCX und TXT Dateien werden unterstützt.');
      return;
    }
    
    onUpload(file);
  };

  const formatFileSize = (bytes: number): string => {
    if (bytes < 1024) return bytes + ' B';
    if (bytes < 1024 * 1024) return (bytes / 1024).toFixed(1) + ' KB';
    return (bytes / (1024 * 1024)).toFixed(1) + ' MB';
  };

  return (
    <Box sx={{ mt: 3 }}>
      <Typography variant="h6" gutterBottom>
        Dokumente hochladen
      </Typography>
      <Typography variant="body2" color="text.secondary" paragraph>
        Laden Sie Dokumente hoch, die als Grundlage für die Karteikartengenerierung dienen sollen.
        Unterstützte Formate: PDF, DOCX, TXT.
      </Typography>
      
      {error && (
        <Alert severity="error" sx={{ mb: 2 }}>
          {error}
        </Alert>
      )}
      
      <Paper
        onDragEnter={handleDrag}
        onDragLeave={handleDrag}
        onDragOver={handleDrag}
        onDrop={handleDrop}
        sx={{
          border: '2px dashed',
          borderColor: dragActive ? 'primary.main' : 'divider',
          borderRadius: 2,
          p: 3,
          textAlign: 'center',
          cursor: 'pointer',
          backgroundColor: dragActive ? 'action.hover' : 'background.paper',
          transition: 'all 0.2s ease'
        }}
      >
        <input
          type="file"
          id="file-upload"
          onChange={handleFileInput}
          accept=".pdf,.docx,.txt"
          style={{ display: 'none' }}
        />
        <label htmlFor="file-upload" style={{ cursor: 'pointer', width: '100%', height: '100%', display: 'block' }}>
          <CloudUploadIcon sx={{ fontSize: 48, color: 'primary.main', mb: 1 }} />
          <Typography variant="body1" gutterBottom>
            Dateien hier ablegen oder klicken zum Auswählen
          </Typography>
          <Typography variant="body2" color="text.secondary">
            PDF, DOCX oder TXT
          </Typography>
        </label>
      </Paper>
      
      {isLoading && (
        <Box sx={{ display: 'flex', justifyContent: 'center', mt: 2 }}>
          <CircularProgress size={24} />
          <Typography variant="body2" sx={{ ml: 1 }}>
            Dokument wird hochgeladen...
          </Typography>
        </Box>
      )}
      
      {uploadedDocuments.length > 0 && (
        <Box sx={{ mt: 3 }}>
          <Typography variant="subtitle1" gutterBottom>
            Hochgeladene Dokumente
          </Typography>
          <List>
            {uploadedDocuments.map((doc) => (
              <ListItem key={doc.id} divider>
                <ListItemText
                  primary={doc.originalFilename}
                  secondary={`${doc.contentType} • ${formatFileSize(doc.fileSize)}`}
                />
                {onRemove && (
                  <ListItemSecondaryAction>
                    <IconButton 
                      edge="end" 
                      aria-label="delete"
                      onClick={() => doc.id && onRemove(doc.id)}
                    >
                      <DeleteIcon />
                    </IconButton>
                  </ListItemSecondaryAction>
                )}
              </ListItem>
            ))}
          </List>
        </Box>
      )}
    </Box>
  );
};

export default DocumentUpload;