package com.handyhive.backend.controller;

import com.handyhive.backend.model.Customer;
import com.handyhive.backend.service.CustomerService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/customers")
public class CustomerController {

    private final CustomerService customerService;

    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }



    @GetMapping("/search")
    public ResponseEntity<List<Customer>> getCustomerByEmail(@RequestParam String email) {
        return customerService.findByEmail(email)
                .map(customer -> ResponseEntity.ok(List.of(customer)))
                .orElse(ResponseEntity.ok(List.of()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Customer> getCustomerById(@PathVariable Long id) {
        return customerService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Customer> registerCustomer(@RequestBody Customer customer) {
        Customer newCustomer = customerService.registerCustomer(customer);
        return ResponseEntity.ok(newCustomer);
    }
}