package com.pody.repository;

import com.pody.dto.repositories.PodcastListDto;
import com.pody.model.BlogLike;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface BlogLikeRepository extends AbstractRepository<BlogLike, UUID> {
    @Override
    BlogLike save(BlogLike bl);

    @Modifying
    @Query("delete from BlogLike bl where bl.blog.id = :blogId and bl.user.id = :userId")
    int deleteBlogLike(@Param("blogId") UUID blogId, @Param("userId") UUID userId);
}
