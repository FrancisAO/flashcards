import React from 'react';
import {
  Grid,
  Pagination,
  Box,
  Typography
} from '@mui/material';
import { Project } from '../types/ocr';
import ProjectCard from './ProjectCard';

interface ProjectListProps {
  projects: Project[];
  loading: boolean;
  totalPages: number;
  currentPage: number;
  onPageChange: (page: number) => void;
  onProjectDeleted: (projectId: string) => void;
  onProjectUpdated: (project: Project) => void;
}

const ProjectList: React.FC<ProjectListProps> = ({
  projects,
  loading,
  totalPages,
  currentPage,
  onPageChange,
  onProjectDeleted,
  onProjectUpdated
}) => {
  const handlePageChange = (event: React.ChangeEvent<unknown>, page: number) => {
    onPageChange(page - 1); // MUI Pagination ist 1-basiert, unsere API ist 0-basiert
  };

  if (loading) {
    return null; // Loading wird von der übergeordneten Komponente behandelt
  }

  return (
    <Box>
      {/* Projektliste */}
      <Grid container spacing={3}>
        {projects.map((project) => (
          <Grid item xs={12} sm={6} md={4} lg={3} key={project.id}>
            <ProjectCard
              project={project}
              onDeleted={() => onProjectDeleted(project.id!)}
              onUpdated={onProjectUpdated}
            />
          </Grid>
        ))}
      </Grid>

      {/* Pagination */}
      {totalPages > 1 && (
        <Box sx={{ display: 'flex', justifyContent: 'center', mt: 4 }}>
          <Pagination
            count={totalPages}
            page={currentPage + 1} // MUI Pagination ist 1-basiert
            onChange={handlePageChange}
            color="primary"
            size="large"
            showFirstButton
            showLastButton
          />
        </Box>
      )}

      {/* Anzahl der Ergebnisse */}
      {projects.length > 0 && (
        <Box sx={{ mt: 2, textAlign: 'center' }}>
          <Typography variant="body2" color="text.secondary">
            Seite {currentPage + 1} von {totalPages} • {projects.length} Projekte angezeigt
          </Typography>
        </Box>
      )}
    </Box>
  );
};

export default ProjectList;