package com.pody.repository;

import com.pody.dto.repositories.NewsListDto;
import com.pody.model.News;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@Repository
public interface NewsRepository extends AbstractRepository<News, UUID> {
    @Query("select n.id as id, n.title as title, n.description as description, n.likeCount as likeCount, n.createdDate as createdDate," +
            " n.imageAddress as imageAddress, n.disLikeCount as disLikeCount, u.username as username,  u.title as userTitle," +
            " u.id as userId, u.profileImageAddress as profileImageAddress" +
            " from News n inner join User u on n.user.id = u.id where n.createdDate between :today and :lastWeek")
    List<NewsListDto> listNews(@Param("today") Date today, @Param("lastWeek") Date todayLastWeek, Pageable pageable);

    @Query("select n.id as id, n.title as title, n.description as description, n.likeCount as likeCount, n.createdDate as createdDate," +
            " n.imageAddress as imageAddress, n.disLikeCount as disLikeCount, u.username as username,  u.title as userTitle," +
            " u.id as userId, u.profileImageAddress as profileImageAddress" +
            " from News n inner join User u on n.user.id = u.id")
    List<NewsListDto> listNewsMobile(Pageable pageable);

    @Query("select n.id as id, n.title as title, n.description as description, n.likeCount as likeCount, n.createdDate as createdDate," +
            " n.imageAddress as imageAddress, n.disLikeCount as disLikeCount, u.username as username,  u.title as userTitle," +
            " u.id as userId, u.profileImageAddress as profileImageAddress" +
            " from News n inner join User u on n.user.id = u.id " +
            " inner join UserFollow uf on uf.follower.id = n.user.id " +
            " where n.createdDate between :today and :lastWeek and uf.user.id = :userId ")
    List<NewsListDto> listNewsLoginedUser(@Param("today") Date today, @Param("lastWeek") Date todayLastWeek, @Param("userId") UUID userId, Pageable pageable);

    @Query("select n.id as id, n.title as title, n.description as description, n.likeCount as likeCount, n.createdDate as createdDate," +
            " n.imageAddress as imageAddress, n.disLikeCount as disLikeCount, u.username as username,  u.title as userTitle," +
            " u.id as userId, u.profileImageAddress as profileImageAddress" +
            " from News n inner join User u on n.user.id = u.id " +
            " inner join UserFollow uf on uf.follower.id = n.user.id " +
            " where uf.user.id = :userId ")
    List<NewsListDto> listNewsLoginedUserMobile(@Param("userId") UUID userId, Pageable pageable);

    @Query("select n.id as id, n.title as title, n.description as description, n.likeCount as likeCount, n.disLikeCount as disLikeCount, " +
            " n.createdDate as createdDate, n.imageAddress as imageAddress, u.username as username, u.title as userTitle," +
            " u.id as userId, u.profileImageAddress as profileImageAddress" +
            " from News n inner join User u on n.user.id = u.id where n.user.id = :userId")
    List<NewsListDto> listNewsEachUser(@Param("userId") UUID userId, Pageable pageable);

    @Modifying
    @Query("update News n set n.likeCount  = n.likeCount + 1 where n.id = :id")
    int updateLikesCount(@Param("id") UUID id);

    @Modifying
    @Query("update News n set n.disLikeCount= n.disLikeCount + 1 where n.id = :id")
    int updateDisLikesCount(@Param("id") UUID id);
}
