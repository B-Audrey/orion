import { ChangeDetectionStrategy, Component, inject } from '@angular/core';
import { AsyncPipe, CurrencyPipe, NgForOf } from '@angular/common';
import {
  MatCard,
  MatCardHeader,
  MatCardTitle,
  MatCardContent,
  MatCardActions,
} from '@angular/material/card';
import { MatButton } from '@angular/material/button';
import { CardService } from '../../shared/services/card.service';
import { Observable } from 'rxjs';
import { CardItem } from '../../shared/interfaces';

@Component({
  selector: 'mdd-cards-list',
  standalone: true,
  imports: [
    NgForOf,
    MatCard,
    MatCardHeader,
    MatCardTitle,
    MatCardContent,
    MatCardActions,
    MatButton,
    AsyncPipe,
    CurrencyPipe,
  ],
  template: `
    <section class="cards-grid">
      <mat-card *ngFor="let item of cards$ | async" appearance="outlined">
        <img mat-card-image [src]="item.imageUrl" [alt]="item.title" />
        <mat-card-header>
          <mat-card-title>{{ item.title }}</mat-card-title>
        </mat-card-header>
        <mat-card-content>
          <p>{{ item.description }}</p>
          <p>
            <strong>{{ item.price | currency: 'EUR' }}</strong>
          </p>
        </mat-card-content>
        <mat-card-actions>
          <a mat-stroked-button color="primary" [href]="item.linkUrl" target="_blank" rel="noopener"
            >Voir</a
          >
        </mat-card-actions>
      </mat-card>
    </section>
  `,
  styles: [
    `
      .cards-grid {
        display: grid;
        grid-template-columns: repeat(auto-fill, minmax(260px, 1fr));
        gap: 16px;
        padding: 16px;
      }
      mat-card img {
        object-fit: cover;
        height: 160px;
      }
    `,
  ],
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class CardsListComponent {
  #cardService = inject(CardService);
  cards$: Observable<CardItem[]> = this.#cardService.getCards$();
}
