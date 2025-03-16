import { inject, Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Page, Topic } from '../interfaces';

@Injectable({ providedIn: 'root' })
export class TopicService {
  private readonly http = inject(HttpClient);

  getTopics$() {
    return this.http.get<Page<Topic>>('/api/topics');
  }
}
