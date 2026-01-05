package com.handyhive.backend.dto;

public class ProviderMatchItemDTO {

    private Long serviceId;
    private ProviderResponseDTO provider;

    public Long getServiceId() {
        return serviceId;
    }

    public void setServiceId(Long serviceId) {
        this.serviceId = serviceId;
    }

    public ProviderResponseDTO getProvider() {
        return provider;
    }

    public void setProvider(ProviderResponseDTO provider) {
        this.provider = provider;
    }
}
