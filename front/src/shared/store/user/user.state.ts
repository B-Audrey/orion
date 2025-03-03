import { inject, Injectable } from '@angular/core';
import { Action, Selector, State, StateContext } from '@ngxs/store';
import * as UserActions from './user.actions';
import { catchError, tap } from 'rxjs/operators';
import { EMPTY, Observable, switchMap } from 'rxjs';
import { Router } from '@angular/router';
import { User } from '../../interfaces/user';
import { AuthService } from '../../services/auth.service';
import { ToastService } from '../../services/toast.service';
import { UserService } from '../../services/user.service';
import { HttpErrorResponse } from '@angular/common/http';

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
  readonly #userService = inject(UserService);
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

  @Selector()
  static getTopics(state: UserStateModel): string[] {
    return state.user?.topics?.map(topic => topic.uuid) || [];
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

  @Action(UserActions.Update)
  update$(ctx: StateContext<UserStateModel>, { user }: UserActions.Update) {
    if (!ctx.getState().user) {
      return this.#router.navigate(['/auth/login']);
    } else {
      return this.#userService
        .putUser$({
          ...user,
          uuid: ctx.getState().user?.uuid,
        })
        .pipe(
          catchError((e: HttpErrorResponse) => {
            if (e.status === 403) {
              this.#toastService.error(
                "Vous ne pouvez pas utiliser des informations déjà existantes, veuillez en choisir d'autres",
              );
            } else {
              this.#toastService.error('Erreur lors de la mise à jour de votre profil');
            }
            return EMPTY;
          }),
          tap(user => {
            ctx.patchState({ user });
            this.#toastService.success('Votre profil a été mis à jour');
          }),
        );
    }
  }

  @Action(UserActions.ChangePassword)
  patchPassword$(
    ctx: StateContext<UserStateModel>,
    { actualPassword, newPassword }: UserActions.ChangePassword,
  ) {
    return this.#userService
      .patchPassword$(ctx.getState().user?.uuid || '', {
        actualPassword,
        newPassword,
      })
      .pipe(
        catchError(() => {
          this.#toastService.error('Erreur lors de la mise à jour de votre mot de passe');
          return EMPTY;
        }),
        tap(() => {
          this.#toastService.success('Votre mot de passe a été mis à jour');
        }),
      );
  }

  @Action(UserActions.AddTopic)
  addTopic$(ctx: StateContext<UserStateModel>, { topicUuid }: UserActions.AddTopic) {
    return this.#userService.addTopic$(ctx.getState().user?.uuid || '', topicUuid).pipe(
      catchError(() => {
        this.#toastService.error("Erreur lors de l'ajout du thème");
        return EMPTY;
      }),
      tap(user => {
        ctx.patchState({ user });
        this.#toastService.success('Le thème vous a bien été ajouté');
      }),
    );
  }

  @Action(UserActions.RemoveTopic)
  removeTopic$(ctx: StateContext<UserStateModel>, { topicUuid }: UserActions.RemoveTopic) {
    return this.#userService.removeTopic$(ctx.getState().user?.uuid || '', topicUuid).pipe(
      catchError(() => {
        this.#toastService.error('Erreur lors de la suppression du thème');
        return EMPTY;
      }),
      tap(user => {
        ctx.patchState({ user });
        this.#toastService.success('Le thème vous a bien été supprimé');
      }),
    );
  }
}
