package com.pody.repository;

import com.pody.model.PodcastLike;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface PodcastLikeRepository extends AbstractRepository<PodcastLike, UUID> {

    PodcastLike save(PodcastLike pl);

    @Modifying
    @Query("delete from PodcastLike pl where pl.podcast.id = :podcastId and pl.user.id = :userId")
    int deletePodcastLike(@Param("podcastId") UUID podcastId, @Param("userId") UUID userId);

    @Query("select pl from PodcastLike pl where pl.user.id = :userId and pl.podcast.id = :podcastId")
    List<PodcastLike> checkIsLike(@Param("userId") UUID userId, @Param("podcastId") UUID podcastId);
}
