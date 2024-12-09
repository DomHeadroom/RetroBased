import { Injectable } from '@angular/core';
import { ProductDtoQuantity } from './models/product-dto-quantity';

@Injectable({
  providedIn: 'root',
})
export class ProductCartService {
  private addressId: string = '';
  private products: ProductDtoQuantity[] = [];
  public totalPrice: number = 0;

  setAddressId(address: string): void {
    this.addressId = address;
  }

  getAddressId(): string {
    return this.addressId;
  }

  addProduct(product: ProductDtoQuantity): void {
    const existingProduct = this.products.find((p) => p.product.id === product.product.id);

    if (existingProduct) {
      this.totalPrice += product.quantity * product.product.salePrice;
      existingProduct.quantity += product.quantity;
    } else {
      this.products.push(product);
      this.totalPrice += product.quantity * product.product.salePrice;
    }
  }

  updateProductQuantity(productId: string, quantity: number): void {
    const product = this.products.find((p) => p.product.id === productId);

    if (product) {
      if (quantity > 0) {
        const quantityDifference = quantity - product.quantity;
        this.totalPrice += quantityDifference * product.product.salePrice;
        product.quantity = quantity;
      } else {
        this.removeProduct(productId);
      }
    }
  }

  removeProduct(productId: string): void {
    const productIndex = this.products.findIndex((p) => p.product.id === productId);

    if (productIndex !== -1) {
      const product = this.products[productIndex];
      this.totalPrice -= product.quantity * product.product.salePrice;

      this.products.splice(productIndex, 1);
    }
  }

  getProducts(): ProductDtoQuantity[] {
    return [...this.products];
  }

  clearCart(): void {
    this.addressId = '';
    this.products = [];
    this.totalPrice = 0;
  }

  getCartSummary(): { addressId: string; products: ProductDtoQuantity[]; totalPrice: number } {
    return {
      addressId: this.addressId,
      products: [...this.products],
      totalPrice: this.totalPrice,
    };
  }
  
}