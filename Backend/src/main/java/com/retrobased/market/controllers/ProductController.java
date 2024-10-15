package com.retrobased.market.controllers;

import com.retrobased.market.authentications.AuthenticationService;
import com.retrobased.market.dtos.ProductCategoryDTO;
import com.retrobased.market.dtos.ProductDTO;
import com.retrobased.market.entities.Attribute;
import com.retrobased.market.entities.Category;
import com.retrobased.market.entities.Product;
import com.retrobased.market.entities.Seller;
import com.retrobased.market.entities.Tag;
import com.retrobased.market.mappers.ProductMapper;
import com.retrobased.market.services.AttributeService;
import com.retrobased.market.services.CategoryService;
import com.retrobased.market.services.CustomerService;
import com.retrobased.market.services.ProductAttributeService;
import com.retrobased.market.services.ProductCategoryService;
import com.retrobased.market.services.ProductSellerService;
import com.retrobased.market.services.ProductService;
import com.retrobased.market.services.ProductTagService;
import com.retrobased.market.services.SellerService;
import com.retrobased.market.utils.exceptions.ArgumentValueNotValidException;
import com.retrobased.market.utils.exceptions.AttributeNotFoundException;
import com.retrobased.market.utils.exceptions.CategoryNotFoundException;
import com.retrobased.market.utils.exceptions.CustomerNotFoundException;
import com.retrobased.market.utils.exceptions.ProductNotFoundException;
import com.retrobased.market.utils.exceptions.SellerNotFoundException;
import com.retrobased.market.utils.exceptions.TagNotFoundException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("product")
@Validated
public class ProductController {

    private final ProductService productService;
    private final ProductSellerService productSellerService;
    private final CategoryService categoryService;
    private final AttributeService attributeService;
    private final ProductCategoryService productCategoryService;
    private final ProductAttributeService productAttributeService;
    private final ProductTagService productTagService;
    private final AuthenticationService authenticationService;
    private final CustomerService customerService;
    private final SellerService sellerService;

    public ProductController(
            ProductService productService,
            ProductSellerService productSellerService,
            ProductCategoryService productCategoryService,
            CategoryService categoryService,
            AttributeService attributeService,
            ProductAttributeService productAttributeService,
            ProductTagService productTagService,
            AuthenticationService authenticationService,
            CustomerService customerService, SellerService sellerService) {
        this.productService = productService;
        this.productSellerService = productSellerService;
        this.productCategoryService = productCategoryService;
        this.categoryService = categoryService;
        this.attributeService = attributeService;
        this.productAttributeService = productAttributeService;
        this.productTagService = productTagService;
        this.authenticationService = authenticationService;
        this.customerService = customerService;
        this.sellerService = sellerService;
    }

    /**
     * <p>This method retrieves a list of random products to be displayed on the public homepage.</p>
     * <p>
     * The number of products returned can be specified via the {@code limit} parameter,
     * which defaults to 10. If no products are available, a <strong>204 No Content</strong>
     * response is returned.
     * </p>
     *
     * @param pageNumber the page number of the products to retrieve, starting from 0;
     *                   defaults to 0 if not specified, must be a non-negative integer
     * @return A {@link ResponseEntity} containing a list of {@link ProductDTO} objects
     * representing the random products. If no products are found, it returns a
     * status of <strong>204 No Content</strong>.
     * <ul>
     *     <li><strong>200 OK</strong> – If random products are successfully retrieved.</li>
     *     <li><strong>204 No Content</strong> – If no products are available.</li>
     * </ul>
     */
    @GetMapping("public/products")
    public ResponseEntity<?> getRandomProducts(
            @RequestParam(value = "page", defaultValue = "0") @Min(0) int pageNumber) {

        List<ProductDTO> randomProducts = productService.getRandomProducts(pageNumber);
        if (randomProducts.isEmpty())
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();

        return ResponseEntity.ok(randomProducts);
    }

    /**
     * <p>This method retrieves a specific product based on the provided product ID
     * for public page display.</p>
     * <p>
     * If the product exists, it returns the corresponding {@link ProductDTO}.
     * If the product does not exist, it returns a status of <strong>204 No Content</strong>.
     * </p>
     *
     * @param productId The UUID of the product to be fetched; must not be {@code null}.
     * @return A {@link ResponseEntity} containing the {@link ProductDTO} if the product is found,
     * or a status of <strong>204 No Content</strong> if the product does not exist.
     * <ul>
     *     <li><strong>200 OK</strong> – If the product is successfully retrieved.</li>
     *     <li><strong>204 No Content</strong> – If the product with the specified ID does not exist.</li>
     * </ul>
     */
    @GetMapping("public/{productId}")
    public ResponseEntity<?> getProduct(
            @PathVariable @NotNull UUID productId) {

        Optional<ProductDTO> product = productService.getProduct(productId);
        if (product.isEmpty())
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();

        return ResponseEntity.ok(product);
    }

    /**
     * <p>Searches for products based on the provided keyword. Supports pagination and sorting.</p>
     *
     * <p>This method allows clients to search for products using a keyword and optionally specify
     * sorting and pagination. The sort field defaults to "id" and the page number defaults to 0.</p>
     *
     * @param keyword    The search keyword used to filter products; must not be {@code null}.
     * @param pageNumber The page number for paginated results (default is 0); must be a non-negative integer.
     * @param sortBy     The field by which to sort the products (default is "id"); should match an existing product field.
     * @return A {@link ResponseEntity} containing a list of products that match the search criteria,
     * or a status of <strong>204 No Content</strong> if no products are found.
     * <ul>
     *     <li><strong>200 OK</strong> – If products matching the search criteria are successfully retrieved.</li>
     *     <li><strong>204 No Content</strong> – If no products match the search criteria.</li>
     * </ul>
     * @apiNote This method supports full-text search on product names, descriptions, or other fields based on the
     * provided keyword.
     * @see ProductService#searchProduct(String, int, String) ProductService.searchProduct
     */
    @GetMapping("public")
    public ResponseEntity<?> searchProducts(
            @RequestParam(value = "k") String keyword,
            @RequestParam(value = "page", defaultValue = "0") @Min(0) int pageNumber,
            @RequestParam(value = "s", defaultValue = "id") String sortBy) {

        List<ProductDTO> result = productService.searchProduct(keyword, pageNumber, sortBy);
        if (result.isEmpty())
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();

        return ResponseEntity.ok(result);
    }

    /**
     * <p>Adds a product to the database and associates it with a seller, categories, attributes, and tags.</p>
     *
     * <p>This method ensures that the provided product is linked to valid categories, attributes, and tags,
     * and it registers the product in the product-seller table to establish a relationship between the product
     * and the seller. The product is added to the database if all provided data is valid.</p>
     *
     * <p>Upon successful addition, a {@link ProductDTO} is returned with the details of the added product,
     * including its associations with categories, attributes, and tags.</p>
     *
     * @param productCategory a {@link ProductCategoryDTO} object containing the product information,
     *                        categories, attributes, and tags. It must not be null and should be validated.
     * @return a {@link ResponseEntity} containing the status and body of the response.
     * <ul>
     *     <li><strong>201 Created</strong> – If the product is successfully added, a {@link ProductDTO} is returned.</li>
     *     <li><strong>400 Bad Request</strong> – If there is an invalid argument provided.</li>
     *     <li><strong>403 Forbidden</strong> – If the seller is not found or the user is not authorized.</li>
     *     <li><strong>404 Not Found</strong> – If the specified category, attribute, or tag does not exist.</li>
     * </ul>
     */
    @PostMapping
    // @PreAuthorize("hasRole('SELLER')")
    public ResponseEntity<?> addProduct(
            @RequestBody @Valid @NotNull ProductCategoryDTO productCategory
    ) throws CustomerNotFoundException, CategoryNotFoundException, ArgumentValueNotValidException, AttributeNotFoundException, TagNotFoundException, SellerNotFoundException {
        String keycloakUserId = authenticationService.extractUserId().orElseThrow(CustomerNotFoundException::new);

        Seller seller = sellerService.findByKeycloakId(keycloakUserId);

        Category firstCategory = validateCategory(productCategory.firstCategoryId());
        Category secondCategory = validateCategory(productCategory.secondCategoryId());

        if (firstCategory != null &&
                secondCategory != null &&
                !categoryService.areCategoriesValid(firstCategory, secondCategory))
            throw new ArgumentValueNotValidException();

        Attribute attribute = validateAttribute(productCategory.attributeId());
        Tag tag = validateTag(productCategory.tagId());

        Product product = productService.addProduct(productCategory.product(), seller.getId());

        if (firstCategory != null)
            productCategoryService.create(firstCategory, product);

        if (secondCategory != null)
            productCategoryService.create(secondCategory, product);

        if (attribute != null)
            productAttributeService.create(attribute, product);

        if (tag != null)
            productTagService.create(tag, product);

        ProductDTO productDTO = ProductMapper.toDTO(product);
        return ResponseEntity.status(HttpStatus.CREATED).body(productDTO);
    }

    /**
     * <p>This method attempts to remove a product associated with the seller.</p>
     *
     * <p>The seller ID extraction from the token is currently not implemented and will be addressed in the future.</p>
     *
     * @param productId The UUID of the product to be removed. This value must not be {@code null}.
     * @return A {@link ResponseEntity} indicating the result of the operation:
     * <ul>
     *     <li><strong>204 No Content</strong> – If the product is successfully removed.</li>
     *     <li><strong>400 Bad Request</strong> – If the product does not exist or if the seller is not authorized to remove the product.</li>
     * </ul>
     */
    @DeleteMapping
    // @PreAuthorize("hasRole('SELLER')")
    public ResponseEntity<?> removeProduct(
            @RequestParam(value = "product") @NotNull UUID productId
    ) throws ProductNotFoundException, CustomerNotFoundException {
        // TODO cambiare gestione venditore a keycloak
        String keycloakUserId = authenticationService.extractUserId().orElseThrow(CustomerNotFoundException::new);

        Seller seller = sellerService.findByKeycloakId(keycloakUserId);
        if (!productSellerService.existsProductForSeller(productId, seller.getId()))
            throw new ProductNotFoundException();

        productService.removeProduct(productId);
        return ResponseEntity.noContent().build();
    }

    private Tag validateTag(UUID id) throws TagNotFoundException {
        if (id == null)
            return null;

        return productTagService.get(id)
                .orElseThrow(TagNotFoundException::new);
    }

    private Category validateCategory(UUID id) throws CategoryNotFoundException {
        if (id == null)
            return null;

        return categoryService.get(id)
                .orElseThrow(CategoryNotFoundException::new);
    }

    private Attribute validateAttribute(UUID id) throws AttributeNotFoundException {
        if (id == null)
            return null;

        return attributeService.get(id)
                .orElseThrow(AttributeNotFoundException::new);
    }
}
