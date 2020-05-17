package com.pody.repository;

import com.pody.model.Podcast;
import com.pody.model.PodcastCategory;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface PodcastCategoryRepository extends AbstractRepository<PodcastCategory, UUID> {
    PodcastCategory findOneByPodcast(Podcast podcast);
}
