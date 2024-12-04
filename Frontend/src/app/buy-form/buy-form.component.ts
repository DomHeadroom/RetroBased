import { Component, OnInit } from '@angular/core';
import { CustomerAddressControllerService } from '../services/customer-address-controller.service';
import { ProductCartService } from '../services/product-cart.service';
import { CustomerAddressDto } from '../services/models/customer-address-dto';

@Component({
  selector: 'app-buy-form',
  standalone: true,
  imports: [],
  templateUrl: './buy-form.component.html',
  styleUrl: './buy-form.component.scss'
})
export class BuyFormComponent implements OnInit {
  activeTab: string = 'address';
  customerAddresses: CustomerAddressDto[] = [];
  selectedAddress: CustomerAddressDto | null = null;
  selectedCard: { brand: string, number: string } | null = null;
  isSummaryEnabled: boolean = false;
  totalPrice: number = 0;

  constructor(private customerAddressController: CustomerAddressControllerService,
    private cartService: ProductCartService
  ){
  }

  ngOnInit(){
    this.customerAddressController.getCustomerAddresses().subscribe({
      next: (response: any) => {
        this.customerAddresses = response as CustomerAddressDto[];
      },
      error: (err) => {
        console.error('Error fetching customer addresses', err);
      }
    });
    this.totalPrice = this.cartService.getTotalPrice();
  }
  
  switchTab(tab: string): void {
    this.activeTab = tab;
  }

  onAddressSelect(address: CustomerAddressDto): void {
    this.selectedAddress = address;
    this.checkSummaryAvailability();
  }

  onCardSelect(event: Event, cardNumber: string): void {
    const selectElement = event.target as HTMLSelectElement;
    const brand = selectElement.value;
    this.selectedCard = { brand, number: cardNumber };
    this.checkSummaryAvailability();
  }

  checkSummaryAvailability(): void {
    if (this.selectedAddress && this.selectedCard) {
      this.isSummaryEnabled = true;
    } else {
      this.isSummaryEnabled = false;
    }
  }
}
