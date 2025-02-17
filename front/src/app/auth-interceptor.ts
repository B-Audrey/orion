import { inject } from '@angular/core';
import {
  HttpErrorResponse,
  HttpEvent,
  HttpHandlerFn,
  HttpInterceptorFn,
  HttpRequest,
  HttpStatusCode,
} from '@angular/common/http';
import { EMPTY, first, Observable, switchMap, throwError } from 'rxjs';
import { catchError } from 'rxjs/operators';
import { Store } from '@ngxs/store';
import { Router } from '@angular/router';
import { ToastService } from '../shared/services/toast.service';
import { UserState, UserActions } from '../shared/store';

export const authInterceptor: HttpInterceptorFn = (
  req: HttpRequest<unknown>,
  next: HttpHandlerFn,
) => {
  const store = inject(Store);
  const router = inject(Router);
  const toastService = inject(ToastService);

  return next(_addBearer(req)) //
    .pipe(catchError(err => _errorHandler(err, req, next)));

  function _addBearer(req: HttpRequest<unknown>): HttpRequest<unknown> {
    const token = store.selectSnapshot(UserState.getAccessToken);
    const headers = req.headers.append('Authorization', `Bearer ${token}`);
    return token ? req.clone({ headers }) : req;
  }

  function _errorHandler(
    err: HttpErrorResponse,
    request: HttpRequest<unknown>,
    next: HttpHandlerFn,
  ): Observable<HttpEvent<unknown>> {
    switch (err.status) {
      case HttpStatusCode.Unauthorized:
        return _refreshAndRetry(request, next);
      case HttpStatusCode.InternalServerError:
        toastService.error('Oups, erreur 500');
        return throwError(() => err);
      case HttpStatusCode.GatewayTimeout:
        toastService.error('Le service ne répond pas');
        return throwError(() => err);
      default:
        return throwError(() => err);
    }
  }

  function _refreshAndRetry(
    request: HttpRequest<unknown>,
    next: HttpHandlerFn,
  ): Observable<HttpEvent<unknown>> {
    return store.dispatch(new UserActions.Refresh()).pipe(
      first(),
      switchMap(() => next(_addBearer(request))),
      catchError(err => {
        const error = err as HttpErrorResponse;
        if (error.status === HttpStatusCode.Unauthorized) {
          router.navigate(['/auth/login']);
          return EMPTY;
        }
        throw err;
      }),
    );
  }
};
