import { Injectable } from '@angular/core';
import { BehaviorSubject } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class NotificationService {
    private notificationSource = new BehaviorSubject<{ header: string, message: string, position: number } | null>(null);
    notification$ = this.notificationSource.asObservable();
  
    showNotification(header: string, message: string, position: number) {
      this.notificationSource.next({ header, message, position });
    }
  
    clearNotification() {
      this.notificationSource.next(null);
    }
}
