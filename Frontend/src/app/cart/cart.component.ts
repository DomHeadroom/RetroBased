import { Component } from '@angular/core';
import { ProductCartService } from '../services/services/product-cart.service';
import { ProductDtoQuantity } from '../services/models/product-dto-quantity';
import { TaskbarComponent } from '../taskbar/taskbar.component';
import { SubWindowComponent } from '../sub-window/sub-window.component';

@Component({
  selector: 'app-cart',
  standalone: true,
  imports: [TaskbarComponent, SubWindowComponent],
  templateUrl: './cart.component.html',
  styleUrl: './cart.component.scss'
})
export class CartComponent {
  products: ProductDtoQuantity[] = [];

  constructor(private productCartService: ProductCartService) {}

  ngOnInit(): void {
    this.loadProducts();
  }

  loadProducts(): void {
    this.products = this.productCartService.getProducts();
  }

  updateQuantity(productId: string, newQuantity: number): void {
    if (newQuantity > 0) {
      this.productCartService.updateProductQuantity(productId, newQuantity);
    } else {
      this.productCartService.removeProduct(productId);
    }
    this.loadProducts();
  }

  getProducts(): ProductDtoQuantity[] {
    return this.productCartService.getProducts();
  }

  onQuantityChange(event: Event, productId: string | undefined): void {
    if (!productId) {
      console.error('Product ID is null or undefined.');
      return;
    }

    const inputElement = event.target as HTMLInputElement;
    const inputValue = inputElement.value;

    const newQuantity = parseInt(inputValue, 10);

    if (isNaN(newQuantity) || newQuantity < 0 || inputValue !== newQuantity.toString()) {
      console.error('Invalid quantity entered. Please enter a valid number.');
      return;
    }

    if (newQuantity > 0) {
      this.productCartService.updateProductQuantity(productId, newQuantity);
    } else {
      this.productCartService.removeProduct(productId);
    }

    this.loadProducts();
  }

  emptyRecycleBin(): void {
    this.productCartService.clearCart();
    this.loadProducts();
  }

  checkout(): void {

  }

  get taskbarIcon(): string {
    return this.products.length > 0 
      ? 'assets/taskbar/icons/recycle_bin_full.ico' 
      : 'assets/taskbar/icons/recycle_bin.ico';
  }

  get titleBarIcon(): string {
    return this.products.length > 0 
      ? '/assets/cart/recycle_bin_full.ico' 
      : '/assets/cart/recycle_bin_empty.ico';
  }
}
