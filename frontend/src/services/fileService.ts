import axios, { AxiosProgressEvent } from 'axios';
import {
  ProjectFile,
  ProjectFileDTO,
  FileUploadResponse,
  FileSearchParams,
  PaginatedResponse,
  FileUploadProgress,
  BatchUploadState,
  FileType
} from '../types/ocr';

const API_URL = '/api/v1/files';

/**
 * Service für Datei-Management API-Calls
 */

// Datei-Upload (Einzel)
export const uploadFile = async (
  projectId: string,
  file: File,
  onProgress?: (progress: number) => void
): Promise<FileUploadResponse> => {
  const formData = new FormData();
  formData.append('file', file);
  formData.append('projectId', projectId);

  const response = await axios.post(`${API_URL}/upload`, formData, {
    headers: {
      'Content-Type': 'multipart/form-data',
    },
    onUploadProgress: (progressEvent: AxiosProgressEvent) => {
      if (progressEvent.total && onProgress) {
        const progress = Math.round((progressEvent.loaded * 100) / progressEvent.total);
        onProgress(progress);
      }
    },
  });

  return response.data;
};

// Datei-Upload (Batch)
export const uploadMultipleFiles = async (
  projectId: string,
  files: File[],
  onProgress?: (uploadStates: FileUploadProgress[]) => void,
  onComplete?: (results: FileUploadResponse[]) => void
): Promise<FileUploadResponse[]> => {
  const uploadPromises = files.map(async (file, index) => {
    let uploadProgress: FileUploadProgress = {
      fileId: `temp-${index}`,
      filename: file.name,
      progress: 0,
      status: 'pending'
    };

    try {
      uploadProgress.status = 'uploading';
      onProgress?.(files.map((f, i) => i === index ? uploadProgress : {
        fileId: `temp-${i}`,
        filename: f.name,
        progress: i < index ? 100 : 0,
        status: i < index ? 'completed' : 'pending'
      }));

      const result = await uploadFile(projectId, file, (progress) => {
        uploadProgress.progress = progress;
        onProgress?.(files.map((f, i) => i === index ? uploadProgress : {
          fileId: `temp-${i}`,
          filename: f.name,
          progress: i < index ? 100 : 0,
          status: i < index ? 'completed' : 'pending'
        }));
      });

      uploadProgress.status = 'completed';
      uploadProgress.fileId = result.id;
      return result;
    } catch (error) {
      uploadProgress.status = 'error';
      uploadProgress.error = error instanceof Error ? error.message : 'Upload fehler';
      throw error;
    }
  });

  const results = await Promise.all(uploadPromises);
  onComplete?.(results);
  return results;
};

// Chunked Upload für große Dateien
export const uploadLargeFile = async (
  projectId: string,
  file: File,
  chunkSize: number = 1024 * 1024, // 1MB chunks
  onProgress?: (progress: number) => void
): Promise<FileUploadResponse> => {
  const totalChunks = Math.ceil(file.size / chunkSize);
  const uploadId = `upload-${Date.now()}-${Math.random().toString(36).substr(2, 9)}`;
  
  // Initiate multipart upload
  const initiateResponse = await axios.post(`${API_URL}/upload/multipart/initiate`, {
    projectId,
    filename: file.name,
    fileSize: file.size,
    contentType: file.type,
    uploadId
  });

  const { uploadSessionId } = initiateResponse.data;
  const uploadedParts: { partNumber: number; etag: string }[] = [];

  try {
    // Upload chunks
    for (let i = 0; i < totalChunks; i++) {
      const start = i * chunkSize;
      const end = Math.min(start + chunkSize, file.size);
      const chunk = file.slice(start, end);
      
      const chunkFormData = new FormData();
      chunkFormData.append('chunk', chunk);
      chunkFormData.append('partNumber', (i + 1).toString());
      chunkFormData.append('uploadSessionId', uploadSessionId);

      const chunkResponse = await axios.post(`${API_URL}/upload/multipart/chunk`, chunkFormData, {
        headers: { 'Content-Type': 'multipart/form-data' }
      });

      uploadedParts.push({
        partNumber: i + 1,
        etag: chunkResponse.data.etag
      });

      const progress = Math.round(((i + 1) / totalChunks) * 100);
      onProgress?.(progress);
    }

    // Complete multipart upload
    const completeResponse = await axios.post(`${API_URL}/upload/multipart/complete`, {
      uploadSessionId,
      parts: uploadedParts
    });

    return completeResponse.data;
  } catch (error) {
    // Abort multipart upload on error
    await axios.post(`${API_URL}/upload/multipart/abort`, { uploadSessionId });
    throw error;
  }
};

// Datei-Download und Metadaten
export const downloadFile = async (fileId: string): Promise<Blob> => {
  const response = await axios.get(`${API_URL}/${fileId}/download`, {
    responseType: 'blob'
  });
  return response.data;
};

export const getFileMetadata = async (fileId: string): Promise<ProjectFileDTO> => {
  const response = await axios.get(`${API_URL}/${fileId}`);
  return response.data;
};

export const getFilesByProject = async (
  projectId: string, 
  params?: FileSearchParams
): Promise<PaginatedResponse<ProjectFile>> => {
  const queryParams = new URLSearchParams({ projectId });
  
  if (params) {
    if (params.filename) queryParams.append('filename', params.filename);
    if (params.fileType) queryParams.append('fileType', params.fileType);
    if (params.page !== undefined) queryParams.append('page', params.page.toString());
    if (params.size !== undefined) queryParams.append('size', params.size.toString());
  }

  const response = await axios.get(`${API_URL}?${queryParams.toString()}`);
  return response.data;
};

export const deleteFile = async (fileId: string): Promise<void> => {
  await axios.delete(`${API_URL}/${fileId}`);
};

export const deleteMultipleFiles = async (fileIds: string[]): Promise<void> => {
  await axios.delete(`${API_URL}/batch`, { data: { fileIds } });
};

// File-Validation
export const validateFile = (file: File): { isValid: boolean; errors: string[] } => {
  const errors: string[] = [];
  const maxSize = 50 * 1024 * 1024; // 50MB
  const allowedTypes = [
    'image/jpeg', 'image/png', 'image/gif', 'image/bmp', 'image/tiff',
    'application/pdf',
    'application/msword', 'application/vnd.openxmlformats-officedocument.wordprocessingml.document',
    'text/plain', 'text/rtf'
  ];

  // Size validation
  if (file.size > maxSize) {
    errors.push(`Datei ist zu groß. Maximum: ${maxSize / (1024 * 1024)}MB`);
  }

  // Type validation
  if (!allowedTypes.includes(file.type)) {
    errors.push(`Dateityp nicht unterstützt: ${file.type}`);
  }

  // Name validation
  if (file.name.length > 255) {
    errors.push('Dateiname ist zu lang (max. 255 Zeichen)');
  }

  return {
    isValid: errors.length === 0,
    errors
  };
};

export const validateMultipleFiles = (files: File[]): { 
  validFiles: File[]; 
  invalidFiles: { file: File; errors: string[] }[] 
} => {
  const validFiles: File[] = [];
  const invalidFiles: { file: File; errors: string[] }[] = [];

  files.forEach(file => {
    const validation = validateFile(file);
    if (validation.isValid) {
      validFiles.push(file);
    } else {
      invalidFiles.push({ file, errors: validation.errors });
    }
  });

  return { validFiles, invalidFiles };
};

// File-Preview und Thumbnails
export const getFilePreview = async (fileId: string): Promise<Blob> => {
  const response = await axios.get(`${API_URL}/${fileId}/preview`, {
    responseType: 'blob'
  });
  return response.data;
};

export const getFileThumbnail = async (fileId: string, size: 'small' | 'medium' | 'large' = 'medium'): Promise<Blob> => {
  const response = await axios.get(`${API_URL}/${fileId}/thumbnail`, {
    params: { size },
    responseType: 'blob'
  });
  return response.data;
};

// Datei-Konvertierung
export const convertFile = async (
  fileId: string, 
  targetFormat: string
): Promise<{ conversionId: string; status: string }> => {
  const response = await axios.post(`${API_URL}/${fileId}/convert`, {
    targetFormat
  });
  return response.data;
};

export const getConversionStatus = async (conversionId: string): Promise<{
  status: 'pending' | 'processing' | 'completed' | 'failed';
  progress: number;
  downloadUrl?: string;
  error?: string;
}> => {
  const response = await axios.get(`${API_URL}/conversions/${conversionId}/status`);
  return response.data;
};

// Datei-Statistiken
export const getFileStatistics = async (projectId?: string): Promise<{
  totalFiles: number;
  totalSize: number;
  fileTypeDistribution: { [type: string]: number };
  averageFileSize: number;
  largestFile: { id: string; name: string; size: number };
  oldestFile: { id: string; name: string; uploadedAt: string };
  newestFile: { id: string; name: string; uploadedAt: string };
}> => {
  const url = projectId ? `${API_URL}/statistics?projectId=${projectId}` : `${API_URL}/statistics`;
  const response = await axios.get(url);
  return response.data;
};

// Datei-Suche
export const searchFiles = async (query: string, projectId?: string): Promise<ProjectFile[]> => {
  const params = new URLSearchParams({ q: query });
  if (projectId) params.append('projectId', projectId);
  
  const response = await axios.get(`${API_URL}/search?${params.toString()}`);
  return response.data;
};

// Datei-Duplikate
export const findDuplicateFiles = async (projectId?: string): Promise<{
  duplicateGroups: Array<{
    hash: string;
    files: ProjectFile[];
    totalSize: number;
  }>;
  totalDuplicates: number;
  potentialSavings: number;
}> => {
  const url = projectId ? `${API_URL}/duplicates?projectId=${projectId}` : `${API_URL}/duplicates`;
  const response = await axios.get(url);
  return response.data;
};

// Datei-Backup und Restore
export const backupFile = async (fileId: string): Promise<{ backupId: string; message: string }> => {
  const response = await axios.post(`${API_URL}/${fileId}/backup`);
  return response.data;
};

export const restoreFile = async (backupId: string): Promise<FileUploadResponse> => {
  const response = await axios.post(`${API_URL}/restore/${backupId}`);
  return response.data;
};

// Utility-Funktionen
export const getFileTypeFromExtension = (filename: string): FileType => {
  const extension = filename.split('.').pop()?.toLowerCase();
  
  switch (extension) {
    case 'jpg':
    case 'jpeg':
    case 'png':
    case 'gif':
    case 'bmp':
    case 'tiff':
    case 'svg':
      return FileType.IMAGE;
    case 'pdf':
      return FileType.PDF;
    case 'doc':
    case 'docx':
    case 'txt':
    case 'rtf':
    case 'odt':
      return FileType.DOCUMENT;
    default:
      return FileType.DOCUMENT;
  }
};

export const formatFileSize = (bytes: number): string => {
  if (bytes === 0) return '0 B';
  
  const k = 1024;
  const sizes = ['B', 'KB', 'MB', 'GB'];
  const i = Math.floor(Math.log(bytes) / Math.log(k));
  
  return parseFloat((bytes / Math.pow(k, i)).toFixed(2)) + ' ' + sizes[i];
};

export const createFileUrl = (file: File): string => {
  return URL.createObjectURL(file);
};

export const revokeFileUrl = (url: string): void => {
  URL.revokeObjectURL(url);
};