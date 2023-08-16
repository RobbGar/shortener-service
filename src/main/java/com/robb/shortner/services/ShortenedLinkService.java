package com.robb.shortner.services;

import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.AccessDeniedException;
import java.security.Principal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.robb.shortner.models.ShortenedLink;
import com.robb.shortner.repositories.ShortenedLinkRepository;

import jakarta.persistence.EntityNotFoundException;

@Service
public class ShortenedLinkService {

    private final ShortenedLinkRepository shortenedLinkRepository;

    @Autowired
    public ShortenedLinkService(ShortenedLinkRepository shortenedLinkRepository) {
        this.shortenedLinkRepository = shortenedLinkRepository;
    }


    private boolean isValidURL(String urlStr) {
        try {
            new URL(urlStr);
            return true;
        } catch (MalformedURLException e) {
            return false;
        }
    }


    private boolean isOwner(ShortenedLink link, Principal principal) {
        return link.getKeycloakUserId() == principal.getName();
    }


    @Transactional
    public ShortenedLink saveShortenedLink(ShortenedLink link) throws IllegalArgumentException {
        if (!isValidURL(link.getUrl())) {
            throw new IllegalArgumentException("Invalid URL");
        }
        return shortenedLinkRepository.save(link);
    }

    public ShortenedLink findShortenedLinkById(Long id) throws EntityNotFoundException {
        return shortenedLinkRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Link with url " + id + " not Found"));
    }

    public List<ShortenedLink> listShortenedLinksByUser(String userId) {
        return shortenedLinkRepository.findByKeycloakUserId(userId);
    }

    public void deleteShortenedLink(Long id, Principal principal) throws AccessDeniedException, EntityNotFoundException {
        ShortenedLink link = shortenedLinkRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Shortened Link with id " + id + " not found"));
        if (!isOwner(link, principal)) {
            throw new AccessDeniedException("User " + principal.getName() + " is not authorized to make this request");
        }
        shortenedLinkRepository.deleteById(id);
    }

    public ShortenedLink updateShortenedLink(ShortenedLink newShortenedLink, Principal principal) throws AccessDeniedException, EntityNotFoundException {
        ShortenedLink existingLink = shortenedLinkRepository.findById(newShortenedLink.getId()).orElseThrow(() -> new EntityNotFoundException("Shortened Link with id " + newShortenedLink.getId() + " not found"));
        if (!isOwner(existingLink, principal)) {
            throw new AccessDeniedException("User " + principal.getName() + " is not authorized to make this request");
        }
        existingLink.setUrl(newShortenedLink.getUrl());

        return shortenedLinkRepository.save(existingLink);
    }

}
