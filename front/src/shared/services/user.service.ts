import { inject, Injectable } from '@angular/core';
import { HttpBackend, HttpClient } from '@angular/common/http';
import { User } from '../interfaces/user';
import { Observable } from 'rxjs';

@Injectable({ providedIn: 'root' })
export class UserService {
  private readonly httpWithoutInterceptor: HttpClient;
  private readonly http = inject(HttpClient);

  constructor(httpBackend: HttpBackend) {
    this.httpWithoutInterceptor = new HttpClient(httpBackend);
  }

  postUser$(user: User): Observable<User> {
    return this.httpWithoutInterceptor.post<User>('/api/users/new', user);
  }

  putUser$(user: User): Observable<User> {
    return this.http.put<User>(`/api/users/${user.uuid}`, user);
  }
}
