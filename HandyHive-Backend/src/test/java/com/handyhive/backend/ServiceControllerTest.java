package com.handyhive.backend;

import com.handyhive.backend.model.Service;
import com.handyhive.backend.service.ServiceService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest // ✅ Fixes context crash
@AutoConfigureMockMvc
public class ServiceControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean // ✅ Replaces deprecated @MockBean
    private ServiceService serviceService;

    @Test
    public void testGetAllServices() throws Exception {
        Service s1 = new Service();
        s1.setName("Plumbing");
        Service s2 = new Service();
        s2.setName("Electrician");

        when(serviceService.getAllServices()).thenReturn(Arrays.asList(s1, s2));

        mockMvc.perform(get("/api/services"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(2))
                .andExpect(jsonPath("$[0].name").value("Plumbing"));
    }
}