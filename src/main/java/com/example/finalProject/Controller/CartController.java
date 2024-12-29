package com.example.finalProject.Controller;

import com.example.finalProject.Model.CartItem;
import com.example.finalProject.Model.Customer;
import com.example.finalProject.Model.Product;
import com.example.finalProject.Service.CartService;
import com.example.finalProject.Service.CustomerService;
import com.example.finalProject.Service.ProductService;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cart")
public class CartController {

    private final CartService cartService;
    private final CustomerService customerService;
    private final ProductService productService;

    public CartController(CartService cartService,
                          CustomerService customerService,
                          ProductService productService) {
        this.cartService = cartService;
        this.customerService = customerService;
        this.productService = productService;
    }

    @GetMapping
    public List<CartItem> getCartItems(Authentication authentication) {
        Customer customer = getCurrentCustomer(authentication);
        return cartService.getCartItems(customer);
    }

    @PostMapping("/add")
    public CartItem addToCart(Authentication authentication,
                              @RequestParam Long productId,
                              @RequestParam int quantity) {
        Customer customer = getCurrentCustomer(authentication);
        Product product = productService.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found: " + productId));
        return cartService.addToCart(customer, product, quantity);
    }

    @DeleteMapping("/remove/{cartItemId}")
    public void removeFromCart(@PathVariable Long cartItemId) {
        cartService.removeFromCart(cartItemId);
    }

    @DeleteMapping("/clear")
    public void clearCart(Authentication authentication) {
        Customer customer = getCurrentCustomer(authentication);
        cartService.clearCart(customer);
    }

    private Customer getCurrentCustomer(Authentication authentication) {
        String username = authentication.getName();
        return customerService.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found: " + username));
    }
}


