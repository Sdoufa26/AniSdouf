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

  // Filtres Top 3
  selectedGenre: string = 'Global';
  topGenres: string[] = ['Global'];
  dynamicTop3: NoteAnimeResponse[] = [];

  // Tableau de données brutes
  fullList: NoteAnimeResponse[] = [];
  rankedAnimes: NoteAnimeResponse[] = [];

  // Nouveaux KPI analytiques
  averageNote: string = '0.0';
  favoriteGenre: string = '-';
  completionRate: string = '0';

  // Variables pour la pagination
  currentPage: number = 1;
  itemsPerPage: number = 10;
  paginatedAnimes: NoteAnimeResponse[] = [];
  totalPages: number = 1;

  constructor(private authService: AuthService, private animeService: AnimeService, private cdr: ChangeDetectorRef) {}

  ngOnInit() {
    this.authService.getProfile().subscribe({
      next: (data) => {
        this.profile = data;
        this.loading = false;
        this.cdr.detectChanges();
        this.totalPages = Math.ceil(this.rankedAnimes.length / this.itemsPerPage);
        this.updatePagination();
      },
      error: () => {
        this.loading = false;
        this.cdr.detectChanges();
      }
    });

    this.animeService.getMaListe().subscribe({
      next: (animes) => {
        this.fullList = animes;

        // 1. Calcul des statistiques et KPI
        const ratedAnimes = animes.filter(a => a.noteA != null);

        if (ratedAnimes.length > 0) {
          const sum = ratedAnimes.reduce((acc, curr) => acc + curr.noteA!, 0);
          this.averageNote = (sum / ratedAnimes.length).toFixed(1);
        }

        const termines = animes.filter(a => a.statutA === 'TERMINEE').length;
        if (animes.length > 0) {
          this.completionRate = Math.round((termines / animes.length) * 100).toString();
        }

        // 2. Calcul des genres (pour les KPI et les filtres)
        const genreCounts: { [key: string]: number } = {};
        const motsExclus = ['Inconnu', 'Unknown', 'Award Winning', 'Kids', 'Gag Humor', 'School', 'Action', 'Comedy', 'Drama', 'Fantasy', 'Adventure'];

        animes.forEach(anime => {
          if (anime.genre && anime.genre !== 'Inconnu') {
            anime.genre.split(',').forEach(g => {
              const genreClean = g.trim();
              if (!motsExclus.includes(genreClean)) {
                genreCounts[genreClean] = (genreCounts[genreClean] || 0) + 1;
              }
            });
          }
        });

        const sortedGenres = Object.keys(genreCounts).sort((a, b) => genreCounts[b] - genreCounts[a]);
        this.favoriteGenre = sortedGenres.length > 0 ? sortedGenres[0] : '-';
        this.topGenres = ['Global', ...sortedGenres.slice(0, 4)];

        // 3. Initialisation du Top 3
        this.updateTop3('Global');

        // 4. Création du tableau de classement (Data Table)
        this.rankedAnimes = [...ratedAnimes].sort((a, b) => (b.noteA || 0) - (a.noteA || 0));

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

    this.dynamicTop3 = animesNotes.sort((a, b) => (b.noteA || 0) - (a.noteA || 0)).slice(0, 3);
    this.cdr.detectChanges();
  }

  // Utilitaire pour nettoyer le texte des genres dans le tableau
  getPrimaryGenre(genres: string | undefined): string {
    if (!genres) return '-';
    return genres.split(',')[0].trim();
  }

  updatePagination() {
    const startIndex = (this.currentPage - 1) * this.itemsPerPage;
    const endIndex = startIndex + this.itemsPerPage;
    this.paginatedAnimes = this.rankedAnimes.slice(startIndex, endIndex);
  }

  changePage(delta: number) {
    const newPage = this.currentPage + delta;
    if (newPage >= 1 && newPage <= this.totalPages) {
      this.currentPage = newPage;
      this.updatePagination();
    }
  }
}
