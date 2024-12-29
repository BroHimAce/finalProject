package com.example.finalProject.Service;

import com.example.finalProject.Model.CartItem;
import com.example.finalProject.Model.Customer;
import com.example.finalProject.Model.Product;
import com.example.finalProject.Repository.CartItemRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CartService {

    private final CartItemRepository cartItemRepository;

    public CartService(CartItemRepository cartItemRepository) {
        this.cartItemRepository = cartItemRepository;
    }

    public List<CartItem> getCartItems(Customer customer) {
        return cartItemRepository.findByCustomer(customer);
    }

    public CartItem addToCart(Customer customer, Product product, int quantity) {
        // Check if item already in cart
        List<CartItem> cartItems = cartItemRepository.findByCustomer(customer);
        for (CartItem item : cartItems) {
            if (item.getProduct().getId().equals(product.getId())) {
                item.setQuantity(item.getQuantity() + quantity);
                return cartItemRepository.save(item);
            }
        }
        // Otherwise create new CartItem
        CartItem newCartItem = CartItem.builder()
                .customer(customer)
                .product(product)
                .quantity(quantity)
                .build();
        return cartItemRepository.save(newCartItem);
    }

    public void removeFromCart(Long cartItemId) {
        cartItemRepository.deleteById(cartItemId);
    }

    public void clearCart(Customer customer) {
        List<CartItem> cartItems = cartItemRepository.findByCustomer(customer);
        cartItemRepository.deleteAll(cartItems);
    }
}

