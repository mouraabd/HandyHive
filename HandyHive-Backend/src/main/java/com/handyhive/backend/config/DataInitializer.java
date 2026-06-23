package com.handyhive.backend.config;

import com.handyhive.backend.model.Service;
import com.handyhive.backend.model.Subscription;
import com.handyhive.backend.repository.ServiceRepository;
import com.handyhive.backend.repository.SubscriptionRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.math.BigDecimal;

@Configuration
public class DataInitializer {

    @Bean
    CommandLineRunner seedDefaultData(ServiceRepository serviceRepository,
                                      SubscriptionRepository subscriptionRepository) {
        return args -> {
            createServiceIfMissing(serviceRepository, "Plumbing", "General plumbing services", 1500.0);
            createServiceIfMissing(serviceRepository, "Electrician", "Electrical repairs and installations", 2000.0);
            createServiceIfMissing(serviceRepository, "Handyman", "General home repairs", 800.0);
            createServiceIfMissing(serviceRepository, "Deep Cleaning", "Full house deep cleaning", 2500.0);

            createSubscriptionIfMissing(subscriptionRepository, "Standard", BigDecimal.ZERO, "Basic provider plan");
            createSubscriptionIfMissing(subscriptionRepository, "Monthly", BigDecimal.valueOf(400.0), "Monthly provider visibility plan");
            createSubscriptionIfMissing(subscriptionRepository, "Annual", BigDecimal.valueOf(4000.0), "Annual provider visibility plan");
        };
    }

    private void createServiceIfMissing(ServiceRepository repository,
                                        String name,
                                        String description,
                                        Double basePrice) {
        repository.findFirstByNameIgnoreCase(name).orElseGet(() -> {
            Service service = new Service();
            service.setName(name);
            service.setDescription(description);
            service.setBasePrice(basePrice);
            return repository.save(service);
        });
    }

    private void createSubscriptionIfMissing(SubscriptionRepository repository,
                                             String planName,
                                             BigDecimal priceCzk,
                                             String description) {
        repository.findFirstByPlanNameIgnoreCase(planName).orElseGet(() ->
                repository.save(new Subscription(planName, priceCzk, description))
        );
    }
}
