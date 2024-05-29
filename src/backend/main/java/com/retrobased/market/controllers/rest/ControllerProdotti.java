package com.retrobased.market.controllers.rest;

import com.retrobased.market.entities.Prodotto;
import com.retrobased.market.services.ServiceProdotto;
import com.retrobased.market.support.ResponseMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class ControllerProdotti {

    @Autowired
    private ServiceProdotto serviceProduct;

    @GetMapping("/filter")
    public ResponseEntity getAll(
            @RequestParam(value = "page", defaultValue = "0") int pageNumber,
            @RequestParam(value = "sort", defaultValue = "id") String sortBy,
            @RequestParam(value = "keyword") String keyword) {

        List<Prodotto> result = serviceProduct.searchProducts(keyword,pageNumber, sortBy);
        if (result.isEmpty())
            return new ResponseEntity<>(new ResponseMessage("No results!"), HttpStatus.OK);

        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @GetMapping("/filter")
    public ResponseEntity getAll(
            @RequestParam(value = "page", defaultValue = "0") int pageNumber,
            @RequestParam(value = "sort", defaultValue = "id") String sortBy,
            @RequestParam(value = "keyword") String keyword) {

        List<Prodotto> result = serviceProduct.searchProducts(keyword,pageNumber, sortBy);
        if (result.isEmpty())
            return new ResponseEntity<>(new ResponseMessage("No results!"), HttpStatus.OK);

        return new ResponseEntity<>(result, HttpStatus.OK);
    }


}
