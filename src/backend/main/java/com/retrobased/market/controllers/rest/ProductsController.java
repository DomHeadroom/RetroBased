package com.retrobased.market.controllers.rest;

import com.retrobased.market.entities.Product;
import com.retrobased.market.services.ProductService;
import com.retrobased.market.support.ResponseMessage;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/cart")
public class ProductsController {

    private final ProductService serviceProduct;

    public ProductsController(ProductService serviceProduct) {
        this.serviceProduct = serviceProduct;
    }

    @GetMapping("/filter")
    public ResponseEntity<?> getAll(
            @RequestParam(value = "page", defaultValue = "0") int pageNumber,
            @RequestParam(value = "sort", defaultValue = "id") String sortBy,
            @RequestParam(value = "keyword") String keyword) {

        List<Product> result = serviceProduct.searchProduct(keyword,pageNumber, sortBy);
        if (result.isEmpty())
            return new ResponseEntity<>(new ResponseMessage("No results!"), HttpStatus.OK);

        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @PostMapping("/add")
    public ResponseEntity<?> addProdotto(@RequestBody @Valid Product prodotto) {
        try {
            Product newProdotto = serviceProduct.saveProduct(prodotto);
            return new ResponseEntity<>(newProdotto, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>("Errore durante l'aggiunta del prodotto", HttpStatus.BAD_REQUEST);
        }
    }

//    @GetMapping("/filter")
//    public ResponseEntity getAll(
//            @RequestParam(value = "page", defaultValue = "0") int pageNumber,
//            @RequestParam(value = "sort", defaultValue = "id") String sortBy,
//            @RequestParam(value = "keyword") String keyword) {
//
//        List<Prodotto> result = serviceProduct.searchProducts(keyword,pageNumber, sortBy);
//        if (result.isEmpty())
//            return new ResponseEntity<>(new ResponseMessage("No results!"), HttpStatus.OK);
//
//        return new ResponseEntity<>(result, HttpStatus.OK);
//    }


}
