import { Component, inject, input, OnChanges, signal, SimpleChanges } from '@angular/core';
import { PostService } from '../../../shared/services/post.service';
import { BehaviorSubject } from 'rxjs';
import { AsyncPipe, DatePipe } from '@angular/common';
import { ToastService } from '../../../shared';
import { Router, RouterLink } from '@angular/router';
import { MatProgressSpinner } from '@angular/material/progress-spinner';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { Comment, Post } from '../../../shared/interfaces/post';

@Component({
  selector: 'mdd-post-details',
  standalone: true,
  imports: [AsyncPipe, MatProgressSpinner, RouterLink, DatePipe, ReactiveFormsModule],
  templateUrl: './post-details.component.html',
  styleUrl: './post-details.component.scss',
})
export class PostDetailsComponent implements OnChanges {
  uuid = input<string>();
  isLoading = signal(true);
  #postService = inject(PostService);
  #toastService = inject(ToastService);
  #router = inject(Router);
  #fb = inject(FormBuilder);

  private readonly postSubject$ = new BehaviorSubject<Post | null>(null);
  viewModel$ = this.postSubject$.asObservable();

  form = this.#fb.group({
    content: ['', Validators.minLength(2)],
  });

  ngOnChanges(simpleChanges: SimpleChanges): void {
    if (this.uuid() && simpleChanges['uuid'].previousValue !== this.uuid()) {
      this.isLoading.set(true);
      this.#postService.getPost$(this.uuid()!).subscribe({
        next: (post: Post) => {
          this.postSubject$.next(post);
          this.isLoading.set(false);
        },
        error: () => {
          this.#toastService.error(
            'Une erreur est survenue lors de la récupération du post, redirection.',
          );
          this.#router.navigate(['/posts']);
        },
      });
    }
  }

  addComment(): void {
    const contentValue = this.form.value.content;
    if (!contentValue) {
      this.#toastService.error('Votre message ne peut pas être vide');
      return;
    }
    this.isLoading.set(true);
    const comment: Omit<Comment, 'user'> = { content: contentValue };
    this.#postService.postComment$(this.uuid()!, comment).subscribe({
      next: updatedPost => {
        this.postSubject$.next(updatedPost);
        this.form.reset();
      },
      error: () => {
        this.#toastService.error('Une erreur est survenue lors de l’envoi du commentaire.');
      },
      complete: () => this.isLoading.set(false),
    });
  }
}
