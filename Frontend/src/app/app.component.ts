import { Component, ViewChild } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import { TaskbarComponent } from './taskbar/taskbar.component'
import { WindowComponent } from './window/window.component'
import { NotificationComponent } from "./notification/notification.component";


@Component({
  selector: 'app-root',
  standalone: true,
  imports: [RouterOutlet, TaskbarComponent, WindowComponent, NotificationComponent],
  templateUrl: './app.component.html',
  styleUrl: './app.component.scss'
})
export class AppComponent {
  title = 'Frontend';
  notificationHeader: string = 'Login';
  notificationText: string = 'If you want to login, click here.';

  @ViewChild(NotificationComponent) notificationComponent?: NotificationComponent
}
