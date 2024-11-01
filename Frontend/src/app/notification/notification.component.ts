import { Component, Input } from '@angular/core';
import { NgClass } from '@angular/common';

@Component({
  selector: 'app-notification',
  standalone: true,
  imports: [NgClass],
  templateUrl: './notification.component.html',
  styleUrl: './notification.component.scss'
})
export class NotificationComponent {
  @Input() header: string = '';
  @Input() notificationText: string = '';
  fadeClass: boolean = false;
  visible: boolean = true;
  position: number = 92.200;
  lock: boolean = false;

  ngOnInit() {
    if(this.lock)
      return;

    this.lock = true;
    this.visible = true;
    setTimeout(() => {
      this.fadeClass = true;
    }, 5000);
    setTimeout(() => {
      this.visible = false;
      this.fadeClass = false;
      this.lock = false;
    }, 7100);
  }

  setVariable(header: string, notificationText: string, position: number){
    if(this.lock)
      return;
    this.header = header;
    this.notificationText = notificationText;
    this.position = position;
  }
}
