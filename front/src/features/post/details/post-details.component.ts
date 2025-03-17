import { Component, inject, input, OnChanges, signal, SimpleChanges } from '@angular/core';
import { PostService } from '../../../shared/services/post.service';
import { BehaviorSubject, EMPTY, filter, finalize, switchMap } from 'rxjs';
import { AsyncPipe, DatePipe, JsonPipe } from '@angular/common';
import { catchError, tap } from 'rxjs/operators';
import { ToastService } from '../../../shared';
import { Router, RouterLink } from '@angular/router';
import { MatProgressSpinner } from '@angular/material/progress-spinner';

@Component({
  selector: 'mdd-post-details',
  standalone: true,
  imports: [AsyncPipe, JsonPipe, MatProgressSpinner, RouterLink, DatePipe],
  templateUrl: './post-details.component.html',
  styleUrl: './post-details.component.scss',
})
export class PostDetailsComponent implements OnChanges {
  uuid = input<string>();
  isLoading = signal(true);
  #postService = inject(PostService);
  #toastService = inject(ToastService);
  #router = inject(Router);

  readonly postSubject$ = new BehaviorSubject<string | undefined>(this.uuid());

  ngOnChanges(simpleChanges: SimpleChanges): void {
    if (this.uuid() && simpleChanges['uuid'].previousValue !== this.uuid()) {
      this.postSubject$.next(this.uuid());
    }
  }

  viewModel$ = this.postSubject$.pipe(
    filter(uuid => !!uuid),
    tap(() => this.isLoading.set(true)),
    switchMap(() => this.#postService.getPost$(this.uuid()!)),
    catchError(() => {
      this.#toastService.error(
        'Une erreur est survenue lors de la récupération du post, vous êtes redirigé sur la page principale',
      );
      this.#router.navigate(['/posts']);
      return EMPTY;
    }),
    finalize(() => this.isLoading.set(false)),
  );
}
