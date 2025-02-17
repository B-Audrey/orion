import { Route } from '@angular/router';
import { LayoutComponent } from './layout/layout.component';
import { LoginComponent } from '../features/auth/login/login.component';
import { ErrorComponent } from '../features/error/error.component';
import { ArticlesComponent } from '../features/articles/articles.component';
import { authGuard } from './auth.guard';
import { AuthComponent } from '../features/auth/auth.component';
import { SigninComponent } from '../features/auth/signin/signin.component';

export const routes: Route[] = [
  {
    path: '',
    component: LayoutComponent,
    canActivate: [authGuard],
    children: [
      {
        path: '',
        pathMatch: 'full',
        redirectTo: 'home',
      },
      {
        path: 'home',
        component: ArticlesComponent,
      },
    ],
  },
  {
    path: 'auth',
    children: [
      {
        path: '',
        pathMatch: 'full',
        component: AuthComponent,
      },
      {
        path: 'login',
        component: LoginComponent,
      },
      {
        path: 'signin',
        component: SigninComponent,
      },
    ],
  },
  {
    path: 'errors',
    component: ErrorComponent,
  },
  {
    path: '**',
    component: ErrorComponent,
  },
];
