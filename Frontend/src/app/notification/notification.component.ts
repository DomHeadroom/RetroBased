import { Component, OnInit, OnDestroy } from '@angular/core';
import { NgClass } from '@angular/common';
import { NotificationService } from '../services/services/notification.service';
import { Subscription } from 'rxjs';

@Component({
  selector: 'app-notification',
  standalone: true,
  imports: [NgClass],  
  templateUrl: './notification.component.html',
  styleUrl: './notification.component.scss'
})
export class NotificationComponent implements OnInit, OnDestroy {
  header: string = '';
  notificationText: string = '';
  visible: boolean = false;
  fadeClass: boolean = false;
  position: number = 0;
  private notificationSubscription?: Subscription;

  constructor(private notificationService: NotificationService) {}

  ngOnInit() {
    this.notificationSubscription = this.notificationService.notification$.subscribe(notification => {
      if (notification) {
        this.showNotification(notification.header, notification.message, notification.position);
      }
    });
  }

  showNotification(header: string, message: string, position: number) {
    this.header = header;
    this.notificationText = message;
    this.position = position;
    this.visible = true;
    this.fadeClass = false;

    setTimeout(() => {
      this.fadeClass = true;
    }, 5000);

    setTimeout(() => {
      this.visible = false;
      this.fadeClass = false;
    }, 7100);
  }

  ngOnDestroy() {
    this.notificationSubscription?.unsubscribe();
  }
}
