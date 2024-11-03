import { Component, ViewChild } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import { TaskbarComponent } from '../taskbar/taskbar.component'
import { WindowComponent } from '../window/window.component'
import { NotificationComponent } from '../notification/notification.component';
import { CardComponent } from '../card/card.component';
import { NotificationService } from '../services/services/notification.service';

@Component({
  selector: 'app-home',
  standalone: true,
  imports: [RouterOutlet,NotificationComponent, TaskbarComponent, WindowComponent, CardComponent],
  templateUrl: './home.component.html',
  styleUrl: './home.component.scss'
})

export class HomeComponent {
  notificationHeader: string = 'Login';
  notificationText: string = 'If you want to login, click here.';

  constructor(private notificationService: NotificationService) {}

  ngOnInit() {
    this.notificationService.showNotification(this.notificationHeader, this.notificationText, 92.200);
  }

}
