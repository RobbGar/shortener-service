package com.robb.shortner.controllers;

import java.net.URI;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.robb.shortner.models.ShortenedLink;
import com.robb.shortner.services.ShortenedLinkService;

import jakarta.persistence.EntityNotFoundException;


@RestController
@RequestMapping("/r")
public class ShortenerController {
    
    private final ShortenedLinkService shortenedLinkService;

    @Autowired
    public ShortenerController(ShortenedLinkService shortenedLinkService) {
        this.shortenedLinkService = shortenedLinkService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<Void> redirectToOriginal(@PathVariable Long id) {
        try {
            ShortenedLink link = shortenedLinkService.findShortenedLinkById(id);
            return ResponseEntity.status(HttpStatus.FOUND)
                                .location(URI.create(link.getUrl()))
                                .build();
        }
        catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }
    
}
