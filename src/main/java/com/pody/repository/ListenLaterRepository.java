package com.pody.repository;

import com.pody.dto.repositories.PodcastListDto;
import com.pody.model.ListenLater;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ListenLaterRepository extends AbstractRepository<ListenLater, UUID> {
    @Query("select p.id as podcastId, p.title as title, p.imageAddress as podcastImage, p.audioAddress as audioAddress," +
            " p.episodeNumber as episodeNumber, p.seasonNumber as seasonNumber, p.duration as duration, p.viewCount as viewCount," +
            " p.likeCount as likeCount, u.id as userId, u.username as username, u.title as userTitle, u.profileImageAddress as profileImageAddress" +
            " from Podcast p inner join User u on p.user.id = u.id " +
            " inner join ListenLater ll on p.id = ll.podcast.id " +
            " where ll.user.id = :userId")
    List<PodcastListDto> listenLaterList(@Param("userId") UUID userId, Pageable pageable);

    @Query("select new ListenLater (ll.id) from ListenLater ll where ll.user.id = :userId and ll.podcast.id = :podcastId")
    ListenLater checkIsListenLater(@Param("userId") UUID userId, @Param("podcastId") UUID podcastId);

    @Modifying
    @Query("delete from ListenLater ll where ll.user.id = :userId and ll.podcast.id = :podcastId")
    int deleteListenLater(@Param("userId") UUID userId, @Param("podcastId") UUID podcastId);
}
