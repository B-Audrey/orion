import { inject, Injectable } from '@angular/core';
import { Action, Selector, State, StateContext } from '@ngxs/store';
import * as UserActions from './user.actions';
import { catchError, tap } from 'rxjs/operators';
import { Observable, switchMap } from 'rxjs';
import { Router } from '@angular/router';
import { User } from '../../interfaces/user';
import { AuthService } from '../../services/auth.service';
import { ToastService } from '../../services/toast.service';

export interface UserStateModel {
  user?: User;
  accessToken?: string;
}

@State<UserStateModel>({
  name: 'user',
  defaults: {},
})
@Injectable()
export class UserState {
  readonly #authService = inject(AuthService);
  readonly #router = inject(Router);
  readonly #toastService = inject(ToastService);

  @Selector()
  static getAccessToken(state: UserStateModel): string | undefined {
    return state?.accessToken || undefined;
  }

  @Selector()
  static getMe(state: UserStateModel): User | undefined {
    return state?.user?.uuid ? state.user : undefined;
  }

  @Action(UserActions.Me)
  me$(ctx: StateContext<UserStateModel>): Observable<unknown> {
    return this.#authService.getMe$().pipe(tap((user: User) => ctx.patchState({ user })));
  }

  @Action(UserActions.Refresh)
  refresh$(ctx: StateContext<UserStateModel>): Observable<unknown> {
    return this.#authService.refresh$().pipe(
      tap(({ token }) => ctx.patchState({ accessToken: token })),
      catchError(() => {
        return this.#router.navigate(['/auth/login']);
      }),
    );
  }

  @Action(UserActions.Login)
  login$(ctx: StateContext<UserStateModel>, { username, password }: UserActions.Login) {
    return this.#authService
      .login$(username, password)
      .pipe(tap(({ accessToken }) => ctx.patchState({ accessToken })));
  }

  @Action(UserActions.Logout)
  logout$(ctx: StateContext<UserStateModel>) {
    return this.#authService.logout$().pipe(
      switchMap(() => {
        ctx.patchState({
          user: null,
          accessToken: null,
        } as unknown as UserStateModel);
        this.#toastService.success('Vous êtes déconnecté');
        return this.#router.navigate(['/auth/login']);
      }),
    );
  }
}
