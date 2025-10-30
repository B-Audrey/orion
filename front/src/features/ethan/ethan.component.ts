import { ChangeDetectionStrategy, Component } from '@angular/core';

@Component({
  selector: 'mdd-ethan',
  standalone: true,
  template: `
    <h1>Ethan</h1>
  `,
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class EthanComponent {}


