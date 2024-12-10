import { Injectable } from '@angular/core';
import { ProductDtoQuantity } from './models/product-dto-quantity';
import { MakeOrder$Params } from '../services/fn/order-controller/make-order';
import { OrderControllerService } from './order-controller.service';

@Injectable({
  providedIn: 'root',
})
export class ProductCartService {
  private addressId: string = '';
  private products: ProductDtoQuantity[] = [];
  public totalPrice: number = 0;

  constructor(private orderService: OrderControllerService){}

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

  order(): boolean{
    if(!this.addressId || this.products.length<=0){
      return false;
    }

    const mappedProducts = this.products.map(product => {
      if (!product.product.id) {
        throw new Error(`Product ID is missing for product with quantity: ${product.quantity}`);
      }
  
      return {
        id: product.product.id,
        quantity: product.quantity,
      };
    });
  
    const mappedParams: MakeOrder$Params = {
      body: {
        address: this.addressId,
        products: mappedProducts,
      },
    };
    this.orderService.makeOrder(mappedParams).subscribe({
      next: (response: any) => {
        this.clearCart();
        return true;
      },
      error: (err) => {
        return false;
      },
    });
    return false;
  }
  
}