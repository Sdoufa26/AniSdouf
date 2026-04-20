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

  // Tableau de données brutes
  fullList: NoteAnimeResponse[] = [];
  rankedAnimes: NoteAnimeResponse[] = [];

  // Nouvelles variables pour les statistiques de genres et filtres
  genreStats: { genre: string, count: number }[] = [];
  availableGenres: string[] = ['Tous'];
  tableFilterGenre: string = 'Tous';

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
      },
      error: () => {
        this.loading = false;
        this.cdr.detectChanges();
      }
    });

    this.animeService.getMaListe().subscribe({
      next: (animes) => {
        this.fullList = animes;

        // 1. Calcul des genres (pour l'histogramme, les cards et le filtre)
        const genreCounts: { [key: string]: number } = {};
        const motsExclus = ['Inconnu', 'Unknown', 'Award Winning', 'Kids', 'Gag Humor', 'School', 'Action', 'Comedy', 'Drama', 'Fantasy', 'Adventure'];

        animes.forEach(anime => {
          if (anime.genre && anime.genre !== 'Inconnu') {
            anime.genre.split(',').forEach(g => {
              const clean = g.trim();
              if (!motsExclus.includes(clean)) {
                genreCounts[clean] = (genreCounts[clean] || 0) + 1;
              }
            });
          }
        });

        // Transformation en tableau trié du plus regardé au moins regardé
        this.genreStats = Object.keys(genreCounts)
          .map(key => ({ genre: key, count: genreCounts[key] }))
          .sort((a, b) => b.count - a.count);

        this.availableGenres = ['Tous', ...this.genreStats.map(g => g.genre)];

        // 2. Création du tableau global
        const ratedAnimes = animes.filter(a => a.noteA != null);
        this.rankedAnimes = [...ratedAnimes].sort((a, b) => (b.noteA || 0) - (a.noteA || 0));

        // 3. Initialisation correcte de la pagination et du filtre
        this.filterTable();
        this.cdr.detectChanges();

        // 4. Création du tableau de classement (Data Table)
        this.rankedAnimes = [...ratedAnimes].sort((a, b) => (b.noteA || 0) - (a.noteA || 0));

        this.cdr.detectChanges();
      },
      error: (err) => console.error("Erreur chargement liste", err)
    });
  }

  // Utilitaire pour nettoyer le texte des genres dans le tableau
  getPrimaryGenre(genres: string | undefined): string {
    if (!genres) return '-';
    return genres.split(',')[0].trim();
  }

  onFilterChange(event: any) {
    this.tableFilterGenre = event.target.value;
    this.filterTable();
  }

  filterTable() {
    let filtered = this.rankedAnimes;
    if (this.tableFilterGenre !== 'Tous') {
      filtered = this.rankedAnimes.filter(a => a.genre && a.genre.includes(this.tableFilterGenre));
    }

    this.totalPages = Math.ceil(filtered.length / this.itemsPerPage) || 1;
    this.currentPage = 1; // On revient à la page 1 quand on filtre

    const startIndex = 0;
    const endIndex = this.itemsPerPage;
    this.paginatedAnimes = filtered.slice(startIndex, endIndex);
  }

  changePage(delta: number) {
    const newPage = this.currentPage + delta;
    if (newPage >= 1 && newPage <= this.totalPages) {
      this.currentPage = newPage;

      // On recalcule la liste filtrée pour la page demandée
      let filtered = this.rankedAnimes;
      if (this.tableFilterGenre !== 'Tous') {
        filtered = this.rankedAnimes.filter(a => a.genre && a.genre.includes(this.tableFilterGenre));
      }

      const startIndex = (this.currentPage - 1) * this.itemsPerPage;
      this.paginatedAnimes = filtered.slice(startIndex, startIndex + this.itemsPerPage);
    }
  }
}
