import { Component } from '@angular/core';
import { TaskbarComponent } from '../taskbar/taskbar.component';

@Component({
  selector: 'app-order-window',
  standalone: true,
  imports: [TaskbarComponent],
  templateUrl: './order-window.component.html',
  styleUrl: './order-window.component.scss'
})
export class OrderWindowComponent {

}
