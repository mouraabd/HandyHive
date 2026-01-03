package com.handyhive.backend.service;

import com.handyhive.backend.exception.ResourceNotFoundException;
import com.handyhive.backend.model.Service;
import com.handyhive.backend.repository.ServiceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.List;

@org.springframework.stereotype.Service
public class ServiceService {

    @Autowired
    private ServiceRepository serviceRepository;

    public List<Service> getAllServices() {
        return serviceRepository.findAll();
    }

    public Service getServiceById(Long serviceId) {
        return serviceRepository.findById(serviceId)
                .orElseThrow(() -> new ResourceNotFoundException("Service not found with id: " + serviceId));
    }

    public Service createService(Service service) {
        return serviceRepository.save(service);
    }

    public Service updateService(Long serviceId, Service serviceDetails) {
        Service existingService = getServiceById(serviceId);

        // FIX: Use the correct new field names (name, description, basePrice)
        existingService.setName(serviceDetails.getName());
        existingService.setDescription(serviceDetails.getDescription());
        existingService.setBasePrice(serviceDetails.getBasePrice());

        return serviceRepository.save(existingService);
    }

    public void deleteService(Long serviceId) {
        Service service = getServiceById(serviceId);
        serviceRepository.delete(service);
    }
}