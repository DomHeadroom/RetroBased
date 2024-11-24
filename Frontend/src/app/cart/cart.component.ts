import { Component, OnInit } from '@angular/core';
import { ProductCartService } from '../services/models/product-cart';
import { ProductDtoQuantity } from '../services/models/product-dto-quantity';
import { TaskbarComponent } from '../taskbar/taskbar.component';

@Component({
  selector: 'app-cart',
  standalone: true,
  imports: [TaskbarComponent],
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
    const newQuantity = inputElement.valueAsNumber;
  
    if (isNaN(newQuantity)) {
      console.error('Invalid quantity entered.');
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
    this.loadProducts(); // Refresh the products list in the UI
  }
}
