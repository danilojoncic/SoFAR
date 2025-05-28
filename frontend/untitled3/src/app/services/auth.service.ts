import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Router} from '@angular/router';
import {Observable, tap} from 'rxjs';

@Injectable({ providedIn: 'root' })
export class AuthService {
  private readonly TOKEN_KEY = 'auth-token';
  private readonly EMAIL_KEY = "email";
  private readonly PERMISSIONS_KEY = 'auth-permissions';
  private readonly API_URL = 'http://localhost:8080/auth/login';

  constructor(private http: HttpClient, private router: Router) {}

  login(email: string, password: string): Observable<any> {
    return this.http.post<{ token: string }>(this.API_URL, { email, password }).pipe(
      tap(response => {
        localStorage.setItem(this.TOKEN_KEY, response.token);
        this.decodeToken();

      })
    );
  }

  logout(): void {
    localStorage.removeItem(this.TOKEN_KEY);
    localStorage.removeItem(this.PERMISSIONS_KEY);
    this.router.navigate(['/login']);
  }

  isAuthenticated(): boolean {
    return !!localStorage.getItem(this.TOKEN_KEY);
  }

  decodeToken(): void {
    const token = localStorage.getItem(this.TOKEN_KEY);
    if (!token) return;

    try {
      const payload = JSON.parse(atob(token.split('.')[1]));
      const permissions = payload.permissions || [];
      console.log('JWT permissions:', permissions);
      localStorage.setItem(this.EMAIL_KEY,payload.sub)
      console.log(payload.sub)
      localStorage.setItem(this.PERMISSIONS_KEY, JSON.stringify(permissions));
    } catch (err) {
      console.error('Failed to decode token:', err);
    }
  }

  getPermissions(): string[] {
    const raw = localStorage.getItem(this.PERMISSIONS_KEY);
    return raw ? JSON.parse(raw) : [];
  }

  hasPermission(permission: string): boolean {
    return this.getPermissions().includes(permission);
  }

  getCurrentUserEmail(): string {
    return localStorage.getItem(this.EMAIL_KEY) || "Nothing";
  }

  updatePermissionsOfCurrentUser(permissions: string[]): void{
     localStorage.setItem(this.PERMISSIONS_KEY,JSON.stringify(permissions))
  }


}
