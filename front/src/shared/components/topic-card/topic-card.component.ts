import { ChangeDetectionStrategy, Component, inject, input } from '@angular/core';
import {
  MatCard,
  MatCardActions,
  MatCardContent,
  MatCardHeader,
  MatCardTitle,
} from '@angular/material/card';
import { MatButton } from '@angular/material/button';
import { Store } from '@ngxs/store';
import { Topic } from '../../interfaces';
import { UserState, UserActions } from '../../store/user';
import { AsyncPipe } from '@angular/common';
import { MatProgressSpinner } from '@angular/material/progress-spinner';

@Component({
  selector: 'mdd-topic-card',
  standalone: true,
  imports: [
    MatCard,
    MatCardHeader,
    MatCardContent,
    MatButton,
    MatCardActions,
    MatCardTitle,
    AsyncPipe,
    MatProgressSpinner,
  ],
  templateUrl: './topic-card.component.html',
  styleUrl: './topic-card.component.scss',
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class TopicCardComponent {
  #store = inject(Store);
  topic = input<Topic>({} as Topic);
  isRemoveTopicsDisabled = input<boolean>();

  userTopics$ = this.#store.select(UserState.getTopics);

  removeTopic(uuid: string) {
    this.#store.dispatch(new UserActions.RemoveTopic(uuid));
  }

  addTopic(uuid: string) {
    this.#store.dispatch(new UserActions.AddTopic(uuid));
  }
}
