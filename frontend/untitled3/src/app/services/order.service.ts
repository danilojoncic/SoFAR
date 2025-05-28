import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

export interface Dish {
  id: number;
  title: string;
  desciption: string;
}

export interface Order {
  id: number;
  email: string;
  items: Dish[];
  orderStatus: string;
  scheduledAt: string;
  active: boolean;
}


@Injectable({
  providedIn: 'root'
})
export class OrderService {
  private apiUrl = 'http://localhost:8080/order';

  constructor(private http: HttpClient) {}

  getOrders(): Observable<Order[]> {
    return this.http.get<Order[]>(this.apiUrl);
  }


  cancelOrder(orderId: number): Observable<void> {
    return this.http.put<void>(`${this.apiUrl}/cancel/${orderId}`, {});
  }

  searchOrders(params: {
    userId?: number;
    status?: string[];
    dateFrom?: string;
    dateTo?: string;
  }): Observable<Order[]> {
    const queryParams = new URLSearchParams();

    if (params.userId) queryParams.append('userId', params.userId.toString());
    if (params.status) params.status.forEach(s => queryParams.append('status', s));
    if (params.dateFrom) queryParams.append('dateFrom', params.dateFrom);
    if (params.dateTo) queryParams.append('dateTo', params.dateTo);

    return this.http.get<Order[]>(`${this.apiUrl}/search?${queryParams.toString()}`);
  }




}
