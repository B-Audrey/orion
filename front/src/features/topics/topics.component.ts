import { ChangeDetectionStrategy, Component } from '@angular/core';

@Component({
  selector: 'mdd-topics',
  standalone: true,
  imports: [],
  templateUrl: './topics.component.html',
  styleUrl: './topics.component.scss',
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class TopicsComponent {}
