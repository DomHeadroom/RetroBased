package com.retrobased.market.services;

import com.retrobased.market.entities.Customer;
import com.retrobased.market.entities.Seller;

import java.util.List;
import java.util.Map;


public class KeycloakService {

    private final SellerService sellerService;
    private final CustomerService customerService;

    public KeycloakService(
            SellerService sellerService,
            CustomerService customerService
    ){
        this.sellerService = sellerService;
        this.customerService = customerService;
    }

    public void saveUserFromKeycloak(Map<String, Object> userClaims) {
        String userId = (String) userClaims.get("sub");
        String email = (String) userClaims.get("email");
        String givenName = (String) userClaims.get("given_name");
        String familyName = (String) userClaims.get("family_name");

        List<String> roles = (List<String>) userClaims.get("roles");

        if (roles != null && roles.contains("seller")) {
            Seller seller = sellerService.findByKeycloakId(userId);

            if (seller == null) {
                seller = new Seller();
                seller.setKeycloakId(userId);
                seller.setEmail(email);
                seller.setFirstName(givenName);
                seller.setLastName(familyName);
                sellerService.save(seller);
            } else {
                seller.setFirstName(givenName);
                seller.setLastName(familyName);
                sellerService.save(seller);
            }

        } else {
            Customer customer = customerService.findByKeycloakId(userId);

            if (customer == null) {
                // Create new user
                customer = new Customer();
                customer.setKeycloakId(userId);
                customer.setEmail(email);
                customer.setFirstName(givenName);
                customer.setLastName(familyName);
                customerService.save(customer);
            } else {
                customer.setFirstName(givenName);
                customer.setLastName(familyName);
                customerService.save(customer);
            }
        }
    }
}




