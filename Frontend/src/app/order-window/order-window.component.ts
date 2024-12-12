import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { TaskbarComponent } from '../taskbar/taskbar.component';
import { OrderControllerService } from '../services/order-controller.service';
import { OrderDTO } from '../services/models/order-dto';
import { Router } from '@angular/router';

@Component({
  selector: 'app-order-window',
  standalone: true,
  imports: [TaskbarComponent, CommonModule],
  templateUrl: './order-window.component.html',
  styleUrl: './order-window.component.scss',
})
export class OrderWindowComponent {
  protected orders: OrderDTO[] = [];

  constructor(private orderService: OrderControllerService,
    private router: Router
  ) {}

  ngOnInit() {
    const attemptedUrl = this.router.url;
    localStorage.setItem('redirectUrl', attemptedUrl);
    this.orderService.getOrder().subscribe({
      next: (response: any) => {
        if (response != null) {
          this.orders = response as OrderDTO[];
        }
      },
      error: (err) => {
        console.error('Error fetching orders', err);
      },
    });
  }
}
