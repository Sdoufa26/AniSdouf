import { Component, OnInit, ChangeDetectorRef } from '@angular/core';
import { CommonModule } from '@angular/common';
import { NavbarComponent } from '../../../shared/components/navbar.component/navbar.component';
import { AuthService, UtilisateurResponse } from '../../../core/services/auth.service';
import { AnimeService, NoteAnimeResponse } from '../../../core/services/anime.service';

@Component({
  selector: 'app-dashboard',
  imports: [CommonModule, NavbarComponent],
  templateUrl: './dashboard.component.html',
  styleUrl: './dashboard.component.css',
})
export class DashboardComponent implements OnInit {
  profile: UtilisateurResponse | null = null;
  loading = true;

  tierList: {
    GOAT: NoteAnimeResponse[];
    S: NoteAnimeResponse[];
    A: NoteAnimeResponse[];
    B: NoteAnimeResponse[];
    C: NoteAnimeResponse[];
    D: NoteAnimeResponse[];
  } = { GOAT: [], S: [], A: [], B: [], C: [], D: [] };

  constructor(private authService : AuthService, private animeService : AnimeService, private cdr : ChangeDetectorRef) {
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

    this.animeService.getMaListe().subscribe({
      next: (animes) => {
        animes.forEach(anime => {
          if (anime.noteA != null) {
            // Répartition selon les notes
            if (anime.noteA >= 9) this.tierList.GOAT.push(anime);
            else if (anime.noteA >= 8) this.tierList.S.push(anime);
            else if (anime.noteA >= 6) this.tierList.A.push(anime);
            else if (anime.noteA >= 4) this.tierList.B.push(anime);
            else if (anime.noteA >= 5) this.tierList.C.push(anime);
            else this.tierList.D.push(anime);
          }
        });

        // Optionnel : on trie chaque ligne pour que les meilleures notes soient au début
        Object.values(this.tierList).forEach(tier => {
          tier.sort((a, b) => (b.noteA || 0) - (a.noteA || 0));
        });

        this.cdr.detectChanges();
      },
      error: (err) => console.error("Erreur chargement liste", err)
    });
  }
}
