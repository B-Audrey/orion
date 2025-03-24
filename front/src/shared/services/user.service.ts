import { inject, Injectable } from '@angular/core';
import { HttpBackend, HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Page, PageQueryParams, User } from '../interfaces';
import { Post } from '../interfaces/post';

@Injectable({ providedIn: 'root' })
export class UserService {
  readonly #httpWithoutInterceptor: HttpClient;
  readonly #http = inject(HttpClient);
  readonly #url = '/api/users';

  constructor(httpBackend: HttpBackend) {
    this.#httpWithoutInterceptor = new HttpClient(httpBackend);
  }

  postUser$(user: User): Observable<User> {
    return this.#httpWithoutInterceptor.post<User>(`${this.#url}/new`, user);
  }

  putUser$(user: User): Observable<User> {
    return this.#http.put<User>(`${this.#url}/${user.uuid}`, user);
  }

  patchPassword$(userUuid: string, body: { actualPassword: string; newPassword: string }) {
    return this.#http.patch(`${this.#url}/${userUuid}/password`, body);
  }

  addTopic$(userUuid: string, topicUuid: string) {
    return this.#http.get<User>(`${this.#url}/${userUuid}/topic-subscription/${topicUuid}`);
  }

  removeTopic$(userUuid: string, topicUuid: string) {
    return this.#http.get<User>(`${this.#url}/${userUuid}/topic-unsubscription/${topicUuid}`);
  }

  getUserFeed$(params: PageQueryParams): Observable<Page<Post>> {
    return this.#http.get<Page<Post>>(`${this.#url}/my-feed`, { params: { ...params } });
  }
}
