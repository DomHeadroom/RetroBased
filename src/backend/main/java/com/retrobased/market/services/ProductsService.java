package com.retrobased.market.services;

import com.retrobased.market.entities.Products;
import com.retrobased.market.repositories.ProductsRepository;
import com.retrobased.market.support.exceptions.ArgumentValueNotValidException;
import com.retrobased.market.support.exceptions.ProductIdAlreadyUsedException;

import com.retrobased.market.support.exceptions.ProductDontExistsException;
import com.retrobased.market.support.exceptions.ValueCannotBeEmptyException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class ProductsService {
    private final ProductsRepository productsRepository;

    public ProductsService(ProductsRepository productsRepository) {
        this.productsRepository = productsRepository;
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public Products addProducts(@NonNull Products product) throws ArgumentValueNotValidException, ValueCannotBeEmptyException {
        if (product.getSlug() == null ||
                product.getProductName() == null ||
                product.getSalePrice() == null ||
                product.getQuantity() == null ||
                product.getShortDescription() == null ||
                product.getProductDescription() == null
        )
            throw new ValueCannotBeEmptyException();

        if (product.getQuantity() < 0 ||
                product.getSalePrice().signum() == -1
        )
            throw new ArgumentValueNotValidException();


        return productsRepository.save(product);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public void removeProducts(@NonNull Products products) throws ValueCannotBeEmptyException, ProductDontExistsException {
        if (products.getId() == null)
            throw new ValueCannotBeEmptyException();

        if (!productsRepository.existsById(products.getId()))
            throw new ProductDontExistsException();

        productsRepository.deleteById(products.getId());

    }

    // TODO SI DEVE CREARE UN NUOVO METODO PER LA RICERCA SORTATA
    @Transactional(readOnly = true)
    public List<Products> showAllProducts(int pageNumber, String sortBy) {
        Pageable paging = PageRequest.of(pageNumber, 20, Sort.by(sortBy));
        Page<Products> pagedResult = productsRepository.findAll(paging);

        if (pagedResult.hasContent())
            return pagedResult.getContent();

        return new ArrayList<>();
    }

    @Transactional(readOnly = true)
    public List<Products> searchProducts(String name, int pageNumber, String sortBy) {
        Pageable paging = PageRequest.of(pageNumber, 20, Sort.by(sortBy));
        Page<Products> pagedResult = productsRepository.find(name, paging);

        if (pagedResult.hasContent())
            return pagedResult.getContent();

        return new ArrayList<>();
    }

    public Products saveProducts(Products Products) {
        return productsRepository.save(Products);
    }
}
