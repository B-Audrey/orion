import { NgOptimizedImage } from '@angular/common';
import { Component } from '@angular/core';
import { MatIconButton } from '@angular/material/button';
import { MatNavList } from '@angular/material/list';
import { MatSidenav, MatSidenavContainer, MatSidenavContent } from '@angular/material/sidenav';
import { MatToolbar } from '@angular/material/toolbar';
import { RouterLink, RouterLinkActive, RouterOutlet } from '@angular/router';

@Component({
  selector: 'mdd-layout',
  standalone: true,
  imports: [
    RouterOutlet,
    MatNavList,
    MatSidenav,
    MatSidenavContent,
    MatSidenavContainer,
    RouterLinkActive,
    RouterLink,
    MatIconButton,
    MatToolbar,
    NgOptimizedImage,
  ],
  templateUrl: './layout.component.html',
  styleUrl: './layout.component.scss',
})
export class LayoutComponent {}
