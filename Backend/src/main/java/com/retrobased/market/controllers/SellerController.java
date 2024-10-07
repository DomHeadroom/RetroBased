package com.retrobased.market.controllers;

import com.retrobased.market.dtos.ProductDTO;
import com.retrobased.market.dtos.SellerDTO;
import com.retrobased.market.services.ProductSellerService;
import com.retrobased.market.services.SellerService;
import com.retrobased.market.utils.ResponseMessage;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;


@RestController
@RequestMapping("sellers")
@Validated
public class SellerController {
    private final SellerService sellerService;

    private final ProductSellerService productSellerService;

    public SellerController(
            SellerService sellerService,
            ProductSellerService productSellerService
    ) {
        this.sellerService = sellerService;
        this.productSellerService = productSellerService;
    }

    // TODO forse sta roba è da deprecare
    @PostMapping("public")
    public ResponseEntity<?> registerSeller(@RequestBody @Valid @NotNull SellerDTO seller) {
        sellerService.registerSeller(seller);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping("public/{seller}/products")
    public ResponseEntity<?> getSellerProducts(
            @PathVariable("seller") @NotNull UUID sellerId,
            @RequestParam(value = "page", defaultValue = "0") @Min(0) int pageNumber,
            @RequestParam(value = "s", defaultValue = "id") String sortBy) {

        if (!sellerService.exists(sellerId))
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseMessage("ERROR_USER_NOT_FOUND"));

        List<ProductDTO> result = productSellerService.getSellerProducts(sellerId, pageNumber, sortBy);

        if (result.isEmpty())
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        return ResponseEntity.ok(result);

    }

}
