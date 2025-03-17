import { Routes } from '@angular/router';
import { PostComponent } from './post.component';
import { PostFeedComponent } from './feed/post-feed.component';
import { PostDetailsComponent } from './details/post-details.component';
import { PostNewComponent } from './new/post-new.component';

const routes: Routes = [
  {
    path: '',
    component: PostComponent,
    children: [
      {
        path: '',
        pathMatch: 'full',
        redirectTo: 'feed',
      },
      {
        path: 'feed',
        component: PostFeedComponent,
      },
      {
        path: 'new-post',
        component: PostNewComponent,
      },
      {
        path: ':uuid',
        component: PostDetailsComponent,
      },
    ],
  },
];

export default routes;
