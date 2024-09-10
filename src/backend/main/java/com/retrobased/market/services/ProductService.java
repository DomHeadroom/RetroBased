package com.retrobased.market.services;

import com.retrobased.market.entities.Product;
import com.retrobased.market.repositories.ProductRepository;
import com.retrobased.market.support.exceptions.ArgumentValueNotValidException;

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
public class ProductService {
    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public Product addProduct(@NonNull Product product) throws ArgumentValueNotValidException, ValueCannotBeEmptyException {
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


        return productRepository.save(product);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public void removeProduct(@NonNull Product product) throws ValueCannotBeEmptyException, ProductDontExistsException {
        if (product.getId() == null)
            throw new ValueCannotBeEmptyException();

        if (!productRepository.existsById(product.getId()))
            throw new ProductDontExistsException();

        productRepository.deleteById(product.getId());

    }

    // TODO SI DEVE CREARE UN NUOVO METODO PER LA RICERCA SORTATA
    @Transactional(readOnly = true)
    public List<Product> showAllProducts(int pageNumber, String sortBy) {
        Pageable paging = PageRequest.of(pageNumber, 20, Sort.by(sortBy));
        Page<Product> pagedResult = productRepository.findAll(paging);

        if (pagedResult.hasContent())
            return pagedResult.getContent();

        return new ArrayList<>();
    }

    @Transactional(readOnly = true)
    public List<Product> searchProduct(String name, int pageNumber, String sortBy) {
        Pageable paging = PageRequest.of(pageNumber, 20, Sort.by(sortBy));
        Page<Product> pagedResult = productRepository.find(name, paging);

        if (pagedResult.hasContent())
            return pagedResult.getContent();

        return new ArrayList<>();
    }

    public Product saveProduct(Product Product) {
        return productRepository.save(Product);
    }
}
