import { Component, OnInit, ChangeDetectorRef} from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router } from '@angular/router';
import { NavbarComponent } from '../../../shared/components/navbar.component/navbar.component';
import { AuthService, UtilisateurResponse } from '../../../core/services/auth.service';

@Component({
  selector: 'app-profile',
  imports: [CommonModule, NavbarComponent],
  templateUrl: './profile.component.html',
  styleUrl: './profile.component.css',
})
export class ProfileComponent implements OnInit{
  profile : UtilisateurResponse | null = null;
  loading = true;
  availableAvatars = [
    '/avatars/avatar1.png',
    '/avatars/avatar2.png',
    '/avatars/avatar3.png',
    '/avatars/avatar4.png',
    '/avatars/avatar5.png'
  ];
  showAvatarSelector = false;

  constructor(private authService : AuthService, private router: Router, private cdr : ChangeDetectorRef) {
  }

  ngOnInit(): void {
    this.authService.getProfile().subscribe({
      next: (data) => {
        this.profile = data;
        // Si aucun avatar n'est en base, on lui en donne un par défaut
        if (this.profile) {
          if (!this.profile.avatar || this.profile.avatar === 'avatar1.png') {
            this.profile.avatar = this.availableAvatars[0];
          }
        }
        this.loading = false;
        this.cdr.detectChanges();
      },
      error: () => {
        this.loading = false
        this.cdr.detectChanges();
      }
    });
  }

  toggleAvatarSelector(): void {
    this.showAvatarSelector = !this.showAvatarSelector;
    this.cdr.detectChanges();
  }

  selectAvatar(avatarUrl: string): void {
    if (!this.profile) return;

    // Mise à jour visuelle instantanée
    this.profile.avatar = avatarUrl;
    this.showAvatarSelector = false;
    this.cdr.detectChanges();

    // Envoi au backend
    this.authService.updateAvatar(avatarUrl).subscribe({
      error: (err) => console.error("Erreur de sauvegarde de l'avatar", err)
    });
  }
}
