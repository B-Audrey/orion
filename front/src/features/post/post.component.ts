import { Component } from '@angular/core';
import { RouterOutlet } from '@angular/router';

@Component({
  selector: 'mdd-post',
  template: '<router-outlet />',
  standalone: true,
  imports: [RouterOutlet],
})
export class PostComponent {}
