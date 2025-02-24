import { ChangeDetectionStrategy, Component, inject } from '@angular/core';
import { Store } from '@ngxs/store';
import { UserActions } from '../../shared/store/user';

@Component({
  selector: 'mdd-user-profile',
  standalone: true,
  imports: [],
  templateUrl: './user-profile.component.html',
  styleUrl: './user-profile.component.scss',
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class UserProfileComponent {
  #store = inject(Store);

  logout() {
    this.#store.dispatch(new UserActions.Logout());
  }
}
