import { inject, Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Comment, Post, PostCreation } from '../interfaces/post';

@Injectable({
  providedIn: 'root',
})
export class PostService {
  private readonly http = inject(HttpClient);
  readonly #url = '/api/posts';

  getPost$(uuid: string) {
    return this.http.get<Post>(`${this.#url}/${uuid}`);
  }

  postNewPost$(post: PostCreation) {
    return this.http.post<Post>(`${this.#url}/new`, post);
  }

  postComment$(postUuid: string, comment: Omit<Comment, 'user'>) {
    return this.http.post<Post>(`${this.#url}/${postUuid}/comment`, comment);
  }
}
