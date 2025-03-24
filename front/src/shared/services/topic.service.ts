import { inject, Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Page, PageQueryParams, Topic } from '../interfaces';

@Injectable({ providedIn: 'root' })
export class TopicService {
  readonly #http = inject(HttpClient);
  readonly #url = '/api/topics';

  getTopics$(params?: PageQueryParams) {
    return this.#http.get<Page<Topic>>(`${this.#url}`, { params: { ...params } });
  }
}
