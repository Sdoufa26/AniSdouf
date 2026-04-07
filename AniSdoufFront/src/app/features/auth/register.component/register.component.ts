import { Component } from '@angular/core';
import {FormsModule, ReactiveFormsModule} from '@angular/forms';
import {Router, RouterLink} from '@angular/router';
import {AuthService} from '../../../core/services/auth.service';

@Component({
  selector: 'app-register.component',
  imports: [
    FormsModule,
    ReactiveFormsModule,
    RouterLink
  ],
  templateUrl: './register.component.html',
  styleUrl: './register.component.css',
})
export class RegisterComponent {
  pseudo = '';
  mail = '';
  mdp = '';
  confirmMdp = '';
  loading = false;
  errorMessage = '';

  public constructor(private authService : AuthService, private router : Router) {}

  onSubmit() : void {
    if (!this.pseudo || !this.mail || !this.mdp) {
      this.errorMessage = 'Veuillez remplir tous les champs';
      return;
    }
    if (this.mdp !== this.confirmMdp) {
      this.errorMessage = 'Les mots de passe ne correspondent pas';
      return;
    }

    this.loading = true;
    this.errorMessage = '';

    this.authService.register({
      mail: this.mail,
      pseudo: this.pseudo,
      mdp: this.mdp
    }).subscribe({
      next: () => this.router.navigate(['/login']),
      error: (err) => {
        this.loading = false;
        this.errorMessage = err.error?.erreur ?? 'Une erreur est survenue.';
      }
    });
  }
}
