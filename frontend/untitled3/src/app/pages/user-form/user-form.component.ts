import { Component, EventEmitter, Input, Output } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import {UserOperation, UserPresentation, UserService} from '../../services/user.service';
import { OnInit } from '@angular/core';


@Component({
  selector: 'app-user-form',
  standalone: true,
  imports: [CommonModule, FormsModule],
  template: `
    <div class="modal-backdrop fade show" (click)="close()"></div>
    <div class="modal fade show d-block" tabindex="-1">
      <div class="modal-dialog modal-lg">
        <div class="modal-content">
          <form (ngSubmit)="submit()">
            <div class="modal-header">
              <h5 class="modal-title">{{ initialUser ? 'Edit User' : 'Create New User' }}</h5>
              <button type="button" class="btn-close" (click)="close()"></button>
            </div>
            <div class="modal-body">
              <div class="mb-3">
                <label>Email</label>
                <input type="email" class="form-control" [(ngModel)]="user.email" name="email" required />
              </div>
              <div class="mb-3">
                <label>First Name</label>
                <input class="form-control" [(ngModel)]="user.firstName" name="firstName" required />
              </div>
              <div class="mb-3">
                <label>Last Name</label>
                <input class="form-control" [(ngModel)]="user.lastName" name="lastName" required />
              </div>
              <div class="mb-3">
                <label>Password</label>
                <input type="password" class="form-control" [(ngModel)]="user.password" name="password" required />
              </div>
              <div class="mb-3">
                <label>Permissions</label>
                <div class="input-group">
                  <select class="form-select" [(ngModel)]="selectedPermission" name="perm">
                    <option value="">-- Select Permission --</option>
                    <option *ngFor="let perm of allPermissions" [value]="perm">
                      {{ perm }}
                    </option>
                  </select>
                  <button type="button" class="btn btn-outline-secondary" (click)="addPermission()">Add</button>
                </div>
                <div class="mt-2">
                <span
                    class="badge bg-primary me-2 d-inline-flex align-items-center"
                    *ngFor="let p of user.permissions"
                >
                {{ p }}
                <button
                    type="button"
                    class="btn-close btn-close-white ms-2"
                    aria-label="Remove"
                    (click)="removePermission(p)"
                    ></button>
                </span>
                </div>
              </div>
            </div>
            <div class="modal-footer">
              <button class="btn btn-secondary" type="button" (click)="close()">Cancel</button>
              <button class="btn btn-primary" type="submit">
                {{ initialUser ? 'Update' : 'Create' }}
              </button>
            </div>
          </form>
        </div>
      </div>
    </div>
  `,
  styles: [`
    .modal-backdrop {
      z-index: 1040;
    }
    .modal {
      z-index: 1050;
    }
  `]
})
export class UserFormComponent implements OnInit {
  @Output() save = new EventEmitter<UserOperation>();
  @Output() cancel = new EventEmitter<void>();
  @Input() initialUser: UserPresentation | null = null;

  user: UserOperation = {
    email: '',
    firstName: '',
    lastName: '',
    password: '',
    permissions: []
  };

  allPermissions: string[] = [];
  selectedPermission: string = '';

  constructor(private userService: UserService) {}

  ngOnInit(): void {
    this.userService.getAllPermissions().subscribe({
      next: perms => this.allPermissions = perms,
      error: err => alert('Failed to load permissions: ' + err.message)
    });

    if (this.initialUser) {
      this.user = {
        email: this.initialUser.email,
        firstName: this.initialUser.firstName,
        lastName: this.initialUser.lastName,
        permissions: [...this.initialUser.permissions],
        password: '' // leave blank to avoid showing actual password
      };
    }


  }

  addPermission(): void {
    if (this.selectedPermission && !this.user.permissions.includes(this.selectedPermission)) {
      this.user.permissions.push(this.selectedPermission);
    }
    this.selectedPermission = '';
  }

  removePermission(p: string): void {
    this.user.permissions = this.user.permissions.filter(perm => perm !== p);
  }

  submit(): void {
    this.save.emit(this.user);
  }

  close(): void {
    this.cancel.emit();
  }
}
