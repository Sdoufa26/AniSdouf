import {Component, OnInit} from '@angular/core';
import { NavbarComponent } from '../../../shared/components/navbar.component/navbar.component';
import {AuthService} from '../../../core/services/auth.service';

@Component({
  selector: 'app-my-list',
  imports: [NavbarComponent],
  templateUrl: './my-list.component.html',
  styleUrl: './my-list.component.css',
})
export class MyListComponent implements OnInit {
  pseudo = '';

  constructor(private authService : AuthService) {}

  ngOnInit(): void {
    this.authService.getProfile().subscribe({
      next: (user) => {
        this.pseudo = user.pseudo;
      },
        error: (err) => console.error("Erreur lors de la récupération du profil", err)
    });
  }
}
