package com.pody.repository;

import com.pody.dto.repositories.PodcastListDto;
import com.pody.dto.repositories.PodcastReadDto;
import com.pody.model.Podcast;
import com.pody.model.User;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@Repository
public interface PodcastRepository extends AbstractRepository<Podcast, UUID> {

    @Override
    Podcast save(Podcast podcast);

    @Modifying
    @Query("update Podcast p set p.likeCount  = p.likeCount + 1 where p.id = :id")
    int updateLikesCount(@Param("id") UUID id);

    @Modifying
    @Query("update Podcast p set p.disLikeCount= p.disLikeCount + 1 where p.id = :id")
    int updateDisLikesCount(@Param("id") UUID id);

    @Modifying
    @Query("update Podcast p set p.viewCount  = p.viewCount + 1 where p.id = :id")
    int updateViewCount(@Param("id") UUID id);

    @Query("select distinct p.id as podcastId, p.title as title, p.imageAddress as podcastImage, p.audioAddress as audioAddress," +
            " p.episodeNumber as episodeNumber, p.seasonNumber as seasonNumber, p.shortDescription as shortDescription," +
            " p.viewCount as viewCount , p.likeCount as likeCount, p.duration as duration, u.id as userId," +
            " u.username as username, u.title as userTitle, u.profileImageAddress as profileImageAddress," +
            " c.id as categoryId, c.name as categoryName, c.imageAddress as categoryIcon " +
            " from Podcast p inner join User u on p.user.id = u.id " +
            " inner join PodcastCategory pc on p.id = pc.podcast.id " +
            " inner join Category c on pc.category.id = c.id " +
            " where p.title like :name or p.shortDescription like :name or p.description like :name or u.username like :name " +
            " or u.title like :name or c.name like :name or c.englishName like :name ")
    List<PodcastListDto> searchPodcast(@Param("name") String name, Pageable pageable);

    @Query("select new Podcast (p.id , p.title, p.imageAddress , p.shortDescription ,p.viewCount) from Podcast p")
    List<Podcast> listMostViwedAndLiked(Sort sort);

    @Query("select p.id as podcastId, p.title as title, p.imageAddress as podcastImage, p.audioAddress as audioAddress," +
            " p.episodeNumber as episodeNumber, p.seasonNumber as seasonNumber, p.shortDescription as shortDescription," +
            " p.viewCount as viewCount , p.likeCount as likeCount, p.duration as duration, u.id as userId," +
            " u.username as username, u.title as userTitle, u.profileImageAddress as profileImageAddress," +
            " c.id as categoryId, c.name as categoryName, c.imageAddress as categoryIcon " +
            " from Podcast p inner join User u on p.user.id = u.id " +
            " inner join PodcastCategory pc on p.id = pc.podcast.id " +
            " inner join Category c on pc.category.id = c.id " +
            " where p.createdDate between :lastWeek and :today ")
    List<PodcastListDto> listLatestReleased(@Param("lastWeek") Date lastWeek, @Param("today") Date today, Pageable pageable);//Also use for One Month Data

    @Query("select distinct  p.id as podcastId, p.title as title, p.imageAddress as podcastImage, p.audioAddress as audioAddress," +
            " p.episodeNumber as episodeNumber, p.seasonNumber as seasonNumber, p.shortDescription as shortDescription," +
            " p.viewCount as viewCount , p.likeCount as likeCount, p.duration as duration, u.id as userId," +
            " u.username as username, u.title as userTitle, u.profileImageAddress as profileImageAddress," +
            " c.id as categoryId, c.name as categoryName, c.imageAddress as categoryIcon " +
            " from Podcast p inner join User u on p.user.id = u.id " +
            " inner join PodcastCategory pc on p.id = pc.podcast.id " +
            " inner join Category c on pc.category.id = c.id " +
            " where c.id = :categoryId ")
    List<PodcastListDto> listPodcastsEachCategory(@Param("categoryId") UUID categoryId, Pageable pageable);

    @Query("select distinct p.id as podcastId, p.title as title, p.imageAddress as podcastImage, p.audioAddress as audioAddress," +
            " p.episodeNumber as episodeNumber, p.seasonNumber as seasonNumber, p.description as description," +
            " p.createdDate as createdDate, p.duration as duration, p.viewCount as viewCount , p.likeCount as likeCount," +
            " u.id as userId, u.username as username, u.title as userTitle, u.profileImageAddress as profileImageAddress," +
            " c.id as categoryId, c.name as categoryName, c.imageAddress as categoryIcon " +
            " from Podcast p inner join User u on p.user.id = u.id " +
            " inner join PodcastCategory pc on p.id = pc.podcast.id " +
            " inner join Category c on pc.category.id = c.id " +
            " where p.id = :id")
    PodcastReadDto readPodcast(@Param("id") UUID id);

    @Query("select distinct  p.id as podcastId, p.title as title, p.imageAddress as podcastImage, p.audioAddress as audioAddress," +
            " p.episodeNumber as episodeNumber, p.seasonNumber as seasonNumber, p.shortDescription as shortDescription," +
            " p.duration as duration, p.viewCount as viewCount , p.likeCount as likeCount, u.id as userId," +
            " u.username as username, u.title as userTitle, u.profileImageAddress as profileImageAddress," +
            " c.id as categoryId, c.name as categoryName, c.imageAddress as categoryIcon " +
            " from Podcast p inner join User u on p.user.id = u.id " +
            " inner join PodcastView pv on p.id = pv.podcast.id " +
            " inner join PodcastCategory pc on p.id = pc.podcast.id " +
            " inner join Category c on pc.category.id = c.id " +
            " where pv.count >= 500 and pv.date between :yesterday and :today ")
    List<PodcastListDto> listDailyTrends(@Param("yesterday") Date yesterday, @Param("today") Date today, Pageable pageable);//Also use for One Month Data

    @Query("select distinct p.id as podcastId, p.title as title, p.imageAddress as podcastImage, p.audioAddress as audioAddress," +
            " p.episodeNumber as episodeNumber, p.seasonNumber as seasonNumber, p.shortDescription as shortDescription," +
            " p.duration as duration, p.viewCount as viewCount , p.likeCount as likeCount, u.id as userId," +
            " u.username as username, u.title as userTitle, u.profileImageAddress as profileImageAddress," +
            " c.id as categoryId, c.name as categoryName, c.imageAddress as categoryIcon " +
            " from Podcast p inner join User u on p.user.id = u.id " +
            " inner join PodcastCategory pc on p.id = pc.podcast.id " +
            " inner join Category c on pc.category.id = c.id " +
            " where pc.category.id = :categoryId and not p.id = :podcastId ")
    List<PodcastListDto> listRandomPodcastForEachPodcast(@Param("categoryId") UUID categoryId, @Param("podcastId") UUID podcastId, Pageable pageable);

    @Query("select distinct p.id as podcastId, p.title as title, p.imageAddress as podcastImage, p.audioAddress as audioAddress," +
            " p.episodeNumber as episodeNumber, p.seasonNumber as seasonNumber, p.shortDescription as shortDescription," +
            " p.duration as duration, p.viewCount as viewCount, p.likeCount as likeCount, u.id as userId," +
            " u.username as username, u.title as userTitle, u.profileImageAddress as profileImageAddress," +
            " c.id as categoryId, c.name as categoryName, c.imageAddress as categoryIcon " +
            " from Podcast p inner join User u on p.user.id = u.id " +
            " inner join PodcastCategory pc on p.id = pc.podcast.id " +
            " inner join Category c on pc.category.id = c.id ")
    List<PodcastListDto> listMostViewedAndLiked(Pageable pageable);

    @Query("select distinct p.id as podcastId, p.title as title, p.imageAddress as podcastImage, p.audioAddress as audioAddress," +
            " p.episodeNumber as episodeNumber, p.seasonNumber as seasonNumber, p.shortDescription as shortDescription," +
            " p.duration as duration, p.viewCount as viewCount, p.likeCount as likeCount, u.id as userId," +
            " u.username as username, u.title as userTitle, u.profileImageAddress as profileImageAddress," +
            " c.id as categoryId, c.name as categoryName, c.imageAddress as categoryIcon " +
            " from Podcast p inner join User u on p.user.id = u.id " +
            " inner join PodcastCategory pc on p.id = pc.podcast.id " +
            " inner join Category c on pc.category.id = c.id where p.episodeNumber = :episodeNum and p.seasonNumber = :seasonNum and p.user.id = :userId ")
    PodcastListDto previousAndNextPodcastEpisode(@Param("episodeNum") int episodeNumber, @Param("seasonNum") int seasonNumber, @Param("userId") UUID userId);

    @Query("select p.id as podcastId, p.title as title, p.imageAddress as podcastImage, p.audioAddress as audioAddress," +
            " p.episodeNumber as episodeNumber, p.seasonNumber as seasonNumber, p.shortDescription as shortDescription," +
            " p.duration as duration, p.viewCount as viewCount, p.likeCount as likeCount, u.id as userId," +
            " u.username as username, u.title as userTitle, u.profileImageAddress as profileImageAddress," +
            " c.id as categoryId, c.name as categoryName, c.imageAddress as categoryIcon " +
            " from Podcast p inner join User u on p.user.id = u.id " +
            " inner join PodcastCategory pc on p.id = pc.podcast.id " +
            " inner join Category c on pc.category.id = c.id where p.episodeNumber = :episodeNum and p.user.id = :userId")
    PodcastListDto previousAndNextPodcastEpisodeWithoutSeason(@Param("episodeNum") int episodeNumber, @Param("userId") UUID userId);

    @Query("select distinct p.id as podcastId, p.title as title, p.imageAddress as podcastImage, p.audioAddress as audioAddress, " +
            " p.episodeNumber as episodeNumber, p.seasonNumber as seasonNumber, p.shortDescription as shortDescription," +
            " p.duration as duration, p.viewCount as viewCount, p.likeCount as likeCount, u.id as userId," +
            " u.username as username,u.title as userTitle, u.profileImageAddress as profileImageAddress," +
            " c.id as categoryId, c.name as categoryName, c.imageAddress as categoryIcon " +
            " from Podcast p inner join User u on p.user.id = u.id " +
            " inner join PodcastCategory pc on p.id = pc.podcast.id " +
            " inner join Category c on pc.category.id = c.id where p.user.id = :userId")
    List<PodcastListDto> listPodcastEachUser(@Param("userId") UUID id, Pageable pageable);

    @Modifying
    @Query("update Podcast p set p.imageAddress = :imagePath where p.id = :id")
    int updateImageAddress(@Param("imagePath") String imagePath, @Param("id") UUID id);

    @Modifying
    @Query("update Podcast p set p.audioAddress= :audioPath where p.id = :id")
    int updateAudioAddress(@Param("audioPath") String audioPath, @Param("id") UUID id);

    Podcast findOneByTitleAndUser(String title, User user);
}
