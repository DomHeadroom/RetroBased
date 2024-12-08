import { Component, OnInit } from '@angular/core';
import { FormsModule, NgForm } from '@angular/forms';
import { CustomerAddressControllerService } from '../services/customer-address-controller.service';
import { ProductCartService } from '../services/product-cart.service';
import { CustomerAddressDto } from '../services/models/customer-address-dto';
import { AddCustomerAddress$Params } from '../services/fn/customer-address-controller/add-customer-address';

@Component({
  selector: 'app-buy-form',
  standalone: true,
  imports: [FormsModule],
  templateUrl: './buy-form.component.html',
  styleUrl: './buy-form.component.scss',
})
export class BuyFormComponent implements OnInit {
  activeTab: string = 'address';
  customerAddresses: CustomerAddressDto[] = [];
  selectedAddress: CustomerAddressDto | null = null;
  isSummaryEnabled: boolean = false;
  isAddressSet: boolean = false;
  isPaymentSet: boolean = false;
  totalPrice: number = 0;

  inputAddress = {
    address1: '',
    address2: '',
    city: '',
    zip: '',
    country: 0,
  };

  payment = {
    cardBrand: '',
    cardNumber: '',
  };

  constructor(
    private customerAddressController: CustomerAddressControllerService,
    private cartService: ProductCartService
  ) {}

  ngOnInit() {
    this.customerAddressController.getCustomerAddresses().subscribe({
      next: (response: any) => {
        if (response != null) {
          this.customerAddresses = response as CustomerAddressDto[];
        }
      },
      error: (err) => {
        console.error('Error fetching customer addresses', err);
      },
    });
    this.totalPrice = this.cartService.getTotalPrice();
  }

  setPayment() {
    if (this.payment.cardBrand && this.payment.cardNumber) {
      this.isPaymentSet = true;
      this.checkSummaryAvailability();
    } else {
      this.isPaymentSet = false;
    }
  }

  setAddress() {
    if (
      this.inputAddress.address1 &&
      this.inputAddress.city &&
      this.inputAddress.country > 0 &&
      this.inputAddress.zip
    ) {
      this.sendAddress();
    }
  }

  switchTab(tab: string): void {
    this.activeTab = tab;
  }

  onAddressSelect(address: CustomerAddressDto): void {
    this.selectedAddress = address;
    this.isAddressSet = true;
    this.checkSummaryAvailability();
  }

  checkSummaryAvailability(): void {
    if (this.isAddressSet && this.isPaymentSet) {
      this.isSummaryEnabled = true;
    } else {
      this.isSummaryEnabled = false;
    }
  }

  sendAddress(){
    const customerAddressDto: CustomerAddressDto = {
      addressLine1: this.inputAddress.address1,
      addressLine2: this.inputAddress.address2 || undefined,
      city: this.inputAddress.city,
      postalCode: this.inputAddress.zip,
      country: this.inputAddress.country,
    };

    const params: AddCustomerAddress$Params = {
      body: customerAddressDto,
    };
    console.log(customerAddressDto);

    this.customerAddressController.addCustomerAddress(params).subscribe({
      next: (response) => {
        console.log('Address added successfully', response);
  
        this.customerAddresses.push(customerAddressDto);
        
        this.inputAddress = {
          address1: '',
          address2: '',
          city: '',
          zip: '',
          country: 0,
        };
        
        this.selectedAddress = customerAddressDto;
        this.isAddressSet = true;
        this.checkSummaryAvailability();
      },
      error: (err) => {
        console.error('Error adding address', err);
      },
    });

  }
}
