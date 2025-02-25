import { inject, Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Page } from '../interfaces/page';
import { Topic } from '../interfaces/topic';

@Injectable({ providedIn: 'root' })
export class TopicService {
  private readonly http = inject(HttpClient);

  getTopics$() {
    return this.http.get<Page<Topic>>('/api/topics');
  }
}
