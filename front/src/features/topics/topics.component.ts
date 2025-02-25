import { ChangeDetectionStrategy, Component, inject } from '@angular/core';
import { TopicService } from '../../shared/services/topics.service';
import { AsyncPipe } from '@angular/common';
import {
  MatCard,
  MatCardActions,
  MatCardContent,
  MatCardHeader,
  MatCardTitle,
} from '@angular/material/card';
import { MatButton } from '@angular/material/button';

@Component({
  selector: 'mdd-topics',
  standalone: true,
  imports: [
    AsyncPipe,
    MatCard,
    MatCardHeader,
    MatCardContent,
    MatButton,
    MatCardActions,
    MatCardTitle,
  ],
  templateUrl: './topics.component.html',
  styleUrl: './topics.component.scss',
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class TopicsComponent {
  #topicService = inject(TopicService);

  viewModel$ = this.#topicService.getTopics$();
}
