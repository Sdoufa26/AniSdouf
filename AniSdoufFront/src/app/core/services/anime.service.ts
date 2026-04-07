import { Injectable } from '@angular/core';
import {HttpClient, HttpParams} from '@angular/common/http';
import { Observable } from 'rxjs';

export interface Anime {
  idA: number;
  titreA: string;
  description?: string;
  genre: string;
  nbEpisodes: number;
  image: string;
}

export interface NoteAnimeRequest {
  idA: number;
  noteA?: number;
  statutA: 'A_VOIR' | 'EN_COURS' | 'TERMINEE';
  estFavori: boolean;
  episodesVus: number;
}

@Injectable({
  providedIn: 'root',
})
export class AnimeService {
  private readonly API_ANIMES = 'http://localhost:8080/api/animes';
  private readonly API_NOTES = 'http://localhost:8080/api/notes-anime';

  constructor(private httpClient : HttpClient) {}

  rechercherAnime(titreA: string) : Observable<Anime[]> {
    const params = new HttpParams().set('titre', titreA);
    return this.httpClient.get<Anime[]>(`${this.API_ANIMES}/recherche`, {params});
  }

  ajouterOuModifierNote(noteAnimeRequest : NoteAnimeRequest) : Observable<any> {
    return this.httpClient.post(this.API_NOTES, noteAnimeRequest)
  }

  getSuggestions(): Observable<Anime[]> {
    return this.httpClient.get<Anime[]>(`${this.API_ANIMES}/suggestions`);
  }
}
