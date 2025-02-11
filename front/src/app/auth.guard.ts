import { inject } from '@angular/core';
import { CanActivateFn, Router } from '@angular/router';
import { Store } from '@ngxs/store';
import { map, of, switchMap } from 'rxjs';
import { UserActions, UserState } from '../shared/store';

export const authGuard: CanActivateFn = () => {
  const store: Store = inject(Store);
  const router: Router = inject(Router);
  const accessToken = store.selectSnapshot(UserState.getAccessToken);

  const accessToken$ = accessToken
    ? of(accessToken)
    : store.dispatch(new UserActions.Refresh()).pipe(map(() => store.selectSnapshot(UserState.getAccessToken)));

  return accessToken$.pipe(
    switchMap(() => store.dispatch(new UserActions.Me())),
    map(() => {
      const user = store.selectSnapshot(UserState.getMe);
      if (user?.uuid) {
        return true;
      } else {
        return router.createUrlTree(['/auth/login']);
      }
    }),
  );
};
