package com.robb.shortner;

import com.robb.shortner.models.ShortenedLink;
import com.robb.shortner.repositories.ShortenedLinkRepository;
import com.robb.shortner.services.ShortenedLinkService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.security.Principal;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

public class ShortenedLinkServiceTest {

    @InjectMocks
    private ShortenedLinkService shortenedLinkService;

    @Mock
    private ShortenedLinkRepository shortenedLinkRepository;

    @Mock
    private Principal principal;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void saveShortenedLinkWithValidURLShouldSave() {
        ShortenedLink link = new ShortenedLink();
        link.setUrl("http://valid.com");

        when(shortenedLinkRepository.save(any(ShortenedLink.class))).thenReturn(link);

        ShortenedLink savedLink = shortenedLinkService.saveShortenedLink(link);
        assertEquals("http://valid.com", savedLink.getUrl());
    }

    @Test
    public void saveShortenedLinkWithInvalidURLShouldThrowException() {
        ShortenedLink link = new ShortenedLink();
        link.setUrl("invalidURL");

        assertThrows(IllegalArgumentException.class, () -> {
            shortenedLinkService.saveShortenedLink(link);
        });
    }

    @Test
    public void findShortenedLinkByIdExistingIdShouldReturnLink() {
        ShortenedLink link = new ShortenedLink();
        link.setUrl("http://valid.com");

        when(shortenedLinkRepository.findById(1L)).thenReturn(Optional.of(link));

        ShortenedLink foundLink = shortenedLinkService.findShortenedLinkById(1L);
        assertEquals("http://valid.com", foundLink.getUrl());
    }

}
