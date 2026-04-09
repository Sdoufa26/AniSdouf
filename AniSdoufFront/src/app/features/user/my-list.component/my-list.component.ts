import { Component, OnInit, ChangeDetectorRef } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { RouterModule } from '@angular/router';
import { NavbarComponent } from '../../../shared/components/navbar.component/navbar.component';
import { AuthService } from '../../../core/services/auth.service';
import { AnimeService, NoteAnimeResponse, EpisodeResponse, NoteEpisodeRequest, NoteAnimeRequest } from '../../../core/services/anime.service';
import { forkJoin } from 'rxjs';

@Component({
  selector: 'app-my-list',
  imports: [NavbarComponent, CommonModule, FormsModule, RouterModule],
  templateUrl: './my-list.component.html',
  styleUrl: './my-list.component.css',
})
export class MyListComponent implements OnInit {
  pseudo = '';
  loading = true;
  listeTotale: NoteAnimeResponse[] =  [];
  listeAffichee: NoteAnimeResponse[] = [];
  filtreStatut: string = 'TOUS';
  filtreFavoris: string = 'TOUS';
  triActuel: string = 'TITRE_ASC';
  animeDeroule: NoteAnimeResponse | null = null;
  episodes: EpisodeResponse[] = [];
  loadingEpisodes = false;
  episodesModifies: { [idE: number]: NoteEpisodeRequest } = {};
  isSaving = false;

  constructor(private authService : AuthService, private animeService : AnimeService, private cdr : ChangeDetectorRef) {}

  ngOnInit(): void {
    this.authService.getProfile().subscribe({
      next: (user) => {
        this.pseudo = user.pseudo;
        this.cdr.detectChanges();
      },
        error: (err) => console.error("Erreur lors de la récupération du profil", err)
    });
    this.animeService.getMaListe().subscribe({
      next: (data) => {
        this.listeTotale = data;
        this.appliquerFiltresEtTri();
        this.loading = false;
        this.cdr.detectChanges();
      },
      error: (err) => {
        console.error("Erreur de liste", err);
        this.loading = false;
        this.cdr.detectChanges();
      }
    });
  }

  appliquerFiltresEtTri(): void {
    // 1. On repart de la liste complète
    let resultat = [...this.listeTotale];

    // 2. On applique le filtre par statut
    if (this.filtreStatut !== 'TOUS') {
      resultat = resultat.filter(note => note.statutA === this.filtreStatut);
    }

    // 2. Filtre Favoris
    if (this.filtreFavoris === 'FAVORIS') {
      resultat = resultat.filter(note => note.estFavori === true);
    }

    // 3. On applique le tri
    resultat.sort((a, b) => {
      if (this.triActuel === 'TITRE_ASC') {
        return a.titreA.localeCompare(b.titreA); // Ordre alphabétique
      } else if (this.triActuel === 'NOTE_DESC') {
        // Gère le cas où l'animé n'a pas encore de note (remplace undefined par 0)
        return (b.noteA || 0) - (a.noteA || 0);
      }
      return 0;
    });

    // 4. On met à jour l'écran
    this.listeAffichee = resultat;
    this.cdr.detectChanges();
  }

  toggleFavoriAnime(note: NoteAnimeResponse, event: Event): void {
    event.stopPropagation(); // Empêche le clic de déclencher d'autres actions
    note.estFavori = !note.estFavori; // Mise à jour visuelle instantanée

    const request: NoteAnimeRequest = {
      idA: note.idA,
      statutA: note.statutA,
      estFavori: note.estFavori,
      episodesVus: note.episodesVus,
      noteA: note.noteA
    };

    this.animeService.ajouterOuModifierNote(request).subscribe({
      error: (err) => {
        console.error("Erreur sauvegarde favori", err);
        note.estFavori = !note.estFavori; // Annulation en cas d'erreur serveur
        this.cdr.detectChanges();
      }
    });
  }

  ouvrirModalEpisodes(anime: NoteAnimeResponse): void {
    this.animeDeroule = anime;
    this.loadingEpisodes = true;
    this.episodesModifies = {}; // On vide les modifications précédentes
    this.cdr.detectChanges();

    this.animeService.getEpisodes(anime.idA).subscribe({
      next: (data) => {
        this.episodes = data;
        this.loadingEpisodes = false;
        this.cdr.detectChanges();
      },
      error: (err) => {
        console.error("Erreur épisodes", err);
        this.loadingEpisodes = false;
        this.cdr.detectChanges();
      }
    });
  }

  fermerModal(): void {
    this.animeDeroule = null;
    this.episodes = [];
    this.episodesModifies = {};
    this.cdr.detectChanges();
  }

  modifierEpisode(ep: EpisodeResponse, isChecked: boolean, noteString: string): void {
    const noteValue = noteString ? parseInt(noteString, 10) : undefined;

    this.episodesModifies[ep.idE] = {
      idE: ep.idE,
      idA: this.animeDeroule!.idA,
      statutE: isChecked ? 'TERMINEE' : 'A_VOIR',
      estFavori: ep.estFavori || false, // Récupère l'état de l'étoile
      noteE: noteValue
    };
  }

  enregistrerNotes(): void {
    const requetes = Object.values(this.episodesModifies);

    // Si l'utilisateur n'a rien touché, on ferme juste la modale
    if (requetes.length === 0) {
      this.fermerModal();
      return;
    }

    this.isSaving = true;

    let nouveauTotalVus = 0;
    for (const ep of this.episodes) {
      // Si l'utilisateur a modifié l'épisode, on regarde son nouveau statut
      if (this.episodesModifies[ep.idE]) {
        if (this.episodesModifies[ep.idE].statutE === 'TERMINEE') nouveauTotalVus++;
      }
      // Sinon, on garde son ancien statut de la base de données
      else {
        if (ep.estVu) nouveauTotalVus++;
      }
    }

    let nouveauStatut: 'A_VOIR' | 'EN_COURS' | 'TERMINEE' = this.animeDeroule!.statutA;

    if (nouveauTotalVus === 0) {
      nouveauStatut = 'A_VOIR'; // 0 épisode = À voir
    } else if (this.animeDeroule!.nbEpisodes && nouveauTotalVus === this.animeDeroule!.nbEpisodes) {
      nouveauStatut = 'TERMINEE'; // Tous les épisodes vus = Terminé
    } else {
      nouveauStatut = 'EN_COURS'; // Entre 1 et la fin = En cours
    }

    const updateAnimeRequest: NoteAnimeRequest = {
      idA: this.animeDeroule!.idA,
      noteA: this.animeDeroule!.noteA,
      statutA: nouveauStatut,
      estFavori: this.animeDeroule!.estFavori,
      episodesVus: nouveauTotalVus
    };

    // 3. ON RASSEMBLE TOUTES LES REQUÊTES
    // On met les requêtes des épisodes...
    const appelsBackend = requetes.map(req => this.animeService.ajouterOuModifierNoteEpisode(req));

    // ... ET on ajoute la requête de l'animé !
    appelsBackend.push(this.animeService.ajouterOuModifierNote(updateAnimeRequest));
    // forkJoin attend que TOUTES les requêtes soient terminées
    forkJoin(appelsBackend).subscribe({
      next: () => {
        this.isSaving = false;
        this.fermerModal();
        this.ngOnInit();
      },
      error: (err) => {
        console.error("Erreur lors de la sauvegarde", err);
        this.isSaving = false;
      }
    });
  }
}
