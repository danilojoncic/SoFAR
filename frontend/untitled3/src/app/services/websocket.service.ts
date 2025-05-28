import { Injectable } from '@angular/core';
import * as StompJs from '@stomp/stompjs';
import SockJS from 'sockjs-client';
import { Observable, Subject, BehaviorSubject } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class WebSocketService {
  private stompClient!: StompJs.Client;
  private orderUpdates$: Subject<any> = new Subject();
  private subscribedOrders: Set<number> = new Set();
  private connectionState$: BehaviorSubject<boolean> = new BehaviorSubject(false);
  private connectionPromise: Promise<void> | null = null;
  private subscriptions: Map<number, StompJs.StompSubscription> = new Map();

  constructor() {
    this.initializeClient();
  }

  private initializeClient(): void {
    const socket = new SockJS('http://localhost:8080/ws');
    this.stompClient = new StompJs.Client({
      webSocketFactory: () => socket,
      debug: (str) => {
        console.log('STOMP DEBUG:', str);
      },
      reconnectDelay: 5000,
      heartbeatIncoming: 4000,
      heartbeatOutgoing: 4000,
    });

    this.stompClient.onConnect = (frame) => {
      console.log('WebSocket connected successfully!', frame);
      this.connectionState$.next(true);
      this.resubscribeToOrders();
    };

    this.stompClient.onStompError = (frame) => {
      console.error('STOMP protocol error:', frame.headers['message']);
      console.error('Error details:', frame.body);
      this.connectionState$.next(false);
    };

    this.stompClient.onWebSocketError = (event) => {
      console.error('WebSocket error:', event);
      this.connectionState$.next(false);
    };

    this.stompClient.onWebSocketClose = (event) => {
      console.log('WebSocket closed:', event);
      this.connectionState$.next(false);
    };

    this.stompClient.onDisconnect = () => {
      console.log('STOMP client disconnected');
      this.connectionState$.next(false);
    };
  }

  public connect(): Promise<void> {
    if (this.connectionPromise) {
      return this.connectionPromise;
    }

    this.connectionPromise = new Promise((resolve, reject) => {
      if (this.stompClient?.active) {
        resolve();
        return;
      }

      const connectTimeout = setTimeout(() => {
        reject(new Error('Connection timeout'));
      }, 10000);

      const originalOnConnect = this.stompClient.onConnect;
      this.stompClient.onConnect = (frame) => {
        clearTimeout(connectTimeout);
        originalOnConnect.call(this.stompClient, frame);
        resolve();
      };

      const originalOnStompError = this.stompClient.onStompError;
      this.stompClient.onStompError = (frame) => {
        clearTimeout(connectTimeout);
        originalOnStompError.call(this.stompClient, frame);
        reject(new Error(`STOMP error: ${frame.headers['message']}`));
      };

      try {
        this.stompClient.activate();
      } catch (error) {
        clearTimeout(connectTimeout);
        reject(error);
      }
    });

    return this.connectionPromise;
  }

  public disconnect(): void {
    if (this.stompClient?.active) {
      // Unsubscribe from all active subscriptions
      this.subscriptions.forEach((subscription, orderId) => {
        subscription.unsubscribe();
        console.log(`Unsubscribed from order ${orderId}`);
      });
      this.subscriptions.clear();

      this.stompClient.deactivate().then(() => {
        console.log('WebSocket successfully disconnected');
        this.connectionState$.next(false);
        this.subscribedOrders.clear();
      });
    }
    this.connectionPromise = null;
  }

  public getConnectionState(): Observable<boolean> {
    return this.connectionState$.asObservable();
  }

  private resubscribeToOrders(): void {
    console.log('Resubscribing to orders:', Array.from(this.subscribedOrders));
    this.subscribedOrders.forEach(orderId => {
      this.subscribeToOrderStatus(orderId);
    });
  }

  public subscribeToOrderStatus(orderId: number): void {
    console.log(`Attempting to subscribe to order ${orderId}`);

    // Add to set regardless of connection state
    this.subscribedOrders.add(orderId);

    if (!this.stompClient?.active) {
      console.log(`STOMP client not active, subscription will be attempted when connected`);
      return;
    }

    // Unsubscribe if already subscribed
    if (this.subscriptions.has(orderId)) {
      this.subscriptions.get(orderId)?.unsubscribe();
    }

    try {
      const subscription = this.stompClient.subscribe(
        `/tracker/order-status/${orderId}`,
        (message) => {
          try {
            console.log(`Received update for order ${orderId}:`, message.body);
            const order = JSON.parse(message.body);
            this.orderUpdates$.next(order);
          } catch (error) {
            console.error('Error processing message:', error);
          }
        },
        { id: `sub-${orderId}` }
      );

      this.subscriptions.set(orderId, subscription);
      console.log(`Successfully subscribed to order ${orderId}`);
    } catch (error) {
      console.error(`Error subscribing to order ${orderId}:`, error);
    }
  }

  public unsubscribeFromOrder(orderId: number): void {
    this.subscribedOrders.delete(orderId);

    if (this.subscriptions.has(orderId)) {
      this.subscriptions.get(orderId)?.unsubscribe();
      this.subscriptions.delete(orderId);
      console.log(`Unsubscribed from order ${orderId}`);
    }
  }

  public getOrderUpdates(): Observable<any> {
    return this.orderUpdates$.asObservable();
  }

  public isConnected(): boolean {
    return this.stompClient?.active || false;
  }
}
