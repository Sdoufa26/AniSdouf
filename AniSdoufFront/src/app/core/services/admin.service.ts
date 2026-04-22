import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { UtilisateurResponse } from './auth.service';

@Injectable({ providedIn: 'root' })
export class AdminService {
  private readonly API_ADMIN = 'https://anisdouf.onrender.com/api/admin';

  constructor(private http: HttpClient) {}

  /** Récupère tous les utilisateurs (Route protégée ADMIN) */
  getUsers(): Observable<UtilisateurResponse[]> {
    return this.http.get<UtilisateurResponse[]>(`${this.API_ADMIN}/utilisateurs`);
  }
}
