package com.handyhive.backend;

import com.handyhive.backend.model.Service;
import com.handyhive.backend.repository.ServiceRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class ServiceRepositoryTest {

    @Autowired
    private ServiceRepository serviceRepository;

    @Test
    public void testSaveAndFindService() {
        // 1. Save
        Service service = new Service();
        service.setName("Gardening");
        service.setDescription("Cutting grass");
        service.setBasePrice(500.0);

        Service saved = serviceRepository.save(service);

        // 2. Find
        Optional<Service> found = serviceRepository.findById(saved.getServiceId());

        // 3. Assert
        assertThat(found).isPresent();
        assertThat(found.get().getName()).isEqualTo("Gardening");
        assertThat(found.get().getBasePrice()).isEqualTo(500.0);
    }
}