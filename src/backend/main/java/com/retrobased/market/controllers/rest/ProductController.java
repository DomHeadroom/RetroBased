package com.retrobased.market.controllers.rest;

import com.retrobased.market.entities.Product;
import com.retrobased.market.services.ProductService;
import com.retrobased.market.support.ResponseMessage;
import com.retrobased.market.support.exceptions.ArgumentValueNotValidException;
import jakarta.validation.constraints.Min;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

import java.util.List;

@RestController
@RequestMapping("/api/product")
@Validated
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping("/search")
    public ResponseEntity<?> searchProduct(
            @RequestParam(value = "k") String keyword,
            @RequestParam(value = "page", defaultValue = "0") @Min(0) int pageNumber,
            @RequestParam(value = "s", defaultValue = "id") String sortBy) {

        List<Product> result = productService.searchProduct(keyword, pageNumber, sortBy);
        if (result.isEmpty())
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();

        return ResponseEntity.ok(result);
    }

    // aggiunta di un prodotto al db
    @PostMapping("/add")
    public ResponseEntity<?> addProduct(@RequestBody @Valid Product product) {
        try {
            Product newProduct = productService.addProduct(product);
            return ResponseEntity.status(HttpStatus.CREATED).body(newProduct);
        } catch (ArgumentValueNotValidException e) {
            return ResponseEntity.badRequest().body(new ResponseMessage("ERROR_ARGUMENT_VALUE_NOT_VALID"));
        }
    }

    /*@GetMapping("/filter")
    public ResponseEntity advancedSearchProduct(
            @RequestParam(value = "page", defaultValue = "0") int pageNumber,
            @RequestParam(value = "sort", defaultValue = "id") String sortBy,
            @RequestParam(value = "keyword") String keyword) {

        List<Prodotto> result = serviceProduct.searchProducts(keyword,pageNumber, sortBy);
        if (result.isEmpty())
            return new ResponseEntity<>(new ResponseMessage("No results!"), HttpStatus.OK);

        return new ResponseEntity<>(result, HttpStatus.OK);
   }*/

}
