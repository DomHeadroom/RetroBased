import { Injectable } from '@angular/core';
import { ProductDtoQuantity } from './product-dto-quantity';

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

  updateProductQuantity(productId: string, quantity: number): boolean {
    const product = this.products.find((p) => p.product.id === productId);

    if (product) {
      if (quantity > 0) {
        product.quantity = quantity;
        return true;
      } else {
        this.removeProduct(productId);
        return true;
      }
    }
    return false;
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
}