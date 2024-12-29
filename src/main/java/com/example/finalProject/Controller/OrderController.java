package com.example.finalProject.Controller;

import com.example.finalProject.Model.Customer;
import com.example.finalProject.Model.Order;
import com.example.finalProject.Service.CustomerService;
import com.example.finalProject.Service.OrderService;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private final OrderService orderService;
    private final CustomerService customerService;

    public OrderController(OrderService orderService,
                           CustomerService customerService) {
        this.orderService = orderService;
        this.customerService = customerService;
    }

    @PostMapping("/checkout")
    public Order checkout(Authentication authentication) {
        Customer customer = getCurrentCustomer(authentication);
        return orderService.placeOrder(customer);
    }

    @GetMapping
    public List<Order> getOrdersForCustomer(Authentication authentication) {
        Customer customer = getCurrentCustomer(authentication);
        return orderService.getOrdersByCustomer(customer);
    }

    private Customer getCurrentCustomer(Authentication authentication) {
        String username = authentication.getName();
        return customerService.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found: " + username));
    }
}


