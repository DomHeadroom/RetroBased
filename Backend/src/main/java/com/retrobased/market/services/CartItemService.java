package com.retrobased.market.services;

import com.retrobased.market.dtos.CartItemDTO;
import com.retrobased.market.dtos.ProductDTO;
import com.retrobased.market.dtos.ProductQuantityDTO;
import com.retrobased.market.entities.Cart;
import com.retrobased.market.entities.CartItem;
import com.retrobased.market.entities.Product;
import com.retrobased.market.mappers.CartItemMapper;
import com.retrobased.market.mappers.ProductMapper;
import com.retrobased.market.repositories.CartItemRepository;
import com.retrobased.market.utils.exceptions.ArgumentValueNotValidException;
import com.retrobased.market.utils.exceptions.CustomerNotFoundException;
import com.retrobased.market.utils.exceptions.ProductNotFoundException;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
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
import java.util.stream.Collectors;

@Service
public class CartItemService {
    private final CartItemRepository cartItemRepository;


    private final CartService cartService;
    private final ProductService productService;

    public CartItemService(
            CartItemRepository cartItemRepository,
            CartService cartService,
            ProductService productService
    ) {
        this.cartItemRepository = cartItemRepository;
        this.cartService = cartService;
        this.productService = productService;
    }

    /**
     * Retrieves a paginated list of {@link Product} objects contained in a customer's shopping cart.
     * The products are sorted in descending order based on their creation date.
     * Supports pagination with a page size of 20 items.
     *
     * @param customerId The ID of the customer whose cart items are to be retrieved.
     *                   This identifies the customer in the database and is used to fetch their cart.
     * @param pageNumber The page number to retrieve, with results paginated in sets of 20.
     *                   Must be a non-negative integer.
     * @return A list of {@link Product} objects present in the specified customer's cart.
     * If the cart is empty or the page is out of range, returns an empty list.
     * @apiNote This method is read-only, utilizing pagination to manage the number of products returned per request.
     * The products are sorted by their creation timestamp in descending order to show the most recent products first.
     * @see CartService#getCustomerCart(String) CartService.getCustomerCart
     * @see CartItemRepository#findByCartId(UUID, Pageable) CartItemRepository.findProductsByCartId
     */
    @Transactional(readOnly = true)
    public List<CartItemDTO> getCartItems(String keycloakId, int pageNumber) throws CustomerNotFoundException {
        Cart customerCart = getCustomerCart(keycloakId);
        Pageable paging = PageRequest.of(pageNumber, 20, Sort.by(Sort.Order.desc("createdAt")));
        Page<CartItem> cartItems = cartItemRepository.findByCartId(customerCart.getId(), paging);
        if (cartItems.hasContent())
            return cartItems.getContent()
                    .stream()
                    .map(CartItemMapper::toDTO)
                    .collect(Collectors.toList());

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
     * @param keycloakId the keycloak ID of the customer whose cart is being modified; must not be null
     * @param productId  the ID of the product to update in the cart; must not be null
     * @param quantity   the new quantity for the product in the cart; must be non-negative (0 will remove the item)
     * @throws ArgumentValueNotValidException if the requested quantity exceeds available stock
     * @throws ProductNotFoundException       if the product is not found in the customer's cart
     * @throws CustomerNotFoundException      if the customer does not exist in the system
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public void changeQuantity(@NotEmpty String keycloakId, @NotNull UUID productId, @NotNull @Min(0) Long quantity) throws ArgumentValueNotValidException, ProductNotFoundException, CustomerNotFoundException {
        checkProduct(productId);

        if (productService.getQuantity(productId) < quantity)
            throw new ArgumentValueNotValidException();

        Cart cart = getCustomerCart(keycloakId);

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
    public List<CartItemDTO> addProductsToCart(@NotEmpty String keycloakId, List<ProductQuantityDTO> productQuantities)
            throws ArgumentValueNotValidException, ProductNotFoundException {

        Map<UUID, Long> aggregatedProductQuantities = new HashMap<>();
        for (ProductQuantityDTO productQuantity : productQuantities) {
            UUID productId = productQuantity.productId();
            Long quantityToAdd = productQuantity.quantity();

            aggregatedProductQuantities.merge(productId, quantityToAdd, Long::sum);
        }

        Cart cart = cartService.findCartByKeycloakIdWithLock(keycloakId);

        List<CartItemDTO> resultItems = new LinkedList<>();
        List<CartItem> newItems = new LinkedList<>();

        for (Map.Entry<UUID, Long> entry : aggregatedProductQuantities.entrySet()) {
            UUID productId = entry.getKey();
            Long totalQuantityToAdd = entry.getValue();

            Product product = productService.findProductWithLock(productId);

            if (product.getDeleted() ||
                    product.getDisableOutOfStock() ||
                    !product.getPublished()
            )
                throw new ProductNotFoundException();


            if (product.getQuantity() < totalQuantityToAdd)
                throw new ArgumentValueNotValidException();

            Optional<CartItem> existingCartItemOpt = cartItemRepository.findByCartIdAndProductId(cart.getId(), productId);
            ProductDTO productDTO = ProductMapper.toDTO(product);

            if (existingCartItemOpt.isPresent()) {
                CartItem existingCartItem = existingCartItemOpt.get();
                Long newQuantity = existingCartItem.getQuantity() + totalQuantityToAdd;
                existingCartItem.setQuantity(newQuantity);
                cartItemRepository.save(existingCartItem);

                resultItems.add(createProductObjQuantityDTO(productDTO, newQuantity));
                continue;
            }

            CartItem newItem = new CartItem();
            newItem.setProduct(product);
            newItem.setCart(cart);
            newItem.setQuantity(totalQuantityToAdd);
            newItems.add(newItem);

            resultItems.add(createProductObjQuantityDTO(productDTO, totalQuantityToAdd));

        }

        if (!newItems.isEmpty())
            cartItemRepository.saveAll(newItems);

        return resultItems;
    }

    private CartItemDTO createProductObjQuantityDTO(ProductDTO product, Long quantity) {
        return new CartItemDTO(product, quantity);
    }

    private void checkProduct(UUID productId) throws ProductNotFoundException, CustomerNotFoundException {

        if (!productService.exists(productId))
            throw new ProductNotFoundException();
    }

    @Transactional(readOnly = true)
    public Cart getCustomerCart(@NotEmpty String keycloakId) throws CustomerNotFoundException {
        return cartService.getCustomerCart(keycloakId);
    }

    @Transactional(readOnly = true)
    public Optional<CartItem> getCartItem(UUID cartId, UUID productId) {
        return cartItemRepository.findByCartIdAndProductId(cartId, productId);
    }

}
