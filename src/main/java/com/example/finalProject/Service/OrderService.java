package com.example.finalProject.Service;

import com.example.finalProject.Model.*;
import com.example.finalProject.Repository.OrderItemRepository;
import com.example.finalProject.Repository.OrderRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final CartService cartService;

    public OrderService(OrderRepository orderRepository,
                        OrderItemRepository orderItemRepository,
                        CartService cartService) {
        this.orderRepository = orderRepository;
        this.orderItemRepository = orderItemRepository;
        this.cartService = cartService;
    }

    public Order placeOrder(Customer customer) {
        // get cart items
        List<CartItem> cartItems = cartService.getCartItems(customer);
        if (cartItems.isEmpty()) {
            throw new RuntimeException("Cart is empty");
        }

        // create new Order
        Order order = Order.builder()
                .customer(customer)
                .orderDate(LocalDateTime.now())
                .build();

        double totalPrice = 0.0;
        List<OrderItem> orderItems = new ArrayList<>();

        for (CartItem cartItem : cartItems) {
            double price = cartItem.getProduct().getPrice();
            int quantity = cartItem.getQuantity();

            OrderItem orderItem = OrderItem.builder()
                    .order(order)
                    .product(cartItem.getProduct())
                    .quantity(quantity)
                    .price(price)
                    .build();

            orderItems.add(orderItem);
            totalPrice += price * quantity;
        }

        order.setOrderItems(orderItems);
        order.setTotalPrice(totalPrice);

        // save order
        Order savedOrder = orderRepository.save(order);

        // save order items
        for (OrderItem item : orderItems) {
            orderItemRepository.save(item);
        }

        // clear cart
        cartService.clearCart(customer);

        return savedOrder;
    }

    public List<Order> getOrdersByCustomer(Customer customer) {
        // If you wanted a custom query: findByCustomer(customer)
        // For demonstration, we'll filter from all:
        List<Order> allOrders = orderRepository.findAll();
        List<Order> customerOrders = new ArrayList<>();
        for (Order o : allOrders) {
            if (o.getCustomer().getId().equals(customer.getId())) {
                customerOrders.add(o);
            }
        }
        return customerOrders;
    }
}


