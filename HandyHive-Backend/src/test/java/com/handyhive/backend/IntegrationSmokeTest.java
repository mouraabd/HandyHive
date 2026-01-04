package com.handyhive.backend;

import com.handyhive.backend.controller.JobController;
import com.handyhive.backend.controller.CustomerController;
import com.handyhive.backend.controller.ProviderController;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class IntegrationSmokeTest {

    @Autowired private JobController jobController;
    @Autowired private CustomerController customerController;
    @Autowired private ProviderController providerController;

    @Test
    void contextLoads() {
        // This fails if the application crashes on startup
        assertThat(jobController).isNotNull();
        assertThat(customerController).isNotNull();
        assertThat(providerController).isNotNull();
    }
}