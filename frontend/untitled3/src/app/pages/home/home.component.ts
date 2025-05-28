import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { AuthService } from '../../services/auth.service';
import { RouterModule } from '@angular/router';
import {NavbarComponent} from '../navbar/navbar.component';
import {UserService} from '../../services/user.service';

@Component({
  selector: 'app-home',
  standalone: true,
  imports: [CommonModule, RouterModule, NavbarComponent],
  template: `
    <app-navbar></app-navbar>

    <div class="container mt-5">
      <h1 class="display-5 mb-4">Based on your permissions, you will be able to:</h1>
      <ul class="list-group">
        <li
          *ngFor="let perm of allPermissions"
          class="list-group-item d-flex justify-content-between align-items-center"
          [ngClass]="{
            'list-group-item-success': hasPermission(perm),
            'list-group-item-danger': !hasPermission(perm)
          }"
        >
          {{ perm }}
          <span>
            <i
              class="bi"
              [ngClass]="{
                'bi-check-circle-fill text-success': hasPermission(perm),
                'bi-x-circle-fill text-danger': !hasPermission(perm)
              }"
            ></i>
          </span>
        </li>
      </ul>
    </div>
  `
})
export class HomeComponent implements OnInit {
  userPermissions: string[] = [];
  allPermissions : string[] = [];
  constructor(private authService: AuthService,private userService: UserService) {}

  ngOnInit(): void {
    this.userPermissions = this.authService.getPermissions()
    this.userService.getAllPermissions().subscribe({
      next: (permissions) => {
        this.allPermissions = permissions;
      },
      error: (err) => {
        console.error('Failed to fetch permissions:', err);
      }
    });
  }

  hasPermission(permission: string): boolean {
    return this.userPermissions.includes(permission);
  }

  logout(): void {
    this.authService.logout();
  }
}
