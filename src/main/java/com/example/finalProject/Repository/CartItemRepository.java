package com.example.finalProject.Repository;

import com.example.finalProject.Model.CartItem;
import com.example.finalProject.Model.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {
    List<CartItem> findByCustomer(Customer customer);
}

