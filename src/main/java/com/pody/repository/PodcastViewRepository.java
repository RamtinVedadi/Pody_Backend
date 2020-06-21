package com.pody.repository;

import com.pody.model.PodcastView;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.UUID;

@Repository
public interface PodcastViewRepository extends AbstractRepository<PodcastView, UUID> {

    @Query("select new PodcastView(pv.id) from PodcastView pv where pv.podcast.id = :podcastId and  pv.date between :yesterday and :today")
    PodcastView findPodcastView(@Param("podcastId") UUID podcastId, @Param("yesterday") Date yesterday, @Param("today") Date today);

    @Modifying
    @Query("update PodcastView p set p.count = p.count + 1 where p.id = :id")
    int updateViewCount(@Param("id") UUID id);

    @Query("select sum(pv.count) from PodcastView pv INNER join Podcast p on pv.podcast.id = p.id where  p.user.id = :userId")
    Integer channelListenCount(@Param("userId") UUID id);
}
