package com.handyhive.backend;

import com.handyhive.backend.dto.JobRequestDTO;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class JobRequestDTOTest {

    @Test
    public void testGettersAndSetters() {
        // 1. Create DTO
        JobRequestDTO dto = new JobRequestDTO();

        // 2. Set Values
        dto.setUserId(1L);
        dto.setProviderId(20L);
        dto.setServiceId(5L);
        dto.setDescription("Test Description");
        dto.setIsUrgent(true);

        // 3. Assert Values (Tests Getters)
        assertEquals(1L, dto.getUserId());
        assertEquals(20L, dto.getProviderId());
        assertEquals(5L, dto.getServiceId());
        assertEquals("Test Description", dto.getDescription());
        assertTrue(dto.getIsUrgent());
    }
}