/**
 * TypeScript Interfaces für OCR-Funktionalität
 * Entspricht den Backend-Domain-Models
 */

// Enums
export enum FileType {
  IMAGE = 'IMAGE',
  PDF = 'PDF',
  DOCUMENT = 'DOCUMENT'
}

export enum ExtractionSource {
  OCR = 'OCR',
  MANUAL = 'MANUAL',
  IMPORTED = 'IMPORTED'
}

export enum OCRStatus {
  PENDING = 'PENDING',
  PROCESSING = 'PROCESSING',
  SUCCESS = 'SUCCESS',
  FAILED = 'FAILED'
}

// Core Interfaces
export interface Project {
  id?: string;
  name: string;
  description?: string;
  tags: string[];
  createdAt?: string;
  updatedAt?: string;
  fileCount?: number;
  totalTextCount?: number;
}

export interface ProjectFile {
  id?: string;
  projectId: string;
  originalFilename: string;
  storedFilename: string;
  fileType: FileType;
  contentType: string;
  fileSize: number;
  uploadedAt?: string;
  hasExtractedText?: boolean;
}

export interface ExtractedText {
  id?: string;
  projectFileId: string;
  content: string;
  source: ExtractionSource;
  pageNumber?: number;
  boundingBox?: string;
  confidence?: number;
  createdAt?: string;
  updatedAt?: string;
}

export interface OCRResult {
  id?: string;
  projectFileId: string;
  status: OCRStatus;
  extractedTextCount: number;
  totalPages?: number;
  processingTimeMs?: number;
  errorMessage?: string;
  createdAt?: string;
  completedAt?: string;
}

export interface OCROptions {
  language?: string;
  detectRotation?: boolean;
  enhanceImage?: boolean;
  extractTables?: boolean;
  outputFormat?: string;
}

// Request/Response Types
export interface CreateProjectRequest {
  name: string;
  description?: string;
  tags: string[];
}

export interface UpdateProjectRequest {
  name?: string;
  description?: string;
  tags?: string[];
}

export interface OCRProcessRequest {
  fileIds: string[];
  options?: OCROptions;
}

export interface OCRProcessResponse {
  resultIds: string[];
  message: string;
}

export interface UpdateTextRequest {
  content: string;
}

export interface FileUploadResponse {
  id: string;
  originalFilename: string;
  storedFilename: string;
  fileType: FileType;
  contentType: string;
  fileSize: number;
  uploadedAt: string;
}

export interface ProjectDTO extends Project {
  files?: ProjectFileDTO[];
}

export interface ProjectFileDTO extends ProjectFile {
  extractedTexts?: ExtractedTextDTO[];
  ocrResult?: OCRResultDTO;
}

export interface ExtractedTextDTO extends ExtractedText {}

export interface OCRResultDTO extends OCRResult {}

// Search and Filter Types
export interface ProjectSearchParams {
  name?: string;
  tags?: string[];
  page?: number;
  size?: number;
  sortBy?: string;
  sortDirection?: 'ASC' | 'DESC';
}

export interface FileSearchParams {
  projectId: string;
  filename?: string;
  fileType?: FileType;
  page?: number;
  size?: number;
}

export interface ExtractedTextSearchParams {
  projectFileId?: string;
  projectId?: string;
  content?: string;
  source?: ExtractionSource;
  page?: number;
  size?: number;
}

// UI State Types
export interface ProjectListState {
  projects: Project[];
  loading: boolean;
  error: string | null;
  totalPages: number;
  currentPage: number;
}

export interface FileListState {
  files: ProjectFile[];
  loading: boolean;
  error: string | null;
  uploadProgress: { [fileId: string]: number };
}

export interface OCRProcessingState {
  activeProcesses: { [fileId: string]: OCRResult };
  loading: boolean;
  error: string | null;
}

export interface ExtractedTextState {
  texts: ExtractedText[];
  loading: boolean;
  error: string | null;
  editingTextId: string | null;
}

// File Upload Types
export interface FileUploadProgress {
  fileId: string;
  filename: string;
  progress: number;
  status: 'pending' | 'uploading' | 'completed' | 'error';
  error?: string;
}

export interface BatchUploadState {
  uploads: FileUploadProgress[];
  overallProgress: number;
  isUploading: boolean;
}

// OCR Processing Types
export interface OCRBatchOperation {
  id: string;
  fileIds: string[];
  options: OCROptions;
  startedAt: string;
  completedCount: number;
  totalCount: number;
  status: OCRStatus;
  results: OCRResult[];
}

// API Response Wrappers
export interface ApiResponse<T> {
  data: T;
  message?: string;
  success: boolean;
}

export interface PaginatedResponse<T> {
  content: T[];
  page: number;
  size: number;
  totalElements: number;
  totalPages: number;
  first: boolean;
  last: boolean;
}

// Error Types
export interface OCRError {
  code: string;
  message: string;
  details?: string;
  timestamp: string;
}

// Statistics Types
export interface ProjectStatistics {
  totalProjects: number;
  totalFiles: number;
  totalExtractedTexts: number;
  averageTextsPerFile: number;
  mostUsedTags: string[];
  recentActivity: {
    projectsCreated: number;
    filesUploaded: number;
    textsExtracted: number;
    period: string;
  };
}

export interface OCRStatistics {
  totalProcessed: number;
  successRate: number;
  averageProcessingTime: number;
  languageDistribution: { [language: string]: number };
  fileTypeDistribution: { [fileType: string]: number };
}