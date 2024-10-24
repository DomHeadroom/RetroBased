package com.retrobased.market.services;

import com.retrobased.market.entities.Customer;
import com.retrobased.market.entities.Seller;
import com.retrobased.market.utils.exceptions.UserSaveException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class KeycloakService {

    private final SellerService sellerService;
    private final CustomerService customerService;

    public KeycloakService(
            SellerService sellerService,
            CustomerService customerService
    ) {
        this.sellerService = sellerService;
        this.customerService = customerService;
    }

    public void saveUserFromKeycloak(Map<String, Object> userClaims) throws UserSaveException {
        String userId = (String) userClaims.get("sub");
        String email = (String) userClaims.get("email");
        String givenName = (String) userClaims.get("given_name");
        String familyName = (String) userClaims.get("family_name");

        List<String> roles = null;
        Object rolesObject = userClaims.get("roles");

        if (rolesObject instanceof List<?>)
            roles = ((List<?>) rolesObject).stream()
                    .filter(String.class::isInstance)
                    .map(String.class::cast)
                    .toList();
        try {
            if (roles != null && roles.contains("seller")) {
                Seller seller = sellerService.findByKeycloakId(userId);

                if (seller == null) {
                    seller = new Seller();
                    seller.setKeycloakId(userId);
                    seller.setEmail(email);
                    seller.setFirstName(givenName);
                    seller.setLastName(familyName);
                    sellerService.save(seller);
                }

            } else {
                Customer customer = customerService.findByKeycloakId(userId);

                if (customer == null) {
                    customer = new Customer();
                    customer.setKeycloakId(userId);
                    customer.setEmail(email);
                    customer.setFirstName(givenName);
                    customer.setLastName(familyName);
                    customerService.save(customer);
                }
            }
        } catch (Exception e) {
            throw new UserSaveException();
        }
    }
}




