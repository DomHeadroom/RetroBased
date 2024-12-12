export interface OrderDTO {
    id: string;
    address: {
      addressLine1: string;
      city: string;
    };
    createdAt: string;
    orderApprovedAt: string;
    orderDeliveredCarrierDate?: string;
    orderDeliveredCustomerDate?: string;
  }
  