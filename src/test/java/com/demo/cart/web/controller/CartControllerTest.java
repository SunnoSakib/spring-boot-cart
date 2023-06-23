package com.demo.cart.web.controller;
import com.demo.cart.model.*;
import com.demo.cart.service.*;
import com.demo.cart.web.dto.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

/**
 * @author Md Mahmud Hasan
 */
class CartControllerTest {

    @Mock
    private CartService cartService;
    @Mock
    private ProductService productService;
    @Mock
    private CustomerService customerService;
    @Mock
    private CartItemService cartItemService;

    private CartController cartController;

    @BeforeEach
    void setup() {
        MockitoAnnotations.initMocks(this);
        cartController = new CartController(cartService, productService, customerService, cartItemService);
    }

    /**
     * Testing cart/list
     */
    @Test
    void testList() {
        // Prepare
        Pageable pageable = Pageable.ofSize(10);
        Page<Cart> expectedPage = mock(Page.class);
        when(cartService.getAll(pageable)).thenReturn(expectedPage);

        // Execute
        Page<Cart> actualPage = cartController.list(pageable);

        // Verify
        assertEquals(expectedPage, actualPage);
        verify(cartService).getAll(pageable);
    }

    @Test
    void testGetCartByUUID() {
        // Prepare
        String uuid = "test-uuid";
        Cart expectedCart = mock(Cart.class);
        when(cartService.getByUId(uuid)).thenReturn(expectedCart);

        // Execute
        Cart actualCart = cartController.getCartByUUID(uuid);

        // Verify
        assertEquals(expectedCart, actualCart);
        verify(cartService).getByUId(uuid);
    }

    @Test
    public void testAddProductToCart() {
        // Mock the dependencies and input data
        AddProductRequest request = new AddProductRequest();
        request.setCustomerId(1L);
        request.setCartUUID("test-uuid");
        request.setProductId(1L);
        request.setUnit(2F);

        Cart cart = mock(Cart.class);
        Customer customer = mock(Customer.class);
        Product product = mock(Product.class);
        CartItem cartItem = mock(CartItem.class);
        when(product.getUnitPrice()).thenReturn(10.0);
        when(product.getDiscountPrice()).thenReturn(2.0);
        when(product.getStockAvailable()).thenReturn(20.0);
        when(customerService.getCustomerById(request.getCustomerId())).thenReturn(Optional.of(customer));
        when(cartService.getByCustomerId(request.getCustomerId())).thenReturn(cart);
        when(cartService.getByUId(request.getCartUUID())).thenReturn(null);
        when(productService.getById(request.getProductId())).thenReturn(Optional.of(product));
        when(cartItemService.getByProductIdAndCartId(request.getProductId(), cart.getId())).thenReturn(null);
        when(cartItemService.save(any())).thenReturn(cartItem);
        when(cartService.save(cart)).thenReturn(cart);

        // Perform the API call
        ResponseEntity<?> responseEntity = cartController.addProductToCart(request);

        // Verify the result
        assertEquals(ResponseEntity.ok(cart), responseEntity);
        verify(cartService, times(1)).getByCustomerId(request.getCustomerId());
        verify(cartService, times(1)).save(cart);
        verify(cartService).save(cart);

    }

    @Test
    void testRemoveProductFromCart() {
        // Prepare
        String uuid = "test-cart-uuid";
        Long productId = 456L;
        Cart cart = mock(Cart.class);
        CartItem cartItem = mock(CartItem.class);
        ResponseEntity<ApiResponse> expectedResponse = mock(ResponseEntity.class);

        when(cartService.getByUId(uuid)).thenReturn(cart);
        when(cartItemService.getByProductIdAndCartId(productId, cart.getId())).thenReturn(cartItem);
        doNothing().when(cartItemService).delete(cartItem);
        when(expectedResponse.getBody()).thenReturn(new ApiResponse(true, "Successfully Deleted"));

        // Execute
        ResponseEntity<ApiResponse> actualResponse = cartController.removeProductFromCart(uuid, productId);

        // Verify
        assertEquals(expectedResponse.getBody(), actualResponse.getBody());
        verify(cartService).getByUId(uuid);
        verify(cartItemService).getByProductIdAndCartId(productId, cart.getId());
        verify(cartItemService).delete(cartItem);
    }

}
