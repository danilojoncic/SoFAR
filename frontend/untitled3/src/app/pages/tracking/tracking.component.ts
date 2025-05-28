import { Component, OnDestroy, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { WebSocketService } from '../../services/websocket.service';
import { Subscription, timer } from 'rxjs';
import { NavbarComponent } from '../navbar/navbar.component';

@Component({
  selector: 'app-tracking',
  standalone: true,
  imports: [CommonModule, NavbarComponent],
  template: `
    <app-navbar></app-navbar>
    <div class="container mt-4">
      <h2>Live Order Tracking</h2>

      <div class="alert" [ngClass]="connectionStatus ? 'alert-success' : 'alert-warning'">
        <strong>Connection Status:</strong>
        {{ connectionStatus ? 'Connected' : 'Disconnected' }}
        <span *ngIf="!connectionStatus">- Attempting to reconnect...</span>
      </div>

      <div class="mb-3">
        <button
          class="btn btn-primary me-2"
          (click)="reconnect()"
          [disabled]="connectionStatus">
          Reconnect
        </button>
        <button
          class="btn btn-info"
          (click)="refreshOrders()">
          Refresh
        </button>
      </div>

      <div class="alert alert-info" *ngIf="orderIdsToTrack.length > 0">
        <strong>Tracking Orders:</strong> {{ orderIdsToTrack.join(', ') }}
      </div>

      <div *ngIf="trackedOrders.length === 0 && connectionStatus" class="alert alert-secondary">
        <p>No order updates received yet. Make sure:</p>
        <ul>
          <li>Your backend is running on http://localhost:8080</li>
          <li>WebSocket endpoint '/ws' is properly configured</li>
          <li>Orders with IDs {{ orderIdsToTrack.join(', ') }} exist in your system</li>
          <li>Try updating an order status from your backend</li>
        </ul>
      </div>

      <div *ngFor="let order of trackedOrders; trackBy: trackByOrderId"
           class="card mb-3"
           [ngClass]="getCardClass(order.orderStatus)">
        <div class="card-body">
          <h5 class="card-title">Order #{{ order.id }}</h5>
          <p class="card-text">
            <strong>Status:</strong>
            <span class="badge" [ngClass]="getStatusBadgeClass(order.orderStatus)">
              {{ order.orderStatus }}
            </span>
        </div>
      </div>
    </div>
  `,
  styles: [`
    .card {
      transition: all 0.3s ease;
    }
    .card:hover {
      transform: translateY(-2px);
      box-shadow: 0 4px 8px rgba(0,0,0,0.1);
    }
    .status-pending { border-left: 4px solid #ffc107; }
    .status-confirmed { border-left: 4px solid #17a2b8; }
    .status-preparing { border-left: 4px solid #fd7e14; }
    .status-ready { border-left: 4px solid #28a745; }
    .status-delivered { border-left: 4px solid #6c757d; }
    .status-cancelled { border-left: 4px solid #dc3545; }
  `]
})
export class TrackingComponent implements OnInit, OnDestroy {
  trackedOrders: any[] = [];
  connectionStatus: boolean = false;
  private subscriptions: Subscription = new Subscription();

  // Replace with actual order IDs to track
  orderIdsToTrack: number[] = [1, 2, 3, 4, 5,6,7,8,9,10];

  constructor(private webSocketService: WebSocketService) {}

  ngOnInit(): void {
    this.initializeWebSocket();
  }

  private async initializeWebSocket(): Promise<void> {
    try {
      this.subscriptions.add(
        this.webSocketService.getConnectionState().subscribe(
          status => {
            this.connectionStatus = status;
            console.log('Connection status changed:', status);
          }
        )
      );

      this.subscriptions.add(
        this.webSocketService.getOrderUpdates().subscribe(
          order => {
            this.handleOrderUpdate(order);
          },
          error => {
            console.error('Error receiving order updates:', error);
          }
        )
      );

      await this.webSocketService.connect();
      console.log('WebSocket connection initiated');

      this.orderIdsToTrack.forEach(id => {
        this.webSocketService.subscribeToOrderStatus(id);
      });

    } catch (error) {
      console.error('Failed to initialize WebSocket:', error);
    }
  }

  private handleOrderUpdate(order: any): void {
    console.log('Processing order update:', order);

    const existingIndex = this.trackedOrders.findIndex(o => o.id === order.id);
    const isDelivered = order.orderStatus === 'DELIVERED';

    if (existingIndex !== -1) {
      this.trackedOrders[existingIndex] = { ...order, lastUpdated: new Date() };
      console.log('Updated existing order:', order.id);
    } else {
      this.trackedOrders.push({ ...order, lastUpdated: new Date() });
      console.log('Added new order:', order.id);
    }

    // Schedule stopping tracking 30 seconds after delivered
    if (isDelivered) {
      console.log(`Order ${order.id} delivered. Will stop tracking in 30s...`);
      const sub = timer(30000).subscribe(() => {
        this.stopTrackingOrder(order.id);
        sub.unsubscribe(); // Prevent memory leaks
      });
    }

    this.trackedOrders.sort((a, b) => a.id - b.id);
  }


  trackByOrderId(index: number, order: any): number {
    return order.id;
  }

  getCardClass(status: string): string {
    return `status-${status?.toLowerCase() || 'unknown'}`;
  }

  getStatusBadgeClass(status: string): string {
    switch (status) {
      case 'PREPARING':
        return 'bg-warning text-black';
      case 'DELIVERED':
        return 'bg-success text-black';
      case 'CANCELLED':
        return 'bg-danger text-black';
      default:
        return 'bg-secondary text-black';
    }
  }

  async reconnect(): Promise<void> {
    try {
      this.webSocketService.disconnect();
      await new Promise(resolve => setTimeout(resolve, 1000)); // Wait 1 second
      await this.webSocketService.connect();

      this.orderIdsToTrack.forEach(id => {
        this.webSocketService.subscribeToOrderStatus(id);
      });

      console.log('Reconnected successfully');
    } catch (error) {
      console.error('Reconnection failed:', error);
    }
  }


  stopTrackingOrder(orderId: number): void {
    this.orderIdsToTrack = this.orderIdsToTrack.filter(id => id !== orderId);
    this.trackedOrders = this.trackedOrders.filter(order => order.id !== orderId);
    this.webSocketService.unsubscribeFromOrder(orderId);
    console.log('Stopped tracking order:', orderId);
  }

  refreshOrders(): void {
    console.log('Refreshing order subscriptions...');
    this.orderIdsToTrack.forEach(id => {
      this.webSocketService.subscribeToOrderStatus(id);
    });
  }

  ngOnDestroy(): void {
    console.log('TrackingComponent destroyed, cleaning up...');
    this.subscriptions.unsubscribe();
    this.webSocketService.disconnect();
  }
}
