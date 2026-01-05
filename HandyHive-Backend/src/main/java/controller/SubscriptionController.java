package com.handyhive.backend.controller;

import com.handyhive.backend.model.Subscription;
import com.handyhive.backend.service.SubscriptionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/subscriptions")
public class SubscriptionController {

    private final SubscriptionService subscriptionService;

    public SubscriptionController(SubscriptionService subscriptionService) {
        this.subscriptionService = subscriptionService;
    }

    @GetMapping
    public ResponseEntity<List<Subscription>> getAll() {
        return ResponseEntity.ok(subscriptionService.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Subscription> getById(@PathVariable Integer id) {
        return ResponseEntity.ok(subscriptionService.getById(id));
    }

    @PostMapping
    public ResponseEntity<Subscription> create(@RequestBody Subscription subscription) throws Exception {
        Subscription created = subscriptionService.create(subscription);
        return ResponseEntity.created(new URI("/api/subscriptions/" + created.getId())).body(created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Subscription> update(@PathVariable Integer id, @RequestBody Subscription subscription) {
        return ResponseEntity.ok(subscriptionService.update(id, subscription));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        subscriptionService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
