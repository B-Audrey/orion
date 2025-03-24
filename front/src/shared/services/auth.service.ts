import { inject, Injectable } from '@angular/core';
import { HttpBackend, HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { User } from '../interfaces';

@Injectable({ providedIn: 'root' })
export class AuthService {
  private readonly httpWithoutInterceptor: HttpClient;
  private readonly http = inject(HttpClient);

  constructor(httpBackend: HttpBackend) {
    this.httpWithoutInterceptor = new HttpClient(httpBackend);
  }

  refresh$(): Observable<{ token: string }> {
    return this.httpWithoutInterceptor //
      .get<{ token: string }>('/api/auth/refresh');
  }

  getMe$(): Observable<User> {
    return this.http.get<User>('/api/auth/me');
  }

  logout$() {
    return this.http.get('/api/auth/logout');
  }

  login$(username: string, password: string): Observable<{ accessToken: string }> {
    return this.httpWithoutInterceptor.post<{ accessToken: string }>('/api/auth/login', {
      username,
      password,
    });
  }
}
