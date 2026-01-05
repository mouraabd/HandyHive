package com.handyhive.backend.controller;

import com.handyhive.backend.model.Customer;
import com.handyhive.backend.service.CustomerService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/customers")
public class CustomerController {

    private final CustomerService customerService;

    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    // ✅ READ (list)
    @GetMapping
    public ResponseEntity<List<Customer>> getAllCustomers() {
        return ResponseEntity.ok(customerService.getAllCustomers());
    }

    // ✅ READ (search by email)
    @GetMapping("/search")
    public ResponseEntity<List<Customer>> getCustomerByEmail(@RequestParam String email) {
        return customerService.findByEmail(email)
                .map(c -> ResponseEntity.ok(List.of(c)))
                .orElse(ResponseEntity.ok(List.of()));
    }

    // ✅ READ (by id)
    @GetMapping("/{id}")
    public ResponseEntity<Customer> getCustomerById(@PathVariable Long id) {
        return ResponseEntity.ok(customerService.getById(id));
    }

    // ✅ CREATE (201 Created + Location)
    @PostMapping
    public ResponseEntity<Customer> registerCustomer(@RequestBody Customer customer) {
        Customer saved = customerService.registerCustomer(customer);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(saved.getId())
                .toUri();

        return ResponseEntity.created(location).body(saved);
    }

    // ✅ UPDATE
    @PutMapping("/{id}")
    public ResponseEntity<Customer> updateCustomer(@PathVariable Long id, @RequestBody Customer patch) {
        return ResponseEntity.ok(customerService.updateCustomer(id, patch));
    }

    // ✅ DELETE (204 No Content)
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCustomer(@PathVariable Long id) {
        customerService.deleteCustomer(id);
        return ResponseEntity.noContent().build();
    }
}
