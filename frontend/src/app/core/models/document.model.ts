export interface Document {
  id: number;
  title: string;
  description?: string;
  tags?: string[];
  ownerUsername: string;
  status: DocumentStatus;
  createdAt: string;
  updatedAt: string;
}

export type DocumentStatus = 'DRAFT' | 'PUBLISHED' | 'ARCHIVED';

export interface CreateDocumentRequest {
  title: string;
  description?: string;
  tags?: string[];
}

export interface UpdateDocumentRequest {
  title?: string;
  description?: string;
  tags?: string[];
}

export interface PageResponse<T> {
  content: T[];
  totalElements: number;
  totalPages: number;
  size: number;
  number: number;
}
