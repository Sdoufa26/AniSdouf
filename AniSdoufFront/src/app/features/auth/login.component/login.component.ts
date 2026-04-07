import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router, RouterLink } from '@angular/router';
import { AuthService } from '../../../core/services/auth.service';

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterLink],
  templateUrl: './login.component.html',
  styleUrl: './login.component.css'
})
export class LoginComponent {
  mail = '';
  mdp = '';
  loading = false;
  errorMessage = '';

  constructor(private authService: AuthService, private router: Router) {}

  onSubmit(): void {
    if (!this.mail || !this.mdp) {
      this.errorMessage = 'Veuillez remplir tous les champs.';
      return;
    }
    this.loading = true;
    this.errorMessage = '';

    this.authService.login({ mail: this.mail, mdp: this.mdp }).subscribe({
      next: () => {
        this.authService.getProfile().subscribe({
          next: () => this.router.navigate(['/']),
          error: () => this.router.navigate(['/'])
        });
      },
      error: (err) => {
        this.loading = false;
        this.errorMessage = err.error?.erreur ?? 'Email ou mot de passe incorrect.';
      }
    });
  }
}
