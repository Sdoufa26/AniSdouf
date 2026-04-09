import { Component, ChangeDetectorRef } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { RouterModule } from '@angular/router';
import { NavbarComponent } from '../../../shared/components/navbar.component/navbar.component';
import { AnimeService, Anime, NoteAnimeRequest } from '../../../core/services/anime.service';

@Component({
  selector: 'app-home',
  imports: [CommonModule, FormsModule, NavbarComponent, RouterModule],
  templateUrl: './home.component.html',
  styleUrl: './home.component.css',
})
export class HomeComponent {
  recherche = '';
  animes: Anime[] = [];
  loading = false;
  error = '';
  modeSuggestion = true;

  constructor(private animeService: AnimeService, private cdr: ChangeDetectorRef) {
  }

  ngOnInit() {
    this.chargerSuggestions();
  }

  onSearch(): void {
    if (!this.recherche.trim()) {
      this.chargerSuggestions();
      return;
    }

    this.loading = true;
    this.modeSuggestion = false;
    this.error = '';

    this.animeService.rechercherAnime(this.recherche).subscribe({
      next: (data) => {
        this.animes = data;
        this.loading = false;
        if (this.animes.length == 0) {
          this.error = "Aucun animé trouvé pour cette recherche";
        }
        this.cdr.detectChanges();
      },
      error: (err) => {
        console.error(err);
        this.error = "Erreur lors de la recherche";
        this.loading = false;
        this.cdr.detectChanges();
      }
    });
  }

  onAddToList(idA: number): void {
    const request: NoteAnimeRequest = {
      idA: idA,
      statutA: 'A_VOIR',
      estFavori: false,
      episodesVus: 0
    };

    this.animeService.ajouterOuModifierNote(request).subscribe({
      next: () => alert('Animé ajouté à ta liste avec succès !'),
      error: (err) => alert('Erreur : ' + (err.error?.erreur || 'Impossible d\'ajouter l\'animé.'))
    });
  }

  chargerSuggestions(): void {
    this.loading = true;
    this.modeSuggestion = true;
    this.error = '';

    this.animeService.getSuggestions().subscribe({
      next: (data) => {
        this.animes = data;
        this.loading = false;
        this.cdr.detectChanges();
      },
      error: (err) => {
        console.error(err);
        this.error = "Erreur lors du chargement des suggestions";
        this.loading = false;
        this.cdr.detectChanges();
      }
    });
  }
}
