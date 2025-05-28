import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

export interface ErrorMessage {
  errorId: number;
  orderId: number;
  emailOfCreator: string;
  description: string;
  operation: string;
  timestamp: string;
}


@Injectable({ providedIn: 'root' })
export class ErrorService {
  private baseUrl = 'http://localhost:8080/error';
  constructor(private http: HttpClient) {}

  getErrors(page: number, size: number): Observable<ErrorMessage[]> {
    return this.http.get<ErrorMessage[]>(`${this.baseUrl}/yourself-jwt`);
  }
}
