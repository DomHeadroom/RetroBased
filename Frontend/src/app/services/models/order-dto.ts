export interface OrderDTO {
    id: string;
    address: {
      street: string;
      city: string;
    };
    createdAt: string;
    orderApprovedAt: string;
    orderDeliveredCarrierDate?: string;
    orderDeliveredCustomerDate?: string;
  }
  