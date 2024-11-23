import { Component, OnInit, signal } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import { TaskbarComponent } from '../taskbar/taskbar.component';
import { WindowComponent } from '../window/window.component';
import { NotificationComponent } from '../notification/notification.component';
import { CardComponent } from '../card/card.component';

import { NotificationService } from '../services/services/notification.service';
import { ProductControllerService } from '../services/services';
import { ProductDto } from '../services/models';

@Component({
  selector: 'app-home',
  standalone: true,
  imports: [RouterOutlet, NotificationComponent, TaskbarComponent, WindowComponent, CardComponent],
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.scss']
})
export class HomeComponent implements OnInit {
  notificationHeader: string = 'Login';
  notificationText: string = 'If you want to login, click here.';

  products = signal<ProductDto[]>([]);
  
  constructor(
    private notificationService: NotificationService,
    private productService: ProductControllerService,
  ) {}

  ngOnInit(): void {
    this.notificationService.showNotification(this.notificationHeader, this.notificationText, 92.200);
    this.getRandomProducts();
  }

  getRandomProducts(): void {
    this.productService.getRandomProducts().subscribe(
      (response: any) => {
        this.products.set(response as ProductDto[]);
        console.log('Random Products:', response);
      },
      (error) => {
        console.error('Error fetching random products:', error);
      }
    );
  }

  getProducts() {
    return this.products();
  }
}
