package com.pody.repository;

import com.pody.model.CommentNews;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface CommentNewsRepository extends AbstractRepository<CommentNews, UUID> {
    @Modifying
    @Query("update CommentNews c set c.likeCount  = c.likeCount + 1 where c.id = :id")
    int updateLikesCount(@Param("id") UUID id);

    @Modifying
    @Query("update CommentNews c set c.isApprove = 1 where c.id = :id")
    int updateApproveComment(@Param("id") UUID id);
}
