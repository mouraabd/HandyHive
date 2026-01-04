package com.handyhive.backend;

import com.handyhive.backend.model.Service;
import com.handyhive.backend.repository.ServiceRepository;
import com.handyhive.backend.service.ServiceService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ServiceServiceTest {

    @Mock
    private ServiceRepository serviceRepository;

    @InjectMocks
    private ServiceService serviceService;

    @Test
    public void testGetAllServices() {
        // 1. Arrange
        Service s1 = new Service();
        s1.setName("Plumbing");
        Service s2 = new Service();
        s2.setName("Cleaning");

        when(serviceRepository.findAll()).thenReturn(Arrays.asList(s1, s2));

        // 2. Act
        List<Service> result = serviceService.getAllServices();

        // 3. Assert
        assertFalse(result.isEmpty());
        assertEquals(2, result.size());
        assertEquals("Plumbing", result.get(0).getName());

        verify(serviceRepository).findAll();
    }
}