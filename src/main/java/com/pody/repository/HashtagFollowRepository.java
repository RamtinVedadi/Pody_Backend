package com.pody.repository;

import com.pody.model.HashtagFollow;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.UUID;


@Repository
public interface HashtagFollowRepository extends AbstractRepository<HashtagFollow, UUID> {
    @Modifying
    @Query("delete from HashtagFollow hf where hf.hashtag.id = :hashtagId and hf.follower.id = :followerId")
    int deleteFollowership(@Param("hashtagId") UUID hashtagid, @Param("followerId") UUID followerid);
}
