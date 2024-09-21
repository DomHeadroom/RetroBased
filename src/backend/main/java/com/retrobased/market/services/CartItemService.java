package com.retrobased.market.services;

import com.retrobased.market.dto.ProductObjQuantityDTO;
import com.retrobased.market.dto.ProductQuantityDTO;
import com.retrobased.market.entities.Cart;
import com.retrobased.market.entities.CartItem;
import com.retrobased.market.entities.Product;
import com.retrobased.market.repositories.*;
import com.retrobased.market.support.exceptions.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Service
public class CartItemService {
    private final CartItemRepository cartItemRepository;


    private final CartService cartService;
    private final ProductService productService;
    private final CustomerService customerService;

    public CartItemService(CartItemRepository cartItemRepository, CartService cartService, ProductService productService, CustomerService customerService) {
        this.cartItemRepository = cartItemRepository;
        this.cartService = cartService;
        this.productService = productService;
        this.customerService = customerService;
    }

    /**
     * Retrieves a paginated list of {@link Product} objects contained in a customer's shopping cart.
     * The products are sorted in descending order based on their creation date.
     * Supports pagination with a page size of 20 items.
     *
     * @param customerId The UUID of the customer whose cart items are to be retrieved.
     *                   This identifies the customer in the database and is used to fetch their cart.
     * @param pageNumber The page number to retrieve, with results paginated in sets of 20.
     *                   Must be a non-negative integer.
     * @return A list of {@link Product} objects present in the specified customer's cart.
     * If the cart is empty or the page is out of range, returns an empty list.
     * @apiNote This method is read-only, utilizing pagination to manage the number of products returned per request.
     * The products are sorted by their creation timestamp in descending order to show the most recent products first.
     * @see CartService#getCustomerCart(UUID) CartService.getCustomerCart
     * @see CartItemRepository#findProductsByCartId(UUID, Pageable) CartItemRepository.findProductsByCartId
     */
    @Transactional(readOnly = true)
    public List<Product> getCart(UUID customerId, int pageNumber) {
        Cart customerCart = getCustomerCart(customerId);
        Pageable paging = PageRequest.of(pageNumber, 20, Sort.by(Sort.Order.desc("createdAt")));
        Page<Product> cartItems = cartItemRepository.findProductsByCartId(customerCart.getId(), paging);

        if (cartItems.hasContent())
            return cartItems.getContent();

        return new ArrayList<>();
    }

    /**
     * Updates the quantity of a specified product in the customer's shopping cart.
     * <p>
     * This method ensures that the customer's cart item is either updated with the new quantity or
     * removed from the cart if the quantity is set to zero. If the requested quantity exceeds the
     * available stock or if the product is not found in the cart, it will throw appropriate exceptions.
     * </p>
     *
     * <p><strong>Transaction:</strong> This method is executed within a required transaction scope. If a transaction
     * is already in progress, this method will participate in it. If not, a new transaction is started.</p>
     *
     * @param customerId the ID of the customer whose cart is being modified; must not be null
     * @param productId  the ID of the product to update in the cart; must not be null
     * @param quantity   the new quantity for the product in the cart; must be non-negative (0 will remove the item)
     * @throws ArgumentValueNotValidException if the requested quantity exceeds available stock
     * @throws ProductNotFoundException       if the product is not found in the customer's cart
     * @throws CustomerDontExistsException    if the customer does not exist in the system
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public void changeQuantity(@NotNull UUID customerId, @NotNull UUID productId, @NotNull @Min(0) Long quantity) throws ArgumentValueNotValidException, ProductNotFoundException, CustomerDontExistsException {
        checkValues(customerId, productId);

        if (productService.getQuantity(productId) < quantity)
            throw new ArgumentValueNotValidException();

        Cart cart = getCustomerCart(customerId);

        CartItem cartItem = getCartItem(cart.getId(), productId)
                .orElseThrow(ProductNotFoundException::new);

        if (quantity == 0) {
            cartItemRepository.delete(cartItem);
            return;
        }

        cartItem.setQuantity(quantity);
        cartItemRepository.save(cartItem);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public List<ProductObjQuantityDTO> addProductToCart(@NotNull UUID customerId, @NotNull List<ProductQuantityDTO> productQuantities)
            throws ArgumentValueNotValidException, ProductNotFoundException {

        Map<UUID, Long> productIds = new HashMap<>();

        for (ProductQuantityDTO productQuantity : productQuantities) {
            UUID productId = productQuantity.productId();
            if (!productService.exists(productId) ||
                    productService.isDeleted(productId) ||
                    productService.isOutOfStock(productId) ||
                    !productService.isPublished(productId))
                throw new ProductNotFoundException();

            productIds.merge(productId, productQuantity.quantity(), Long::sum);
        }

        List<Product> products = productService.get(productIds.keySet());

        Cart cart = getCustomerCart(customerId);

        List<CartItem> newItems = new LinkedList<>();
        List<ProductObjQuantityDTO> resultItems = new LinkedList<>();

        for (Product product : products) {
            UUID productId = product.getId();
            Long quantityToAdd = productIds.get(productId);

            if (product.getQuantity() < quantityToAdd)
                throw new ArgumentValueNotValidException();

            Optional<CartItem> existingCartItemOpt = cartItemRepository.findByCartIdAndProductId(cart.getId(), productId);

            if (existingCartItemOpt.isPresent()) {
                CartItem existingCartItem = existingCartItemOpt.get();
                Long newQuantity = existingCartItem.getQuantity() + quantityToAdd;
                existingCartItem.setQuantity(newQuantity);
                cartItemRepository.save(existingCartItem);

                resultItems.add(createProductObjQuantityDTO(product, newQuantity));
                continue;
            }

            CartItem newItem = new CartItem();
            newItem.setProduct(product);
            newItem.setCart(cart);
            newItem.setQuantity(quantityToAdd);
            newItems.add(newItem);

            resultItems.add(createProductObjQuantityDTO(product, quantityToAdd));

        }

        if (!newItems.isEmpty())
            cartItemRepository.saveAll(newItems);

        return resultItems;
    }

    private ProductObjQuantityDTO createProductObjQuantityDTO(Product product, Long quantity) {
        return new ProductObjQuantityDTO(product, quantity);
    }

    private void checkValues(UUID customerId, UUID productId) throws ProductNotFoundException, CustomerDontExistsException {
        if (!customerService.exists(customerId))
            throw new CustomerDontExistsException();

        if (!productService.exists(productId))
            throw new ProductNotFoundException();
    }

    @Transactional(readOnly = true)
    public Cart getCustomerCart(UUID customerId) {
        return cartService.getCustomerCart(customerId);
    }

    @Transactional(readOnly = true)
    public Long getProductQuantityInCart(UUID cartId, UUID productId) {
        return cartItemRepository.findQuantityByCartIdAndProductId(cartId, productId);
    }

    @Transactional(readOnly = true)
    public Optional<CartItem> getCartItem(UUID cartId, UUID productId) {
        return cartItemRepository.findByCartIdAndProductId(cartId, productId);
    }

    @Transactional(readOnly = true)
    public boolean existsProductInCart(UUID cartId, UUID productId) {
        return cartItemRepository.existsByCartIdAndProductId(cartId, productId);
    }

}
