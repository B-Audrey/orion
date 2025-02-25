import { ChangeDetectionStrategy, Component, inject } from '@angular/core';
import { TopicService } from '../../shared/services/topic.service';
import { AsyncPipe } from '@angular/common';
import {
  MatCard,
  MatCardActions,
  MatCardContent,
  MatCardHeader,
  MatCardTitle,
} from '@angular/material/card';
import { MatButton } from '@angular/material/button';
import { Store } from '@ngxs/store';
import { UserState } from '../../shared';
import { map } from 'rxjs';
import { MatProgressSpinner } from '@angular/material/progress-spinner';
import { UserActions } from '../../shared';

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
    MatProgressSpinner,
  ],
  templateUrl: './topics.component.html',
  styleUrl: './topics.component.scss',
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class TopicsComponent {
  #topicService = inject(TopicService);
  #store = inject(Store);

  viewModel$ = this.#topicService.getTopics$();

  userTopics$ = this.#store
    .select(UserState.getMe)
    .pipe(map(user => user?.topics?.map(topic => topic.uuid)));

  removeTopic(uuid: string) {
    this.#store.dispatch(new UserActions.RemoveTopic(uuid));
  }

  addTopic(uuid: string) {
    this.#store.dispatch(new UserActions.AddTopic(uuid));
  }
}
