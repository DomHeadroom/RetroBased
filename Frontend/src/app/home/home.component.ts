import { Component, OnInit } from '@angular/core';
import { TaskbarComponent } from '../taskbar/taskbar.component';
import { WindowComponent } from '../window/window.component';
import { NotificationComponent } from '../notification/notification.component';

import { NotificationService } from '../services/services/notification.service';
import { ProductControllerService } from '../services/services';
import { ProductDto } from '../services/models';
import { ProductDisplay } from '../services/models/product-display';

@Component({
  selector: 'app-home',
  standalone: true,
  imports: [NotificationComponent, TaskbarComponent, WindowComponent],
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.scss']
})
export class HomeComponent implements OnInit {
  notificationHeader: string = 'Login';
  notificationText: string = 'If you want to login, click here.';
  
  constructor(
    private productService: ProductControllerService,
    private notificationService: NotificationService,
    private productDisplay: ProductDisplay
  ) {}

  ngOnInit(): void {
    this.notificationService.showNotification(this.notificationHeader, this.notificationText, 92.200);
    this.getRandomProducts();
  }
  
  getRandomProducts(): void {
    this.productService.getRandomProducts().subscribe(
      (response: any) => {
        const products = response as ProductDto[];
        this.productDisplay.setProducts(products);
      },
      (error) => {
        console.error('Error fetching random products:', error);
      }
    );
  }

}
