package com.pody.repository;

import com.pody.model.PlaylistPodcasts;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface PlaylistPodcastsRepository extends AbstractRepository<PlaylistPodcasts, UUID> {
}
