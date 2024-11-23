import { Injectable, signal } from '@angular/core';
import { ProductDto } from './product-dto';

@Injectable({
  providedIn: 'root',
})
export class ProductDisplay {
  products = signal<ProductDto[]>([]);

  setProducts(newProducts: ProductDto[]) {
    this.products.set(newProducts);
  }

  getProducts() {
    return this.products();
  }
}