import { Routes } from '@angular/router';

import { LoginComponent } from './features/auth/login.component/login.component';
import { RegisterComponent } from './features/auth/register.component/register.component';
import {MyListComponent} from './features/user/my-list.component/my-list.component';


export const routes: Routes = [
  {path : '', redirectTo: '/login', pathMatch: 'full'},
  {path: 'login', component: LoginComponent},
  {path: 'register', component : RegisterComponent},
  {path: 'my-list', component : MyListComponent}
];
