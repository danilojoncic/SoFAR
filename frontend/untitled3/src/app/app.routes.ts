import { Routes } from '@angular/router';
import { LoginComponent } from './pages/login/login.component';
import { HomeComponent } from './pages/home/home.component';
import { AuthGuard } from './guards/auth.guard';
import {PermissionGuard} from './guards/permission.guard';
import {ViewUsersComponent} from './pages/view-users/view-users.component';
import {ViewErrorsComponent} from './pages/view-errors/view-errors.component';
import {ViewDishesComponent} from './pages/view-dishes/view-dishes.component';
import {ViewOrdersComponent} from './pages/view-orders/view-orders.component';
import {TrackingComponent} from './pages/tracking/tracking.component';

export const appRoutes: Routes = [




  { path: '', redirectTo: '/home', pathMatch: 'full' },

  { path: 'login', component: LoginComponent },

  {
    path: 'home',
    component: HomeComponent,
    canActivate: [AuthGuard,PermissionGuard],
    data: { requiredPermission: "CAN_VIEW" }
  },

  {
    path: 'users',
    component: ViewUsersComponent,
    canActivate: [AuthGuard,PermissionGuard],
    data: { requiredPermission: 'CAN_VIEW' }
  },

  {
    path: 'errors',
    component: ViewErrorsComponent,
    canActivate: [AuthGuard,PermissionGuard],
    data: { requiredPermission: 'CAN_VIEW' }
  },

  {
    path: 'dishes',
    component: ViewDishesComponent,
    canActivate: [AuthGuard,PermissionGuard],
    data: { requiredPermission: 'CAN_VIEW' }
  },

  {
    path: 'orders',
    component: ViewOrdersComponent,
    canActivate: [AuthGuard,PermissionGuard],
    data: { requiredPermission: 'CAN_VIEW' }
  },

  {
    path: 'tracking',
    component: TrackingComponent,
    canActivate: [AuthGuard,PermissionGuard],
    data: { requiredPermission: 'CAN_TRACK_ORDER' }
  },



  //wildcard always should be at the bottom
  { path: '**', redirectTo: '/home' },


  ];
