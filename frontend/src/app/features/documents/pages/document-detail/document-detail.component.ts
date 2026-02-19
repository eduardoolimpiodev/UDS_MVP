import { Component, OnInit, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ActivatedRoute, Router } from '@angular/router';
import { MatCardModule } from '@angular/material/card';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatChipsModule } from '@angular/material/chips';
import { MatTableModule } from '@angular/material/table';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { DocumentService } from '../../services/document.service';
import { Document } from '../../../../core/models/document.model';
import { DocumentVersion } from '../../../../core/models/version.model';

@Component({
  selector: 'app-document-detail',
  standalone: true,
  imports: [
    CommonModule,
    MatCardModule,
    MatButtonModule,
    MatIconModule,
    MatChipsModule,
    MatTableModule,
    MatProgressSpinnerModule
  ],
  templateUrl: './document-detail.component.html',
  styleUrl: './document-detail.component.scss'
})
export class DocumentDetailComponent implements OnInit {
  private route = inject(ActivatedRoute);
  private router = inject(Router);
  private documentService = inject(DocumentService);

  document?: Document;
  versions: DocumentVersion[] = [];
  displayedColumns = ['versionNumber', 'fileName', 'fileSize', 'uploadedAt', 'uploadedBy', 'actions'];
  loading = false;
  uploading = false;

  ngOnInit(): void {
    const id = Number(this.route.snapshot.paramMap.get('id'));
    this.loadDocument(id);
    this.loadVersions(id);
  }

  loadDocument(id: number): void {
    this.documentService.getDocument(id).subscribe({
      next: (doc) => this.document = doc,
      error: (error) => console.error('Error loading document:', error)
    });
  }

  loadVersions(id: number): void {
    this.documentService.getVersions(id).subscribe({
      next: (versions) => this.versions = versions,
      error: (error) => console.error('Error loading versions:', error)
    });
  }

  onFileSelected(event: Event): void {
    const input = event.target as HTMLInputElement;
    if (input.files && input.files.length > 0 && this.document) {
      const file = input.files[0];
      this.uploading = true;

      this.documentService.uploadVersion(this.document.id, file).subscribe({
        next: () => {
          this.uploading = false;
          this.loadVersions(this.document!.id);
          input.value = '';
        },
        error: (error) => {
          this.uploading = false;
          console.error('Error uploading file:', error);
        }
      });
    }
  }

  downloadVersion(versionId: number, fileName: string): void {
    if (!this.document) return;

    this.documentService.downloadVersion(this.document.id, versionId).subscribe({
      next: (blob) => {
        const url = window.URL.createObjectURL(blob);
        const link = document.createElement('a');
        link.href = url;
        link.download = fileName;
        link.click();
        window.URL.revokeObjectURL(url);
      },
      error: (error) => console.error('Error downloading file:', error)
    });
  }

  goBack(): void {
    this.router.navigate(['/documents']);
  }

  formatFileSize(bytes: number): string {
    if (bytes === 0) return '0 Bytes';
    const k = 1024;
    const sizes = ['Bytes', 'KB', 'MB', 'GB'];
    const i = Math.floor(Math.log(bytes) / Math.log(k));
    return Math.round(bytes / Math.pow(k, i) * 100) / 100 + ' ' + sizes[i];
  }
}
