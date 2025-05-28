// services/user.service.ts
import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';

export interface UserPresentation {
  id: number;
  firstName: string,
  lastName: string,
  email: string;
  permissions: string[];
}
export interface UserOperation {
  firstName: string;
  lastName: string;
  email: string;
  password: string;
  permissions: string[];
}

export interface PaginatedResponse<T> {
  content: T[];
  totalPages: number;
  totalElements: number;
  number: number;
}

@Injectable({ providedIn: 'root' })
export class UserService {
  private readonly API_URL = 'http://localhost:8080/user';
  private readonly API_URL_PERMISSIONS = 'http://localhost:8080/permissions';

  constructor(private http: HttpClient) {}

  getUsers(page: number, size: number): Observable<PaginatedResponse<UserPresentation>> {
    return this.http.get<PaginatedResponse<UserPresentation>>(`${this.API_URL}/pg?page=${page}&size=${size}`);
  }


  deleteUser(id: number): Observable<void> {
    return this.http.delete<void>(`${this.API_URL}/delete/${id}`);
  }

  updateUser(id: number, user: UserOperation): Observable<void> {
    return this.http.put<void>(`${this.API_URL}/edit/${id}`, user);
  }

  createUser(user: UserOperation): Observable<UserPresentation> {
    return this.http.post<UserPresentation>(`${this.API_URL}/create`, user);
  }

  getAllPermissions(): Observable<string[]> {
    return this.http.get<string[]>(`${this.API_URL_PERMISSIONS}/as-string`);
  }
}
