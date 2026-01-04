package com.handyhive.backend;

import com.handyhive.backend.model.Customer;
import com.handyhive.backend.model.Provider;
import com.handyhive.backend.model.Job;
import com.handyhive.backend.model.Service;
import com.handyhive.backend.model.JobStatus;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class EntityCoverageTest {

    @Test
    public void testCustomerEntity() {
        Customer c = new Customer();
        c.setId(1L);
        c.setFirstName("John");
        c.setLastName("Doe");
        c.setPhoneNumber("12345");

        assertEquals(1L, c.getId());
        assertEquals("John", c.getFirstName());
        assertEquals("Doe", c.getLastName());
        assertEquals("12345", c.getPhoneNumber());
        assertNull(c.getSubscriptionId()); // Verify default null
    }

    @Test
    public void testProviderEntity() {
        Provider p = new Provider();
        p.setProviderId(2L);
        p.setBio("Expert Plumber");
        p.setIsVetted(true);
        p.setAvgRating(4.5);
        p.setJobsCompleted(10);

        assertEquals(2L, p.getProviderId());
        assertEquals("Expert Plumber", p.getBio());
        assertTrue(p.getIsVetted());
        assertEquals(4.5, p.getAvgRating());
        assertEquals(10, p.getJobsCompleted());
    }

    @Test
    public void testJobEntityAndEnum() {
        Job j = new Job();
        j.setDescription("Fix Sink");
        j.setIsUrgent(true);
        j.setStatus(JobStatus.IN_PROGRESS); // Test Enum

        assertEquals("Fix Sink", j.getDescription());
        assertTrue(j.getIsUrgent());
        assertEquals(JobStatus.IN_PROGRESS, j.getStatus());
    }

    @Test
    public void testServiceEntity() {
        Service s = new Service();
        s.setServiceId(10L);
        s.setName("Cleaning");
        s.setBasePrice(100.0);

        assertEquals(10L, s.getServiceId());
        assertEquals("Cleaning", s.getName());
        assertEquals(100.0, s.getBasePrice());
    }
}