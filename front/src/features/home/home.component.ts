import { ChangeDetectionStrategy, Component } from '@angular/core';

@Component({
  selector: 'mdd-home',
  standalone: true,
  templateUrl: './home.component.html',
  styleUrl: './home.component.scss',
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class HomeComponent {}


