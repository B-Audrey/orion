import { inject, Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { CardItem } from '../interfaces';

@Injectable({ providedIn: 'root' })
export class CardService {
  readonly #http = inject(HttpClient);

  getCards$(): Observable<CardItem[]> {
    return this.#http.get<CardItem[]>('assets/mock/cards.json');
  }
}
