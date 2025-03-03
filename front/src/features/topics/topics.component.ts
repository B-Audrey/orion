import { ChangeDetectionStrategy, Component, inject } from '@angular/core';
import { TopicService, TopicCardComponent } from '../../shared';
import { AsyncPipe } from '@angular/common';

@Component({
  selector: 'mdd-topics',
  standalone: true,
  imports: [AsyncPipe, TopicCardComponent],
  templateUrl: './topics.component.html',
  styleUrl: './topics.component.scss',
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class TopicsComponent {
  #topicService = inject(TopicService);

  viewModel$ = this.#topicService.getTopics$();
}
