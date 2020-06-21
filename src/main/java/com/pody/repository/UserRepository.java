package com.pody.repository;

import com.pody.dto.repositories.ChannelsListDto;
import com.pody.dto.repositories.ChannelsPageDto;
import com.pody.dto.repositories.UserRssUpdateDto;
import com.pody.dto.repositories.UserSearchDto;
import com.pody.model.User;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface UserRepository extends AbstractRepository<User, UUID> {
    User findOneByPhoneNumber(String phoneNumber);

    @Query("select u from User  u where (u.username = :username or u.email = :username)")
    User loginUser(@Param("username") String username);

    @Query("select new User(u.id) from User  u where (u.username = :username or u.email = :username)")
    User findUserForResetPassword(@Param("username") String username);

    @Modifying
    @Query("update User u set u.password = :password where u.id = :id")
    int forgotPassword(@Param("id") UUID id, @Param("password") String password);

    @Query("select distinct u.id as id, u.username as username, u.title as userTitle, u.profileImageAddress as imageAddress, u.followerCount as followerCount " +
            " from User u  inner join UserCategory uc on u.id = uc.user.id " +
            " inner join Category c on uc.category.id = c.id " +
            " where u.username like :username or u.title like :username or c.name like :username or c.englishName like :username")
    List<UserSearchDto> searchUsers(@Param("username") String username);

    @Modifying
    @Query("update User u set u.isPremium = 1 where u.id = :id")
    int isPremium(@Param("id") UUID id);

    @Modifying
    @Query("update User u set u.profileImageAddress = :imagePath where u.id = :id")
    int updateImageAddress(@Param("imagePath") String imagePath, @Param("id") UUID id);

    @Modifying
    @Query("update User u set u.channelImage = :imagePath where u.id = :id")
    int updateChannelImage(@Param("imagePath") String imagePath, @Param("id") UUID id);

    @Modifying
    @Query("update User u set u.password = :password where u.id = :id")
    int resetPassword(@Param("password") String password, @Param("id") UUID id);

    @Modifying
    @Query("update User u set u.rssUrl = :url where u.id = :id")
    int updateRssUrl(@Param("url") String url, @Param("id") UUID id);

    User findOneByUsername(String username);

    User findOneByEmail(String email);

    User findOneByRssUrl(String rssUrl);

    @Query("select u.id as id, u.rssUrl as rssUrl, u.profileImageAddress as profileImageAddress from User u where rssUrl is not null")
    List<UserRssUpdateDto> rssUpdateList();

    @Modifying
    @Query("update User u set u.followerCount = u.followerCount + 1 where u.id = :id")
    int updateFollowerCount(@Param("id") UUID id);//count of when users follow a podcaster

    @Modifying
    @Query("update User u set u.followerCount = u.followerCount - 1 where u.id = :id")
    int updateUnFollowerCount(@Param("id") UUID id);//count of when users unfollow a podcaster

    @Modifying
    @Query("update User u set u.followingCount = u.followingCount + 1 where u.id = :id")
    int updateFollowingCount(@Param("id") UUID id);//count when user is following some one

    @Modifying
    @Query("update User u set u.followingCount  = u.followingCount  - 1 where u.id = :id")
    int updateUnFollowingCount(@Param("id") UUID id);//count when user is unfollowing some one

    @Modifying
    @Query("update User u set u.isChannel = true where u.id = :id")
    int updateIsChannel(@Param("id") UUID id);

    @Modifying
    @Query("update User u set u.followerCount = u.followerCount - 1 where u.id = :id")
    int updateUnFollowCount(@Param("id") UUID id);

    @Query("select u.id as id, u.username as username, u.title as userTitle, u.profileImageAddress as imageAddress, u.followerCount as followCount from User u where u.isChannel = 1")
    List<ChannelsListDto> listHomePageUsers(Pageable pageable);

    @Query("select u.id as id, u.username as username, u.title as userTitle, u.profileImageAddress as imageAddress," +
            " u.firstName as firstName, u.lastName as lastName, u.followerCount as followCount, u.instagramUrl as instagramUrl," +
            " u.twitterUrl as twitterUrl, u.facebookUrl as facebookUrl, u.youtubeUrl as youtubeUrl, u.websiteUrl as websiteUrl" +
            " from User u where u.isChannel = 1")
    List<ChannelsPageDto> listChannels(Pageable pageable);

    @Query("select u.id as id, u.username as username, u.title as userTitle, u.profileImageAddress as imageAddress," +
            " u.firstName as firstName, u.lastName as lastName, u.followerCount as followCount, u.instagramUrl as instagramUrl," +
            " u.twitterUrl as twitterUrl, u.facebookUrl as facebookUrl, u.youtubeUrl as youtubeUrl, u.websiteUrl as websiteUrl" +
            " from User u where u.isChannel = 1 and not u.id = :userId")
    List<ChannelsPageDto> listChannelsLoginedUser(@Param("userId") UUID userId, Pageable pageable);

    @Query("select u.id as id, u.username as username, u.title as userTitle, u.profileImageAddress as imageAddress, u.followerCount as followCount" +
            " from User u where u.isChannel = 1 and not u.id = :userId")
    List<ChannelsListDto> listHomePageUsersLoginedUser(@Param("userId") UUID userId, Pageable pageable);

    @Query("select u.id as id, u.username as username, u.title as userTitle, u.profileImageAddress as imageAddress, u.followerCount as followCount, u.bio as userBio" +
            " from User u inner join UserFollow uf on u.id = uf.follower.id where uf.user.id = :userId")
    List<ChannelsListDto> listFollowingChannels(@Param("userId") UUID userId, Pageable pageable);

    @Query("select u.id as id, u.username as username, u.title as userTitle, u.profileImageAddress as imageAddress, u.followerCount as followCount, u.bio as userBio" +
            " from User u inner join UserFollow uf on u.id = uf.follower.id where uf.user.id = :userId")
    List<ChannelsListDto> listFollowingChannelsSideNav(@Param("userId") UUID userId);

    @Query("select u.id as id, u.username as username, u.profileImageAddress as imageAddress, u.followerCount as followCount ," +
            " case when uf.id is null  THEN 'False' ELSE 'True' END AS hasFollowed " +
            " from User u inner join UserFollow uf on u.id = uf.follower.id where u.id = :userId")
    List<ChannelsListDto> listHomePageUsersWithFollowed(@Param("userId") UUID userId, Pageable pageable);//this user id is id of signed in user
}
