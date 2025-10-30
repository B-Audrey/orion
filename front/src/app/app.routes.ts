import { Route } from '@angular/router';
import { LayoutComponent } from './layout/layout.component';
import { ErrorComponent } from '../features/error/error.component';
import { UserProfileComponent } from '../features/user-profile/user-profile.component';
import { HomeComponent } from '../features/home/home.component';
import { EthanComponent } from '../features/ethan/ethan.component';
import { WilliamComponent } from '../features/william/william.component';

export const routes: Route[] = [
  {
    path: '',
    component: LayoutComponent,
    children: [
      {
        path: '',
        pathMatch: 'full',
        redirectTo: 'home',
      },
      {
        path: 'home',
        component: HomeComponent,
      },
      {
        path: 'ethan',
        component: EthanComponent,
      },
      {
        path: 'william',
        component: WilliamComponent,
      },
      {
        path: 'my-profile',
        component: UserProfileComponent,
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
