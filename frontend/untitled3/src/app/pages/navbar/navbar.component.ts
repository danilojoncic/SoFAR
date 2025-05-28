import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';
import { AuthService } from '../../services/auth.service';

@Component({
  selector: 'app-navbar',
  standalone: true,
  imports: [CommonModule, RouterModule],
  template: `
    <nav class="navbar navbar-expand-lg navbar-dark bg-primary">
      <div class="container-fluid">
        <a class="navbar-brand" href="#">SoFar</a>
        <div class="collapse navbar-collapse">
          <ul class="navbar-nav me-auto mb-2 mb-lg-0">

            <li class="nav-item">
              <a
                class="nav-link"
                [ngClass]="{ 'disabled text-secondary': !hasPermission('CAN_VIEW') }"
                [routerLink]="'/users'"
                (click)="!hasPermission('CAN_VIEW') && $event.preventDefault()"
              >
                View Users
              </a>
            </li>
            <li class="nav-item">
              <a
                  class="nav-link"
                  [ngClass]="{ 'disabled text-secondary': !hasPermission('CAN_PLACE_ORDER') }"
                  [routerLink]="'/dishes'"
                  (click)="!hasPermission('CAN_PLACE_ORDER') && $event.preventDefault()"
              >
                See Menu
              </a>
            </li>
            <li class="nav-item">
              <a
                class="nav-link"
                [ngClass]="{ 'disabled text-secondary': !hasPermission('CAN_PLACE_ORDER') }"
                [routerLink]="'/orders'"
                (click)="!hasPermission('CAN_PLACE_ORDER') && $event.preventDefault()"
              >
                See Orders
              </a>
            </li>

            <li class="nav-item">
              <a
                class="nav-link"
                [ngClass]="{ 'disabled text-secondary': !hasPermission('CAN_TRACK_ORDER') }"
                [routerLink]="'/tracking'"
                (click)="!hasPermission('CAN_TRACK_ORDER') && $event.preventDefault()"
              >
                Track Orders
              </a>
            </li>

            <li class="nav-item">
              <a
                class="nav-link"
                [ngClass]="{ 'disabled text-secondary': !hasPermission('CAN_VIEW') }"
                [routerLink]="'/errors'"
                (click)="!hasPermission('CAN_VIEW') && $event.preventDefault()"
              >
                View Errors
              </a>
            </li>

          </ul>

          <button class="btn btn-outline-light ms-auto" (click)="logout()">Logout</button>
        </div>
      </div>
    </nav>
  `
})
export class NavbarComponent implements OnInit {
  userPermissions: string[] = [];

  constructor(private authService: AuthService) {}

  ngOnInit(): void {
    this.userPermissions = this.authService.getPermissions();
  }

  hasPermission(permission: string): boolean {
    return this.userPermissions.includes(permission);
  }

  logout(): void {
    this.authService.logout();
  }
}
