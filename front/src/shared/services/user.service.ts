import { inject, Injectable } from '@angular/core';
import { HttpBackend, HttpClient } from '@angular/common/http';
import { User } from '../interfaces/user';
import { Observable } from 'rxjs';
import { Page, PageQueryParams } from '../interfaces';
import { Post } from '../interfaces/post';
import { parsePaginationParams } from '../utils';

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

  patchPassword$(userUuid: string, param: { actualPassword: string; newPassword: string }) {
    return this.http.patch(`/api/users/${userUuid}/password`, param);
  }

  addTopic$(userUuid: string, topicUuid: string) {
    return this.http.get<User>(`/api/users/${userUuid}/topic-subscription/${topicUuid}`);
  }

  removeTopic$(userUuid: string, topicUuid: string) {
    return this.http.get<User>(`/api/users/${userUuid}/topic-unsubscription/${topicUuid}`);
  }

  getUserFeed$(params: PageQueryParams): Observable<Page<Post>> {
    return this.http.get<Page<Post>>(`/api/users/my-feed${parsePaginationParams(params)}`);
  }
}
