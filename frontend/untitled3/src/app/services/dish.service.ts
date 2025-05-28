import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

export interface Dish {
  id: number;
  title: string;
  description: string;
}
export interface PaginatedResponse<T> {
  content: T[];
  totalElements: number;
  totalPages: number;
  number: number;
}


@Injectable({ providedIn: 'root' })
export class DishService {
  private baseUrl = 'http://localhost:8080/dish';
  private orderCreationUrl = 'http://localhost:8080/order/place'
  private orderScheduleUrl = 'http://localhost:8080/order/schedule'

  constructor(private http: HttpClient) {}

  getDishesPaginated(page: number, size: number): Observable<PaginatedResponse<Dish>> {
    return this.http.get<PaginatedResponse<Dish>>(`${this.baseUrl}/pg?page=${page}&size=${size}`);
  }

  placeOrder(order: { dishes: string[] }): Observable<any> {
    return this.http.post(this.orderCreationUrl, order);
  }
  scheduleOrder(payload: { dishes: string[], scheduleDateTime: string }) {
    return this.http.post(this.orderScheduleUrl, payload);
  }
}
