import React, { useState } from 'react';
import {
  Card,
  CardContent,
  CardActions,
  Typography,
  Chip,
  Box,
  IconButton,
  Menu,
  MenuItem,
  Tooltip,
  Button,
  Dialog,
  DialogTitle,
  DialogContent,
  DialogActions,
  DialogContentText,
  LinearProgress
} from '@mui/material';
import {
  MoreVert as MoreVertIcon,
  Edit as EditIcon,
  Delete as DeleteIcon,
  Folder as FolderIcon,
  Description as DescriptionIcon,
  Schedule as ScheduleIcon,
  Archive as ArchiveIcon,
  Unarchive as UnarchiveIcon,
  FileCopy as FileCopyIcon,
  Download as DownloadIcon
} from '@mui/icons-material';
import { useNavigate } from 'react-router-dom';
import { Project } from '../types/ocr';
import * as projectService from '../services/projectService';

interface ProjectCardProps {
  project: Project;
  onDeleted: () => void;
  onUpdated: (project: Project) => void;
}

const ProjectCard: React.FC<ProjectCardProps> = ({ project, onDeleted, onUpdated }) => {
  const navigate = useNavigate();
  const [anchorEl, setAnchorEl] = useState<null | HTMLElement>(null);
  const [deleteDialogOpen, setDeleteDialogOpen] = useState(false);
  const [duplicateDialogOpen, setDuplicateDialogOpen] = useState(false);
  const [loading, setLoading] = useState(false);
  const [newProjectName, setNewProjectName] = useState(`${project.name} (Kopie)`);

  const handleMenuOpen = (event: React.MouseEvent<HTMLElement>) => {
    setAnchorEl(event.currentTarget);
  };

  const handleMenuClose = () => {
    setAnchorEl(null);
  };

  const handleCardClick = () => {
    navigate(`/projects/${project.id}`);
  };

  const handleEdit = () => {
    navigate(`/projects/${project.id}/edit`);
    handleMenuClose();
  };

  const handleDelete = async () => {
    try {
      setLoading(true);
      await projectService.deleteProject(project.id!);
      onDeleted();
      setDeleteDialogOpen(false);
    } catch (error) {
      console.error('Error deleting project:', error);
    } finally {
      setLoading(false);
    }
  };

  const handleDuplicate = async () => {
    try {
      setLoading(true);
      const duplicatedProject = await projectService.duplicateProject(project.id!, newProjectName);
      onUpdated(duplicatedProject);
      setDuplicateDialogOpen(false);
      handleMenuClose();
    } catch (error) {
      console.error('Error duplicating project:', error);
    } finally {
      setLoading(false);
    }
  };

  const handleArchive = async () => {
    try {
      setLoading(true);
      const updatedProject = await projectService.archiveProject(project.id!);
      onUpdated(updatedProject);
      handleMenuClose();
    } catch (error) {
      console.error('Error archiving project:', error);
    } finally {
      setLoading(false);
    }
  };

  const handleUnarchive = async () => {
    try {
      setLoading(true);
      const updatedProject = await projectService.unarchiveProject(project.id!);
      onUpdated(updatedProject);
      handleMenuClose();
    } catch (error) {
      console.error('Error unarchiving project:', error);
    } finally {
      setLoading(false);
    }
  };

  const handleExport = async () => {
    try {
      setLoading(true);
      const blob = await projectService.exportProject(project.id!, 'json');
      const url = URL.createObjectURL(blob);
      const a = document.createElement('a');
      a.href = url;
      a.download = `${project.name}.json`;
      document.body.appendChild(a);
      a.click();
      document.body.removeChild(a);
      URL.revokeObjectURL(url);
      handleMenuClose();
    } catch (error) {
      console.error('Error exporting project:', error);
    } finally {
      setLoading(false);
    }
  };

  const formatDate = (dateString?: string) => {
    if (!dateString) return 'N/A';
    return new Date(dateString).toLocaleDateString('de-DE');
  };

  const formatTime = (dateString?: string) => {
    if (!dateString) return 'N/A';
    return new Date(dateString).toLocaleString('de-DE');
  };

  return (
    <>
      <Card 
        sx={{ 
          height: '100%', 
          display: 'flex', 
          flexDirection: 'column',
          cursor: 'pointer',
          transition: 'all 0.2s ease-in-out',
          '&:hover': {
            transform: 'translateY(-2px)',
            boxShadow: 4
          }
        }}
      >
        {loading && <LinearProgress />}
        
        <CardContent 
          sx={{ flexGrow: 1 }}
          onClick={handleCardClick}
        >
          {/* Projekt-Name */}
          <Typography variant="h6" component="h2" gutterBottom noWrap>
            {project.name}
          </Typography>

          {/* Beschreibung */}
          {project.description && (
            <Typography 
              variant="body2" 
              color="text.secondary" 
              sx={{ 
                mb: 2,
                overflow: 'hidden',
                textOverflow: 'ellipsis',
                display: '-webkit-box',
                WebkitLineClamp: 2,
                WebkitBoxOrient: 'vertical'
              }}
            >
              {project.description}
            </Typography>
          )}

          {/* Statistiken */}
          <Box sx={{ display: 'flex', gap: 2, mb: 2 }}>
            <Box sx={{ display: 'flex', alignItems: 'center', gap: 0.5 }}>
              <FolderIcon fontSize="small" color="action" />
              <Typography variant="caption" color="text.secondary">
                {project.fileCount || 0} Dateien
              </Typography>
            </Box>
            <Box sx={{ display: 'flex', alignItems: 'center', gap: 0.5 }}>
              <DescriptionIcon fontSize="small" color="action" />
              <Typography variant="caption" color="text.secondary">
                {project.totalTextCount || 0} Texte
              </Typography>
            </Box>
          </Box>

          {/* Tags */}
          {project.tags && project.tags.length > 0 && (
            <Box sx={{ mb: 2 }}>
              <Box sx={{ display: 'flex', flexWrap: 'wrap', gap: 0.5 }}>
                {project.tags.slice(0, 3).map((tag) => (
                  <Chip
                    key={tag}
                    label={tag}
                    size="small"
                    variant="outlined"
                    sx={{ fontSize: '0.7rem' }}
                  />
                ))}
                {project.tags.length > 3 && (
                  <Chip
                    label={`+${project.tags.length - 3}`}
                    size="small"
                    variant="outlined"
                    sx={{ fontSize: '0.7rem' }}
                  />
                )}
              </Box>
            </Box>
          )}

          {/* Datum */}
          <Box sx={{ display: 'flex', alignItems: 'center', gap: 0.5 }}>
            <ScheduleIcon fontSize="small" color="action" />
            <Typography variant="caption" color="text.secondary">
              Aktualisiert: {formatDate(project.updatedAt)}
            </Typography>
          </Box>
        </CardContent>

        <CardActions sx={{ justifyContent: 'space-between', px: 2, pb: 2 }}>
          <Button 
            size="small" 
            onClick={handleCardClick}
            variant="contained"
          >
            Öffnen
          </Button>
          
          <Tooltip title="Mehr Optionen">
            <IconButton
              onClick={(e) => {
                e.stopPropagation();
                handleMenuOpen(e);
              }}
              disabled={loading}
            >
              <MoreVertIcon />
            </IconButton>
          </Tooltip>
        </CardActions>
      </Card>

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
        <MenuItem onClick={handleEdit}>
          <EditIcon sx={{ mr: 1 }} fontSize="small" />
          Bearbeiten
        </MenuItem>
        
        <MenuItem onClick={() => setDuplicateDialogOpen(true)}>
          <FileCopyIcon sx={{ mr: 1 }} fontSize="small" />
          Duplizieren
        </MenuItem>
        
        <MenuItem onClick={handleExport}>
          <DownloadIcon sx={{ mr: 1 }} fontSize="small" />
          Exportieren
        </MenuItem>
        
        <MenuItem onClick={handleArchive}>
          <ArchiveIcon sx={{ mr: 1 }} fontSize="small" />
          Archivieren
        </MenuItem>
        
        <MenuItem onClick={() => setDeleteDialogOpen(true)} sx={{ color: 'error.main' }}>
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
        <DialogTitle>Projekt löschen</DialogTitle>
        <DialogContent>
          <DialogContentText>
            Sind Sie sicher, dass Sie das Projekt <strong>"{project.name}"</strong> löschen möchten?
            Diese Aktion kann nicht rückgängig gemacht werden. Alle Dateien und extrahierten Texte 
            werden ebenfalls gelöscht.
          </DialogContentText>
        </DialogContent>
        <DialogActions>
          <Button 
            onClick={() => setDeleteDialogOpen(false)}
            disabled={loading}
          >
            Abbrechen
          </Button>
          <Button 
            onClick={handleDelete}
            color="error"
            variant="contained"
            disabled={loading}
          >
            Löschen
          </Button>
        </DialogActions>
      </Dialog>

      {/* Duplizieren-Dialog */}
      <Dialog
        open={duplicateDialogOpen}
        onClose={() => setDuplicateDialogOpen(false)}
        maxWidth="sm"
        fullWidth
      >
        <DialogTitle>Projekt duplizieren</DialogTitle>
        <DialogContent>
          <DialogContentText sx={{ mb: 2 }}>
            Geben Sie einen Namen für das duplizierte Projekt ein:
          </DialogContentText>
          <input
            type="text"
            value={newProjectName}
            onChange={(e) => setNewProjectName(e.target.value)}
            style={{ width: '100%', padding: '8px', border: '1px solid #ccc', borderRadius: '4px' }}
          />
        </DialogContent>
        <DialogActions>
          <Button 
            onClick={() => setDuplicateDialogOpen(false)}
            disabled={loading}
          >
            Abbrechen
          </Button>
          <Button 
            onClick={handleDuplicate}
            variant="contained"
            disabled={loading || !newProjectName.trim()}
          >
            Duplizieren
          </Button>
        </DialogActions>
      </Dialog>
    </>
  );
};

export default ProjectCard;