import { ChangeDetectionStrategy, Component } from '@angular/core';

@Component({
  selector: 'mdd-william',
  standalone: true,
  template: `
    <h1>William</h1>
  `,
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class WilliamComponent {}


