import { inject, Injectable } from '@angular/core';
import { HttpBackend, HttpClient, HttpErrorResponse } from '@angular/common/http';
import { Observable, of, throwError } from 'rxjs';
import { map, switchMap } from 'rxjs/operators';
import { Store } from '@ngxs/store';
import { User } from '../interfaces';
import { UserState } from '../store/user/user.state';

@Injectable({ providedIn: 'root' })
export class AuthService {
  private readonly httpWithoutInterceptor: HttpClient;
  private readonly http = inject(HttpClient);
  private readonly store = inject(Store);

  constructor(httpBackend: HttpBackend) {
    this.httpWithoutInterceptor = new HttpClient(httpBackend);
  }

  refresh$(): Observable<{ token: string }> {
    const currentToken = this.store.selectSnapshot(UserState.getAccessToken);
    return currentToken
      ? of({ token: currentToken })
      : throwError(() => new HttpErrorResponse({ status: 401, statusText: 'Unauthorized' }));
  }

  getMe$(): Observable<User> {
    const token = this.store.selectSnapshot(UserState.getAccessToken);
    if (!token || !token.startsWith('mock-')) {
      return throwError(() => new HttpErrorResponse({ status: 401, statusText: 'Unauthorized' }));
    }
    const userUuid = token.substring('mock-'.length);
    const users = this.#readLocalUsers();
    if (users) {
      return of(users).pipe(
        map(u => u.find(x => x.uuid === userUuid)),
        switchMap(user =>
          user
            ? of(user)
            : throwError(
                () => new HttpErrorResponse({ status: 404, statusText: 'User not found' }),
              ),
        ),
      );
    }
    return this.http.get<User[]>('assets/mock/users.json').pipe(
      map(users => users.find(u => u.uuid === userUuid)),
      switchMap(user =>
        user
          ? of(user)
          : throwError(() => new HttpErrorResponse({ status: 404, statusText: 'User not found' })),
      ),
    );
  }

  logout$() {
    return of(true);
  }

  login$(username: string, password: string): Observable<{ accessToken: string }> {
    const localUsers = this.#readLocalUsers();
    if (localUsers) {
      return of(localUsers).pipe(
        map(users =>
          users.find(u => (u.email === username || u.name === username) && u.password === password),
        ),
        switchMap(user =>
          user
            ? of({ accessToken: `mock-${user.uuid}` })
            : throwError(() => new HttpErrorResponse({ status: 401, statusText: 'Unauthorized' })),
        ),
      );
    }
    return this.httpWithoutInterceptor.get<User[]>('assets/mock/users.json').pipe(
      map(users =>
        users.find(u => (u.email === username || u.name === username) && u.password === password),
      ),
      switchMap(user =>
        user
          ? of({ accessToken: `mock-${user.uuid}` })
          : throwError(() => new HttpErrorResponse({ status: 401, statusText: 'Unauthorized' })),
      ),
    );
  }

  #readLocalUsers(): User[] | null {
    try {
      const raw = localStorage.getItem('mockUsers');
      if (raw) {
        return JSON.parse(raw) as User[];
      }
    } catch {
      // ignore JSON/localStorage errors in PoC mode
    }
    return null;
  }
}
