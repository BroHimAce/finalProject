package com.example.finalProject.Service;

import com.example.finalProject.Model.Customer;
import com.example.finalProject.Model.Role;
import com.example.finalProject.Repository.CustomerRepository;
import com.example.finalProject.Repository.RoleRepository;
import com.example.finalProject.Util.PasswordEncoderUtil;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Service
public class CustomerService {

    private final CustomerRepository customerRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoderUtil passwordEncoderUtil;

    public CustomerService(CustomerRepository customerRepository,
                           RoleRepository roleRepository,
                           PasswordEncoderUtil passwordEncoderUtil) {
        this.customerRepository = customerRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoderUtil = passwordEncoderUtil;
    }

    public Optional<Customer> findByUsername(String username) {
        return customerRepository.findByUsername(username);
    }

    public Optional<Customer> findByEmail(String email) {
        return customerRepository.findByEmail(email);
    }

    public Customer registerNewCustomer(String username, String email, String rawPassword) {
        // Create new Customer
        Customer customer = new Customer();
        customer.setUsername(username);
        customer.setEmail(email);
        customer.setPassword(passwordEncoderUtil.passwordEncoder().encode(rawPassword));

        // Ensure "ROLE_USER" exists
        Role userRole = roleRepository.findByName("ROLE_USER");
        if (userRole == null) {
            userRole = new Role();
            userRole.setName("ROLE_USER");
            roleRepository.save(userRole);
        }

        // Assign that role
        Set<Role> roles = new HashSet<>();
        roles.add(userRole);
        customer.setRoles(roles);

        return customerRepository.save(customer);
    }
}


