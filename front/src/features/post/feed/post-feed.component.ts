import { Component, inject, signal } from '@angular/core';
import { MatButton } from '@angular/material/button';
import { AsyncPipe, DatePipe, NgClass } from '@angular/common';
import { ActivatedRoute, Router, RouterLink } from '@angular/router';
import { switchMap } from 'rxjs';
import { PageQueryParams, SortDirection, UserService } from '../../../shared';
import { MatCard, MatCardContent, MatCardHeader, MatCardTitle } from '@angular/material/card';
import { MatProgressSpinner } from '@angular/material/progress-spinner';

@Component({
  selector: 'mdd-posts',
  standalone: true,
  imports: [
    MatButton,
    NgClass,
    AsyncPipe,
    MatCard,
    MatCardContent,
    MatCardHeader,
    MatCardTitle,
    DatePipe,
    MatProgressSpinner,
    RouterLink,
  ],
  templateUrl: './post-feed.component.html',
  styleUrl: './post-feed.component.scss',
})
export class PostFeedComponent {
  #userService = inject(UserService);
  isAcsSort = signal<SortDirection>(SortDirection.DESC);
  activeRoute = inject(ActivatedRoute);
  router = inject(Router);

  viewModel$ = this.activeRoute.queryParams.pipe(
    switchMap(params => {
      const postParams: PageQueryParams = {
        sort: params['sort'] ? params['sort'] : SortDirection.ASC,
        page: params['page'] ? params['page'] : 0,
        size: params['size'] ? params['size'] : 500,
      };
      return this.#userService.getUserFeed$(postParams);
    }),
  );

  changeSort() {
    this.isAcsSort.set(
      this.isAcsSort() === SortDirection.ASC ? SortDirection.DESC : SortDirection.ASC,
    );
    this.router.navigate([], { queryParams: { sort: this.isAcsSort() } });
  }

  protected readonly SortDirection = SortDirection;
}
