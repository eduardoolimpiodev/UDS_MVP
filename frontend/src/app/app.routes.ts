import { Routes } from '@angular/router';
import { authGuard } from './core/guards/auth.guard';

export const routes: Routes = [
  {
    path: '',
    redirectTo: 'documents',
    pathMatch: 'full'
  },
  {
    path: 'login',
    loadComponent: () => import('./features/auth/pages/login/login.component').then(m => m.LoginComponent)
  },
  {
    path: 'documents',
    canActivate: [authGuard],
    loadComponent: () => import('./features/documents/pages/document-list/document-list.component').then(m => m.DocumentListComponent)
  },
  {
    path: 'documents/:id',
    canActivate: [authGuard],
    loadComponent: () => import('./features/documents/pages/document-detail/document-detail.component').then(m => m.DocumentDetailComponent)
  },
  {
    path: '**',
    redirectTo: 'documents'
  }
];
