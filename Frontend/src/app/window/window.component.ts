import { Component } from '@angular/core';

import { CardComponent } from '../card/card.component';
import { ProductDisplay } from '../services/models/product-display';

@Component({
  selector: 'app-window',
  standalone: true,
  imports: [CardComponent],
  templateUrl: './window.component.html',
  styleUrl: './window.component.scss'
})
export class WindowComponent {
  
  constructor(
    private productDisplay: ProductDisplay
  ) {}

  ngOnInit(): void {
  }

  getProducts() {
    return this.productDisplay.products();
  }

}
