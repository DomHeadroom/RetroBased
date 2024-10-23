import { Component } from '@angular/core';

@Component({
  selector: 'app-taskbar',
  standalone: true,
  imports: [],
  templateUrl: './taskbar.component.html',
  styleUrl: './taskbar.component.scss'
})
export class TaskbarComponent {
  currentTime: string = '';
  currentDate: string = '';

  constructor() {}

  ngOnInit(): void {
    this.updateTime();
    setInterval(() => this.updateTime(), 1000);
  }

  updateTime() {
    const now = new Date();
    this.currentTime = now.toLocaleTimeString();
    this.currentDate = now.toLocaleDateString();
  }

}
