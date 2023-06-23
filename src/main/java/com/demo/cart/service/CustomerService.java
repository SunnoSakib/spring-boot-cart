package com.demo.cart.service;

import com.demo.cart.model.Customer;
import com.demo.cart.repository.CustomerRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
public class CustomerService {
    private final CustomerRepository customerRepository;
    public CustomerService(CustomerRepository customerRepository){
        this.customerRepository = customerRepository;
    }
    public Optional<Customer> getCustomerById(Long id){return customerRepository.findById(id);}
}
