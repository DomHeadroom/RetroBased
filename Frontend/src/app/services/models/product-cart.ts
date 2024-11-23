import { Injectable } from '@angular/core';
import { ProductQuantityDto } from './product-quantity-dto';

@Injectable({
  providedIn: 'root',
})
export class ProductCartService {
  private addressId: string = '';
  private products: ProductQuantityDto[] = [];

  setAddressId(address: string): void {
    this.addressId = address;
  }

  getAddressId(): string {
    return this.addressId;
  }

  addProduct(product: ProductQuantityDto): void {
    const existingProduct = this.products.find((p) => p.id === product.id);

    if (existingProduct) {
      existingProduct.quantity += product.quantity;
    } else {
      this.products.push(product);
    }
  }

  updateProductQuantity(productId: string, quantity: number): boolean {
    const product = this.products.find((p) => p.id === productId);

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
    this.products = this.products.filter((p) => p.id !== productId);
  }

  getProducts(): ProductQuantityDto[] {
    return [...this.products];
  }

  clearCart(): void {
    this.addressId = '';
    this.products = [];
  }

  getCartSummary(): { addressId: string; products: ProductQuantityDto[] } {
    return {
      addressId: this.addressId,
      products: [...this.products],
    };
  }
}
