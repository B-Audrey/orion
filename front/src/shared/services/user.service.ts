import { inject, Injectable } from '@angular/core';
import { Observable, of } from 'rxjs';
import { User } from '../interfaces';
import { Store } from '@ngxs/store';
import { UserState } from '../store/user/user.state';

@Injectable({ providedIn: 'root' })
export class UserService {
  readonly #store = inject(Store);

  postUser$(user: User): Observable<User> {
    const uuid = crypto.randomUUID ? crypto.randomUUID() : `${Date.now()}`;
    return of({ ...user, uuid });
  }

  putUser$(user: User): Observable<User> {
    const current = this.#store.selectSnapshot(UserState.getMe);
    const merged: User = { ...current, ...user } as User;
    return of(merged);
  }
}
