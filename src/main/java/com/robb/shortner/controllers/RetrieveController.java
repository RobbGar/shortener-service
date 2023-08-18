package com.robb.shortner.controllers;

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
@RequestMapping("/retrieve")
public class RetrieveController {
    
    private final ShortenedLinkService shortenedLinkService;

    @Autowired
    public RetrieveController(ShortenedLinkService shortenedLinkService) {
        this.shortenedLinkService = shortenedLinkService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> retrieveLink(@PathVariable Long id) {
        try {
            final ShortenedLink link = shortenedLinkService.findShortenedLinkById(id);
            return new ResponseEntity<String>(link.getUrl(), HttpStatus.OK);
        }
        catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }
    
}
