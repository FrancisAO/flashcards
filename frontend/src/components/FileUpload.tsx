import React, { useState, useCallback } from 'react';
import {
  Box,
  Paper,
  Typography,
  Button,
  LinearProgress,
  Alert,
  List,
  ListItem,
  ListItemText,
  ListItemIcon,
  IconButton,
  Chip,
  Grid
} from '@mui/material';
import {
  CloudUpload as CloudUploadIcon,
  InsertDriveFile as FileIcon,
  CheckCircle as CheckCircleIcon,
  Error as ErrorIcon,
  Clear as ClearIcon,
  Delete as DeleteIcon
} from '@mui/icons-material';
import { FileUploadProgress, FileUploadResponse } from '../types/ocr';
import * as fileService from '../services/fileService';

interface FileUploadProps {
  projectId: string;
  onFilesUploaded: (uploadedFiles: FileUploadResponse[]) => void;
  maxFiles?: number;
  acceptedFormats?: string[];
}

const FileUpload: React.FC<FileUploadProps> = ({
  projectId,
  onFilesUploaded,
  maxFiles = 10,
  acceptedFormats = [
    'image/jpeg', 'image/png', 'image/gif', 'image/bmp', 'image/tiff',
    'application/pdf',
    'application/msword', 'application/vnd.openxmlformats-officedocument.wordprocessingml.document',
    'text/plain'
  ]
}) => {
  const [uploadQueue, setUploadQueue] = useState<FileUploadProgress[]>([]);
  const [isUploading, setIsUploading] = useState(false);
  const [error, setError] = useState<string | null>(null);
  const [uploadedFiles, setUploadedFiles] = useState<FileUploadResponse[]>([]);
  const [isDragActive, setIsDragActive] = useState(false);

  const onDrop = useCallback((acceptedFiles: File[], rejectedFiles: any[]) => {
    setError(null);

    // Validiere abgelehnte Dateien
    if (rejectedFiles.length > 0) {
      const reasons = rejectedFiles.map(({ file, errors }) => 
        `${file.name}: ${errors.map((e: any) => e.message).join(', ')}`
      ).join('\n');
      setError(`Einige Dateien wurden abgelehnt:\n${reasons}`);
    }

    // Validiere akzeptierte Dateien
    const { validFiles, invalidFiles } = fileService.validateMultipleFiles(acceptedFiles);
    
    if (invalidFiles.length > 0) {
      const invalidReasons = invalidFiles.map(({ file, errors }) => 
        `${file.name}: ${errors.join(', ')}`
      ).join('\n');
      setError(prev => prev ? `${prev}\n\n${invalidReasons}` : invalidReasons);
    }

    if (validFiles.length === 0) {
      return;
    }

    // Erstelle Upload-Queue
    const newUploads: FileUploadProgress[] = validFiles.map((file, index) => ({
      fileId: `upload-${Date.now()}-${index}`,
      filename: file.name,
      progress: 0,
      status: 'pending'
    }));

    setUploadQueue(newUploads);
    startUpload(validFiles, newUploads);
  }, [projectId]);

  const startUpload = async (files: File[], uploads: FileUploadProgress[]) => {
    setIsUploading(true);
    setError(null);
    const results: FileUploadResponse[] = [];

    try {
      for (let i = 0; i < files.length; i++) {
        const file = files[i];
        const upload = uploads[i];

        // Update Status zu 'uploading'
        setUploadQueue(prev => prev.map(u => 
          u.fileId === upload.fileId 
            ? { ...u, status: 'uploading' as const }
            : u
        ));

        try {
          const result = await fileService.uploadFile(
            projectId,
            file,
            (progress) => {
              setUploadQueue(prev => prev.map(u => 
                u.fileId === upload.fileId 
                  ? { ...u, progress }
                  : u
              ));
            }
          );

          // Update Status zu 'completed'
          setUploadQueue(prev => prev.map(u => 
            u.fileId === upload.fileId 
              ? { ...u, status: 'completed' as const, progress: 100 }
              : u
          ));

          results.push(result);
        } catch (error) {
          // Update Status zu 'error'
          setUploadQueue(prev => prev.map(u => 
            u.fileId === upload.fileId 
              ? { 
                  ...u, 
                  status: 'error' as const, 
                  error: error instanceof Error ? error.message : 'Upload fehler'
                }
              : u
          ));
        }
      }

      setUploadedFiles(prev => [...prev, ...results]);
      onFilesUploaded(results);
    } catch (error) {
      setError(error instanceof Error ? error.message : 'Unbekannter Upload-Fehler');
    } finally {
      setIsUploading(false);
    }
  };

  const removeFromQueue = (fileId: string) => {
    setUploadQueue(prev => prev.filter(u => u.fileId !== fileId));
  };

  const clearQueue = () => {
    if (!isUploading) {
      setUploadQueue([]);
      setError(null);
    }
  };

  // Drag & Drop Handlers
  const handleDragEnter = (e: React.DragEvent) => {
    e.preventDefault();
    setIsDragActive(true);
  };

  const handleDragLeave = (e: React.DragEvent) => {
    e.preventDefault();
    setIsDragActive(false);
  };

  const handleDragOver = (e: React.DragEvent) => {
    e.preventDefault();
  };

  const handleDrop = (e: React.DragEvent) => {
    e.preventDefault();
    setIsDragActive(false);
    
    if (isUploading) return;
    
    const files = Array.from(e.dataTransfer.files);
    onDrop(files, []);
  };

  const handleFileSelect = (e: React.ChangeEvent<HTMLInputElement>) => {
    if (e.target.files) {
      const files = Array.from(e.target.files);
      onDrop(files, []);
    }
  };

  const getFileIcon = (filename: string) => {
    const extension = filename.split('.').pop()?.toLowerCase();
    const imageExtensions = ['jpg', 'jpeg', 'png', 'gif', 'bmp', 'tiff'];
    
    if (imageExtensions.includes(extension || '')) {
      return <FileIcon color="primary" />;
    } else if (extension === 'pdf') {
      return <FileIcon color="error" />;
    } else {
      return <FileIcon color="action" />;
    }
  };

  const getStatusIcon = (status: FileUploadProgress['status']) => {
    switch (status) {
      case 'completed':
        return <CheckCircleIcon color="success" />;
      case 'error':
        return <ErrorIcon color="error" />;
      default:
        return null;
    }
  };

  return (
    <Box>
      {/* Drag & Drop Area */}
      <Paper
        onDragEnter={handleDragEnter}
        onDragLeave={handleDragLeave}
        onDragOver={handleDragOver}
        onDrop={handleDrop}
        onClick={() => !isUploading && document.getElementById('file-input')?.click()}
        sx={{
          p: 4,
          border: '2px dashed',
          borderColor: isDragActive ? 'primary.main' : 'grey.300',
          backgroundColor: isDragActive ? 'action.hover' : 'background.paper',
          cursor: isUploading ? 'not-allowed' : 'pointer',
          transition: 'all 0.2s ease-in-out',
          '&:hover': {
            borderColor: isUploading ? 'grey.300' : 'primary.main',
            backgroundColor: isUploading ? 'background.paper' : 'action.hover'
          }
        }}
      >
        <input
          id="file-input"
          type="file"
          multiple
          onChange={handleFileSelect}
          accept={acceptedFormats.join(',')}
          style={{ display: 'none' }}
          disabled={isUploading}
        />
        
        <Box sx={{ textAlign: 'center' }}>
          <CloudUploadIcon 
            sx={{ 
              fontSize: 48, 
              color: isDragActive ? 'primary.main' : 'text.secondary',
              mb: 2
            }} 
          />
          
          <Typography variant="h6" gutterBottom>
            {isDragActive ? 'Dateien hier ablegen...' : 'Dateien hochladen'}
          </Typography>
          
          <Typography variant="body2" color="text.secondary" sx={{ mb: 2 }}>
            {isUploading 
              ? 'Upload läuft...'
              : `Ziehen Sie Dateien hierher oder klicken Sie zum Auswählen`
            }
          </Typography>
          
          <Typography variant="caption" color="text.secondary">
            Unterstützte Formate: JPG, PNG, PDF, DOC, DOCX, TXT • Max. {maxFiles} Dateien • Max. 50MB pro Datei
          </Typography>
        </Box>
      </Paper>

      {/* Upload Queue */}
      {uploadQueue.length > 0 && (
        <Paper sx={{ mt: 3, p: 2 }}>
          <Box sx={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', mb: 2 }}>
            <Typography variant="h6">
              Upload-Warteschlange ({uploadQueue.length})
            </Typography>
            
            {!isUploading && (
              <Button
                startIcon={<ClearIcon />}
                onClick={clearQueue}
                size="small"
              >
                Warteschlange leeren
              </Button>
            )}
          </Box>

          <List dense>
            {uploadQueue.map((upload) => (
              <ListItem
                key={upload.fileId}
                secondaryAction={
                  !isUploading && upload.status === 'error' ? (
                    <IconButton
                      edge="end"
                      onClick={() => removeFromQueue(upload.fileId)}
                      size="small"
                    >
                      <DeleteIcon />
                    </IconButton>
                  ) : null
                }
              >
                <ListItemIcon>
                  {getFileIcon(upload.filename)}
                </ListItemIcon>
                
                <ListItemText
                  primary={upload.filename}
                  secondary={
                    <Box>
                      {upload.status === 'pending' && 'Warten...'}
                      {upload.status === 'uploading' && (
                        <Box sx={{ mt: 1 }}>
                          <LinearProgress 
                            variant="determinate" 
                            value={upload.progress} 
                            sx={{ mb: 0.5 }}
                          />
                          <Typography variant="caption">
                            {upload.progress}%
                          </Typography>
                        </Box>
                      )}
                      {upload.status === 'completed' && (
                        <Chip 
                          label="Erfolgreich hochgeladen" 
                          color="success" 
                          size="small" 
                        />
                      )}
                      {upload.status === 'error' && (
                        <Chip 
                          label={upload.error || 'Upload fehlgeschlagen'} 
                          color="error" 
                          size="small" 
                        />
                      )}
                    </Box>
                  }
                />
                
                <Box sx={{ ml: 2 }}>
                  {getStatusIcon(upload.status)}
                </Box>
              </ListItem>
            ))}
          </List>
        </Paper>
      )}

      {/* Error Display */}
      {error && (
        <Alert severity="error" sx={{ mt: 2 }}>
          <Typography variant="body2" style={{ whiteSpace: 'pre-line' }}>
            {error}
          </Typography>
        </Alert>
      )}

      {/* Upload Summary */}
      {uploadedFiles.length > 0 && (
        <Paper sx={{ mt: 3, p: 2 }}>
          <Typography variant="h6" gutterBottom>
            Erfolgreich hochgeladene Dateien ({uploadedFiles.length})
          </Typography>
          
          <Grid container spacing={1}>
            {uploadedFiles.map((file, index) => (
              <Grid item key={index}>
                <Chip
                  label={file.originalFilename}
                  variant="outlined"
                  color="success"
                  size="small"
                />
              </Grid>
            ))}
          </Grid>
        </Paper>
      )}
    </Box>
  );
};

export default FileUpload;