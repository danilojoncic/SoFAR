import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { HttpClientModule } from '@angular/common/http';
import { NavbarComponent } from '../navbar/navbar.component';
import { OrderService, Order } from '../../services/order.service';
import {AuthService} from '../../services/auth.service';
import { FormsModule } from '@angular/forms'; // <-- ✅ Import this


@Component({
  selector: 'app-view-orders',
  standalone: true,
  imports: [CommonModule, HttpClientModule, NavbarComponent,FormsModule],
  template: `
    <app-navbar></app-navbar>
    <div class="container mt-4">
      <h2>Orders</h2>


      <form (submit)="search()" class="row g-3 mb-3" *ngIf="this.authService.hasPermission('CAN_SEARCH_ORDER')">
        <div class="col-md-3">
          <input type="number" class="form-control" placeholder="User ID" [(ngModel)]="filters.userId" name="userId">
        </div>
        <div class="col-md-3">
          <select multiple class="form-control" [(ngModel)]="filters.status" name="status">
            <option value="ORDERED">ORDERED</option>
            <option value="PREPARING">PREPARING</option>
            <option value="DELIVERED">DELIVERED</option>
            <option value="CANCELLED">CANCELLED</option>
          </select>
        </div>
        <div class="col-md-3">
          <input type="datetime-local" class="form-control" [(ngModel)]="filters.dateFrom" name="dateFrom">
        </div>
        <div class="col-md-3">
          <input type="datetime-local" class="form-control" [(ngModel)]="filters.dateTo" name="dateTo">
        </div>
        <div class="col-md-12">
          <button class="btn btn-primary" type="submit">Search</button>
        </div>
      </form>



      <table class="table table-striped table-bordered mt-3">
        <thead class="table-dark">
        <tr>
          <th>ID</th>
          <th>Creator</th>
          <th>Status</th>
          <th>Scheduled At</th>
          <th>Dishes</th>
          <th>Actions</th>
        </tr>
        </thead>
        <tbody>
        <tr *ngFor="let order of orders">
          <td>{{ order.id }}</td>
          <td>{{ order.email }}</td>
          <td>{{ order.orderStatus }}</td>
          <td>{{ order.scheduledAt || '-' }}</td>
          <td>
            <ul>
              <li *ngFor="let dish of order.items">{{ dish.title }}</li>
            </ul>
          </td>
          <td>
            <button
              *ngIf="this.authService.hasPermission('CAN_CANCEL_ORDER')"
              class="btn btn-danger btn-sm"
              (click)="cancel(order)"
              [disabled]="!canCancel(order.orderStatus)">
              Cancel
            </button>
          </td>
        </tr>
        </tbody>
      </table>

    </div>
  `
})
export class ViewOrdersComponent implements OnInit {
  orders: Order[] = [];

  constructor(private orderService: OrderService,public authService: AuthService) {}

  ngOnInit(): void {
    this.loadPage();
  }

  loadPage(): void {
    this.orderService.getOrders().subscribe(response => {
      this.orders = response;
    });
  }

  canCancel(status: string): boolean {
    return status === 'ORDERED' || status === 'PREPARING';
  }

  cancel(order: Order): void {
    if (!this.canCancel(order.orderStatus)) return;

    if (!confirm(`Are you sure you want to cancel order #${order.id}?`)) return;

    this.orderService.cancelOrder(order.id).subscribe({
      next: () => {
        this.loadPage();
      },
      error: err => {
        console.error('Failed to cancel order:', err);
        alert('Failed to cancel the order.');
      }
    });
  }

  filters: {
    userId?: number;
    status?: string[];
    dateFrom?: string;
    dateTo?: string;
  } = {};

  search(): void {
    this.orderService.searchOrders(this.filters).subscribe({
      next: response => {
        this.orders = response;
      },
      error: err => {
        console.error('Search failed', err);
        alert('Search failed.');
      }
    });
  }

}

