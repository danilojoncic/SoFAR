import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { HttpClientModule } from '@angular/common/http';
import { Dish, DishService } from '../../services/dish.service';
import { NavbarComponent } from '../navbar/navbar.component';
import {FormsModule} from "@angular/forms";
import {AuthService} from '../../services/auth.service';

@Component({
  selector: 'app-view-dishes',
  standalone: true,
  imports: [CommonModule, HttpClientModule, FormsModule, NavbarComponent],
  template: `
    <app-navbar></app-navbar>

    <div class="container mt-4">
      <h2>All Dishes</h2>

      <table class="table table-striped table-bordered mt-3">
        <thead class="table-dark">
        <tr>
          <th>Title</th>
          <th>Description</th>
          <th>Quantity</th>
        </tr>
        </thead>
        <tbody>
        <tr *ngFor="let dish of dishes">
          <td>{{ dish.title }}</td>
          <td>{{ dish.description }}</td>
          <td>
            <input
                type="number"
                min="0"
                class="form-control"
                [(ngModel)]="quantities[dish.title]"
            />
          </td>
        </tr>
        </tbody>
      </table>

      <nav>
        <ul class="pagination justify-content-center">
          <li class="page-item" [class.disabled]="page === 0">
            <button class="page-link" (click)="loadPage(page - 1)">Previous</button>
          </li>

          <li
            class="page-item"
            *ngFor="let p of [].constructor(totalPages); let i = index"
            [class.active]="i === page"
          >
            <button class="page-link" (click)="loadPage(i)">{{ i + 1 }}</button>
          </li>

          <li class="page-item" [class.disabled]="page + 1 === totalPages">
            <button class="page-link" (click)="loadPage(page + 1)">Next</button>
          </li>
        </ul>
      </nav>
      <div class="position-fixed bottom-0 end-0 m-4 d-flex flex-column align-items-end gap-2">
        <button
            *ngIf="authService.hasPermission('CAN_PLACE_ORDER')"
            class="btn btn-success rounded-circle d-flex align-items-center justify-content-center"
            style="width: 8rem; height: 8rem; font-size: 1.2rem;"
            (click)="placeOrder()"
            title="Place Order"
        >
          🧾
        </button>
        <button
            *ngIf="authService.hasPermission('CAN_SCHEDULE_ORDER')"
            class="btn btn-warning rounded-circle d-flex align-items-center justify-content-center"
            style="width: 8rem; height: 8rem; font-size: 1.2rem;"
            (click)="scheduleOrder()"
            title="Schedule Order"
        >
          ⏰
        </button>
<!--        This can later be implemented, but no need for now-->
<!--        <button-->
<!--            *ngIf="authService.hasPermission('CAN_CREATE')"-->
<!--            class="btn btn-primary rounded-circle d-flex align-items-center justify-content-center"-->
<!--            style="width: 8rem; height: 8rem; font-size: 2rem;"-->
<!--            (click)="showDishFormModal = true"-->
<!--            title="Add Dish"-->
<!--        >-->
<!--          +-->
<!--        </button>-->
      </div>
    </div>
  `
})
export class ViewDishesComponent implements OnInit {
  dishes: Dish[] = [];
  page = 0;
  size = 5;
  totalPages = 0;

  showDishFormModal = false;
  constructor(private dishService: DishService,public authService:AuthService) {}

  ngOnInit(): void {
    this.loadPage(0);
  }

  loadPage(page: number): void {
    if (page < 0 || (this.totalPages && page >= this.totalPages)) return;

    this.dishService.getDishesPaginated(page, this.size).subscribe({
      next: (res) => {
        this.dishes = res.content;
        this.totalPages = res.totalPages;
        this.page = res.number;
      },
      error: (err) => console.error(err)
    });
  }

  quantities: { [title: string]: number } = {};

  placeOrder(): void {
    const dishTitles: string[] = [];

    for (const [title, qty] of Object.entries(this.quantities)) {
      if (qty && qty > 0) {
        for (let i = 0; i < qty; i++) {
          dishTitles.push(title);
        }
      }
    }

    if (dishTitles.length === 0) {
      alert('Please select at least one dish with quantity > 0.');
      return;
    }

    const orderPayload = { dishes: dishTitles };

    this.dishService.placeOrder(orderPayload).subscribe({
      next: () => {
        alert('Order placed successfully!');
        this.quantities = {};
      },
      error: err => alert('Failed to place order: ' + err.message)
    });
  }




  scheduleOrder(): void {
    const dishTitles: string[] = [];

    for (const [title, qty] of Object.entries(this.quantities)) {
      if (qty && qty > 0) {
        for (let i = 0; i < qty; i++) {
          dishTitles.push(title);
        }
      }
    }

    if (dishTitles.length === 0) {
      alert('Please select at least one dish with quantity > 0.');
      return;
    }

    const input = prompt("Enter scheduled time (yyyy-MM-ddTHH:mm):", new Date().toISOString().slice(0, 16));
    if (!input) {
      alert("Scheduling cancelled.");
      return;
    }

    const scheduleDateTime = new Date(input);
    if (isNaN(scheduleDateTime.getTime())) {
      alert("Invalid date format.");
      return;
    }

    const schedulePayload = {
      dishes: dishTitles,
      scheduleDateTime: scheduleDateTime.toISOString().slice(0, 19)
    };


    this.dishService.scheduleOrder(schedulePayload).subscribe({
      next: () => {
        alert('Order scheduled successfully!');
        this.quantities = {};
      },
      error: err => alert('Failed to schedule order: ' + err.message)
    });
  }
}
