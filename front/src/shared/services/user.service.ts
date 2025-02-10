import {inject, Injectable} from '@angular/core';
import {HttpBackend, HttpClient} from '@angular/common/http';
import {User} from '../interfaces/user';
import {Observable} from 'rxjs';


@Injectable({ providedIn: 'root' })
export class UserService {
  private readonly httpWithoutInterceptor: HttpClient;
  private readonly http = inject(HttpClient);

  constructor(httpBackend: HttpBackend) {
    this.httpWithoutInterceptor = new HttpClient(httpBackend);
  }

  signin$({name, email, password}: { name: string, email: string, password: string }): Observable<User> {
    return this.httpWithoutInterceptor.post<User>('/api/users/new', {
      name,
      email,
      password,
    });
  }
}
