package com.handyhive.backend;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.handyhive.backend.controller.JobController;
import com.handyhive.backend.dto.JobRequestDTO;
import com.handyhive.backend.model.Job;
import com.handyhive.backend.model.JobStatus;
import com.handyhive.backend.service.JobService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

// ✅ THESE ARE THE MISSING IMPORTS CAUSING THE ERROR
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put; // Fixes "cannot find symbol method put"
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(JobController.class)
public class JobControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private JobService jobService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void testCreateJobEndpoint() throws Exception {
        Job mockJob = new Job();
        mockJob.setJobId(1L);
        mockJob.setStatus(JobStatus.PENDING);

        when(jobService.createJob(any(JobRequestDTO.class))).thenReturn(mockJob);

        JobRequestDTO request = new JobRequestDTO();
        request.setUserId(1L);
        request.setProviderId(2L);
        request.setServiceId(3L);
        request.setDescription("Fix leaking pipe");
        request.setIsUrgent(true);

        mockMvc.perform(post("/api/jobs")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("PENDING"));
    }

    @Test
    public void testInvalidJobStatus_ShouldReturn400() throws Exception {
        // 1. Mock the Service to throw your specific error
        when(jobService.updateJobStatus(any(), eq("GARBAGE"))) // ✅ Now 'eq' will work
                .thenThrow(new IllegalArgumentException("Invalid status: GARBAGE"));

        // 2. Perform the Request
        mockMvc.perform(put("/api/jobs/1/status") // ✅ Now 'put' will work
                        .param("status", "GARBAGE")
                        .contentType(MediaType.APPLICATION_JSON))

                // 3. ASSERT that we get 400 Bad Request
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("Bad Request"));
    }
}