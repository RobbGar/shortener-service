package com.robb.shortner;

import com.robb.shortner.models.ShortenedLink;
import com.robb.shortner.services.ShortenedLinkService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import jakarta.persistence.EntityNotFoundException;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class RetrieveControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ShortenedLinkService shortenedLinkService;

    @Test
    public void retrieveLinkLinkExistsShouldReturnUrl() throws Exception {
        ShortenedLink mockLink = new ShortenedLink();
        mockLink.setUrl("http://example.com");

        when(shortenedLinkService.findShortenedLinkById(1L)).thenReturn(mockLink);

        mockMvc.perform(get("/retrieve/1"))
               .andExpect(status().isOk())
               .andExpect(content().string("http://example.com"));

        verify(shortenedLinkService, times(1)).findShortenedLinkById(1L);
    }

    @Test
    public void retrieveLinkLinkDoesNotExistShouldReturnNotFound() throws Exception {
        when(shortenedLinkService.findShortenedLinkById(1L)).thenThrow(new EntityNotFoundException());

        mockMvc.perform(get("/retrieve/1"))
               .andExpect(status().isNotFound());

        verify(shortenedLinkService, times(1)).findShortenedLinkById(1L);
    }

}
