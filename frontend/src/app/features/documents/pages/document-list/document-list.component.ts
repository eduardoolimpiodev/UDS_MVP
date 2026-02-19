import { Component, OnInit, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router } from '@angular/router';
import { FormControl, ReactiveFormsModule } from '@angular/forms';
import { MatToolbarModule } from '@angular/material/toolbar';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatTableModule } from '@angular/material/table';
import { MatPaginatorModule, PageEvent } from '@angular/material/paginator';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatSelectModule } from '@angular/material/select';
import { MatChipsModule } from '@angular/material/chips';
import { debounceTime, distinctUntilChanged } from 'rxjs';
import { DocumentService } from '../../services/document.service';
import { AuthService } from '../../../../core/services/auth.service';
import { Document, DocumentStatus } from '../../../../core/models/document.model';

@Component({
  selector: 'app-document-list',
  standalone: true,
  imports: [
    CommonModule,
    ReactiveFormsModule,
    MatToolbarModule,
    MatButtonModule,
    MatIconModule,
    MatTableModule,
    MatPaginatorModule,
    MatFormFieldModule,
    MatInputModule,
    MatSelectModule,
    MatChipsModule
  ],
  templateUrl: './document-list.component.html',
  styleUrl: './document-list.component.scss'
})
export class DocumentListComponent implements OnInit {
  private documentService = inject(DocumentService);
  private authService = inject(AuthService);
  private router = inject(Router);

  documents: Document[] = [];
  displayedColumns = ['title', 'status', 'tags', 'owner', 'createdAt', 'actions'];
  
  totalElements = 0;
  pageSize = 10;
  pageIndex = 0;

  searchControl = new FormControl('');
  statusControl = new FormControl<DocumentStatus | ''>('');
  
  statuses: DocumentStatus[] = ['DRAFT', 'PUBLISHED', 'ARCHIVED'];
  currentUser = this.authService.getCurrentUser();

  ngOnInit(): void {
    this.loadDocuments();
    
    this.searchControl.valueChanges.pipe(
      debounceTime(300),
      distinctUntilChanged()
    ).subscribe(() => this.loadDocuments());

    this.statusControl.valueChanges.subscribe(() => this.loadDocuments());
  }

  loadDocuments(): void {
    const title = this.searchControl.value || undefined;
    const status = this.statusControl.value || undefined;

    this.documentService.getDocuments(
      this.pageIndex,
      this.pageSize,
      status as DocumentStatus,
      title
    ).subscribe({
      next: (response) => {
        this.documents = response.content;
        this.totalElements = response.totalElements;
      },
      error: (error) => console.error('Error loading documents:', error)
    });
  }

  onPageChange(event: PageEvent): void {
    this.pageIndex = event.pageIndex;
    this.pageSize = event.pageSize;
    this.loadDocuments();
  }

  viewDocument(id: number): void {
    this.router.navigate(['/documents', id]);
  }

  logout(): void {
    this.authService.logout();
  }

  getStatusColor(status: DocumentStatus): string {
    switch (status) {
      case 'DRAFT': return 'accent';
      case 'PUBLISHED': return 'primary';
      case 'ARCHIVED': return 'warn';
      default: return '';
    }
  }
}
