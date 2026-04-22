import { Component, signal, inject, OnInit } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import { AuthService } from './core/services/auth.service';
import { inject as injectAnalytics } from '@vercel/analytics';
import { injectSpeedInsights } from '@vercel/speed-insights';
import { HttpClient } from '@angular/common/http';
import { catchError, of } from 'rxjs';

@Component({
  selector: 'app-root',
  imports: [RouterOutlet],
  templateUrl: './app.html',
  styleUrl: './app.css'
})
export class App implements OnInit {
  protected readonly title = signal('AniSdoufFront');
  public authService = inject(AuthService);
  private http = inject(HttpClient)

  ngOnInit() {
    injectAnalytics();
    injectSpeedInsights();

    this.http.get('https://anisdouf.onrender.com', { responseType: 'text' })
      .pipe(
        catchError(() => of(null))
      )
      .subscribe();
  }
}
