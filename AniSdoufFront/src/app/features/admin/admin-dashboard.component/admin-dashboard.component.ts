import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { NavbarComponent } from '../../../shared/components/navbar.component/navbar.component';
import { AdminService } from '../../../core/services/admin.service';
import { UtilisateurResponse } from '../../../core/services/auth.service';

@Component({
  selector: 'app-admin-dashboard',
  standalone: true,
  imports: [CommonModule, FormsModule, NavbarComponent],
  templateUrl: './admin-dashboard.component.html',
  styleUrl: './admin-dashboard.component.css'
})
export class AdminDashboardComponent implements OnInit {
  users: UtilisateurResponse[] = [];
  filteredUsers: UtilisateurResponse[] = [];
  paginatedUsers: UtilisateurResponse[] = [];

  // Filtres et Recherche
  searchTerm: string = '';
  loading: boolean = true;

  // Pagination
  currentPage: number = 1;
  itemsPerPage: number = 8;
  totalPages: number = 1;

  constructor(private adminService: AdminService) {}

  ngOnInit() {
    this.adminService.getUsers().subscribe({
      next: (data) => {
        this.users = data;
        this.applyFilters();
        this.loading = false;
      },
      error: (err) => {
        console.error("Erreur Admin", err);
        this.loading = false;
      }
    });
  }

  applyFilters() {
    this.filteredUsers = this.users.filter(u =>
      u.pseudo.toLowerCase().includes(this.searchTerm.toLowerCase()) ||
      u.mail.toLowerCase().includes(this.searchTerm.toLowerCase())
    );
    this.totalPages = Math.ceil(this.filteredUsers.length / this.itemsPerPage) || 1;
    this.currentPage = 1;
    this.updatePagination();
  }

  updatePagination() {
    const start = (this.currentPage - 1) * this.itemsPerPage;
    this.paginatedUsers = this.filteredUsers.slice(start, start + this.itemsPerPage);
  }

  changePage(delta: number) {
    this.currentPage += delta;
    this.updatePagination();
  }
}
