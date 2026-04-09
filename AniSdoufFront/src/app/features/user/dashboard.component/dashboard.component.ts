import { Component, OnInit, ChangeDetectorRef } from '@angular/core';
import { CommonModule } from '@angular/common';
import { NavbarComponent } from '../../../shared/components/navbar.component/navbar.component';
import { AuthService, UtilisateurResponse } from '../../../core/services/auth.service';

@Component({
  selector: 'app-dashboard',
  imports: [CommonModule, NavbarComponent],
  templateUrl: './dashboard.component.html',
  styleUrl: './dashboard.component.css',
})
export class DashboardComponent implements OnInit {
  profile: UtilisateurResponse | null = null;
  loading = true;

  constructor(private authService : AuthService, private cdr : ChangeDetectorRef) {
  }

  ngOnInit() {
    this.authService.getProfile().subscribe({
      next: (data) => {
        this.profile = data;
        this.loading = false;
        this.cdr.detectChanges();
      },
      error: () => {
        this.loading = false;
        this.cdr.detectChanges();
      }
    });
  }
}
