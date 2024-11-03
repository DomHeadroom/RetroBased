import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { NotificationService } from '../services/services/notification.service';

@Component({
  selector: 'app-card',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './card.component.html',
  styleUrl: './card.component.scss'
})
export class CardComponent {
  title: string = "";
  seller: string = "";
  price: number = 0;

  constructor(private notificationService: NotificationService) {}

  handleClick() {
    this.notificationService.showNotification('Recycle Bin', 'Product/s added to the recycle bin.', 117.200);
  }
}
