package com.handyhive.backend.dto;

import java.util.List;

public class ProviderMatchRequestDTO {

    private List<Long> serviceIds;

    public List<Long> getServiceIds() {
        return serviceIds;
    }

    public void setServiceIds(List<Long> serviceIds) {
        this.serviceIds = serviceIds;
    }
}
