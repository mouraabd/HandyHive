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
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class JobControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private JobService jobService;

    @Test
    public void testCreateJobEndpoint_Returns201() throws Exception {
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
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", org.hamcrest.Matchers.containsString("/api/jobs/1")))
                .andExpect(jsonPath("$.status").value("PENDING"));
    }

    @Test
    public void testInvalidJobStatus_ShouldReturn400() throws Exception {
        when(jobService.updateJobStatus(any(), eq("GARBAGE")))
                .thenThrow(new IllegalArgumentException("Invalid status: GARBAGE"));

        mockMvc.perform(put("/api/jobs/1/status")
                        .param("status", "GARBAGE")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("Bad Request"));
    }
}
