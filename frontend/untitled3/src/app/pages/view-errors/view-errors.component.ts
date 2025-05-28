import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { HttpClientModule } from '@angular/common/http';
import { NavbarComponent } from '../navbar/navbar.component';
import { ErrorService, ErrorMessage } from '../../services/error.service';

@Component({
  selector: 'app-view-errors',
  standalone: true,
  imports: [CommonModule, HttpClientModule, NavbarComponent],
  template: `
    <app-navbar></app-navbar>
    <div class="container mt-4">
      <h2>Error Log</h2>

      <table class="table table-striped table-bordered mt-3">
        <thead class="table-dark">
          <tr>
            <th>Error ID</th>
            <th>Food Order ID</th>
            <th>Creator Email</th>
            <th>Operation</th>
            <th>Description</th>
            <th>Timestamp</th>
          </tr>
        </thead>
        <tbody>
          <tr *ngFor="let error of errors">
            <td>{{ error.errorId }}</td>
            <td>{{ error.orderId }}</td>
            <td>{{ error.emailOfCreator }}</td>
            <td>{{ error.operation }}</td>
            <td>{{ error.description }}</td>
            <td>{{ error.timestamp }}</td>
          </tr>
        </tbody>
      </table>
    </div>
  `
})
export class ViewErrorsComponent implements OnInit {
  errors: ErrorMessage[] = [];
  totalPages = 0;
  pageSize = 5;

  constructor(private errorService: ErrorService) {}

  ngOnInit(): void {
    this.loadPage(0);
  }

  loadPage(page: number): void {
    if (page < 0 || (this.totalPages && page >= this.totalPages)) return;

    this.errorService.getErrors(page, this.pageSize).subscribe(response => {
      this.errors = response;
    });
  }
}
