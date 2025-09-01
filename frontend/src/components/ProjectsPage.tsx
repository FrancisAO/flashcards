import React, { useState, useEffect, useCallback } from 'react';
import {
  Box,
  Container,
  Typography,
  Button,
  TextField,
  Grid,
  Fab,
  Chip,
  Alert,
  CircularProgress,
  Paper,
  InputAdornment,
  FormControl,
  InputLabel,
  Select,
  MenuItem,
  SelectChangeEvent
} from '@mui/material';
import {
  Add as AddIcon,
  Search as SearchIcon,
  Clear as ClearIcon,
  Sort as SortIcon
} from '@mui/icons-material';
import { Project, ProjectSearchParams } from '../types/ocr';
import * as projectService from '../services/projectService';
import ProjectList from './ProjectList';
import CreateProjectModal from './CreateProjectModal';

const ProjectsPage: React.FC = () => {
  const [projects, setProjects] = useState<Project[]>([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);
  const [createModalOpen, setCreateModalOpen] = useState(false);
  const [totalPages, setTotalPages] = useState(0);
  const [currentPage, setCurrentPage] = useState(0);
  const [searchParams, setSearchParams] = useState<ProjectSearchParams>({
    page: 0,
    size: 12,
    sortBy: 'updatedAt',
    sortDirection: 'DESC'
  });
  const [searchQuery, setSearchQuery] = useState('');
  const [selectedTags, setSelectedTags] = useState<string[]>([]);
  const [allTags, setAllTags] = useState<string[]>([]);
  const [statistics, setStatistics] = useState<{
    totalProjects: number;
  } | null>(null);

  // Projekte laden
  const loadProjects = useCallback(async () => {
    try {
      setLoading(true);
      setError(null);

      const params: ProjectSearchParams = {
        ...searchParams,
        name: searchQuery || undefined,
        tags: selectedTags.length > 0 ? selectedTags : undefined
      };

      const response = await projectService.getAllProjects(params);
      setProjects(response.content);
      setTotalPages(response.totalPages);
      setCurrentPage(response.page);
    } catch (err) {
      setError(err instanceof Error ? err.message : 'Fehler beim Laden der Projekte');
      console.error('Error loading projects:', err);
    } finally {
      setLoading(false);
    }
  }, [searchParams, searchQuery, selectedTags]);

  // Tags laden
  const loadTags = useCallback(async () => {
    try {
      const tags = await projectService.getAllProjectTags();
      setAllTags(tags);
    } catch (err) {
      console.error('Error loading tags:', err);
    }
  }, []);

  // Statistiken laden
  const loadStatistics = useCallback(async () => {
    try {
      const stats = await projectService.getProjectStatistics();
      setStatistics({
        totalProjects: stats.totalProjects
      });
    } catch (err) {
      console.error('Error loading statistics:', err);
    }
  }, []);

  useEffect(() => {
    loadProjects();
  }, [loadProjects]);

  useEffect(() => {
    loadTags();
    loadStatistics();
  }, [loadTags, loadStatistics]);

  // Event Handlers
  const handleSearchChange = (event: React.ChangeEvent<HTMLInputElement>) => {
    setSearchQuery(event.target.value);
  };

  const handleSearchSubmit = () => {
    setSearchParams(prev => ({ ...prev, page: 0 }));
  };

  const handleSearchClear = () => {
    setSearchQuery('');
    setSelectedTags([]);
    setSearchParams(prev => ({ ...prev, page: 0 }));
  };

  const handleTagSelect = (tag: string) => {
    if (selectedTags.includes(tag)) {
      setSelectedTags(prev => prev.filter(t => t !== tag));
    } else {
      setSelectedTags(prev => [...prev, tag]);
    }
    setSearchParams(prev => ({ ...prev, page: 0 }));
  };

  const handleTagRemove = (tag: string) => {
    setSelectedTags(prev => prev.filter(t => t !== tag));
    setSearchParams(prev => ({ ...prev, page: 0 }));
  };

  const handleSortChange = (event: SelectChangeEvent<string>) => {
    const [sortBy, sortDirection] = (event.target.value as string).split(':');
    setSearchParams(prev => ({
      ...prev,
      sortBy: sortBy as any,
      sortDirection: sortDirection as 'ASC' | 'DESC',
      page: 0
    }));
  };

  const handlePageChange = (page: number) => {
    setSearchParams(prev => ({ ...prev, page }));
  };

  const handleProjectCreated = (newProject: Project) => {
    setProjects(prev => [newProject, ...prev]);
    setCreateModalOpen(false);
    loadStatistics(); // Statistiken aktualisieren
  };

  const handleProjectDeleted = (deletedProjectId: string) => {
    setProjects(prev => prev.filter(p => p.id !== deletedProjectId));
    loadStatistics(); // Statistiken aktualisieren
  };

  const handleProjectUpdated = (updatedProject: Project) => {
    setProjects(prev => prev.map(p => p.id === updatedProject.id ? updatedProject : p));
  };

  return (
    <Container maxWidth="xl">
      <Box sx={{ py: 3 }}>
        {/* Header */}
        <Box sx={{ mb: 4 }}>
          <Typography variant="h4" component="h1" gutterBottom>
            OCR-Projekte
          </Typography>
          
          {/* Statistiken */}
          {statistics && (
            <Paper sx={{ p: 2, mb: 3 }}>
              <Grid container spacing={3}>
                <Grid item xs={12} sm={4}>
                  <Typography variant="h6" color="primary">
                    {statistics.totalProjects}
                  </Typography>
                  <Typography variant="body2" color="text.secondary">
                    Projekte
                  </Typography>
                </Grid>
              </Grid>
            </Paper>
          )}
        </Box>

        {/* Suchbereich */}
        <Paper sx={{ p: 3, mb: 3 }}>
          <Grid container spacing={2} alignItems="center">
            <Grid item xs={12} md={6}>
              <TextField
                fullWidth
                variant="outlined"
                placeholder="Projekte suchen..."
                value={searchQuery}
                onChange={handleSearchChange}
                onKeyPress={(e) => e.key === 'Enter' && handleSearchSubmit()}
                InputProps={{
                  startAdornment: (
                    <InputAdornment position="start">
                      <SearchIcon />
                    </InputAdornment>
                  ),
                  endAdornment: searchQuery && (
                    <InputAdornment position="end">
                      <Button onClick={handleSearchClear} size="small">
                        <ClearIcon />
                      </Button>
                    </InputAdornment>
                  )
                }}
              />
            </Grid>
            
            <Grid item xs={12} md={3}>
              <FormControl fullWidth>
                <InputLabel>Sortierung</InputLabel>
                <Select
                  value={`${searchParams.sortBy}:${searchParams.sortDirection}`}
                  onChange={handleSortChange}
                  startAdornment={<SortIcon sx={{ mr: 1 }} />}
                >
                  <MenuItem value="updatedAt:DESC">Zuletzt bearbeitet</MenuItem>
                  <MenuItem value="createdAt:DESC">Neueste zuerst</MenuItem>
                  <MenuItem value="name:ASC">Name A-Z</MenuItem>
                  <MenuItem value="name:DESC">Name Z-A</MenuItem>
                  <MenuItem value="fileCount:DESC">Meiste Dateien</MenuItem>
                </Select>
              </FormControl>
            </Grid>

            <Grid item xs={12} md={3}>
              <Button
                variant="contained"
                startIcon={<AddIcon />}
                onClick={() => setCreateModalOpen(true)}
                fullWidth
              >
                Neues Projekt
              </Button>
            </Grid>
          </Grid>

          {/* Tag-Filter */}
          {allTags.length > 0 && (
            <Box sx={{ mt: 2 }}>
              <Typography variant="subtitle2" gutterBottom>
                Filter nach Tags:
              </Typography>
              <Box sx={{ display: 'flex', flexWrap: 'wrap', gap: 1 }}>
                {allTags.map(tag => (
                  <Chip
                    key={tag}
                    label={tag}
                    onClick={() => handleTagSelect(tag)}
                    onDelete={selectedTags.includes(tag) ? () => handleTagRemove(tag) : undefined}
                    variant={selectedTags.includes(tag) ? 'filled' : 'outlined'}
                    color={selectedTags.includes(tag) ? 'primary' : 'default'}
                  />
                ))}
              </Box>
            </Box>
          )}

          {/* Aktive Filter anzeigen */}
          {(searchQuery || selectedTags.length > 0) && (
            <Box sx={{ mt: 2 }}>
              <Typography variant="subtitle2" gutterBottom>
                Aktive Filter:
              </Typography>
              <Box sx={{ display: 'flex', flexWrap: 'wrap', gap: 1 }}>
                {searchQuery && (
                  <Chip
                    label={`Suche: ${searchQuery}`}
                    onDelete={() => setSearchQuery('')}
                    variant="outlined"
                  />
                )}
                {selectedTags.map(tag => (
                  <Chip
                    key={tag}
                    label={`Tag: ${tag}`}
                    onDelete={() => handleTagRemove(tag)}
                    variant="outlined"
                    color="primary"
                  />
                ))}
                <Button
                  size="small"
                  onClick={handleSearchClear}
                  sx={{ ml: 1 }}
                >
                  Alle Filter löschen
                </Button>
              </Box>
            </Box>
          )}
        </Paper>

        {/* Fehlermeldung */}
        {error && (
          <Alert severity="error" sx={{ mb: 3 }}>
            {error}
          </Alert>
        )}

        {/* Ladeindikator */}
        {loading && (
          <Box sx={{ display: 'flex', justifyContent: 'center', py: 4 }}>
            <CircularProgress />
          </Box>
        )}

        {/* Projektliste */}
        {!loading && (
          <ProjectList
            projects={projects}
            loading={loading}
            totalPages={totalPages}
            currentPage={currentPage}
            onPageChange={handlePageChange}
            onProjectDeleted={handleProjectDeleted}
            onProjectUpdated={handleProjectUpdated}
          />
        )}

        {/* Keine Projekte gefunden */}
        {!loading && projects.length === 0 && (
          <Paper sx={{ p: 4, textAlign: 'center' }}>
            <Typography variant="h6" color="text.secondary" gutterBottom>
              {searchQuery || selectedTags.length > 0 
                ? 'Keine Projekte gefunden'
                : 'Noch keine Projekte vorhanden'
              }
            </Typography>
            <Typography variant="body2" color="text.secondary" sx={{ mb: 2 }}>
              {searchQuery || selectedTags.length > 0
                ? 'Versuchen Sie andere Suchbegriffe oder Filter.'
                : 'Erstellen Sie Ihr erstes OCR-Projekt, um loszulegen.'
              }
            </Typography>
            <Button
              variant="contained"
              startIcon={<AddIcon />}
              onClick={() => setCreateModalOpen(true)}
            >
              Erstes Projekt erstellen
            </Button>
          </Paper>
        )}

        {/* Floating Action Button für mobile Geräte */}
        <Fab
          color="primary"
          aria-label="Neues Projekt erstellen"
          onClick={() => setCreateModalOpen(true)}
          sx={{
            position: 'fixed',
            bottom: 16,
            right: 16,
            display: { xs: 'flex', md: 'none' }
          }}
        >
          <AddIcon />
        </Fab>

        {/* Create Project Modal */}
        <CreateProjectModal
          open={createModalOpen}
          onClose={() => setCreateModalOpen(false)}
          onProjectCreated={handleProjectCreated}
        />
      </Box>
    </Container>
  );
};

export default ProjectsPage;