import { inject, Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Comment, Post } from '../interfaces/post';

@Injectable({
  providedIn: 'root',
})
export class PostService {
  private readonly http = inject(HttpClient);
  #url = '/api/posts';

  getPost$(uuid: string) {
    return this.http.get<Post>(`${this.#url}/${uuid}`);
  }

  postComment$(postUuid: string, comment: Omit<Comment, 'user'>) {
    return this.http.post<Post>(`${this.#url}/${postUuid}/comment`, comment);
  }
}
