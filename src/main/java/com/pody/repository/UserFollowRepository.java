package com.pody.repository;

import com.pody.model.UserFollow;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface UserFollowRepository extends AbstractRepository<UserFollow, UUID> {

    @Modifying
    @Query("delete from UserFollow uf where uf.user.id = :userId and uf.follower.id = :followerId")
    int deleteFollowership(@Param("userId") UUID userid, @Param("followerId") UUID followerid);

    @Query("select new UserFollow (uf.id) from UserFollow uf where uf.follower.id = :podcasterId and uf.user.id = :userId")
    UserFollow isUserFollowAvailable(@Param("userId") UUID userId, @Param("podcasterId") UUID podcasterId);
}
