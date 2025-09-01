import axios from 'axios';
import {
  Project,
  ProjectDTO,
  CreateProjectRequest,
  UpdateProjectRequest,
  ProjectSearchParams,
  PaginatedResponse,
  ProjectStatistics
} from '../types/ocr';

const API_URL = '/api/v1/projects';

/**
 * Service für Projekt-Management API-Calls
 */

// CRUD-Operationen
export const getAllProjects = async (params?: ProjectSearchParams): Promise<PaginatedResponse<Project>> => {
  const queryParams = new URLSearchParams();
  
  if (params) {
    if (params.name) queryParams.append('name', params.name);
    if (params.tags && params.tags.length > 0) {
      params.tags.forEach(tag => queryParams.append('tags', tag));
    }
    if (params.page !== undefined) queryParams.append('page', params.page.toString());
    if (params.size !== undefined) queryParams.append('size', params.size.toString());
    if (params.sortBy) queryParams.append('sortBy', params.sortBy);
    if (params.sortDirection) queryParams.append('sortDirection', params.sortDirection);
  }

  const url = queryParams.toString() ? `${API_URL}?${queryParams.toString()}` : API_URL;
  console.log('Fetching projects with URL:', url);
  const response = await axios.get(url);
  console.log('Fetched projects:', response.data);
  return response.data;
};

export const getProjectById = async (id: string): Promise<ProjectDTO> => {
  const response = await axios.get(`${API_URL}/${id}`);
  console.log('Fetched project:', response.data);
  return response.data;
};

export const createProject = async (project: CreateProjectRequest): Promise<Project> => {
  const response = await axios.post(API_URL, project);
  return response.data;
};

export const updateProject = async (id: string, project: UpdateProjectRequest): Promise<Project> => {
  const response = await axios.put(`${API_URL}/${id}`, project);
  return response.data;
};

export const deleteProject = async (id: string): Promise<void> => {
  await axios.delete(`${API_URL}/${id}`);
};

// Tag-Management
export const getAllProjectTags = async (): Promise<string[]> => {
  const response = await axios.get(`${API_URL}/tags`);
  return response.data;
};

export const getProjectsByTag = async (tag: string): Promise<Project[]> => {
  const response = await axios.get(`${API_URL}/tag/${encodeURIComponent(tag)}`);
  return response.data;
};

export const getProjectsByTags = async (tags: string[]): Promise<Project[]> => {
  const response = await axios.post(`${API_URL}/search/tags`, tags);
  return response.data;
};

export const addTagToProject = async (projectId: string, tag: string): Promise<Project> => {
  const response = await axios.post(`${API_URL}/${projectId}/tags`, { tag });
  return response.data;
};

export const removeTagFromProject = async (projectId: string, tag: string): Promise<Project> => {
  await axios.delete(`${API_URL}/${projectId}/tags/${encodeURIComponent(tag)}`);
  const response = await axios.get(`${API_URL}/${projectId}`);
  return response.data;
};

// Projekt-Statistiken und Suche
export const getProjectStatistics = async (): Promise<ProjectStatistics> => {
  const response = await axios.get(`${API_URL}/statistics`);
  return response.data;
};

export const getProjectStats = async (projectId: string): Promise<{
  fileCount: number;
  totalTextCount: number;
  totalFileSize: number;
  lastActivity: string;
}> => {
  const response = await axios.get(`${API_URL}/${projectId}/statistics`);
  return response.data;
};

export const searchProjects = async (query: string): Promise<Project[]> => {
  const response = await axios.get(`${API_URL}/search?q=${encodeURIComponent(query)}`);
  return response.data;
};

// Projekt-Duplikation und Export
export const duplicateProject = async (projectId: string, newName: string): Promise<Project> => {
  const response = await axios.post(`${API_URL}/${projectId}/duplicate`, { name: newName });
  return response.data;
};

export const exportProject = async (projectId: string, format: 'json' | 'csv' | 'txt' = 'json'): Promise<Blob> => {
  const response = await axios.get(`${API_URL}/${projectId}/export`, {
    params: { format },
    responseType: 'blob'
  });
  return response.data;
};

// Projekt-Archivierung
export const archiveProject = async (projectId: string): Promise<Project> => {
  const response = await axios.post(`${API_URL}/${projectId}/archive`);
  return response.data;
};

export const unarchiveProject = async (projectId: string): Promise<Project> => {
  const response = await axios.post(`${API_URL}/${projectId}/unarchive`);
  return response.data;
};

export const getArchivedProjects = async (): Promise<Project[]> => {
  const response = await axios.get(`${API_URL}/archived`);
  return response.data;
};

// Batch-Operationen
export const deleteMultipleProjects = async (projectIds: string[]): Promise<void> => {
  await axios.delete(`${API_URL}/batch`, { data: { projectIds } });
};

export const updateMultipleProjectTags = async (
  projectIds: string[], 
  tagsToAdd: string[], 
  tagsToRemove: string[]
): Promise<Project[]> => {
  const response = await axios.post(`${API_URL}/batch/tags`, {
    projectIds,
    tagsToAdd,
    tagsToRemove
  });
  return response.data;
};

// Validierung
export const validateProjectName = async (name: string, excludeId?: string): Promise<{ isValid: boolean; message?: string }> => {
  const params = new URLSearchParams({ name });
  if (excludeId) params.append('excludeId', excludeId);
  
  const response = await axios.get(`${API_URL}/validate/name?${params.toString()}`);
  return response.data;
};

// Kürzlich verwendete Projekte
export const getRecentProjects = async (limit: number = 5): Promise<Project[]> => {
  const response = await axios.get(`${API_URL}/recent?limit=${limit}`);
  return response.data;
};

// Favoriten-Management
export const addToFavorites = async (projectId: string): Promise<void> => {
  await axios.post(`${API_URL}/${projectId}/favorite`);
};

export const removeFromFavorites = async (projectId: string): Promise<void> => {
  await axios.delete(`${API_URL}/${projectId}/favorite`);
};

export const getFavoriteProjects = async (): Promise<Project[]> => {
  const response = await axios.get(`${API_URL}/favorites`);
  return response.data;
};