package com.robb.shortner;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.security.Principal;

import com.robb.shortner.models.ShortenedLink;
import com.robb.shortner.services.ShortenedLinkService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class AdminControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ShortenedLinkService shortenedLinkService;

    @BeforeEach
    public void setup() {
        // Setup initial state or reset mocks
    }

    @Test
    @WithMockUser(username = "roberto")
    public void createShortenedUrls_ValidUrl_ShouldReturnCreated() throws Exception {
        ShortenedLink link = new ShortenedLink();
        link.setUrl("http://example.com");

        when(shortenedLinkService.saveShortenedLink(any(ShortenedLink.class))).thenReturn(link);

        mockMvc.perform(post("/admin")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"url\":\"http://example.com\"}"))
                .andExpect(status().isCreated());
    }

    @Test    
    @WithMockUser(username = "roberto")
    public void deleteShortenedUrls_ValidId_ShouldReturnNoContent() throws Exception {
        doNothing().when(shortenedLinkService).deleteShortenedLink(anyLong(), any());

        mockMvc.perform(delete("/admin/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    @WithMockUser(username = "roberto")
    public void listShortenedUrls_ReturnList_ShouldReturnOk() throws Exception {
        mockMvc.perform(get("/admin"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "roberto")
    public void updateShortenedUrls_ValidUpdate_ShouldReturnNoContent() throws Exception {
        ShortenedLink existingLink = new ShortenedLink();
        existingLink.setId(1L);
        existingLink.setUrl("http://original.com");
    
        ShortenedLink updatedLink = new ShortenedLink();
        updatedLink.setId(1L);
        updatedLink.setUrl("http://updated.com");

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Principal principal = (Principal) authentication;

        when(shortenedLinkService.findShortenedLinkById(1L)).thenReturn(existingLink);
        when(shortenedLinkService.updateShortenedLink(any(ShortenedLink.class), eq(principal))).thenReturn(updatedLink); // or doNothing() if it's void
    
        mockMvc.perform(put("/admin/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"url\":\"http://updated.com\"}"))
                .andExpect(status().isNoContent());
    
    }
}
