package com.handyhive.backend;

import com.handyhive.backend.model.Provider;
import com.handyhive.backend.model.Service;
import com.handyhive.backend.repository.ProviderRepository;
import com.handyhive.backend.repository.ServiceRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
// ✅ Removed "AutoConfigureTestDatabase". It will now automatically use the empty H2 DB.
public class ProviderRepositoryTest {

    @Autowired
    private ProviderRepository providerRepository;

    @Autowired
    private ServiceRepository serviceRepository;

    @Test
    public void testFindByServiceId() {
        // 1. Arrange: Create a Service and a Provider
        Service plumbing = new Service();
        plumbing.setName("Plumbing");
        // ✅ Kept the 1500.0 (Double) fix
        plumbing.setBasePrice(1500.0);
        serviceRepository.save(plumbing);

        Provider jack = new Provider();
        jack.setFirstName("Jack");
        jack.setEmail("jack@test.com");
        jack.setServices(List.of(plumbing));
        providerRepository.save(jack);

        // 2. Act: Run the query
        // ✅ Kept the getServiceId() fix
        List<Provider> matches = providerRepository.findByServices_ServiceId(plumbing.getServiceId());

        // 3. Assert: Check results
        assertThat(matches).isNotEmpty();
        assertThat(matches.get(0).getFirstName()).isEqualTo("Jack");
    }
}