package com.retrobased.market.controllers;

import com.retrobased.market.dtos.ProductCategoryDTO;
import com.retrobased.market.dtos.ProductDTO;
import com.retrobased.market.entities.Attribute;
import com.retrobased.market.entities.Category;
import com.retrobased.market.entities.Product;
import com.retrobased.market.entities.Tag;
import com.retrobased.market.mappers.ProductMapper;
import com.retrobased.market.services.AttributeService;
import com.retrobased.market.services.CategoryService;
import com.retrobased.market.services.ProductAttributeService;
import com.retrobased.market.services.ProductCategoryService;
import com.retrobased.market.services.ProductSellerService;
import com.retrobased.market.services.ProductService;
import com.retrobased.market.services.ProductTagService;
import com.retrobased.market.utils.ResponseMessage;
import com.retrobased.market.utils.exceptions.ArgumentValueNotValidException;
import com.retrobased.market.utils.exceptions.AttributeNotFoundException;
import com.retrobased.market.utils.exceptions.CategoryNotFoundException;
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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import static com.retrobased.market.utils.ResponseUtils.createErrorResponse;

import java.util.List;
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

    public ProductController(
            ProductService productService,
            ProductSellerService productSellerService,
            ProductCategoryService productCategoryService,
            CategoryService categoryService,
            AttributeService attributeService,
            ProductAttributeService productAttributeService,
            ProductTagService productTagService
    ) {
        this.productService = productService;
        this.productSellerService = productSellerService;
        this.productCategoryService = productCategoryService;
        this.categoryService = categoryService;
        this.attributeService = attributeService;
        this.productAttributeService = productAttributeService;
        this.productTagService = productTagService;
    }

    /**
     * Fetch a list of random products for public homepage display.
     *
     * @param limit The number of random products to return. Defaults to 10.
     * @return A {@link ResponseEntity} containing the list of random products,
     * or a status of 204 No Content if no products are found.
     */
    @GetMapping("/random-products")
    public ResponseEntity<?> getRandomProducts(
            @RequestParam(value = "limit", defaultValue = "10") @Min(1) int limit) {

        List<ProductDTO> randomProducts = productService.getRandomProducts(limit);
        if (randomProducts.isEmpty())
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();

        return ResponseEntity.ok(randomProducts);
    }

    /**
     * Searches for products based on the provided keyword. Supports pagination and sorting.
     *
     * @param keyword    The search keyword used to filter products.
     * @param pageNumber The page number for paginated results (default is 0).
     *                   Must be a non-negative integer.
     * @param sortBy     The field by which to sort the products (default is "id").
     * @return A {@link ResponseEntity} containing the list of products that match the search criteria,
     * or a status of 204 No Content if no products are found.
     * @apiNote This method allows clients to search for products using a keyword and optionally specify
     * sorting and pagination. The sort field defaults to "id" and the page number defaults to 0.
     * @see ProductService#searchProduct(String, int, String) ProductService.searchProduct
     */
    @GetMapping("public")
    public ResponseEntity<?> searchProduct(
            @RequestParam(value = "k") String keyword,
            @RequestParam(value = "page", defaultValue = "0") @Min(0) int pageNumber,
            @RequestParam(value = "s", defaultValue = "id") String sortBy) {

        List<ProductDTO> result = productService.searchProduct(keyword, pageNumber, sortBy);
        if (result.isEmpty())
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();

        return ResponseEntity.ok(result);
    }

    /**
     * Adds a product to the database and associates it with a seller, categories, attributes, and tags.
     * This method ensures that the provided product is linked to valid categories, attributes, and tags,
     * and it registers the product in the product-seller table to establish a relationship between the product
     * and the seller.
     * The product is added to the database if all provided data is valid. Upon successful addition, a {@link ProductDTO}
     * is returned with the details of the added product, including its association with categories, attributes, and tags.
     * The product is also added to the product-seller table to ensure that it is linked with the specified seller.
     *
     * @param productCategory a {@link ProductCategoryDTO} object containing the product information,
     *                        categories, attributes, and tags. It must not be null and should be validated.
     * @return a {@link ResponseEntity} containing the status and body of the response. If the product is
     * successfully added, a {@link ProductDTO} is returned with an HTTP status of 201 (Created).
     * In case of an invalid argument or seller not found, appropriate error messages are returned.
     */
    @PostMapping
    // @PreAuthorize("hasRole('SELLER')")
    public ResponseEntity<?> addProduct(
            @RequestBody @Valid @NotNull ProductCategoryDTO productCategory
    ) {
        try {
            // TODO estrarre sellerId da token
            UUID sellerId = UUID.fromString("c2e56bdd-1a5b-4a14-bc5b-ef069c20060f");

            Category firstCategory = validateCategory(productCategory.firstCategoryId());
            Category secondCategory = validateCategory(productCategory.secondCategoryId());

            if (firstCategory != null &&
                    secondCategory != null &&
                    !categoryService.areCategoriesValid(firstCategory, secondCategory))
                throw new ArgumentValueNotValidException();

            Attribute attribute = validateAttribute(productCategory.attributeId());
            Tag tag = validateTag(productCategory.tagId());

            Product product = productService.addProduct(productCategory.product(), sellerId);

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
        } catch (ArgumentValueNotValidException e) {
            return createErrorResponse("ERROR_ARGUMENT_VALUE_NOT_VALID", HttpStatus.BAD_REQUEST);
        } catch (SellerNotFoundException e) {
            return createErrorResponse("ERROR_USER_NOT_FOUND", HttpStatus.FORBIDDEN);
        } catch (AttributeNotFoundException e) {
            return createErrorResponse("ERROR_ATTRIBUTE_NOT_FOUND", HttpStatus.NOT_FOUND);
        } catch (CategoryNotFoundException e) {
            return createErrorResponse("ERROR_CATEGORY_NOT_FOUND", HttpStatus.NOT_FOUND);
        } catch (TagNotFoundException e) {
            return createErrorResponse("ERROR_TAG_NOT_FOUND", HttpStatus.NOT_FOUND);
        }
    }

    /**
     * <p>This method attempts to remove a product associated with the seller.
     * The seller ID is currently not implemented and will be extracted from
     * the token in the future.</p>
     *
     * @param productId The UUID of the product to be removed. This value must not be {@code null}.
     * @return A {@link ResponseEntity} with no content (204 status) if the product is successfully removed,
     * or a bad request (400 status) with an error message if the product does not exist or if
     * the seller is not authorized to remove the product.
     */
    @DeleteMapping
    // @PreAuthorize("hasRole('SELLER')")
    public ResponseEntity<?> removeProduct(@RequestParam(value = "product") @NotNull UUID productId) {
        try {
            // TODO estrarre sellerId da token
            UUID sellerId = null;
            if (!productSellerService.existsProductForSeller(productId, sellerId))
                return createErrorResponse("ERROR_VALUE_NOT_PERMITTED", HttpStatus.BAD_REQUEST);

            productService.removeProduct(productId);
            return ResponseEntity.noContent().build();
        } catch (ProductNotFoundException e) {
            return createErrorResponse("ERROR_ARGUMENT_VALUE_NOT_VALID", HttpStatus.BAD_REQUEST);        }
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
