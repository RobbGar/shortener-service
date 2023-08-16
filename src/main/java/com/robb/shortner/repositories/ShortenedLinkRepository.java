package com.robb.shortner.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.robb.shortner.models.ShortenedLink;

@Repository
public interface ShortenedLinkRepository extends JpaRepository<ShortenedLink, Long> {
    List<ShortenedLink> findByKeycloakUserId(String userId);
}
