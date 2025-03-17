import { inject, Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Post } from '../interfaces/post';

@Injectable({
  providedIn: 'root',
})
export class PostService {
  private readonly http = inject(HttpClient);
  #url = '/api/posts';

  getPost$(uuid: string) {
    return this.http.get<Post>(`${this.#url}/${uuid}`);
  }
}
