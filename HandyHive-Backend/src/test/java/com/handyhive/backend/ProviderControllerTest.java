package com.handyhive.backend;

import com.handyhive.backend.controller.ProviderController;
import com.handyhive.backend.model.Provider;
import com.handyhive.backend.service.ProviderService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class ProviderControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ProviderService providerService;

    @Test
    public void testRegisterProviderWithFile_Success() throws Exception {
        // 1. Create the File
        MockMultipartFile file = new MockMultipartFile(
                "document",
                "id_card.pdf",
                MediaType.APPLICATION_PDF_VALUE,
                "Dummy PDF Content".getBytes()
        );

        // 2. Mock Service
        when(providerService.registerProviderWithDoc(any(Provider.class), any())).thenReturn(new Provider());

        // 3. Perform Request
        mockMvc.perform(multipart("/api/providers")
                        .file(file)
                        .param("firstName", "Bob")
                        .param("lastName", "Builder")
                        .param("email", "bob@worker.com")
                        .param("password", "123")
                        // ✅ ADDED THESE TO FIX THE ERROR
                        .param("phone", "1234567890")
                        .param("bio", "Expert Builder"))
                .andExpect(status().isOk());
    }
}