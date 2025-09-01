import axios from 'axios';
import {
  OCRResult,
  OCRResultDTO,
  ExtractedText,
  ExtractedTextDTO,
  OCRProcessRequest,
  OCRProcessResponse,
  UpdateTextRequest,
  OCROptions,
  OCRStatus,
  ExtractedTextSearchParams,
  PaginatedResponse,
  OCRStatistics,
  OCRBatchOperation,
  ExtractionSource
} from '../types/ocr';

const OCR_API_URL = '/api/v1/ocr';
const TEXT_API_URL = '/api/v1/extracted-texts';

/**
 * Service für OCR-Verarbeitung und Text-Management
 */

// OCR-Verarbeitung (Einzel und Batch)
export const processOCR = async (request: OCRProcessRequest): Promise<OCRProcessResponse> => {
  const response = await axios.post(`${OCR_API_URL}/process`, request);
  return response.data;
};

export const processSingleFile = async (fileId: string, options?: OCROptions): Promise<OCRResult> => {
  const response = await axios.post(`${OCR_API_URL}/process/single`, {
    fileId,
    options: options || {}
  });
  return response.data;
};

export const processBatchFiles = async (
  fileIds: string[], 
  options?: OCROptions
): Promise<OCRBatchOperation> => {
  const response = await axios.post(`${OCR_API_URL}/process/batch`, {
    fileIds,
    options: options || {}
  });
  return response.data;
};

// Status-Polling für asynchrone Operationen
export const getOCRResult = async (resultId: string): Promise<OCRResultDTO> => {
  const response = await axios.get(`${OCR_API_URL}/results/${resultId}`);
  return response.data;
};

export const getOCRResultsByFile = async (fileId: string): Promise<OCRResult[]> => {
  const response = await axios.get(`${OCR_API_URL}/results/file/${fileId}`);
  return response.data;
};

export const getBatchOperationStatus = async (batchId: string): Promise<OCRBatchOperation> => {
  const response = await axios.get(`${OCR_API_URL}/batch/${batchId}`);
  return response.data;
};

export const cancelOCRProcess = async (resultId: string): Promise<void> => {
  await axios.post(`${OCR_API_URL}/results/${resultId}/cancel`);
};

export const cancelBatchOperation = async (batchId: string): Promise<void> => {
  await axios.post(`${OCR_API_URL}/batch/${batchId}/cancel`);
};

// Status-Polling mit Intervall
export const pollOCRStatus = (
  resultId: string,
  onUpdate: (result: OCRResult) => void,
  onComplete: (result: OCRResult) => void,
  onError: (error: Error) => void,
  intervalMs: number = 2000
): () => void => {
  const interval = setInterval(async () => {
    try {
      const result = await getOCRResult(resultId);
      onUpdate(result);

      if (result.status === OCRStatus.COMPLETED || result.status === OCRStatus.FAILED) {
        clearInterval(interval);
        onComplete(result);
      }
    } catch (error) {
      clearInterval(interval);
      onError(error instanceof Error ? error : new Error('OCR Status-Polling Fehler'));
    }
  }, intervalMs);

  return () => clearInterval(interval);
};

export const pollBatchStatus = (
  batchId: string,
  onUpdate: (batch: OCRBatchOperation) => void,
  onComplete: (batch: OCRBatchOperation) => void,
  onError: (error: Error) => void,
  intervalMs: number = 3000
): () => void => {
  const interval = setInterval(async () => {
    try {
      const batch = await getBatchOperationStatus(batchId);
      onUpdate(batch);

      if (batch.status === OCRStatus.COMPLETED || batch.status === OCRStatus.FAILED) {
        clearInterval(interval);
        onComplete(batch);
      }
    } catch (error) {
      clearInterval(interval);
      onError(error instanceof Error ? error : new Error('Batch Status-Polling Fehler'));
    }
  }, intervalMs);

  return () => clearInterval(interval);
};

// Text-Bearbeitung und -Management
export const getExtractedTexts = async (
  params?: ExtractedTextSearchParams
): Promise<PaginatedResponse<ExtractedText>> => {
  const queryParams = new URLSearchParams();
  
  if (params) {
    if (params.projectFileId) queryParams.append('projectFileId', params.projectFileId);
    if (params.projectId) queryParams.append('projectId', params.projectId);
    if (params.content) queryParams.append('content', params.content);
    if (params.source) queryParams.append('source', params.source);
    if (params.page !== undefined) queryParams.append('page', params.page.toString());
    if (params.size !== undefined) queryParams.append('size', params.size.toString());
  }

  const url = queryParams.toString() ? `${TEXT_API_URL}?${queryParams.toString()}` : TEXT_API_URL;
  const response = await axios.get(url);
  return response.data;
};

export const getExtractedTextById = async (textId: string): Promise<ExtractedTextDTO> => {
  const response = await axios.get(`${TEXT_API_URL}/${textId}`);
  return response.data;
};

export const getExtractedTextsByFile = async (fileId: string): Promise<ExtractedText[]> => {
  const response = await axios.get(`${TEXT_API_URL}/file/${fileId}`);
  return response.data;
};

export const getExtractedTextsByProject = async (projectId: string): Promise<ExtractedText[]> => {
  const response = await axios.get(`${TEXT_API_URL}/project/${projectId}`);
  return response.data;
};

export const updateExtractedText = async (textId: string, request: UpdateTextRequest): Promise<ExtractedText> => {
  const response = await axios.put(`${TEXT_API_URL}/${textId}`, request);
  return response.data;
};

export const deleteExtractedText = async (textId: string): Promise<void> => {
  await axios.delete(`${TEXT_API_URL}/${textId}`);
};

export const deleteMultipleExtractedTexts = async (textIds: string[]): Promise<void> => {
  await axios.delete(`${TEXT_API_URL}/batch`, { data: { textIds } });
};

// Manueller Text hinzufügen
export const addManualText = async (
  fileId: string, 
  content: string, 
  pageNumber?: number
): Promise<ExtractedText> => {
  const response = await axios.post(`${TEXT_API_URL}/manual`, {
    projectFileId: fileId,
    content,
    source: ExtractionSource.MANUAL,
    pageNumber
  });
  return response.data;
};

// Text-Suche und Filter
export const searchExtractedTexts = async (
  query: string, 
  projectId?: string, 
  fileId?: string
): Promise<ExtractedText[]> => {
  const params = new URLSearchParams({ q: query });
  if (projectId) params.append('projectId', projectId);
  if (fileId) params.append('fileId', fileId);
  
  const response = await axios.get(`${TEXT_API_URL}/search?${params.toString()}`);
  return response.data;
};

export const getTextsBySource = async (
  source: ExtractionSource, 
  projectId?: string
): Promise<ExtractedText[]> => {
  const params = new URLSearchParams({ source });
  if (projectId) params.append('projectId', projectId);
  
  const response = await axios.get(`${TEXT_API_URL}/by-source?${params.toString()}`);
  return response.data;
};

// OCR-Statistiken und -Ergebnisse
export const getOCRStatistics = async (projectId?: string): Promise<OCRStatistics> => {
  const url = projectId ? `${OCR_API_URL}/statistics?projectId=${projectId}` : `${OCR_API_URL}/statistics`;
  const response = await axios.get(url);
  return response.data;
};

export const getProjectOCRSummary = async (projectId: string): Promise<{
  totalFiles: number;
  processedFiles: number;
  pendingFiles: number;
  failedFiles: number;
  totalTexts: number;
  averageConfidence: number;
  processingTime: number;
}> => {
  const response = await axios.get(`${OCR_API_URL}/projects/${projectId}/summary`);
  return response.data;
};

export const getFileOCRSummary = async (fileId: string): Promise<{
  status: OCRStatus;
  extractedTextCount: number;
  totalPages: number;
  averageConfidence: number;
  processingTime: number;
  errorMessage?: string;
}> => {
  const response = await axios.get(`${OCR_API_URL}/files/${fileId}/summary`);
  return response.data;
};

// OCR-Konfiguration und Optionen
export const getDefaultOCROptions = async (): Promise<OCROptions> => {
  const response = await axios.get(`${OCR_API_URL}/options/default`);
  return response.data;
};

export const validateOCROptions = async (options: OCROptions): Promise<{
  isValid: boolean;
  errors: string[];
  warnings: string[];
}> => {
  const response = await axios.post(`${OCR_API_URL}/options/validate`, options);
  return response.data;
};

export const getSupportedLanguages = async (): Promise<string[]> => {
  const response = await axios.get(`${OCR_API_URL}/languages`);
  return response.data;
};

export const getOCRCapabilities = async (): Promise<{
  supportedFormats: string[];
  maxFileSize: number;
  maxPages: number;
  features: string[];
}> => {
  const response = await axios.get(`${OCR_API_URL}/capabilities`);
  return response.data;
};

// Text-Export und -Konvertierung
export const exportExtractedTexts = async (
  projectId: string, 
  format: 'txt' | 'json' | 'csv' | 'docx' = 'txt'
): Promise<Blob> => {
  const response = await axios.get(`${TEXT_API_URL}/export/project/${projectId}`, {
    params: { format },
    responseType: 'blob'
  });
  return response.data;
};

export const exportFileTexts = async (
  fileId: string, 
  format: 'txt' | 'json' | 'csv' = 'txt'
): Promise<Blob> => {
  const response = await axios.get(`${TEXT_API_URL}/export/file/${fileId}`, {
    params: { format },
    responseType: 'blob'
  });
  return response.data;
};

export const combineTexts = async (
  textIds: string[], 
  separator: string = '\n\n'
): Promise<{ combinedText: string; wordCount: number; charCount: number }> => {
  const response = await axios.post(`${TEXT_API_URL}/combine`, {
    textIds,
    separator
  });
  return response.data;
};

// Text-Validierung und -Bereinigung
export const validateText = async (content: string): Promise<{
  isValid: boolean;
  issues: Array<{
    type: 'encoding' | 'length' | 'content' | 'format';
    message: string;
    severity: 'error' | 'warning' | 'info';
  }>;
  suggestions: string[];
}> => {
  const response = await axios.post(`${TEXT_API_URL}/validate`, { content });
  return response.data;
};

export const cleanText = async (
  content: string, 
  options: {
    removeExtraWhitespace?: boolean;
    fixEncoding?: boolean;
    removeDuplicateLines?: boolean;
    normalizeLineBreaks?: boolean;
  } = {}
): Promise<{ cleanedText: string; changes: string[] }> => {
  const response = await axios.post(`${TEXT_API_URL}/clean`, {
    content,
    options
  });
  return response.data;
};

// Batch-Operationen für Texte
export const batchUpdateTexts = async (
  updates: Array<{ textId: string; content: string }>
): Promise<ExtractedText[]> => {
  const response = await axios.post(`${TEXT_API_URL}/batch/update`, { updates });
  return response.data;
};

export const batchCleanTexts = async (
  textIds: string[],
  cleanOptions: {
    removeExtraWhitespace?: boolean;
    fixEncoding?: boolean;
    removeDuplicateLines?: boolean;
    normalizeLineBreaks?: boolean;
  } = {}
): Promise<ExtractedText[]> => {
  const response = await axios.post(`${TEXT_API_URL}/batch/clean`, {
    textIds,
    options: cleanOptions
  });
  return response.data;
};

// Real-time Updates für OCR-Status
export const subscribeToOCRUpdates = (
  onUpdate: (update: { type: 'status' | 'progress' | 'result'; data: any }) => void
): WebSocket | null => {
  if (typeof window !== 'undefined' && 'WebSocket' in window) {
    const wsUrl = `${window.location.protocol === 'https:' ? 'wss:' : 'ws:'}//${window.location.host}/api/v1/ocr/ws`;
    const ws = new WebSocket(wsUrl);
    
    ws.onmessage = (event) => {
      try {
        const update = JSON.parse(event.data);
        onUpdate(update);
      } catch (error) {
        console.error('WebSocket message parsing error:', error);
      }
    };
    
    return ws;
  }
  return null;
};

// Utility-Funktionen
export const calculateTextStatistics = (text: string): {
  charCount: number;
  wordCount: number;
  lineCount: number;
  paragraphCount: number;
  averageWordsPerLine: number;
  readingTimeMinutes: number;
} => {
  const charCount = text.length;
  const words = text.trim().split(/\s+/).filter(word => word.length > 0);
  const wordCount = words.length;
  const lines = text.split('\n');
  const lineCount = lines.length;
  const paragraphs = text.split(/\n\s*\n/).filter(p => p.trim().length > 0);
  const paragraphCount = paragraphs.length;
  const averageWordsPerLine = lineCount > 0 ? wordCount / lineCount : 0;
  const readingTimeMinutes = Math.ceil(wordCount / 200); // Durchschnittliche Lesegeschwindigkeit: 200 Wörter/Minute

  return {
    charCount,
    wordCount,
    lineCount,
    paragraphCount,
    averageWordsPerLine: Math.round(averageWordsPerLine * 100) / 100,
    readingTimeMinutes
  };
};

export const formatConfidence = (confidence?: number): string => {
  if (confidence === undefined) return 'N/A';
  return `${Math.round(confidence * 100)}%`;
};

export const getStatusColor = (status: OCRStatus): string => {
  switch (status) {
    case OCRStatus.PENDING:
      return '#ffa726'; // Orange
    case OCRStatus.PROCESSING:
      return '#42a5f5'; // Blue
    case OCRStatus.COMPLETED:
      return '#66bb6a'; // Green
    case OCRStatus.FAILED:
      return '#ef5350'; // Red
    default:
      return '#9e9e9e'; // Grey
  }
};

export const getStatusIcon = (status: OCRStatus): string => {
  switch (status) {
    case OCRStatus.PENDING:
      return 'schedule';
    case OCRStatus.PROCESSING:
      return 'hourglass_empty';
    case OCRStatus.COMPLETED:
      return 'check_circle';
    case OCRStatus.FAILED:
      return 'error';
    default:
      return 'help';
  }
};