import { ChangeDetectionStrategy, Component, inject } from '@angular/core';
import { TopicService, TopicCardComponent } from '../../shared';
import { AsyncPipe } from '@angular/common';
import { MatProgressSpinner } from '@angular/material/progress-spinner';

@Component({
  selector: 'mdd-topics',
  standalone: true,
  imports: [AsyncPipe, TopicCardComponent, MatProgressSpinner],
  templateUrl: './topics.component.html',
  styleUrl: './topics.component.scss',
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class TopicsComponent {
  #topicService = inject(TopicService);

  viewModel$ = this.#topicService.getTopics$();
}
