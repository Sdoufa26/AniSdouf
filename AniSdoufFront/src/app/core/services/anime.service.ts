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

export interface NoteAnimeResponse {
  idNa: number;
  idA: number;
  titreA: string;
  image: string;
  nbEpisodes: number;
  noteA?: number;
  statutA: 'A_VOIR' | 'EN_COURS' | 'TERMINEE';
  estFavori: boolean;
  episodesVus: number;
  genre: string;
}

export interface EpisodeResponse {
  idE: number;
  titreE: string;
  numero: number;
  estVu: boolean;
  noteE?: number;
  estFavori: boolean;
}

export interface NoteEpisodeRequest {
  idE: number;
  idA: number;
  noteE?: number;
  statutE: 'A_VOIR' | 'EN_COURS' | 'TERMINEE';
  estFavori: boolean;
}

@Injectable({
  providedIn: 'root',
})
export class AnimeService {
  private readonly API_ANIMES = 'http://localhost:8080/api/animes';
  private readonly API_NOTES = 'http://localhost:8080/api/notes-anime';
  private readonly API_NOTES_EPISODE = 'http://localhost:8080/api/notes-episode';

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

  getEpisodes(idA: number): Observable<EpisodeResponse[]> {
    return this.httpClient.get<EpisodeResponse[]>(`${this.API_ANIMES}/${idA}/episodes`);
  }

  ajouterOuModifierNoteEpisode(request: NoteEpisodeRequest): Observable<any> {
    return this.httpClient.post(this.API_NOTES_EPISODE, request);
  }

  getMaListe(): Observable<NoteAnimeResponse[]> {
    return this.httpClient.get<NoteAnimeResponse[]>(this.API_NOTES)
  }

  getAnimeById(idA: number): Observable<Anime> {
    return this.httpClient.get<Anime>(`${this.API_ANIMES}/${idA}`);
  }
}
