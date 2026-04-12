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

  selectedGenre: string = 'Global';
  topGenres: string[] = ['Global']; // Contiendra 'Global' + tes 4 meilleurs genres
  dynamicTop3: NoteAnimeResponse[] = [];
  fullList: NoteAnimeResponse[] = [];

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
        this.fullList = animes;

        // --- CALCUL DES GENRES PRÉFÉRÉS ---
        const genreCounts: { [key: string]: number } = {};
        const motsExclus = [
          'Inconnu', 'Unknown', 'Award Winning', 'Kids', 'Gag Humor',
          'School', // Présent partout
          'Action', 'Comedy', 'Drama', 'Fantasy', 'Adventure' // Les "Big 5" qui écrasent tout
        ];
        animes.forEach(anime => {
          if (anime.genre && anime.genre !== 'Inconnu') {
            // On sépare les genres (ex: "Action, Drama") et on les compte
            anime.genre.split(',').forEach(g => {
              const genreClean = g.trim();
              if (!motsExclus.includes(genreClean)) {
                genreCounts[genreClean] = (genreCounts[genreClean] || 0) + 1;
              }
            });
          }
        });
        // On prend les 4 genres les plus présents dans ta liste
        const mostWatchedGenres = Object.keys(genreCounts)
          .sort((a, b) => genreCounts[b] - genreCounts[a])
          .slice(0, 4);

        this.topGenres = ['Global', ...mostWatchedGenres];

        // On initialise le Top 3 avec 'Global'
        this.updateTop3('Global');

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

  updateTop3(genre: string) {
    this.selectedGenre = genre;
    let animesNotes = this.fullList.filter(a => a.noteA != null);

    if (genre !== 'Global') {
      animesNotes = animesNotes.filter(a => a.genre && a.genre.includes(genre));
    }

    // Trie par note décroissante et prend les 3 premiers
    this.dynamicTop3 = animesNotes.sort((a, b) => (b.noteA || 0) - (a.noteA || 0)).slice(0, 3);
    this.cdr.detectChanges();
  }
}
