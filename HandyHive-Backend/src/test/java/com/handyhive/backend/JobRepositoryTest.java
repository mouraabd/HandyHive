package com.handyhive.backend;

import com.handyhive.backend.model.Customer;
import com.handyhive.backend.model.Job;
import com.handyhive.backend.model.JobStatus;
import com.handyhive.backend.model.Provider;
import com.handyhive.backend.repository.CustomerRepository;
import com.handyhive.backend.repository.JobRepository;
import com.handyhive.backend.repository.ProviderRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class JobRepositoryTest {

    @Autowired private JobRepository jobRepository;
    @Autowired private CustomerRepository customerRepository;
    @Autowired private ProviderRepository providerRepository;

    @Test
    public void testFindJobsByCustomerAndProvider() {
        // 1. Setup Data
        Customer cust = new Customer();
        cust.setEmail("c@test.com");
        cust.setFirstName("Cust");   // ✅ Required
        cust.setLastName("Omer");    // ✅ Required
        cust.setPasswordHash("123"); // ✅ Required
        customerRepository.save(cust);

        Provider prov = new Provider();
        prov.setEmail("p@test.com");
        prov.setFirstName("Prov");   // ✅ Required
        prov.setLastName("Ider");    // ✅ Required
        prov.setPasswordHash("123"); // ✅ Required
        providerRepository.save(prov);

        Job job1 = new Job();
        job1.setCustomer(cust);
        job1.setProvider(prov);
        job1.setStatus(JobStatus.PENDING);
        jobRepository.save(job1);

        Job job2 = new Job();
        job2.setCustomer(cust);
        job2.setProvider(prov);
        job2.setStatus(JobStatus.COMPLETED);
        jobRepository.save(job2);

        // 2. Test: Find by Customer
        List<Job> customerJobs = jobRepository.findByCustomer_Id(cust.getId());
        assertThat(customerJobs).hasSize(2);

        // 3. Test: Find by Provider
        List<Job> providerJobs = jobRepository.findByProvider_ProviderId(prov.getProviderId());
        assertThat(providerJobs).hasSize(2);
    }
}