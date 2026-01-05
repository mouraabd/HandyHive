package com.handyhive.backend;

import com.handyhive.backend.model.Provider;
import com.handyhive.backend.service.ProviderService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class ProviderControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ProviderService providerService;

    @Test
    public void testRegisterProviderMultipart_Returns201() throws Exception {
        MockMultipartFile file = new MockMultipartFile(
                "document", "doc.pdf", "application/pdf", "dummy".getBytes()
        );

        Provider saved = new Provider();
        saved.setProviderId(1L);

        when(providerService.registerProviderWithDoc(any(Provider.class), any())).thenReturn(saved);

        mockMvc.perform(multipart("/api/providers")
                        .file(file)
                        .param("firstName", "Bob")
                        .param("lastName", "Builder")
                        .param("email", "bob@worker.com")
                        .param("password", "123")
                        .param("phone", "1234567890")
                        .param("bio", "Expert Builder"))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", org.hamcrest.Matchers.containsString("/api/providers/1")));
    }
}
