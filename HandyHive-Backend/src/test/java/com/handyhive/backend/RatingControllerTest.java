package com.handyhive.backend;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.handyhive.backend.dto.RatingRequestDTO;
import com.handyhive.backend.model.Rating;
import com.handyhive.backend.service.RatingService;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class RatingControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private RatingService ratingService;

    @Test
    public void createRatingForJob_returns201_andSetsJobIdInDTO() throws Exception {
        Rating saved = new Rating();
        saved.setRatingId(10L);

        when(ratingService.createRating(any(RatingRequestDTO.class))).thenReturn(saved);

        Map<String, Object> ratingDto = new HashMap<>();
        ratingDto.put("score", 5.0);
        ratingDto.put("comment", "Excellent service!");

        mockMvc.perform(post("/api/jobs/1/rating")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(ratingDto)))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", org.hamcrest.Matchers.containsString("/api/ratings/10")));

        // Verify controller injected jobId=1 into DTO
        ArgumentCaptor<RatingRequestDTO> captor = ArgumentCaptor.forClass(RatingRequestDTO.class);
        verify(ratingService).createRating(captor.capture());
        assertEquals(1L, captor.getValue().getJobId());
    }
}
