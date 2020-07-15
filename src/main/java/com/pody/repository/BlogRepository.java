package com.pody.repository;

import com.pody.dto.repositories.BlogListDto;
import com.pody.dto.repositories.BlogReadDto;
import com.pody.dto.repositories.CategorySitemapDto;
import com.pody.dto.repositories.PodcastListDto;
import com.pody.model.Blog;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface BlogRepository extends AbstractRepository<Blog, UUID> {
    @Override
    Blog save(Blog blog);

    @Modifying
    @Query("update Blog b set b.imageAddress = :imagePath where b.id = :id")
    int updateImageAddress(@Param("imagePath") String imagePath, @Param("id") UUID id);

    @Modifying
    @Query("update Blog b set b.isPublish = true where b.id = :id")
    int updateIsPublish(@Param("id") UUID id);

    @Modifying
    @Query("update Blog b set b.likeCount = b.likeCount + 1 where b.id = :id")
    int updateLikeCount(@Param("id") UUID id);

    @Modifying
    @Query("update Blog b set b.likeCount = b.likeCount - 1 where b.id = :id")
    int updateDisLikeCount(@Param("id") UUID id);

    @Query("select b.id as id, b.viewCount as viewCount, b.likeCount as likeCount, b.title as title," +
            " b.createdDate as createdDate, b.imageAddress as blogImage, b.shortDescription as shortDescription," +
            " u.id as userId, u.username as username, u.title as userTitle, u.profileImageAddress as profileImageAddress " +
            " from Blog b inner join User u on b.user.id = u.id ")
    List<BlogListDto> listBlogs(Pageable pageable);

    @Query("select b.id as id, b.viewCount as viewCount, b.likeCount as likeCount, b.title as title," +
            " b.createdDate as createdDate, b.imageAddress as blogImage, b.shortDescription as shortDescription," +
            " u.id as userId, u.username as username, u.title as userTitle, u.profileImageAddress as profileImageAddress " +
            " from Blog b inner join User u on b.user.id = u.id where not b.id = :blogId")
    List<BlogListDto> listBlogsWithoutItSelf(@Param("blogId") UUID blogId, Pageable pageable);

    @Query("select b.id as id, b.viewCount as viewCount, b.likeCount as likeCount, b.title as title," +
            " b.createdDate as createdDate, b.imageAddress as blogImage, b.description as description," +
            " u.id as userId, u.username as username, u.title as userTitle, u.profileImageAddress as profileImageAddress," +
            " c.id as categoryId, c.name as categoryName, c.imageAddress as categoryImage " +
            " from Blog b inner join User u on b.user.id = u.id " +
            " inner join BlogCategory bc on b.id = bc.blog.id " +
            " inner join Category c on bc.category.id = c.id " +
            " where b.id = :blogId ")
    BlogReadDto readBlog(@Param("blogId") UUID blogId);

    @Query("select b.id as id, b.viewCount as viewCount, b.likeCount as likeCount, b.title as title," +
            " b.createdDate as createdDate, b.imageAddress as blogImage, b.description as description," +
            " u.id as userId, u.username as username, u.title as userTitle, u.profileImageAddress as profileImageAddress " +
            " from Blog b inner join User u on b.user.id = u.id inner join BlogCategory bc on b.id = bc.blog.id " +
            " where bc.category.id = :categoryId and not b.id = :blogId ")
    List<BlogListDto> blogSameCategory(@Param("categoryId") UUID categoryId, @Param("blogId") UUID blogId, Pageable pageable);

    @Query("select b.id as id, b.createdDate as createdDate from Blog b ")
    List<CategorySitemapDto> listBlogSitemap(Pageable pageable);
}
