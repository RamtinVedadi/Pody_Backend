package com.pody.repository;

import com.pody.model.ReadLater;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface ReadLaterRepository extends AbstractRepository<ReadLater, UUID> {

    @Query("select new ReadLater (rl.id) from ReadLater rl where rl.user.id = :userId and rl.blog.id = :blogId")
    List<ReadLater> checkIsBookmark(@Param("userId") UUID userId, @Param("blogId") UUID blogId);

    @Modifying
    @Query("delete from ReadLater rl where rl.user.id = :userId and rl.blog.id = :blogId")
    int deleteListenLater(@Param("userId") UUID userId, @Param("blogId") UUID blogId);
}
