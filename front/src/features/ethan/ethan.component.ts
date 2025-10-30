import { ChangeDetectionStrategy, Component } from '@angular/core';
import { CardsListComponent } from '../cards/cards-list.component';

@Component({
  selector: 'mdd-ethan',
  standalone: true,
  imports: [CardsListComponent],
  template: `
    <div class="bg-cover" [style.backgroundImage]="'url(assets/img/fond.jpg)'"></div>
    <h1>Ethan</h1>
    <mdd-cards-list />
  `,
  styles: [
    `
      :host {
        display: block;
        min-height: calc(100vh - 60px);
        position: relative;
      }
      .bg-cover {
        position: fixed;
        inset: 0;
        background-size: cover;
        background-position: center;
        background-repeat: no-repeat;
        z-index: -1;
      }
    `,
  ],
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class EthanComponent {}
