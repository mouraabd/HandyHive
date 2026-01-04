package com.handyhive.backend;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.handyhive.backend.model.Rating;
import com.handyhive.backend.service.RatingService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.HashMap;
import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class RatingControllerTest {

    @Autowired private MockMvc mockMvc;
    @Autowired private ObjectMapper objectMapper;

    @MockitoBean
    private RatingService ratingService;

    @Test
    public void testSubmitRating_Success() throws Exception {
        // 1. Create DTO
        Map<String, Object> ratingDto = new HashMap<>();
        ratingDto.put("score", 5.0);
        ratingDto.put("comment", "Excellent service!");

        // 2. Mock Service
        when(ratingService.addRating(eq(1L), any(Rating.class))).thenReturn(new Rating());

        // 3. Perform Request
        mockMvc.perform(post("/api/jobs/1/rating")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(ratingDto)))
                .andExpect(status().isCreated()); // ✅ FIXED: Expect 201 Created instead of 200 OK
    }
}