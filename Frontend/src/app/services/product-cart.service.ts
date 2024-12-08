import { Injectable } from '@angular/core';
import { ProductDtoQuantity } from './models/product-dto-quantity';

@Injectable({
  providedIn: 'root',
})
export class ProductCartService {
  private addressId: string = '';
  private products: ProductDtoQuantity[] = [];

  setAddressId(address: string): void {
    this.addressId = address;
  }

  getAddressId(): string {
    return this.addressId;
  }

  addProduct(product: ProductDtoQuantity): void {
    const existingProduct = this.products.find((p) => p.product.id === product.product.id);

    if (existingProduct) {
      existingProduct.quantity += product.quantity;
    } else {
      this.products.push(product);
    }
  }

  updateProductQuantity(productId: string, quantity: number): void {
    const product = this.products.find((p) => p.product.id === productId);

    if (product) {
      if (quantity > 0) {
        product.quantity = quantity;
      } else {
        this.removeProduct(productId);
      }
    }
  }

  removeProduct(productId: string): void {
    this.products = this.products.filter((p) => p.product.id !== productId);
  }

  getProducts(): ProductDtoQuantity[] {
    return [...this.products];
  }

  clearCart(): void {
    this.addressId = '';
    this.products = [];
  }

  getCartSummary(): { addressId: string; products: ProductDtoQuantity[] } {
    return {
      addressId: this.addressId,
      products: [...this.products],
    };
  }

  getTotalPrice(): number {
    return this.products.reduce((total, product) => {
      return total + product.quantity * product.product.salePrice;
    }, 0);
  }
  
}