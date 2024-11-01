import { Component } from '@angular/core';
import { NgClass } from '@angular/common';

@Component({
  selector: 'app-notification',
  standalone: true,
  imports: [NgClass],
  templateUrl: './notification.component.html',
  styleUrl: './notification.component.scss'
})
export class NotificationComponent {
  header: string = '';
  notificationText: string = '';
  fadeClass: boolean = false;

  ngOnInit() {
    setTimeout(() => {
      this.fadeClass = true;
    }, 5000);
  }
}
