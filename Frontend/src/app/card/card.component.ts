import { Component, Input } from '@angular/core';
import { CommonModule } from '@angular/common';
import { NotificationService } from '../services/notification.service';
import { ProductDto } from '../services/models/product-dto';
import { ProductCartService } from '../services/product-cart.service';

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
      (item) => item.product.id === this.product!.id 
    );

    if (existingProduct) {
      if (existingProduct.quantity + 1 > this.product.quantity) {
        this.notificationService.showNotification(
          'Recycle Bin',
          `Cannot add more of ${this.product.productName}. Available quantity is ${this.product.quantity}.`,
          117.200
        );
        return;
      }
      productCartService.updateProductQuantity(existingProduct.product.id!, existingProduct.quantity + 1);
    } else {
      if (this.product.quantity < 1) {
        this.notificationService.showNotification(
          'Recycle Bin',
          `Cannot add ${this.product.productName}. No stock available.`,
          117.200
        );
        return;
      }
      productCartService.addProduct({
        product: this.product,
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
