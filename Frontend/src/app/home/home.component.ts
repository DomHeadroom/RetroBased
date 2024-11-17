import { Component } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import { TaskbarComponent } from '../taskbar/taskbar.component'
import { WindowComponent } from '../window/window.component'
import { NotificationComponent } from '../notification/notification.component';
import { CardComponent } from '../card/card.component';

import { NotificationService } from '../services/services/notification.service';
import { ProductControllerService } from '../services/services';
import { Observable } from 'rxjs';
import { ProductDto } from '../services/models';

@Component({
  selector: 'app-home',
  standalone: true,
  imports: [RouterOutlet,NotificationComponent, TaskbarComponent, WindowComponent, CardComponent],
  templateUrl: './home.component.html',
  styleUrl: './home.component.scss'
})

export class HomeComponent {
  notificationHeader: string = 'Login';
  notificationText: string = 'If you want to login, click here.';

  products$: Observable<any> | undefined;

  constructor(
    private notificationService: NotificationService,
    private productService: ProductControllerService
  ) {
    this.products$ = this.productService.searchProducts({ k: 'sample product', page: 0 });
    console.log(this.products$);
  }

  ngOnInit() {
    this.notificationService.showNotification(this.notificationHeader, this.notificationText, 92.200);
  }

}
