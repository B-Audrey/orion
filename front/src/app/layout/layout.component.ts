import { Component } from '@angular/core';
import { RouterLink, RouterLinkActive, RouterOutlet } from '@angular/router';
import { MatIconButton } from '@angular/material/button';
import { MatSidenav, MatSidenavContainer, MatSidenavContent } from '@angular/material/sidenav';
import { MatNavList } from '@angular/material/list';
import { MatToolbar } from '@angular/material/toolbar';
import { NgClass, NgOptimizedImage } from '@angular/common';

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
    NgClass,
  ],
  templateUrl: './layout.component.html',
  styleUrl: './layout.component.scss',
})
export class LayoutComponent {}
