import { Routes } from '@angular/router';

import { LoginComponent } from './features/auth/login.component/login.component';
import { RegisterComponent } from './features/auth/register.component/register.component';
import { MyListComponent } from './features/user/my-list.component/my-list.component';
import { HomeComponent } from './features/anime/home.component/home.component';
import { ProfileComponent } from './features/user/profile.component/profile.component';
import {authGuard, guestGuard} from './core/services/auth.guard';


export const routes: Routes = [
  // Routes publiques
  {path: 'login', component: LoginComponent, canActivate: [guestGuard]},
  {path: 'register', component : RegisterComponent, canActivate: [guestGuard]},

  // Routes privées
  {path: '', component : HomeComponent, canActivate: [authGuard]},
  {path: 'my-list', component : MyListComponent, canActivate: [authGuard]},
  {path: 'profile', component: ProfileComponent, canActivate: [authGuard]},

  // Route par défaut
  {path: '**', redirectTo: 'login'}
];
