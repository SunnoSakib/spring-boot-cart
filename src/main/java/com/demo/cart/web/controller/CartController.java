package com.demo.cart.web.controller;

import com.demo.cart.model.Cart;
import com.demo.cart.model.CartItem;
import com.demo.cart.model.Customer;
import com.demo.cart.model.Product;
import com.demo.cart.service.CartItemService;
import com.demo.cart.service.CartService;
import com.demo.cart.service.CustomerService;
import com.demo.cart.service.ProductService;
import com.demo.cart.web.dto.AddProductRequest;
import com.demo.cart.web.dto.ApiResponse;
import com.demo.cart.web.dto.ChangeProductRequest;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

/**
 * @author Md Mahmud Hasan
 */
@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/cart")
public class CartController {

    private final CartService cartService;
    private final ProductService productService;
    private final CustomerService customerService;
    private final CartItemService cartItemService;

    public CartController(CartService cartService, ProductService productService, CustomerService customerService, CartItemService cartItemService){
        this.cartService = cartService;
        this.productService = productService;
        this.customerService = customerService;
        this.cartItemService = cartItemService;
    }

    /**

     Retrieves a paginated list of carts.
     @param pageable the pagination information
     @return a Page containing the list of carts
     */
    @GetMapping
    public Page<Cart> list(@PageableDefault(sort = {"createdOn"}, direction = Sort.Direction.DESC) Pageable pageable) {
        return cartService.getAll(pageable);
    }

    /**

     Retrieves a cart by its UUID(Universal unique id).
     @param uuid the UUID of the cart to retrieve
     @return the Cart object with the specified UUID
     */
    @GetMapping("/{uuid}")
    public Cart getCartByUUID(@PathVariable("uuid") String uuid) {
        return cartService.getByUId(uuid);
    }

    /**

     Adds a product to the cart.
     @param addProductToCartRequest the request object containing the details of the product to add
     @return a ResponseEntity with the added Cart object or an error response
     */
    @PostMapping("/add-product")
    public ResponseEntity<?> addProductToCart(@RequestBody @Valid AddProductRequest addProductToCartRequest) {
        String cartUid = UUID.randomUUID().toString();
        Cart cart = null;

        //Looking whether the registered customer already has a previous cart
        if(addProductToCartRequest.getCustomerId()!= null){
            cart = cartService.getByCustomerId(addProductToCartRequest.getCustomerId());
        }
        //Do Guest customer has a saved cart?
        if (cart==null && addProductToCartRequest.getCartUUID()!=null) {
            cart = cartService.getByUId(addProductToCartRequest.getCartUUID());
        }
        //Brand-new customer
        if(cart==null){
            cart = new Cart();
            cart.setUid(cartUid);
            if(addProductToCartRequest.getCustomerId()!=null){
                Optional<Customer> customer = this.customerService.getCustomerById(addProductToCartRequest.getCustomerId());
                if(customer.isPresent())
                    cart.setCustomer(customer.get());
            }
        }

        Product product = productService.getById(addProductToCartRequest.getProductId()).get();
        if(product==null){
            //raise product not found
            return ResponseEntity.unprocessableEntity ()
                    .body(new ApiResponse(false,"Product Not Found!!!"));
        }
        else if (product.getStockAvailable() < addProductToCartRequest.getUnit())
        {
            //raise Product quantity exceed error
            return ResponseEntity.unprocessableEntity ()
                    .body(new ApiResponse(false,"Product quantity exceed!!! Only "+product.getStockAvailable()+" unit available. "));
        }

        CartItem cartItem = cartItemService.getByProductIdAndCartId(product.getId(),cart.getId());
        if(cartItem==null){
            cartItem = new CartItem();
            cartItem.setCart(cart);
            cartItem.setProduct(product);
            cartItem.setQuantity(addProductToCartRequest.getUnit());
            cart.setItems(Collections.singletonList(cartItem));
        }
        else{
            cartItem.setQuantity(cartItem.getQuantity() +addProductToCartRequest.getUnit());
        }
        cartItem.setAmount(product.getUnitPrice()*addProductToCartRequest.getUnit()-product.getDiscountPrice());
        cartItem.setDiscount(product.getDiscountPrice());
        cartService.save(cart);
        return ResponseEntity.ok(cart);
    }

    /**

     Updates the quantity of a product in the cart.
     @param changeProductRequest the request object containing the details of the product and the new quantity
     @return a ResponseEntity with the updated Cart object or an error response
     */
    @PostMapping("/update-quantity")
    public ResponseEntity<?> updateCart(@RequestBody @Valid ChangeProductRequest changeProductRequest) {
        Cart cart = cartService.getByUId(changeProductRequest.getCartUUID());
        Product product = productService.getById(changeProductRequest.getProductId()).orElse(null);
        if(product==null){
            //raise product not found
            return ResponseEntity.unprocessableEntity ()
                    .body(new ApiResponse(false,"Product Not Found!!!"));
        }
        else if (product.getStockAvailable() < changeProductRequest.getNewUnit())
        {
            //raise Product quantity exceed error
            return ResponseEntity.unprocessableEntity ()
                    .body(new ApiResponse(false,"Product quantity exceed!!! Only "+product.getStockAvailable()+" unit available. "));
        }

        CartItem cartItem = cartItemService.getByProductIdAndCartId(product.getId(),cart.getId());
        if(cartItem==null){
            //raise Product quantity exceed error
            return ResponseEntity.unprocessableEntity ()
                    .body(new ApiResponse(false,"Product Not Found in cart!!!"));
        }

        cartItem.setQuantity(changeProductRequest.getNewUnit());
        cartItem.setAmount(product.getUnitPrice()*changeProductRequest.getNewUnit()-product.getDiscountPrice());
        cartItem.setDiscount(product.getDiscountPrice());
        cartItemService.save(cartItem);

        return ResponseEntity.ok(cart);
    }

    /**

     Removes a product from the cart.
     @param uuid the UUID of the cart from which to remove the product
     @param productId the ID of the product to remove
     @return a ResponseEntity with a success message or an error response
     */
    @DeleteMapping("/{uuid}/remove-product/{productId}")
    public ResponseEntity<ApiResponse> removeProductFromCart( @PathVariable("uuid") String uuid, @PathVariable("productId") Long productId) {
        Cart cart = cartService.getByUId(uuid);
        if(cart==null)
            return ResponseEntity.unprocessableEntity ()
                    .body(new ApiResponse(false,"Cart Not Found!!!"));
        CartItem cartItem = cartItemService.getByProductIdAndCartId(productId,cart.getId());
        if(cartItem!=null){
            cartItemService.delete(cartItem);
            return ResponseEntity.ok ()
                    .body(new ApiResponse(true,"Successfully Deleted"));
        }
        return ResponseEntity.unprocessableEntity ()
                .body(new ApiResponse(false,"Cart Item Not Found!!!"));
    }

}
