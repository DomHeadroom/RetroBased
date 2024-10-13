package com.retrobased.market.controllers;

import com.retrobased.market.authentications.AuthenticationService;
import com.retrobased.market.dtos.CustomerAddressDTO;
import com.retrobased.market.entities.Customer;
import com.retrobased.market.services.CustomerAddressService;
import com.retrobased.market.services.CustomerService;
import com.retrobased.market.utils.ResponseMessage;
import com.retrobased.market.utils.exceptions.AddressNotFoundException;
import com.retrobased.market.utils.exceptions.CountryNotFoundException;
import com.retrobased.market.utils.exceptions.CustomerNotFoundException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

// TODO rinominare CustomerAddressController ?
@RestController
@RequestMapping("user/addresses")
@Validated
public class CustomerAddressController {

    private final CustomerAddressService customerAddressService;
    private final AuthenticationService authenticationService;
    private final CustomerService customerService;

    public CustomerAddressController(
            CustomerAddressService customerAddressService,
            AuthenticationService authenticationService,
            CustomerService customerService) {
        this.customerAddressService = customerAddressService;
        this.authenticationService = authenticationService;
        this.customerService = customerService;
    }

    /**
     * Retrieves a list of addresses associated with the authenticated customer.
     * <p>
     * This method extracts the user ID from the JWT token to identify the customer and fetches
     * their addresses. If the customer is not authenticated, it returns a <strong>401 Unauthorized</strong>
     * response. If no addresses are found for the customer, a <strong>204 No Content</strong> response is returned.
     * </p>
     *
     * @return A {@link ResponseEntity} containing a list of {@link CustomerAddressDTO} if the operation is successful,
     * or an error message if the customer is not authenticated or no addresses are found.
     * <ul>
     *     <li><strong>200 OK</strong> – If addresses are successfully retrieved.</li>
     *     <li><strong>204 No Content</strong> – If the customer has no addresses in the system.</li>
     *     <li><strong>401 Unauthorized</strong> – If the user ID cannot be extracted from the JWT token.</li>
     * </ul>
     */
    @GetMapping
    public ResponseEntity<?> getCustomerAddresses() throws CustomerNotFoundException {
        Optional<String> keycloakUserIdOpt = authenticationService.extractUserId();

        if (keycloakUserIdOpt.isEmpty())
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();

        String keycloakUserId = authenticationService.extractUserId().orElseThrow(CustomerNotFoundException::new);
        Customer customer = customerService.findByKeycloakId(keycloakUserId);

        List<CustomerAddressDTO> randomProducts = customerAddressService.getAddresses(customer.getId());
        if (randomProducts.isEmpty())
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();

        return ResponseEntity.ok(randomProducts);
    }

    /**
     * Adds an address to a customer.
     * <p>
     * This method receives the address details in the request body, attempts to add the address
     * for the customer identified by the user ID extracted from the JWT token. If the customer or
     * country is not found, the method returns an appropriate error response.
     * </p>
     *
     * @param addressDTO The DTO object containing the address details. This must be a valid
     *                   {@link CustomerAddressDTO} object as per the validation constraints.
     * @return A {@link ResponseEntity} containing the created {@link CustomerAddressDTO}
     * if the operation is successful, or an error message if the customer or
     * country is not found.
     * <ul>
     *     <li><strong>201 Created</strong> – If the address was successfully added.</li>
     *     <li><strong>404 Not Found</strong> – If the customer or country is not found.</li>
     *     <li><strong>400 Bad Request</strong> – If the provided country is invalid.</li>
     * </ul>
     */
    @PostMapping
    // @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> addCustomerAddress(
            @RequestBody @Valid CustomerAddressDTO addressDTO
    ) throws CustomerNotFoundException, CountryNotFoundException {
        String keycloakUserId = authenticationService.extractUserId().orElseThrow(CustomerNotFoundException::new);

        CustomerAddressDTO customerAddress = customerAddressService.addAddress(keycloakUserId, addressDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(customerAddress);
    }

    /**
     * Removes a customer's address.
     * <p>
     * This method takes the address ID from the request parameters and attempts to delete the
     * specified address for the customer identified by the user ID extracted from the JWT token.
     * If the customer or address is not found, the method returns an appropriate error response.
     * </p>
     *
     * @param addressId The UUID of the address to be removed; must be provided and cannot be null.
     * @return A {@link ResponseEntity} containing a success message if the operation is successful,
     * or an error message if the customer or address is not found.
     * <ul>
     *     <li><strong>200 OK</strong> – If the address was successfully removed.</li>
     *     <li><strong>404 Not Found</strong> – If the customer is not found.</li>
     *     <li><strong>400 Bad Request</strong> – If the address ID does not correspond to an existing address.</li>
     * </ul>
     */
    @DeleteMapping
    // @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> removeCustomerAddress(
            @RequestParam(value = "id") @NotNull UUID addressId
    ) throws AddressNotFoundException, CustomerNotFoundException {
        String keycloakUserId = authenticationService.extractUserId().orElseThrow(CustomerNotFoundException::new);

        Customer customer = customerService.findByKeycloakId(keycloakUserId);

        customerAddressService.removeAddress(customer.getId(), addressId);
        return ResponseEntity.ok(new ResponseMessage("SUCCESSFUL_ADDRESS_DELETION"));
    }

}
