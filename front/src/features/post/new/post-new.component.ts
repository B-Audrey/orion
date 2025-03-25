import { Component, DestroyRef, inject } from '@angular/core';
import { Router, RouterLink } from '@angular/router';
import { MatFormField, MatLabel } from '@angular/material/form-field';
import { MatInput } from '@angular/material/input';
import { ToastService, Topic, TopicService } from '../../../shared';
import { AsyncPipe } from '@angular/common';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import {
  MatAutocomplete,
  MatAutocompleteSelectedEvent,
  MatAutocompleteTrigger,
  MatOption,
} from '@angular/material/autocomplete';
import { debounceTime, EMPTY, map, startWith, switchMap } from 'rxjs';
import { MatButton } from '@angular/material/button';
import { takeUntilDestroyed } from '@angular/core/rxjs-interop';
import { PostService } from '../../../shared/services/post.service';
import { PostCreation } from '../../../shared/interfaces/post';

@Component({
  selector: 'mdd-post-new',
  standalone: true,
  imports: [
    RouterLink,
    MatFormField,
    MatInput,
    MatLabel,
    AsyncPipe,
    ReactiveFormsModule,
    MatAutocompleteTrigger,
    MatAutocomplete,
    MatOption,
    MatButton,
  ],
  templateUrl: './post-new.component.html',
  styleUrl: './post-new.component.scss',
})
export class PostNewComponent {
  readonly #topicService = inject(TopicService);
  readonly #toastService = inject(ToastService);
  readonly #destroyRef = inject(DestroyRef);
  readonly #fb = inject(FormBuilder);
  readonly #postService = inject(PostService);
  readonly #router = inject(Router);

  form = this.#fb.nonNullable.group({
    topic: ['' as string | Topic, Validators.required],
    title: ['', [Validators.required, Validators.minLength(2)]],
    content: ['', [Validators.required, Validators.minLength(2)]],
  });

  topicsSearch$ = this.form.controls.topic.valueChanges.pipe(
    takeUntilDestroyed(this.#destroyRef),
    debounceTime(300),
    startWith(''),
    switchMap(() => {
      if (typeof this.form.controls.topic.value === 'string') {
        return this.#topicService.getTopics$({ search: this.form.controls.topic.value });
      } else return EMPTY;
    }),
    map(topics => topics.content),
  );

  submitForm() {
    if (typeof this.form.controls.topic.value !== 'string') {
      const postCreation: PostCreation = {
        title: this.form.controls.title.value,
        content: this.form.controls.content.value,
        topicUuid: this.form.controls.topic.value.uuid,
      };
      this.#postService.postNewPost$(postCreation).subscribe({
        next: () => {
          this.#toastService.success('Post créé avec succès');
          this.#router.navigate(['posts']);
        },
        error: () => {
          this.#toastService.error('Une erreur est survenue lors de la création du post');
        },
      });
    } else {
      this.#toastService.error("le topic renseigné n'est pas valide");
    }
  }

  displayFn(topic: Topic) {
    return topic.label || '';
  }

  selectedTopic($event: MatAutocompleteSelectedEvent) {
    this.form.patchValue($event.option.value);
  }
}
