package com.example.finalProject.Controller;

import com.example.finalProject.Model.Customer;
import com.example.finalProject.Model.Product;
import com.example.finalProject.Service.CartService;
import com.example.finalProject.Service.CustomerService;
import com.example.finalProject.Service.ProductService;
import com.example.finalProject.Service.OrderService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class WebController {

    private final ProductService productService;
    private final CustomerService customerService;
    private final CartService cartService;
    private final OrderService orderService;

    public WebController(ProductService productService,
                         CustomerService customerService,
                         CartService cartService,
                         OrderService orderService) {
        this.productService = productService;
        this.customerService = customerService;
        this.cartService = cartService;
        this.orderService = orderService;
    }

    @GetMapping("/")
    public String homePage() {
        return "index";
    }

    @GetMapping("/login")
    public String loginPage() {
        return "login";
    }

    @GetMapping("/register")
    public String registerPage() {
        return "register";
    }

    @GetMapping("/products")
    public String productListingPage(Model model) {
        List<Product> products = productService.findAll();
        model.addAttribute("products", products);
        return "product-list";
    }

    @GetMapping("/cart")
    public String cartPage(Model model, Authentication authentication) {
        if (authentication == null) {
            return "redirect:/login";
        }
        Customer customer = getCurrentCustomer(authentication);
        model.addAttribute("cartItems", cartService.getCartItems(customer));
        return "cart";
    }

    @GetMapping("/checkout")
    public String checkoutPage(Authentication authentication) {

        if (authentication == null) {
            return "redirect:/login";
        }
        return "checkout";
    }

    @GetMapping("/orders")
    public String orderHistoryPage(Model model, Authentication authentication) {
        if (authentication == null) {
            return "redirect:/login";
        }
        Customer customer = getCurrentCustomer(authentication);
        model.addAttribute("orders", orderService.getOrdersByCustomer(customer));
        return "order-history";
    }

    private Customer getCurrentCustomer(Authentication authentication) {
        String username = authentication.getName();
        return customerService.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found: " + username));
    }
}
