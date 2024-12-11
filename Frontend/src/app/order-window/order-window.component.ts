import { Component } from '@angular/core';
import { TaskbarComponent } from '../taskbar/taskbar.component';
import { OrderControllerService } from '../services/order-controller.service';
import { OrderDTO } from '../services/models/order-dto';

@Component({
  selector: 'app-order-window',
  standalone: true,
  imports: [TaskbarComponent],
  templateUrl: './order-window.component.html',
  styleUrl: './order-window.component.scss'
})
export class OrderWindowComponent {
  private orders: OrderDTO[] = [];

  constructor(private orderService: OrderControllerService){}

  ngOnInit(){
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
