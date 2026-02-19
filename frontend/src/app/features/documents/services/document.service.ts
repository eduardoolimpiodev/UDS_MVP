import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../../../environments/environment';
import {
  Document,
  CreateDocumentRequest,
  UpdateDocumentRequest,
  PageResponse,
  DocumentStatus
} from '../../../core/models/document.model';
import { DocumentVersion } from '../../../core/models/version.model';

@Injectable({
  providedIn: 'root'
})
export class DocumentService {
  private http = inject(HttpClient);
  private apiUrl = `${environment.apiUrl}/documents`;

  getDocuments(
    page: number = 0,
    size: number = 10,
    status?: DocumentStatus,
    title?: string
  ): Observable<PageResponse<Document>> {
    let params = new HttpParams()
      .set('page', page.toString())
      .set('size', size.toString())
      .set('sort', 'createdAt,desc');

    if (status) {
      params = params.set('status', status);
    }
    if (title) {
      params = params.set('title', title);
    }

    return this.http.get<PageResponse<Document>>(this.apiUrl, { params });
  }

  getDocument(id: number): Observable<Document> {
    return this.http.get<Document>(`${this.apiUrl}/${id}`);
  }

  createDocument(request: CreateDocumentRequest): Observable<Document> {
    return this.http.post<Document>(this.apiUrl, request);
  }

  updateDocument(id: number, request: UpdateDocumentRequest): Observable<Document> {
    return this.http.put<Document>(`${this.apiUrl}/${id}`, request);
  }

  publishDocument(id: number): Observable<Document> {
    return this.http.patch<Document>(`${this.apiUrl}/${id}/publish`, {});
  }

  archiveDocument(id: number): Observable<Document> {
    return this.http.patch<Document>(`${this.apiUrl}/${id}/archive`, {});
  }

  deleteDocument(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${id}`);
  }

  getVersions(documentId: number): Observable<DocumentVersion[]> {
    return this.http.get<DocumentVersion[]>(`${this.apiUrl}/${documentId}/versions`);
  }

  uploadVersion(documentId: number, file: File): Observable<DocumentVersion> {
    const formData = new FormData();
    formData.append('file', file);
    return this.http.post<DocumentVersion>(`${this.apiUrl}/${documentId}/versions`, formData);
  }

  downloadVersion(documentId: number, versionId: number): Observable<Blob> {
    return this.http.get(`${this.apiUrl}/${documentId}/versions/${versionId}/download`, {
      responseType: 'blob'
    });
  }
}
