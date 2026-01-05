package com.handyhive.backend.service;

import com.handyhive.backend.exception.ResourceNotFoundException;
import com.handyhive.backend.model.Subscription;
import com.handyhive.backend.repository.SubscriptionRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SubscriptionService {

    private final SubscriptionRepository subscriptionRepository;

    public SubscriptionService(SubscriptionRepository subscriptionRepository) {
        this.subscriptionRepository = subscriptionRepository;
    }

    public List<Subscription> getAll() {
        return subscriptionRepository.findAll();
    }

    public Subscription getById(Integer id) {
        return subscriptionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Subscription not found with id: " + id));
    }

    public Subscription create(Subscription subscription) {
        return subscriptionRepository.save(subscription);
    }

    public Subscription update(Integer id, Subscription updated) {
        Subscription existing = getById(id);

        if (updated.getPlanName() != null) existing.setPlanName(updated.getPlanName());
        if (updated.getPriceCzk() != null) existing.setPriceCzk(updated.getPriceCzk());
        if (updated.getDescription() != null) existing.setDescription(updated.getDescription());

        return subscriptionRepository.save(existing);
    }

    public void delete(Integer id) {
        Subscription existing = getById(id);
        subscriptionRepository.delete(existing);
    }
}
