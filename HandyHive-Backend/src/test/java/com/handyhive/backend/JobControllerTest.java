package com.handyhive.backend;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.handyhive.backend.dto.JobRequestDTO;
import com.handyhive.backend.model.Job;
import com.handyhive.backend.model.JobStatus;
import com.handyhive.backend.service.JobService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class JobControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private JobService jobService; // ✅ Mocks the service so we don't need real DB

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void testCreateJobEndpoint() throws Exception {
        // 1. Prepare Input DTO
        JobRequestDTO dto = new JobRequestDTO();
        dto.setUserId(1L);
        dto.setProviderId(2L);
        dto.setServiceId(3L);
        dto.setDescription("Leak fix");
        dto.setIsUrgent(true);

        // 2. Prepare Mock Output
        Job savedJob = new Job();
        savedJob.setJobId(101L);
        savedJob.setStatus(JobStatus.PENDING);

        // 3. Mock the Service Call
        when(jobService.createJob(any(JobRequestDTO.class))).thenReturn(savedJob);

        // 4. Perform Request & Assert
        mockMvc.perform(post("/api/jobs/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.jobId").value(101))
                .andExpect(jsonPath("$.status").value("PENDING"));
    }
}