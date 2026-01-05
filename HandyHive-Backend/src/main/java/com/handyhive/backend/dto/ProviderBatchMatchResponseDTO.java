package com.handyhive.backend.dto;

import java.util.ArrayList;
import java.util.List;

public class ProviderBatchMatchResponseDTO {

    private List<ProviderMatchItemDTO> assignments = new ArrayList<>();
    private List<Long> missingServiceIds = new ArrayList<>();

    public List<ProviderMatchItemDTO> getAssignments() {
        return assignments;
    }

    public void setAssignments(List<ProviderMatchItemDTO> assignments) {
        this.assignments = assignments;
    }

    public List<Long> getMissingServiceIds() {
        return missingServiceIds;
    }

    public void setMissingServiceIds(List<Long> missingServiceIds) {
        this.missingServiceIds = missingServiceIds;
    }
}
