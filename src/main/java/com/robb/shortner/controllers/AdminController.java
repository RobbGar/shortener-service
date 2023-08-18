package com.robb.shortner.controllers;

import java.nio.file.AccessDeniedException;
import java.security.Principal;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.robb.shortner.models.ShortenedLink;
import com.robb.shortner.models.ShortenedLinkCreate;
import com.robb.shortner.services.ShortenedLinkService;

import jakarta.persistence.EntityNotFoundException;

@RestController
@RequestMapping("/admin")
public class AdminController {

    private final ShortenedLinkService shortenedLinkService;

    private static final Logger logger = LoggerFactory.getLogger(AdminController.class);

    @Autowired
    public AdminController(ShortenedLinkService shortenedLinkService) {
        this.shortenedLinkService = shortenedLinkService;
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> createShortenedUrls(@RequestBody ShortenedLinkCreate shortenedLinkCreate, Principal principal) {
        try {
            ShortenedLink shortenedLink = new ShortenedLink();
            shortenedLink.setUrl(shortenedLinkCreate.getUrl());
            shortenedLink.setKeycloakUserId(principal.getName());
            final ShortenedLink savedShortenedLink = shortenedLinkService.saveShortenedLink(shortenedLink);
            logger.info("Created new Shortened URL with id {} and url {}", savedShortenedLink.getId(), savedShortenedLink.getUrl());
            return new ResponseEntity<ShortenedLink>(savedShortenedLink, HttpStatus.CREATED);
        }
        catch (IllegalArgumentException e) {
            logger.warn("Error - malformed URL: {}", e.getMessage());
            return new ResponseEntity<String>("Provided URL is malformed", HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteShortenedUrls(@PathVariable Long id, Principal principal) {
        try {
            shortenedLinkService.deleteShortenedLink(id, principal);
        } catch (AccessDeniedException e) {
            logger.warn("User {} not authorized to make DELETE request for id {}", principal.getName(), id);
            return new ResponseEntity<Void>(HttpStatus.FORBIDDEN);
        }
        logger.info("Deleted link with id {}", id);
        return new ResponseEntity<Void>(HttpStatus.NO_CONTENT);
    }

    @GetMapping
    public ResponseEntity<?> listShortenedUrls(Principal principal) {
        final List<ShortenedLink> shortenedLinks = shortenedLinkService.listShortenedLinksByUser(principal.getName());
        return new ResponseEntity<List<ShortenedLink>>(shortenedLinks, HttpStatus.OK);
    }

    @PutMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> updateShortenedUrls(@RequestBody ShortenedLink shortenedURLUpdate, Principal principal) {
        try {
            final ShortenedLink updatedShortenedLink = shortenedLinkService.updateShortenedLink(shortenedURLUpdate, principal);
            logger.info("Shortened URL with id {} updated to url {}", updatedShortenedLink.getId(), updatedShortenedLink.getUrl());
        } catch (AccessDeniedException e) {
            logger.warn("User {} not authorized to make PUT request for id {}", principal.getName(), shortenedURLUpdate.getId());
            return new ResponseEntity<Void>(HttpStatus.FORBIDDEN);
        } catch (EntityNotFoundException e) {
            logger.debug("Shortened URL not found: {}", shortenedURLUpdate.getId());
            return new ResponseEntity<Void>(HttpStatus.NOT_FOUND); 
        }
        return new ResponseEntity<ShortenedLink>(HttpStatus.NO_CONTENT);
    }
    
}
