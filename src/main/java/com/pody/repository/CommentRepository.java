package com.pody.repository;

import com.pody.dto.repositories.PodcastCommentListDto;
import com.pody.model.Comment;
import com.pody.model.Podcast;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface CommentRepository extends AbstractRepository<Comment, UUID> {
    @Modifying
    @Query("update Comment c set c.likeCount  = c.likeCount + 1 where c.id = :id")
    int updateLikesCount(@Param("id") UUID id);

    @Modifying
    @Query("update Comment c set c.isApprove = 1 where c.id = :id")
    int updateApproveComment(@Param("id") UUID id);

    List<Comment> findAllByPodcast(Podcast p);

    @Query("select c.id as commentId, c.description as description, u.id as userId," +
            " u.profileImageAddress as userImage, u.username as username, u.title as userTitle  " +
            " from Comment c inner join User u on c.user.id = u.id " +
            " where c.podcast.id = :pid and c.isApprove = true")
    List<PodcastCommentListDto> eachPodcastCommentsList(@Param("pid") UUID podcastId, Pageable pageable);
}
