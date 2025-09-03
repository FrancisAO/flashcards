import React, { useState, useEffect, useCallback } from 'react';
import {
  Box,
  Paper,
  List,
  ListItem,
  ListItemIcon,
  ListItemText,
  ListItemSecondaryAction,
  IconButton,
  Typography,
  Chip,
  Menu,
  MenuItem,
  Button,
  CircularProgress,
  Alert,
  Dialog,
  DialogTitle,
  DialogContent,
  DialogActions,
  DialogContentText
} from '@mui/material';
import {
  InsertDriveFile as FileIcon,
  Image as ImageIcon,
  PictureAsPdf as PdfIcon,
  Description as DocIcon,
  MoreVert as MoreVertIcon,
  Download as DownloadIcon,
  Delete as DeleteIcon,
  Visibility as VisibilityIcon,
  Psychology as PsychologyIcon,
  Edit as EditIcon
} from '@mui/icons-material';
import { ProjectFile, FileType } from '../types/ocr';
import * as fileService from '../services/fileService';
import OCRButton from './OCRButton';

interface FileListProps {
  projectId: string;
  onFileDeleted?: () => void;
  onOCRTriggered?: () => void;
}

const FileList: React.FC<FileListProps> = ({
  projectId,
  onFileDeleted,
  onOCRTriggered
}) => {
  const [files, setFiles] = useState<ProjectFile[]>([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);
  const [anchorEl, setAnchorEl] = useState<null | HTMLElement>(null);
  const [selectedFile, setSelectedFile] = useState<ProjectFile | null>(null);
  const [deleteDialogOpen, setDeleteDialogOpen] = useState(false);
  const [fileToDelete, setFileToDelete] = useState<ProjectFile | null>(null);

  // Dateien laden
  const loadFiles = useCallback(async () => {
    try {
      setLoading(true);
      setError(null);
      
      const response = await fileService.getFilesByProject(projectId);
      setFiles(response.content);
    } catch (err) {
      setError(err instanceof Error ? err.message : 'Fehler beim Laden der Dateien');
      console.error('Error loading files:', err);
    } finally {
      setLoading(false);
    }
  }, [projectId]);

  useEffect(() => {
    loadFiles();
  }, [loadFiles]);

  const handleMenuOpen = (event: React.MouseEvent<HTMLElement>, file: ProjectFile) => {
    setAnchorEl(event.currentTarget);
    setSelectedFile(file);
  };

  const handleMenuClose = () => {
    setAnchorEl(null);
    setSelectedFile(null);
  };

  const handleDownload = async (file: ProjectFile) => {
    try {
      const blob = await fileService.downloadFile(file.id!);
      const url = URL.createObjectURL(blob);
      const a = document.createElement('a');
      a.href = url;
      a.download = file.originalFilename;
      document.body.appendChild(a);
      a.click();
      document.body.removeChild(a);
      URL.revokeObjectURL(url);
    } catch (err) {
      setError(err instanceof Error ? err.message : 'Fehler beim Download der Datei');
    }
    handleMenuClose();
  };

  const handlePreview = async (file: ProjectFile) => {
    try {
      const blob = await fileService.getFilePreview(file.id!);
      const url = URL.createObjectURL(blob);
      window.open(url, '_blank');
      URL.revokeObjectURL(url);
    } catch (err) {
      setError(err instanceof Error ? err.message : 'Fehler beim Anzeigen der Datei-Vorschau');
    }
    handleMenuClose();
  };

  const handleDeleteClick = (file: ProjectFile) => {
    setFileToDelete(file);
    setDeleteDialogOpen(true);
    handleMenuClose();
  };

  const handleDeleteConfirm = async () => {
    if (!fileToDelete) return;

    try {
      await fileService.deleteFile(fileToDelete.id!);
      setFiles(prev => prev.filter(f => f.id !== fileToDelete.id));
      onFileDeleted?.();
      setDeleteDialogOpen(false);
      setFileToDelete(null);
    } catch (err) {
      setError(err instanceof Error ? err.message : 'Fehler beim Löschen der Datei');
    }
  };

  const handleOCRComplete = () => {
    loadFiles(); // Dateien neu laden nach OCR
    onOCRTriggered?.();
  };

  const getFileIcon = (fileType: FileType) => {
    switch (fileType) {
      case FileType.IMAGE:
        return <ImageIcon color="primary" />;
      case FileType.PDF:
        return <PdfIcon color="error" />;
      case FileType.DOCUMENT:
        return <DocIcon color="info" />;
      default:
        return <FileIcon color="action" />;
    }
  };

  const formatFileSize = (bytes: number): string => {
    if (bytes === 0) return '0 B';
    const k = 1024;
    const sizes = ['B', 'KB', 'MB', 'GB'];
    const i = Math.floor(Math.log(bytes) / Math.log(k));
    return parseFloat((bytes / Math.pow(k, i)).toFixed(2)) + ' ' + sizes[i];
  };

  const formatDate = (dateString: string): string => {
    return new Date(dateString).toLocaleDateString('de-DE');
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

  if (files.length === 0) {
    return (
      <Paper sx={{ p: 4, textAlign: 'center' }}>
        <Typography variant="h6" color="text.secondary" gutterBottom>
          Keine Dateien vorhanden
        </Typography>
        <Typography variant="body2" color="text.secondary">
          Laden Sie Dateien hoch, um mit der OCR-Verarbeitung zu beginnen.
        </Typography>
      </Paper>
    );
  }

  return (
    <Box>
      <Paper>
        <List>
          {files.map((file, index) => (
            <ListItem
              key={file.id}
              divider={index < files.length - 1}
              sx={{
                '&:hover': {
                  backgroundColor: 'action.hover'
                }
              }}
            >
              <ListItemIcon>
                {getFileIcon(file.fileType)}
              </ListItemIcon>

              <ListItemText
                primary={file.originalFilename}
                secondary={
                  <Box sx={{ mt: 1 }}>
                    <Box sx={{ display: 'flex', gap: 1, mb: 1 }}>
                      <Chip 
                        label={file.fileType} 
                        size="small" 
                        variant="outlined" 
                      />
                      <Chip 
                        label={formatFileSize(file.fileSize)} 
                        size="small" 
                        variant="outlined" 
                      />
                      <Chip 
                        label={`${file.hasExtractedText ? 1 : 0} Text`} 
                        size="small" 
                        variant="outlined"
                        color={file.hasExtractedText ? 'success' : 'default'}
                      />
                    </Box>
                    <Typography variant="caption" color="text.secondary">
                      Hochgeladen: {formatDate(file.uploadedAt!)}
                    </Typography>
                  </Box>
                }
              />

              <ListItemSecondaryAction>
                <Box sx={{ display: 'flex', gap: 1, alignItems: 'center' }}>
                  <OCRButton
                    fileId={file.id!}
                    onOCRComplete={handleOCRComplete}
                    size="small"
                  />
                  
                  <IconButton
                    onClick={(e) => handleMenuOpen(e, file)}
                    size="small"
                  >
                    <MoreVertIcon />
                  </IconButton>
                </Box>
              </ListItemSecondaryAction>
            </ListItem>
          ))}
        </List>
      </Paper>

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
        <MenuItem onClick={() => selectedFile && handlePreview(selectedFile)}>
          <VisibilityIcon sx={{ mr: 1 }} fontSize="small" />
          Vorschau
        </MenuItem>
        
        <MenuItem onClick={() => selectedFile && handleDownload(selectedFile)}>
          <DownloadIcon sx={{ mr: 1 }} fontSize="small" />
          Herunterladen
        </MenuItem>
        
        <MenuItem onClick={() => selectedFile && handleDeleteClick(selectedFile)} sx={{ color: 'error.main' }}>
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
        <DialogTitle>Datei löschen</DialogTitle>
        <DialogContent>
          <DialogContentText>
            Sind Sie sicher, dass Sie die Datei <strong>"{fileToDelete?.originalFilename}"</strong> löschen möchten?
            Diese Aktion kann nicht rückgängig gemacht werden. Alle extrahierten Texte werden ebenfalls gelöscht.
          </DialogContentText>
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

export default FileList;