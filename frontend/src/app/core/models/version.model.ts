export interface DocumentVersion {
  id: number;
  documentId: number;
  versionNumber: number;
  fileName: string;
  fileSize: number;
  contentType: string;
  uploadedAt: string;
  uploadedBy: string;
  isCurrent: boolean;
}
