import { Component, OnInit, ChangeDetectorRef } from '@angular/core';
import { CommonModule, Location } from '@angular/common';
import { ActivatedRoute } from '@angular/router';
import { NavbarComponent } from '../../../shared/components/navbar.component/navbar.component';
import { AnimeService, Anime } from '../../../core/services/anime.service';

@Component({
  selector: 'app-anime-detail',
  imports: [CommonModule, NavbarComponent],
  templateUrl: './anime-detail.component.html',
  styleUrl: './anime-detail.component.css',
})
export class AnimeDetailComponent implements OnInit {
  anime: Anime | null = null;
  loading = true;
  error = '';

  constructor(private route : ActivatedRoute, private animeService : AnimeService, private location  :Location, private cdr : ChangeDetectorRef) {}

  ngOnInit() {
    const idParam = this.route.snapshot.paramMap.get('id');
    if (idParam) {
      const idA = Number(idParam);
      this.animeService.getAnimeById(idA).subscribe({
        next: (data) => {
          this.anime = data;
          this.loading = false;
          this.cdr.detectChanges();
        },
        error: (err) => {
          console.error(err);
          this.error = "Impossible de charger les détails de l'animé.";
          this.loading = false;
          this.cdr.detectChanges();
        }
      });
    } else {
      this.error = "Aucun identifiant d'animé fourni.";
      this.loading = false;
      this.cdr.detectChanges();
    }
  }

  goBack(): void {
    this.location.back();
  }
}
