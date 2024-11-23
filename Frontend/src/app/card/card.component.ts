import { Component, Input } from '@angular/core';
import { CommonModule } from '@angular/common';
import { NotificationService } from '../services/services/notification.service';
import {ProductDto } from '../services/models/product-dto';

@Component({
  selector: 'app-card',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './card.component.html',
  styleUrl: './card.component.scss'
})
export class CardComponent {
  @Input() product: ProductDto | undefined;

  constructor(private notificationService: NotificationService) {}

  handleClick() {
    this.notificationService.showNotification('Recycle Bin', 'Product/s added to the recycle bin.', 117.200);
  }
}
