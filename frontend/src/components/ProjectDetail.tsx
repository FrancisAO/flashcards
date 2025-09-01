import React, { useState, useEffect, useCallback } from 'react';
import {
  Box,
  Container,
  Typography,
  Paper,
  Grid,
  Chip,
  Button,
  IconButton,
  Breadcrumbs,
  Link,
  Alert,
  CircularProgress,
  Tabs,
  Tab,
  Divider
} from '@mui/material';
import {
  ArrowBack as ArrowBackIcon,
  Edit as EditIcon,
  Download as DownloadIcon,
  Delete as DeleteIcon,
  Archive as ArchiveIcon,
  CloudUpload as CloudUploadIcon,
  Psychology as PsychologyIcon
} from '@mui/icons-material';
import { useParams, useNavigate, Link as RouterLink } from 'react-router-dom';
import { ProjectDTO } from '../types/ocr';
import * as projectService from '../services/projectService';
import FileUpload from './FileUpload';
import FileList from './FileList';
import ExtractedTextList from './ExtractedTextList';

interface TabPanelProps {
  children?: React.ReactNode;
  index: number;
  value: number;
}

function TabPanel(props: TabPanelProps) {
  const { children, value, index, ...other } = props;

  return (
    <div
      role="tabpanel"
      hidden={value !== index}
      id={`project-tabpanel-${index}`}
      aria-labelledby={`project-tab-${index}`}
      {...other}
    >
      {value === index && (
        <Box sx={{ py: 3 }}>
          {children}
        </Box>
      )}
    </div>
  );
}

const ProjectDetail: React.FC = () => {
  const { id } = useParams<{ id: string }>();
  const navigate = useNavigate();
  const [project, setProject] = useState<ProjectDTO | null>(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);
  const [activeTab, setActiveTab] = useState(0);
  const [stats, setStats] = useState<{
    fileCount: number;
    totalTextCount: number;
    totalFileSize: number;
  } | null>(null);

  // Projekt laden
  const loadProject = useCallback(async () => {
    if (!id) return;

    try {
      setLoading(true);
      setError(null);
      
      const [projectData, projectStats] = await Promise.all([
        projectService.getProjectById(id),
        projectService.getProjectStats(id)
      ]);
      
      setProject(projectData);
      setStats(projectStats);
    } catch (err) {
      setError(err instanceof Error ? err.message : 'Fehler beim Laden des Projekts');
      console.error('Error loading project:', err);
    } finally {
      setLoading(false);
    }
  }, [id]);

  useEffect(() => {
    loadProject();
  }, [loadProject]);

  const handleTabChange = (event: React.SyntheticEvent, newValue: number) => {
    setActiveTab(newValue);
  };

  const handleBack = () => {
    navigate('/projects');
  };

  const handleEdit = () => {
    navigate(`/projects/${id}/edit`);
  };

  const handleDelete = async () => {
    if (!id || !project) return;
    
    const confirmed = window.confirm(
      `Sind Sie sicher, dass Sie das Projekt "${project.name}" löschen möchten? Diese Aktion kann nicht rückgängig gemacht werden.`
    );
    
    if (confirmed) {
      try {
        await projectService.deleteProject(id);
        navigate('/projects');
      } catch (err) {
        setError(err instanceof Error ? err.message : 'Fehler beim Löschen des Projekts');
      }
    }
  };

  const handleExport = async () => {
    if (!id || !project) return;
    
    try {
      const blob = await projectService.exportProject(id, 'json');
      const url = URL.createObjectURL(blob);
      const a = document.createElement('a');
      a.href = url;
      a.download = `${project.name}.json`;
      document.body.appendChild(a);
      a.click();
      document.body.removeChild(a);
      URL.revokeObjectURL(url);
    } catch (err) {
      setError(err instanceof Error ? err.message : 'Fehler beim Exportieren des Projekts');
    }
  };

  const handleArchive = async () => {
    if (!id || !project) return;
    
    try {
      const updatedProject = await projectService.archiveProject(id);
      setProject(updatedProject);
    } catch (err) {
      setError(err instanceof Error ? err.message : 'Fehler beim Archivieren des Projekts');
    }
  };

  const handleFilesUploaded = () => {
    // Projekt-Daten neu laden nach Upload
    loadProject();
  };

  const formatFileSize = (bytes: number): string => {
    if (bytes === 0) return '0 B';
    const k = 1024;
    const sizes = ['B', 'KB', 'MB', 'GB'];
    const i = Math.floor(Math.log(bytes) / Math.log(k));
    return parseFloat((bytes / Math.pow(k, i)).toFixed(2)) + ' ' + sizes[i];
  };

  const formatDate = (dateString: string): string => {
    return new Date(dateString).toLocaleString('de-DE');
  };

  if (loading) {
    return (
      <Container maxWidth="xl">
        <Box sx={{ display: 'flex', justifyContent: 'center', py: 8 }}>
          <CircularProgress />
        </Box>
      </Container>
    );
  }

  if (error || !project) {
    return (
      <Container maxWidth="xl">
        <Box sx={{ py: 4 }}>
          <Alert severity="error" sx={{ mb: 2 }}>
            {error || 'Projekt nicht gefunden'}
          </Alert>
          <Button onClick={handleBack} startIcon={<ArrowBackIcon />}>
            Zurück zu Projekten
          </Button>
        </Box>
      </Container>
    );
  }

  return (
    <Container maxWidth="xl">
      <Box sx={{ py: 3 }}>
        {/* Breadcrumbs */}
        <Breadcrumbs sx={{ mb: 3 }}>
          <Link component={RouterLink} to="/projects" underline="hover">
            Projekte
          </Link>
          <Typography color="text.primary">{project.name}</Typography>
        </Breadcrumbs>

        {/* Header */}
        <Paper sx={{ p: 3, mb: 3 }}>
          <Box sx={{ display: 'flex', alignItems: 'flex-start', justifyContent: 'space-between', mb: 2 }}>
            <Box sx={{ flexGrow: 1 }}>
              <Box sx={{ display: 'flex', alignItems: 'center', gap: 2, mb: 2 }}>
                <IconButton onClick={handleBack} size="small">
                  <ArrowBackIcon />
                </IconButton>
                <Typography variant="h4" component="h1">
                  {project.name}
                </Typography>
              </Box>
              
              {project.description && (
                <Typography variant="body1" color="text.secondary" sx={{ mb: 2 }}>
                  {project.description}
                </Typography>
              )}
              
              {project.tags && project.tags.length > 0 && (
                <Box sx={{ display: 'flex', flexWrap: 'wrap', gap: 1, mb: 2 }}>
                  {project.tags.map((tag) => (
                    <Chip key={tag} label={tag} variant="outlined" size="small" />
                  ))}
                </Box>
              )}
            </Box>

            {/* Action Buttons */}
            <Box sx={{ display: 'flex', gap: 1, flexShrink: 0 }}>
              <Button
                startIcon={<EditIcon />}
                onClick={handleEdit}
                variant="outlined"
              >
                Bearbeiten
              </Button>
              
              <Button
                startIcon={<DownloadIcon />}
                onClick={handleExport}
                variant="outlined"
              >
                Exportieren
              </Button>
              
              <Button
                startIcon={<ArchiveIcon />}
                onClick={handleArchive}
                variant="outlined"
              >
                Archivieren
              </Button>
              
              <Button
                startIcon={<DeleteIcon />}
                onClick={handleDelete}
                variant="outlined"
                color="error"
              >
                Löschen
              </Button>
            </Box>
          </Box>

          {/* Statistiken */}
          {stats && (
            <>
              <Divider sx={{ my: 2 }} />
              <Grid container spacing={3}>
                <Grid item xs={12} sm={3}>
                  <Typography variant="h6" color="primary">
                    {stats.fileCount}
                  </Typography>
                  <Typography variant="body2" color="text.secondary">
                    Dateien
                  </Typography>
                </Grid>
                <Grid item xs={12} sm={3}>
                  <Typography variant="h6" color="primary">
                    {stats.totalTextCount}
                  </Typography>
                  <Typography variant="body2" color="text.secondary">
                    Extrahierte Texte
                  </Typography>
                </Grid>
                <Grid item xs={12} sm={3}>
                  <Typography variant="h6" color="primary">
                    {formatFileSize(stats.totalFileSize)}
                  </Typography>
                  <Typography variant="body2" color="text.secondary">
                    Gesamtgröße
                  </Typography>
                </Grid>
              </Grid>
            </>
          )}
        </Paper>

        {/* Tabs */}
        <Paper sx={{ mb: 3 }}>
          <Tabs 
            value={activeTab} 
            onChange={handleTabChange}
            variant="fullWidth"
            sx={{ borderBottom: 1, borderColor: 'divider' }}
          >
            <Tab 
              icon={<CloudUploadIcon />} 
              label="Dateien verwalten" 
              iconPosition="start"
            />
            <Tab 
              icon={<PsychologyIcon />} 
              label="Extrahierte Texte" 
              iconPosition="start"
            />
          </Tabs>

          <TabPanel value={activeTab} index={0}>
            {/* Datei-Upload und -Verwaltung */}
            <Box sx={{ px: 3 }}>
              <Typography variant="h6" gutterBottom>
                Dateien hochladen
              </Typography>
              <FileUpload
                projectId={project.id!}
                onFilesUploaded={handleFilesUploaded}
              />
              
              <Typography variant="h6" gutterBottom sx={{ mt: 4 }}>
                Hochgeladene Dateien
              </Typography>
              <FileList
                projectId={project.id!}
                onFileDeleted={handleFilesUploaded}
                onOCRTriggered={handleFilesUploaded}
              />
            </Box>
          </TabPanel>

          <TabPanel value={activeTab} index={1}>
            {/* Extrahierte Texte */}
            <Box sx={{ px: 3 }}>
              <Typography variant="h6" gutterBottom>
                Extrahierte Texte aus OCR
              </Typography>
              <ExtractedTextList
                projectId={project.id!}
                onTextUpdated={loadProject}
                onTextDeleted={loadProject}
              />
            </Box>
          </TabPanel>
        </Paper>

        {/* Fehlermeldung */}
        {error && (
          <Alert severity="error" sx={{ mt: 2 }}>
            {error}
          </Alert>
        )}
      </Box>
    </Container>
  );
};

export default ProjectDetail;