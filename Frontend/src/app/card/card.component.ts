import { Component, Input } from '@angular/core';
import { CommonModule } from '@angular/common';
import { NotificationService } from '../services/services/notification.service';
import {ProductDto } from '../services/models/product-dto';
import { ProductCartService } from '../services/models/product-cart';

@Component({
  selector: 'app-card',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './card.component.html',
  styleUrl: './card.component.scss'
})
export class CardComponent {
  @Input() product: ProductDto | undefined;

  constructor(
    private notificationService: NotificationService,
    private productCartService: ProductCartService
  ) {}

  handleClick() {
    if (!this.product) {
      console.error('No product provided!');
      return;
    }
  
    const productCartService = this.productCartService;
  
    const existingProduct = productCartService.getProducts().find(
      (item) => item.id === this.product!.id
    );
  
    if (existingProduct) {
      if (existingProduct.quantity + 1 > this.product.quantity!) {
        this.notificationService.showNotification(
          'Recycle Bin',
          `Cannot add more of ${this.product.productName}. Available quantity is ${this.product.quantity}.`,
          117.200
        );
        return;
      }
      productCartService.updateProductQuantity(existingProduct.id, existingProduct.quantity + 1);
    } else {
      if (this.product.quantity! < 1) {
        this.notificationService.showNotification(
          'Recycle Bin',
          `Cannot add ${this.product.productName}. No stock available.`,
          117.200
        );
        return;
      }
      productCartService.addProduct({
        id: this.product.id!,
        quantity: 1,
      });
    }
  
    this.notificationService.showNotification(
      'Recycle Bin',
      'Product/s added to the recycle bin.',
      117.200
    );
  }
}
