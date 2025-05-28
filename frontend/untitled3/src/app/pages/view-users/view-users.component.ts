// view-users.component.ts
import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import {UserService, UserPresentation, UserOperation} from '../../services/user.service';
import { HttpClientModule } from '@angular/common/http';
import {NavbarComponent} from '../navbar/navbar.component';
import {AuthService} from '../../services/auth.service';
import { UserFormComponent } from '../user-form/user-form.component';


@Component({
  selector: 'app-view-users',
  standalone: true,
  imports: [CommonModule, HttpClientModule,NavbarComponent,UserFormComponent],
  template: `
    <app-navbar></app-navbar>

    <div class="container mt-4">
      <h2>All Users</h2>

      <table class="table table-striped table-bordered mt-3">
        <thead class="table-dark">
        <tr>
          <th>ID</th>
          <th>Email</th>
          <th>First Name</th>
          <th>Last Name</th>
          <th>Permissions</th>
          <th>Actions</th>
        </tr>
        </thead>
        <tbody>
        <tr *ngFor="let user of users">
          <td>{{ user.id }}</td>
          <td>{{ user.email }}</td>
          <td>{{ user.firstName }}</td>
          <td>{{ user.lastName }}</td>
          <td>
            <ng-container *ngFor="let perm of user.permissions">
              <span class="badge bg-secondary me-1">{{ perm }}</span>
            </ng-container>
          </td>
          <td>
            <div class="d-flex gap-2">
              <button
                *ngIf="authService.hasPermission('CAN_EDIT')"
                class="btn btn-sm btn-warning flex-grow-1"
                (click)="editUser(user)"
              >Edit</button>
              <button
                *ngIf="authService.hasPermission('CAN_DELETE')"
                class="btn btn-sm btn-danger flex-grow-1"
                (click)="confirmDelete(user)"
              >Delete</button>

            </div>
          </td>
        </tr>
        </tbody>
      </table>

      <nav>
        <ul class="pagination justify-content-center">
          <li class="page-item" [class.disabled]="currentPage === 0">
            <button class="page-link" (click)="loadPage(currentPage - 1)">Previous</button>
          </li>

          <li
            class="page-item"
            *ngFor="let page of [].constructor(totalPages); let i = index"
            [class.active]="i === currentPage"
          >
            <button class="page-link" (click)="loadPage(i)">{{ i + 1 }}</button>
          </li>

          <li class="page-item" [class.disabled]="currentPage === totalPages - 1">
            <button class="page-link" (click)="loadPage(currentPage + 1)">Next</button>
          </li>
        </ul>
      </nav>
      <button
        *ngIf="authService.hasPermission('CAN_CREATE')"
        class="btn btn-primary rounded-circle position-fixed bottom-0 end-0 m-4 d-flex align-items-center justify-content-center"
        style="width: 8rem; height: 8rem; font-size: 2rem;"
        (click)="showUserFormModal = true"
      >
        +
      </button>
      <app-user-form
        *ngIf="showUserFormModal"
        [initialUser]="editingUserData"
        (save)="onUserFormSave($event)"
        (cancel)="closeUserForm()"
      ></app-user-form>

    </div>
  `
})
export class ViewUsersComponent implements OnInit {
  users: UserPresentation[] = [];
  currentPage = 0;
  totalPages = 0;
  pageSize = 5;
  currentUserEmail: string = '';
  showUserFormModal = false;
  editingUserId: number | null = null;
  editingUserData: UserPresentation | null = null;



  constructor(private userService: UserService, public authService: AuthService) {}

  ngOnInit(): void {
    this.currentUserEmail = this.authService.getCurrentUserEmail();
    this.loadPage(0);
  }
  editUser(user: UserPresentation): void {
    this.editingUserId = user.id;
    this.editingUserData = user;
    this.showUserFormModal = true;
  }



  loadPage(page: number): void {
    if (page < 0 || (this.totalPages && page >= this.totalPages)) return;

    this.userService.getUsers(page, this.pageSize).subscribe(response => {
      this.users = response.content;
      this.totalPages = response.totalPages;
      this.currentPage = response.number;
    });
  }



  confirmDelete(user: UserPresentation): void {
    const confirmed = confirm(`Are you sure you want to delete user "${user.email}"?`);
    if (confirmed) {
      this.userService.deleteUser(user.id).subscribe({
        next: () => {
          if(user.email === this.authService.getCurrentUserEmail()){
            this.authService.logout();
          }
          this.loadPage(this.currentPage);
        },
        error: (err) => {
          alert('Failed to delete user: ' + err.message);
        }
      });
    }
  }

  createUser(user: UserOperation): void {
    this.userService.createUser(user).subscribe({
      next: () => {
        this.showUserFormModal = false;
        this.loadPage(0); // reload first page
      },
      error: err => {
        alert('Failed to create user: ' + err.message);
      }
    });
  }
  onUserFormSave(user: UserOperation): void {
    if (this.editingUserId !== null) {
      // Update
      this.userService.updateUser(this.editingUserId, user).subscribe({
        next: () => {
          if(user.email === this.authService.getCurrentUserEmail()){
            this.authService.updatePermissionsOfCurrentUser(user.permissions)
          }
          this.resetFormState();
          this.loadPage(this.currentPage);
        },
        error: err => {
          alert('Failed to update user: ' + err.message);
        }
      });
    } else {
      // Create
      this.createUser(user);
    }
  }

  closeUserForm(): void {
    this.resetFormState();
  }

  private resetFormState(): void {
    this.showUserFormModal = false;
    this.editingUserId = null;
    this.editingUserData = null;
  }


}
