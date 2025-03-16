import { inject, Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Post } from '../interfaces/post';
import { Page, PageQueryParams } from '../interfaces';
import { Observable } from 'rxjs';
import { parsePaginationParams } from '../utils';

@Injectable({
  providedIn: 'root',
})
export class PostService {
  private readonly http = inject(HttpClient);

  getPosts$(params: PageQueryParams): Observable<Page<Post>> {
    return this.http.get<Page<Post>>(`/api/posts/my-feed${parsePaginationParams(params)}`);
  }
}
