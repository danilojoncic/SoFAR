import {Injectable} from '@angular/core';
import {ActivatedRouteSnapshot, CanActivate, Router, UrlTree} from '@angular/router';
import {AuthService} from '../services/auth.service';

@Injectable({ providedIn: 'root' })
export class PermissionGuard implements CanActivate {
  constructor(private auth: AuthService, private router: Router) {}

  canActivate(route: ActivatedRouteSnapshot): boolean | UrlTree {
    const userPermissions = this.auth.getPermissions();

    if (userPermissions.length === 0) {
      alert('You do not have any permissions.');
      this.auth.logout();
      return this.router.parseUrl('/login');
    }

    const requiredPermission = route.data['requiredPermission'] as string;
    if (requiredPermission && !userPermissions.includes(requiredPermission)) {
      alert(`Missing required permission: ${requiredPermission}`);
      return this.router.parseUrl('/login');
    }

    return true;
  }
}
