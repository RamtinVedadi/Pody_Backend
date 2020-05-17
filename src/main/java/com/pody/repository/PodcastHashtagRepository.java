package com.pody.repository;

import com.pody.model.PodcastHashtag;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface PodcastHashtagRepository extends AbstractRepository<PodcastHashtag, UUID> {
}
