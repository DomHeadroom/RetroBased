package com.retrobased.market.controllers;

import com.retrobased.market.authentications.AuthenticationService;
import com.retrobased.market.dtos.CustomerAddressDTO;
import com.retrobased.market.services.CustomerAddressService;
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

import static com.retrobased.market.utils.ResponseUtils.createErrorResponse;

// TODO rinominare CustomerAddressController ?
@RestController
@RequestMapping("user/addresses")
@Validated
public class CustomerAddressController {

    private final CustomerAddressService customerAddressService;
    private final AuthenticationService authenticationService;

    public CustomerAddressController(
            CustomerAddressService customerAddressService,
            AuthenticationService authenticationService
    ) {
        this.customerAddressService = customerAddressService;
        this.authenticationService = authenticationService;
    }

    @GetMapping
    public ResponseEntity<?> getCustomerAddresses() {
        Optional<String> keycloakUserIdOpt = authenticationService.extractUserId();

        if (keycloakUserIdOpt.isEmpty())
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();

        String keycloakUserId = keycloakUserIdOpt.get();

        List<CustomerAddressDTO> randomProducts = customerAddressService.getAddresses(keycloakUserId);
        if (randomProducts.isEmpty())
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();

        return ResponseEntity.ok(randomProducts);
    }

    // TODO fare metodo get degli indirizzi

    /**
     * Adds an address to a customer.
     *
     * @param addressDTO The DTO object containing the address details. This must be a valid
     *                   {@link CustomerAddressDTO} object as per the validation constraints.
     * @return A {@link ResponseEntity} containing the created {@link CustomerAddressDTO}
     * if the operation is successful, or an error message if the customer or
     * country is not found.
     */
    @PostMapping
    // @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> addCustomerAddress(
            @RequestBody @Valid CustomerAddressDTO addressDTO
    ) {
        try {
            String keycloakUserId = authenticationService.extractUserId().orElseThrow(CustomerNotFoundException::new);

            CustomerAddressDTO customerAddress = customerAddressService.addAddress(keycloakUserId, addressDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(customerAddress);
        } catch (CustomerNotFoundException e) {
            return createErrorResponse("ERROR_USER_NOT_FOUND", HttpStatus.NOT_FOUND);
        } catch (CountryNotFoundException e) {
            return createErrorResponse("ERROR_COUNTRY_NOT_FOUND", HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping
    // @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> removeCustomerAddress(
            @RequestParam(value = "id") @NotNull UUID addressId
    ) {
        try {
            String keycloakUserId = authenticationService.extractUserId().orElseThrow(CustomerNotFoundException::new);

            customerAddressService.removeAddress(keycloakUserId, addressId);
            return ResponseEntity.ok(new ResponseMessage("SUCCESSFUL_ADDRESS_DELETION"));
        } catch (CustomerNotFoundException e) {
            return createErrorResponse("ERROR_USER_NOT_FOUND", HttpStatus.NOT_FOUND);
        } catch (AddressNotFoundException e) {
            return createErrorResponse("ERROR_ADDRESS_NOT_FOUND", HttpStatus.BAD_REQUEST);
        }
    }

}
