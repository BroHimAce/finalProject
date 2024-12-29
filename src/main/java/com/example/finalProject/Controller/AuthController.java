package com.example.finalProject.Controller;

import com.example.finalProject.Model.Customer;
import com.example.finalProject.Service.CustomerService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final CustomerService customerService;

    public AuthController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestParam String username,
                                      @RequestParam String email,
                                      @RequestParam String password) {
        // Check if user already exists
        if (customerService.findByUsername(username).isPresent()) {
            return ResponseEntity.badRequest().body("Username is already taken.");
        }
        if (customerService.findByEmail(email).isPresent()) {
            return ResponseEntity.badRequest().body("Email is already taken.");
        }

        Customer created = customerService.registerNewCustomer(username, email, password);
        return ResponseEntity.ok(created);
    }

    // Optional: If you want a custom login endpoint (JWT, etc.);
    // For now, we rely on Spring Security formLogin.
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestParam String username, @RequestParam String password) {
        // Dummy example: In real scenario, you'd authenticate with AuthenticationManager
        UsernamePasswordAuthenticationToken authToken =
                new UsernamePasswordAuthenticationToken(username, password);
        SecurityContextHolder.getContext().setAuthentication(authToken);
        return ResponseEntity.ok("Logged in");
    }
}


